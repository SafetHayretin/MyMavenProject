package http;

import java.util.Locale;

public class Cookie {
    private String name;

    private String value;

    private String comment;

    private String domain;

    private int maxAge = -1;

    private String path;

    private boolean secure;

    private boolean isHttpOnly = false;

    public Cookie(String name, String value) {
        if (name == null || name.length() == 0) {
            throw new IllegalArgumentException();
        }
        if (!isToken(name) || name.equalsIgnoreCase("Comment") ||
                name.equalsIgnoreCase("Discard") ||
                name.equalsIgnoreCase("Domain") ||
                name.equalsIgnoreCase("Expires") ||
                name.equalsIgnoreCase("Max-Age") ||
                name.equalsIgnoreCase("Path") ||
                name.equalsIgnoreCase("Secure") ||
                name.equalsIgnoreCase("Version")
                || name.startsWith("$")) {
            throw new IllegalArgumentException();
        }

        this.name = name;
        this.value = value;
    }

    public void setComment(String purpose) {
        comment = purpose;
    }

    public String getComment() {
        return comment;
    }

    public void setDomain(String domain) {
        this.domain = domain.toLowerCase(Locale.ENGLISH);
    }

    public String getDomain() {
        return domain;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(int maxAge) {
        this.maxAge = maxAge;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isSecure() {
        return secure;
    }

    public void setSecure(boolean secure) {
        this.secure = secure;
    }

    public boolean isHttpOnly() {
        return isHttpOnly;
    }

    public void setHttpOnly(boolean httpOnly) {
        isHttpOnly = httpOnly;
    }

    private boolean isToken(String value) {
        int len = value.length();
        for (int i = 0; i < len; i++) {
            char c = value.charAt(i);
            if (c < 0x20 || c >= 0x7f) {
                return false;
            }
        }

        return true;
    }
}
