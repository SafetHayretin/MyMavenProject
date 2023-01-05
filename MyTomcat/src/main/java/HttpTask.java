import http.HttpServlet;
import http.RequestDispatcher;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class HttpTask implements Runnable {
    private final Socket socket;

    public HttpTask(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (InputStream inputStream = socket.getInputStream();
             OutputStream outputStream = socket.getOutputStream()) {
            Request request = new Request(inputStream);
            Response response = new Response(outputStream);

            RequestDispatcher dispatcher = new RequestDispatcher();
            HttpServlet servlet = dispatcher.dispatchRequest(request);

            servlet.service(request, response);
            outputStream.write(response.responseHeaders().getBytes());
            outputStream.write(response.cookiesHeader().getBytes());
            outputStream.write("\n".getBytes());
            outputStream.write(response.byteArrayOutputStream.toByteArray());
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (socket != null)
                    socket.close();
            } catch (IOException ignored) {
            }
        }
    }
}
