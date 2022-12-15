import http.HttpServlet;
import http.HttpServletRequest;
import Servlets.PostServlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HttpTask implements Runnable {
    private final Socket socket;

    private final Map<String, HttpServlet> servlets = new HashMap<>();

    public HttpTask(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (InputStream inputStream = socket.getInputStream();
             OutputStream outputStream = socket.getOutputStream()) {
            servlets.put("/posts/.*", new PostServlet());

            Request request = new Request(inputStream);
            Response response = new Response(outputStream);

            HttpServlet servlet = dispatchRequest(request);

            servlet.service(request, response);
            System.out.println(response.responseHeaders());

            outputStream.write(response.responseHeaders().getBytes());

            outputStream.flush();
            response.bodyWriter.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (socket != null)
                    socket.close();
            } catch (IOException ignored) {
            }
        }
    }

    public HttpServlet dispatchRequest(HttpServletRequest request) {
        for (Map.Entry<String,HttpServlet> entry : servlets.entrySet()) {
            String key = entry.getKey();
            Pattern pattern = Pattern.compile(key);
            Matcher matcher = pattern.matcher(request.getRequestURI());
            if (matcher.matches())
                return entry.getValue();
        }
        return null;
    }
}
