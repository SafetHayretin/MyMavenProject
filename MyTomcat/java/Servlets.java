import http.HttpServlet;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class Servlets {
    private static final Map<String, HttpServlet> servlets = new HashMap<>();

    public static void main(String[] args) throws Exception {
        init();
    }

    public static void init() throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new File("src/main/webapp/WEB-INF/web.xml"));

        Map<String, String> servlet = getElements(doc, "servlet", "servlet-class");
        Map<String, String> mapping = getElements(doc, "servlet-mapping", "url-pattern");

        for (Map.Entry<String,String> entry : servlet.entrySet()) {
            String key = entry.getKey();
            String clazz = servlet.get(key);
            System.out.println(clazz);
            clazz.replaceAll("\\.", "/");
            String url = mapping.get(key);
            System.out.println(url);
            Class<?> newInst = Class.forName(clazz);
            HttpServlet obj;
            try {
                obj = (HttpServlet) newInst.newInstance();
            } catch (RuntimeException e) {
                continue;
            }

            servlets.put(url, obj);
        }
        System.out.println(servlets);
    }

    private static Map<String, String> getElements(Document doc, String name, String val2) {
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
}