package BT4_DB;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private static final int PORT = 3333;
    private static final ExecutorService threadPool = Executors.newFixedThreadPool(10);

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server đang lắng nghe trên cổng " + PORT + "...");
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client đã kết nối.");
                threadPool.execute(() -> handleClient(clientSocket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleClient(Socket clientSocket) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true)) {

            String request = reader.readLine(); 
            if ("GET_USERS".equals(request)) {
                String users = getUsersFromDatabase();
                writer.println(users); 
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static String getUsersFromDatabase() {
        StringBuilder result = new StringBuilder();
        String url = "jdbc:mysql://localhost:3306/login?useSSL=false"; 
        String username = "root"; 
        String password = "12345Thanh"; 

        try (Connection connection = DriverManager.getConnection(url, username, password);
             Statement statement = connection.createStatement()) {

            String query = "SELECT username, password FROM account";
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                result.append("Username: ").append(resultSet.getString("username")).append(", ");
                result.append("Password: ").append(resultSet.getString("password")).append("\n");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result.toString();
    }
}
