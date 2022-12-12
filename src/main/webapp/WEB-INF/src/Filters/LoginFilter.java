package Filters;

import static jakarta.servlet.http.HttpServletResponse.SC_FORBIDDEN;

import Dao.TokenDao;
import Models.Token;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;


public class LoginFilter implements Filter {
    private TokenDao dao = new TokenDao();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    protected boolean isAuthenticated(HttpServletRequest request) {
        List<Token> tokens = dao.getAll();
        String token = request.getHeader("Authorization");
        return isTokenAvailable(tokens, token);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        PrintWriter out = resp.getWriter();

        if (isAuthenticated(req)) {
            System.out.println("authorized");
            filterChain.doFilter(request, response);
        } else {
            System.out.println("unauthorized");
            resp.setStatus(SC_FORBIDDEN);
            out.println("forbidden");
        }
    }

    private boolean isTokenAvailable(List<Token> tokens, String tokenValue) {
        for (Token token : tokens) {
            if (token.getToken().equals(tokenValue))
                return true;
        }
        return false;
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
