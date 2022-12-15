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

        String[] strings = line.split(" ");
        method = strings[0];
        path = strings[1];

        while (true) {
            line = in.nextLine();
            if (line.isBlank()) {
                break;
            }
            strings = line.split(": ");
            headers.put(strings[0], strings[1]);
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
        return null;
    }

    @Override
    public String getPathInfo() {
        return path;
    }

    @Override
    public String getRequestURI() {
        return path;
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
