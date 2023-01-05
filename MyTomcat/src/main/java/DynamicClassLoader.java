import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class DynamicClassLoader {
    private static URLClassLoader classLoader;

    public static void init() throws MalformedURLException {
        URL warUrl = new URL("");
        URL[] wabApps = new URL[] {warUrl};
        classLoader = new URLClassLoader(wabApps);
    }

    public static URL getResource(String resource) {
         return classLoader.getResource(resource);
    }

    public static Class<?> loadClass(String clazz) throws ClassNotFoundException {
        return classLoader.loadClass(clazz);
    }
}
