import http.ServletContext;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class LoadContexts {
    private final Map<String, ServletContext> servletContexts = new HashMap<>();

    public LoadContexts() throws Exception {
        init();
    }

    public void init() throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new File("src/main/webapp/WEB-INF/server.xml"));
        NodeList items = doc.getElementsByTagName("Context");

        for (int i = 0; i < items.getLength(); i++) {
            Element element = (Element) items.item(i);
            String path = element.getAttribute("path");
            ServletContext context = new ServletContext();

            String docBase = element.getAttribute("docBase");
            String reloadable = element.getAttribute("reloadable");

            context.setDocBase(docBase);
            context.setReloadable(Boolean.parseBoolean(reloadable));

            servletContexts.put(path, context);
        }
    }

    public Map<String, ServletContext> getServletContexts() {
        return servletContexts;
    }
}
