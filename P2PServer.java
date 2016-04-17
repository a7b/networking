import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class P2PServer {

    ServerSocket server;
    static ArrayList <String> messages;

    public static void main(String[] args) {
        new P2PServer(4500);
    }

    public P2PServer(int port) {
        try {
            server = new ServerSocket(port);
            messages = new ArrayList <String> ();
            System.out.println(InetAddress.getLocalHost().getHostAddress() + " - " + port);

            // If the server was successfully started
            while (true) {
                Handler h = new Handler(server.accept());
                h.start();
            }
        } catch (IOException e) {}
    }

    // This handles P2PServer requests
    static class Handler extends Thread {
        Socket socket;
        BufferedReader input;
        PrintWriter output;
        int update = 0;

        public Handler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                output = new PrintWriter(socket.getOutputStream(), true);

                // Keep talking to this client until they disconnect
                while (line = input.readLine() != null) {
                    String line = input.readLine();
                    
                    messages.add(line);

                    // Send all updates to the client
                    for (int i = update ; i < messages.size() ; i++) {
                        output.println(messages.get(i));
                    }
                    update = messages.size();
                }

                input.close();
                output.close();
                socket.close();

            } catch (IOException e) {}
        }
    }

}
