import http.HttpServletResponse;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class Response implements HttpServletResponse {
    StringBuilder headerBuilder = new StringBuilder();

    PrintWriter bodyWriter;

    Map<String, String> headers = new HashMap<>();

    OutputStream outputStream;

    int status = 200;

    public Response(OutputStream outputStream) {
        this.outputStream = outputStream;
        bodyWriter = new PrintWriter(outputStream);
    }

    public String responseHeaders() {
        headerBuilder.append("HTTP/1.1  ").append(status).append("\n");
        appendHeaders();
        headerBuilder.append("\n");

        return headerBuilder.toString();
    }

    private void appendHeaders() {
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            headerBuilder.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }
    }

    @Override
    public void setStatus(int var1) {
        if (var1 < 0 || var1 > 505)
            return;

        status = var1;
    }

    @Override
    public void setCharacterEncoding(String var1) {
        headers.put("Character-Encoding", var1);
    }

    public void addHeader(String name, String value) {
        headers.put(name, value);
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        return bodyWriter;
    }

    @Override
    public void setContentType(String var1) {
        headers.put("Content-Type", var1);
    }

    @Override
    public void setContentLength(int var1) {
        headers.put("Content-Length", String.valueOf(var1));
    }
}
