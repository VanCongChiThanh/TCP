package BT1_String_khonggiaodien;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 6867;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             Scanner scanner = new Scanner(System.in)) {

            System.out.println("Kết nối tới server thành công.");
            System.out.println("Nhập chuỗi văn bản để gửi đến server (hoặc 'exit' để thoát):");

            while (true) {
                System.out.print("Chuoi: ");
                String input = scanner.nextLine();

                if ("exit".equalsIgnoreCase(input)) {
                    System.out.println("Đã ngắt kết nối với server.");
                    break;
                }

                out.println(input);

                StringBuilder response = new StringBuilder();
                String line;
                while ((line = in.readLine()) != null && !line.isEmpty()) {
                    response.append(line).append("\n");
                }
                
                System.out.println("Server:\n" + response.toString());
            }

        } catch (IOException e) {
            System.out.println("Không thể kết nối tới server: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
