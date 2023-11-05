import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

public class Server {
    public static void main(String[] args) {
        final int PORT = 5000;
        List<PrintWriter> clientWriters = new ArrayList<>();

        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Server is running and listening on port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket);

                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                clientWriters.add(out);

                Thread clientHandler = new Thread(new ClientHandler(clientSocket, out, clientWriters));
                clientHandler.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class ClientHandler implements Runnable {
    private Socket clientSocket;
    private PrintWriter out;
    private List<PrintWriter> clientWriters;
    private String username; // Add username field

    public ClientHandler(Socket socket, PrintWriter out, List<PrintWriter> clientWriters) {
        this.clientSocket = socket;
        this.out = out;
        this.clientWriters = clientWriters;
    }

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String msg;

            while (true) {
                msg = in.readLine();
                if (msg == null) {
                    break; // Client disconnected
                }

                // Broadcast the message to all connected clients except the sender
                for (PrintWriter writer : clientWriters) {
                    if (writer != out) {
                        writer.println(msg);
                    }
                }
            }

            // Client disconnected, remove its PrintWriter
            clientWriters.remove(out);
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
