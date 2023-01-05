package src.Listener;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;

import java.util.Timer;

public class BackgroundJobManager implements ServletContextListener {
    private static final long DAY = 1000*60*60*24;
    @Override
    public void contextInitialized(ServletContextEvent event) {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TokenChecker(),0, DAY);
    }
}
