package BT4_DB_khonggiaodien;

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

public class Server {
    private static final int PORT = 3333;
	private static final String DB_URL = "jdbc:mysql://localhost:3306/login?useSSL=false";
	private static final String DB_USERNAME = "root";
	private static final String DB_PASSWORD = "12345Thanh";

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server đang lắng nghe trên cổng " + PORT + "...");
            Socket clientSocket = serverSocket.accept();
            System.out.println("Client đã kết nối.");

            handleClient(clientSocket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleClient(Socket clientSocket) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true)) {

            String request;
            while ((request = reader.readLine()) != null) {
                request = request.trim();
                String response;

                if (request.toUpperCase().startsWith("SELECT")) {
                    response = executeSelect(request);
                } else if (request.toUpperCase().startsWith("INSERT") ||
                           request.toUpperCase().startsWith("UPDATE") ||
                           request.toUpperCase().startsWith("DELETE")) {
                    response = executeUpdate(request);
                } else {
                    response = "Yêu cầu không hợp lệ";
                }

                writer.println(response);
                writer.println("");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
                System.out.println("Client đã ngắt kết nối.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

	private static Connection getConnection() throws SQLException {
	    return DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
	}
	private static String executeSelect(String query) {
	    StringBuilder result = new StringBuilder();

	    try (Connection connection = getConnection();
	         Statement statement = connection.createStatement();
	         ResultSet resultSet = statement.executeQuery(query)) {

	        while (resultSet.next()) {
	            result.append("Username: ").append(resultSet.getString("username")).append(", ");
	            result.append("Password: ").append(resultSet.getString("password")).append("\n");
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	        return "Lỗi khi thực hiện truy vấn SELECT: " + e.getMessage();
	    }

	    return result.toString();
	}

	private static String executeUpdate(String query) {
	    try (Connection connection = getConnection();
	         Statement statement = connection.createStatement()) {

	        int rowsAffected = statement.executeUpdate(query);
	        
	        if (query.toUpperCase().startsWith("INSERT")) {
	            return rowsAffected > 0 ? "Thêm dữ liệu thành công!" : "Không có dữ liệu nào được thêm.";
	        } else if (query.toUpperCase().startsWith("UPDATE")) {
	            return rowsAffected > 0 ? "Cập nhật dữ liệu thành công!" : "Không có dữ liệu nào được cập nhật.";
	        } else if (query.toUpperCase().startsWith("DELETE")) {
	            return rowsAffected > 0 ? "Xóa dữ liệu thành công!" : "Không có dữ liệu nào được xóa.";
	        } else {
	            return "Thao tác không xác định.";
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	        return "Lỗi khi thực hiện thao tác: " + e.getMessage();
	    }
	}
}
