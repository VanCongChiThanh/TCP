package BT3_Chat_khonggiaodien;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 6869;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in))) {

            System.out.println("Kết nối đến Server thành công.");

            Thread receiveThread = new Thread(() -> {
                try {
                    String receivedMessage;
                    while ((receivedMessage = in.readLine()) != null) {
                        System.out.println("Server: " + receivedMessage);
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
