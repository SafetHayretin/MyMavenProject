package http;

import java.io.IOException;

public abstract class HttpServlet {
    public void service(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String method = request.getMethod();
        if (method.equals("GET"))
            doGet(request, response);

        if (method.equals("POST"))
            doPost(request, response);

        if (method.equals("PUT"))
            doPut(request, response);

        if (method.equals("DELETE"))
            doDelete(request, response);
    }

    protected abstract void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException;

    protected abstract void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException;

    protected abstract void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException;

    protected abstract void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException;
}
