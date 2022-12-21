import http.HttpServletRequest;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.Part;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Request implements HttpServletRequest {
    String method;

    String path;

    InputStream inputStream;

    Map<String, String> headers = new HashMap<>();

    public Request(InputStream input) {
        inputStream = input;
        handleRequest();
    }

    private void handleRequest() {
        Scanner in = new Scanner(inputStream, StandardCharsets.UTF_8);
        String line = in.nextLine();

        if (line.isEmpty())
            return;

        String[] headerParts = line.split(" ");
        method = headerParts[0];
        path = headerParts[1];

        line = in.nextLine();
        while(!line.isBlank()) {
            //todo: invalid headers handler
            headerParts = line.split(": ");
            headers.put(headerParts[0], headerParts[1]);
            line = in.nextLine();
        }
    }

    @Override
    public BufferedReader getReader() {
        Reader reader = new InputStreamReader(inputStream);
        return new BufferedReader(reader);
    }

    @Override
    public Cookie[] getCookies() {
        String cookiesStr = headers.get("Cookie");
        String[] cookieStr = cookiesStr.split("; ");
        Cookie[] cookies = new Cookie[cookieStr.length];

        for (int i = 0; i < cookieStr.length; i++) {
            String[] keyValue = cookieStr[i].split("=");
            cookies[i] = new Cookie(keyValue[0], keyValue[1]);
        }

        return cookies;
    }

    @Override
    public String getHeader(String var1) {
        return headers.get(var1);
    }

    @Override
    public List<String> getHeaders(String var1) {
        return null;
    }

    @Override
    public String getMethod() {
        return method;
    }

    @Override
    public String getQueryString() {
        String[] spl = path.split("\\?");
        if (spl.length > 1)
            return spl[1];

        return spl[0];
    }

    @Override
    public String getPathInfo() {
        return path;
    }

    @Override
    public String getRequestURI() {
        String[] spl = path.split("\\?");
        return spl[0];
    }

    @Override
    public StringBuffer getRequestURL() {
        return null;
    }

    @Override
    public Part getPart(String var1) {
        return null;
    }
}
