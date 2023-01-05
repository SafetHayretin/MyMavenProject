package http;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;


public class StaticContentServlet extends HttpServlet {

    private String contentPath;

    @Override
    public void init() {
        contentPath = getServletConfig().getInitParameter("contentPath");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String filePath = request.getPathInfo();

        if (filePath == null || filePath.isEmpty()) {
            filePath = "ErrorPage.html";
        }

        File file = new File(contentPath, filePath);
        if (!file.isFile()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "File not found");
            return;
        }

        String contentType = getServletContext().getMimeType(file.getName());
        response.setContentType(contentType != null ? contentType : "text/html");
        response.setContentLength((int) file.length());
        
        Files.copy(file.toPath(), response.getOutputStream());
        response.getOutputStream().flush();
    }
}

