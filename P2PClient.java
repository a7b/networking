import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class P2PClient {

    // Global socket objects
    Socket socket;
    BufferedReader input;
    PrintWriter output;

    public static void main(String[] args) {

        P2PClient client = new P2PClient("10.1.10.42", 4500) {
            // When I receive a message.
            @Override
            public void received(String message) {
                System.out.println(message);
            }
        };

        // Keep reading inputs and sending it.
        Scanner s = new Scanner(System.in);
        while (true) {
            String line = s.nextLine();
            client.write(line);
        }

    }

    public P2PClient(String ip, int port) {
        try {
            socket = new Socket(ip, port);
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(socket.getOutputStream(), true);

            Thread inputThread = new Thread() {
                public void run() {
                    while (true) {
                        try {
                            String line = input.readLine();
                            if (line != null) {
                                received(line);
                            }
                            else {
                                break;
                            }
                        } catch (IOException e) {
                            break;
                        }
                    }
                }
            };
            inputThread.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void write(String out) {

            output.println(out);

    }

    // By default, do nothing with a received message.
    public void received(String message) {}
}
