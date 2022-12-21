package http;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.Part;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

public interface HttpServletRequest {
    BufferedReader getReader() throws IOException;
    Cookie[] getCookies();

    String getHeader(String var1);

    List<String> getHeaders(String var1);

    String getMethod();

    String getPathInfo();

    String getQueryString();

    String getRequestURI();

    StringBuffer getRequestURL();

    Part getPart(String var1) throws IOException, ServletException;
}
