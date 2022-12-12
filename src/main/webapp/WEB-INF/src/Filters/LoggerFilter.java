package Filters;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class LoggerFilter implements Filter {
    private static final Logger LOGGER = LogManager.getLogger(LoggerFilter.class.getName());

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        long start = System.currentTimeMillis();
        filterChain.doFilter(request, response);
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        long end = System.currentTimeMillis();
        doLog(req, resp, end - start);
    }

    private void doLog(HttpServletRequest req, HttpServletResponse resp, long execTimer) throws ServletException, IOException {
        Part username = req.getPart("name");
        String usernameStr = new BufferedReader(new InputStreamReader(username.getInputStream())).readLine();

        String message = usernameStr + " " + req.getMethod() + " " + req.getPathInfo() + " " + execTimer;
        LOGGER.info(message);
    }
}
