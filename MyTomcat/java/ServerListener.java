import org.apache.commons.cli.CommandLine;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerListener implements Runnable {
    private final int port;

    public ServerListener(int port) {
        //TODO: add threads
        this.port = port;
    }

    @Override
    public void run() {
        int threads = Integer.parseInt(Main.cmd.getOptionValue("t", "1"));
        ExecutorService pool = Executors.newFixedThreadPool(threads);
        Servlets servlets = new Servlets();
        try (ServerSocket serverSocket = new ServerSocket(port))  {
            servlets.init();
            while (serverSocket.isBound() && !serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();
                HttpTask htmlTask = new HttpTask(socket);
                pool.submit(htmlTask);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
