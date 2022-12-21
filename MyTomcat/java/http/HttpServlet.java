package http;

import java.io.IOException;

public abstract class HttpServlet {

    public void init() {
    }

    public void service(HttpServletRequest request, HttpServletResponse response) throws Exception {
        init();
        String method = request.getMethod();

        if (method.equals("GET"))
            doGet(request, response);

        if (method.equals("POST"))
            doPost(request, response);

        if (method.equals("PUT"))
            doPut(request, response);

        if (method.equals("DELETE"))
            doDelete(request, response);

        throw new Exception("Request doesn't have method");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    }

    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
    }

    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
    }

    public ServletConfig getServletConfig() {
        return new ServletConfig();
    }

    public ServletContext getServletContext() {
        return new ServletContext();
    }
}
