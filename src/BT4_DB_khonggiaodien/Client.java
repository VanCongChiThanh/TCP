package BT4_DB_khonggiaodien;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 3333;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in))) {

            System.out.println("Kết nối tới server thành công!");
            String userInput;

            while (true) {
                System.out.print("Nhập câu lệnh SQL (hoặc nhập 'exit' để thoát): ");
                userInput = consoleReader.readLine();

                if ("exit".equalsIgnoreCase(userInput)) {
                    System.out.println("Đã ngắt kết nối với server.");
                    break;
                }

                writer.println(userInput);
                String serverResponse;
                while ((serverResponse = reader.readLine()) != null && !serverResponse.isEmpty()) {
                    System.out.println("Server: " + serverResponse);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
