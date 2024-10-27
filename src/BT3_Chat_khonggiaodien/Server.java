package BT3_Chat_khonggiaodien;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private static final int SERVER_PORT = 6869;

    public static void main(String[] args) {
        System.out.println("Server đang chờ kết nối...");

        try (ServerSocket serverSocket = new ServerSocket(SERVER_PORT);
             Socket clientSocket = serverSocket.accept();
             BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
             BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in))) {

            System.out.println("Client đã kết nối.");

            Thread receiveThread = new Thread(() -> {
                try {
                    String receivedMessage;
                    while ((receivedMessage = in.readLine()) != null) {
                        System.out.println("Client: " + receivedMessage);
                    }
                } catch (IOException e) {
                    System.out.println("Kết nối bị ngắt.");
                }
            });
            receiveThread.start();

            String messageToSend;
            while ((messageToSend = consoleReader.readLine()) != null) {
                out.println(messageToSend);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
