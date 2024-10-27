package BT1_String_khonggiaodien;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private static final int PORT = 6867;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server đang chạy và lắng nghe trên cổng " + PORT);

            while (true) {
                try (Socket clientSocket = serverSocket.accept();
                     BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                     PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true)) {

                    System.out.println("Đã kết nối với client");


                    String input = reader.readLine();
                    System.out.println("Nhận từ client: " + input);


                    String response = "Chu hoa: " + input.toUpperCase() + "\n" +
                                      "Chu thuong: " + input.toLowerCase() + "\n" +
                                      "Chu vua hoa vua thuong: " + swapCase(input) + "\n" +
                                      "Chuoi dao nguoc: " + new StringBuilder(input).reverse() + "\n" +
                                      "So tu: " + countWords(input) + "\n" +
                                      "So nguyen am: " + countVowels(input) + "\n";

                    writer.println(response);
                    System.out.println("Đã gửi kết quả cho client");
                } catch (IOException e) {
                    System.out.println("Lỗi khi xử lý client: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("Không thể khởi động server: " + e.getMessage());
        }
    }

    private static String swapCase(String input) {
        StringBuilder swapped = new StringBuilder();
        for (char c : input.toCharArray()) {
            if (Character.isUpperCase(c)) {
                swapped.append(Character.toLowerCase(c));
            } else if (Character.isLowerCase(c)) {
                swapped.append(Character.toUpperCase(c));
            } else {
                swapped.append(c);
            }
        }
        return swapped.toString();
    }

    private static int countWords(String input) {
        if (input == null || input.isEmpty()) {
            return 0;
        }
        String[] words = input.trim().split("\\s+");
        return words.length;
    }

    private static int countVowels(String input) {
        int count = 0;
        String vowels = "aeiouAEIOU";
        for (char c : input.toCharArray()) {
            if (vowels.indexOf(c) != -1) {
                count++;
            }
        }
        return count;
    }
}
