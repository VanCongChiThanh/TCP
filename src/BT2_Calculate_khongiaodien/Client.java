package BT2_Calculate_khongiaodien;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 6666;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             Scanner scanner = new Scanner(System.in)) {

            System.out.println("Đã kết nối đến server. Nhập phép tính hoặc 'exit' để thoát.");

            while (true) {
                System.out.print("Nhập phép tính: ");
                String input = scanner.nextLine();

                if ("exit".equalsIgnoreCase(input)) {
                    System.out.println("Ngắt kết nối.");
                    break;
                }

                writer.println(input);  
                String result = reader.readLine();  

                System.out.println("Kết quả từ server: " + result);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
