package Filters;

import static jakarta.servlet.http.HttpServletResponse.SC_FORBIDDEN;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;


public class LoginFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    protected boolean isAuthenticated(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        return session != null;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        PrintWriter out = resp.getWriter();

        if(isAuthenticated(req)){
            System.out.println("authorized");
           filterChain.doFilter(request, response);
        } else {
            System.out.println("unauthorized");
            resp.setStatus(SC_FORBIDDEN);
            out.println("forbidden");
        }
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
