import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Server {
    public static void main(String[] args) {
        final int PORT = 5000;
        List<PrintWriter> clientWriters = new ArrayList<>();
        Map<String, PrintWriter> clientWritersMap = new HashMap<>();

        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Server is running and listening on port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket);

                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                clientWriters.add(out);

                Thread clientHandler = new Thread(new ClientHandler(clientSocket, out, clientWriters, clientWritersMap));
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
    private Map<String, PrintWriter> clientWritersMap;

    public ClientHandler(Socket socket, PrintWriter out, List<PrintWriter> clientWriters, Map<String, PrintWriter> clientWritersMap) {
        this.clientSocket = socket;
        this.out = out;
        this.clientWriters = clientWriters;
        this.clientWritersMap = clientWritersMap;
    }

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String username = in.readLine();
            clientWritersMap.put(username, out);

            while (true) {
                String msg = in.readLine();
                if (msg == null) {
                    break; // Client disconnected
                }

                if (msg.startsWith("@")) {
                    String[] parts = msg.split(" ", 2);
                    if (parts.length == 2) {
                        String recipient = parts[0].substring(1);
                        sendPrivateMessage(username, recipient, parts[1]);
                    }
                } else {
                    for (PrintWriter writer : clientWriters) {
                        writer.println(username + ": " + msg);
                    }
                }
            }

            clientWritersMap.remove(username);
            clientWriters.remove(out);
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendPrivateMessage(String sender, String recipient, String message) {
        PrintWriter recipientWriter = clientWritersMap.get(recipient);
        if (recipientWriter != null) {
            recipientWriter.println(sender + " (private to " + recipient + "): " + message);
        }
    }
}
