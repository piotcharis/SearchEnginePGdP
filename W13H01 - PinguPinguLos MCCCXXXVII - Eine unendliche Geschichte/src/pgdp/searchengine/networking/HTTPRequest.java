package pgdp.searchengine.networking;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

public class HTTPRequest {
    private final String host;
    private final String path;

    public HTTPRequest(String host, String path) {
        this.host = host;
        this.path = path;
    }

    public String getHost() {
        return host;
    }

    public String getPath() {
        return path;
    }

    public HTTPResponse send(int port) {
        try (SSLSocket socket = (SSLSocket) SSLSocketFactory.getDefault().createSocket(host, port)) {
            PrintStream out = new PrintStream(socket.getOutputStream());
            out.print("GET /" + path + " HTTP/1.1\r\nHost: " + host + "\r\n\r\n");
            out.flush();

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            StringBuilder receivedString = new StringBuilder();
            String line = in.readLine();
            while (line != null) {
                receivedString.append(line);
                line = in.readLine();
            }
            out.close();
            in.close();

            return new HTTPResponse(receivedString.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
