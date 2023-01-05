package http;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RequestDispatcher {
    private final Map<String, HttpServlet> servlets = ServletContext.getServlets();

    public HttpServlet dispatchRequest(HttpServletRequest request) {
        for (Map.Entry<String, HttpServlet> entry : servlets.entrySet()) {
            String key = entry.getKey();
            Pattern pattern = Pattern.compile(key);
            Matcher matcher = pattern.matcher(request.getRequestURI());
            if (matcher.matches())
                return entry.getValue();
        }

        return new StaticContentServlet();
    }
}
