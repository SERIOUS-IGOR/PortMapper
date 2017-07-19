import java.io.IOException;
import java.net.*;

/**
 * Created by samok on 09.07.2017.
 */
public class SocketTransmitter extends Thread {
    public ServerSocket serverSocket;
    private URL targetURL;

    public SocketTransmitter(int listeningPort, String targetURL) {
        setDaemon(true);
        try {
            this.targetURL = new URL(targetURL);
            serverSocket = new ServerSocket(listeningPort, 10, InetAddress.getByName("localhost"));
            start();
        } catch (IOException t) {
            t.printStackTrace();
        }
    }

    @Override
    public void run() {
        System.out.println("Socket transmitter listening on: " + serverSocket.getInetAddress() + ":" + serverSocket.getLocalPort());
        Socket fromServletToClient;
        try {
            System.out.println("Waiting for connect...");
            fromServletToClient = serverSocket.accept();
            System.out.println("Connecting...");

            TransmitterThread thread = new TransmitterThread(targetURL,fromServletToClient.getOutputStream());
            thread.join();

            fromServletToClient.close();
            System.out.println("SocketTransmitter work end");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
