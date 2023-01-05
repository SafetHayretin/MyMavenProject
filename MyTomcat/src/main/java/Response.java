import http.Cookie;
import http.HttpServletResponse;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Response implements HttpServletResponse {
    StringBuilder headerBuilder = new StringBuilder();

    PrintWriter writer;

    Map<String, String> headers = new HashMap<>();

    OutputStream outputStream;

    List<Cookie> cookies = new ArrayList<>();

    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

    int status = SC_OK;

    public Response(OutputStream outputStream) {
        this.outputStream = outputStream;
        writer = new PrintWriter(byteArrayOutputStream);
    }

    public String responseHeaders() {
        headerBuilder.append("HTTP/1.1  ").append(status).append("\n");
        appendHeaders();

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
        return writer;
    }

    public void addCookie(Cookie cookie) {
        String cookieStr = cookie.getName() + "=" + cookie.getValue();
        headers.put("Set-Cookie", cookieStr);
    }

    public List<Cookie> getCookies() {
        return this.cookies;
    }

    String cookiesHeader() {
        StringBuilder builder = new StringBuilder();
        builder.append("cookies: ");

        for (Cookie c : cookies) {
            builder.append(c.getName()).append("=").append(c.getValue()).append("; ");
        }
        builder.append("\n");

        return builder.toString();
    }

    @Override
    public void setContentType(String var1) {
        headers.put("Content-Type", var1);
    }

    @Override
    public void setContentLength(int var1) {
        headers.put("Content-Length", String.valueOf(var1));
    }

    @Override
    public OutputStream getOutputStream() {
        return outputStream;
    }

    @Override
    public void sendError(int scCode, String msg) {
        StringBuilder sb = new StringBuilder();
        sb.append("HTTP/1.1  ").append(scCode).append("\n");
        try {
            outputStream.write(sb.toString().getBytes());
            outputStream.write(msg.getBytes());
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
