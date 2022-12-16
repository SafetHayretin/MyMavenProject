import Servlets.PostServlet;
import http.HttpServlet;
import http.HttpServletRequest;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HttpTask implements Runnable {
    private final Socket socket;

    private final Map<String, HttpServlet> servlets = Servlets.getServlets();

    public HttpTask(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (InputStream inputStream = socket.getInputStream();
             OutputStream outputStream = socket.getOutputStream()) {
            Request request = new Request(inputStream);
            Response response = new Response(outputStream);

            HttpServlet servlet = dispatchRequest(request);

            if (servlet == null) {
                outputStream.write(getErrorMessage().getBytes());
                return;
            }
            servlet.service(request, response);

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

    private HttpServlet dispatchRequest(HttpServletRequest request) {
        for (Map.Entry<String, HttpServlet> entry : servlets.entrySet()) {
            String key = entry.getKey();
            Pattern pattern = Pattern.compile(key);
            Matcher matcher = pattern.matcher(request.getRequestURI());
            if (matcher.matches())
                return entry.getValue();
        }
        return null;
    }

    private String getErrorMessage() {
        return "HTTP/1.1 404 Not Found\n" +
                "Date: "+ new Date()+"\n" +
                "Content-Type: text/html\n\n"+"" +
                "<div class=\"error-message\">\n" +
                "    <h2>Error:</h2>\n" +
                "    <p>Sorry, something went wrong. Please try again later.</p>\n" +
                "</div>";
    }
}
