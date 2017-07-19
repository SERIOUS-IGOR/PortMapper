import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

/**
 * Created by samok on 09.07.2017.
 */
public class TransmitterThread extends Thread {
    URL targetURL;
    OutputStream targetOutput;

    public TransmitterThread(URL targetURL, OutputStream targetOutput) {
        setDaemon(true);
        this.targetURL = targetURL;
        this.targetOutput = targetOutput;
        start();
    }

    @Override
    public void run() {
        try {
            sendResponse(
                    sendGet(targetURL)
                    , targetOutput);
            System.out.println("Transmitter Thread work end\n");
        } catch (IOException cause) {
            cause.printStackTrace();
        }
    }

    public ArrayList<String> sendGet(URL targetURL) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) targetURL.openConnection();
        connection.setRequestMethod("GET");
        System.out.println("Sending \"GET\" request to URL: " + targetURL.getHost() + "\n");
        int responseCode = connection.getResponseCode();
        System.out.println("Response code: " + responseCode + "\n");

        BufferedReader in = new BufferedReader(
                new InputStreamReader(connection.getInputStream()));
        String inputLine;
        ArrayList<String> response = new ArrayList<>();

        while ((inputLine = in.readLine()) != null) {
            response.add(inputLine);
        }
        System.out.println("Response message: \n");
        //print result
        for (String line : response
                ) {
            System.out.println(line);
        }
        connection.disconnect();
        in.close();
        System.out.println("Connection closed: ");
        return response;
    }

    public void sendResponse(ArrayList<String> response, OutputStream targetOutput) throws IOException {
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(targetOutput, StandardCharsets.UTF_8));
        out.write("HTTP/1.0 200 OK\r\n");
        out.write("Date: Fri, 18 Jul 2017 14:21:40 GMT\r\n");

        out.write("Connection: close\r\n");
        out.write("Last-modified: Fri, 18 Jul 2017 14:21:40 GMT\r\n");
        out.write("\r\n");
        for (String line : response
                ) {
            out.write(line);
        }
        out.flush();
        System.out.println("Response was send\n");
        out.close();
        targetOutput.close();
    }
}
