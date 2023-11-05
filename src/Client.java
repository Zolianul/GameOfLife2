import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java Client <username>");
            System.exit(1);
        }

        final String SERVER_ADDRESS = "127.0.0.1";
        final int SERVER_PORT = 5000;
        final String username = args[0];
        System.out.println("Client started with username: " + username);
        try {
            Socket clientSocket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            Scanner sc = new Scanner(System.in);

            Thread sender = new Thread(() -> {
                try {
                    while (true) {
                        String msg = sc.nextLine();
                        out.println(username + ": " + msg); // Include the username in the message
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            sender.start();

            Thread receiver = new Thread(() -> {
                try {
                    while (true) {
                        String msg = in.readLine();
                        if (msg != null && !msg.startsWith(username + ": ")) {
                            System.out.println(msg);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            receiver.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
