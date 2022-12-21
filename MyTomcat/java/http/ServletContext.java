package http;

import jakarta.servlet.http.HttpFilter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ServletContext {
    private static final Map<String, HttpServlet> servlets = new HashMap<>();

    private final Map<String, HttpFilter> filters = new HashMap<>();

    private String docBase;

    private boolean reloadable;

    public String getMimeType(String var1) {
        if (var1.endsWith(".jpg") || var1.endsWith(".jpeg"))
            return "image/jpeg";
        else if (var1.endsWith(".html") || var1.endsWith(".htm"))
            return "text/html";
        else if (var1.endsWith(".mp3"))
            return "audio/mpeg";
        else if (var1.endsWith(".pdf"))
            return "application/pdf";
        else if (var1.endsWith(".png"))
            return "image/png";
        else if (var1.endsWith(".css"))
            return "text/css";

        return null;
    }

    public void setDocBase(String docBase) {
        this.docBase = docBase;
    }

    public void setReloadable(boolean reloadable) {
        this.reloadable = reloadable;
    }

    public void init() throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new File("src/main/webapp/WEB-INF/web.xml"));

        Map<String, String> servlet = getElements(doc, "servlet", "servlet-class");
        Map<String, String> mapping = getElements(doc, "servlet-mapping", "url-pattern");

        for (Map.Entry<String, String> entry : servlet.entrySet()) {
            String key = entry.getKey();
            String clazz = servlet.get(key);
            String url = putDot(mapping.get(key));
            Class<?> newInst = Class.forName(clazz);
            HttpServlet obj;
            try {
                obj = (HttpServlet) newInst.newInstance();
            } catch (RuntimeException e) {
                continue;
            }

            servlets.put(url, obj);
        }
    }

    private Map<String, String> getElements(Document doc, String name, String val2) {
        NodeList items = doc.getElementsByTagName(name);

        Map<String, String> servletMap = new HashMap<>();
        for (int i = 0; i < items.getLength(); i++) {
            Element element = (Element) items.item(i);
            String servletName = element.getElementsByTagName("servlet-name").item(0).getTextContent();
            String servletClass = element.getElementsByTagName(val2).item(0).getTextContent();

            servletMap.put(servletName, servletClass);
        }

        return servletMap;
    }

    private static String putDot(String url) {
        return url.replace("/*", "/.*");
    }

    public URL getResource(String path) throws MalformedURLException {
        if (!path.startsWith("/"))
            return null;

        return new URL(docBase + path);
    }

    public RequestDispatcher getRequestDispatcher(String path) {
        return new RequestDispatcher();
    }

    public InputStream getResourceAsStream(String path) {
        InputStream inputStream;

        try {
            inputStream = new FileInputStream(path);
        } catch (FileNotFoundException e) {
            return null;
        }

        return inputStream;
    }

    public static Map<String, HttpServlet> getServlets() {
        return servlets;
    }
}
