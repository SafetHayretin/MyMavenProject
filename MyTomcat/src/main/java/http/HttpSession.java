package http;

import java.util.HashMap;
import java.util.Map;

public class HttpSession {
    public static Map<String, HttpSession> sessions = new HashMap<>();

    String id;

    Map<String, String> attributes = new HashMap<>();

    long lastAccessed;

    long creationTime;

    int maxInactiveInterval;

    ServletContext context;

    public HttpSession(String id) {
        this.id = id;
        creationTime = System.currentTimeMillis();
        lastAccessed = System.currentTimeMillis();
        sessions.put(id, this);
    }

    long getCreationTime() {
        return this.creationTime;
    }

    String getId() {
        return this.id;
    }

    long getLastAccessedTime() {
        return this.lastAccessed;
    }

    void setMaxInactiveInterval(int interval) {
        this.maxInactiveInterval = interval;
    }

    ServletContext getServletContext() {
        return this.context;
    }

    int getMaxInactiveInterval() {
        return this.maxInactiveInterval;
    }

    Object getAttribute(String name) {
        if (name.isEmpty())
            return null;

        return attributes.get(name);
    }

    void setAttribute(String name, String value) {
        if (name.isEmpty() || value.isEmpty())
            throw new IllegalArgumentException();

        attributes.put(name, value);
    }

    void removeAttribute(String name) {
        attributes.remove(name);
    }
}
