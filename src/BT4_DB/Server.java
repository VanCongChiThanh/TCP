package BT4_DB;

import java.awt.EventQueue;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTextArea;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Server extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField textField;
    private static JTextArea textArea;
    private static final String DB_URL = "jdbc:mysql://localhost:3306/login?useSSL=false";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "12345Thanh";
    private static int PORT = 6767;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Server frame = new Server();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public Server() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 530, 293);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblNewLabel_1 = new JLabel("PORT");
        lblNewLabel_1.setBounds(100, 39, 49, 14);
        contentPane.add(lblNewLabel_1);

        textField = new JTextField();
        textField.setColumns(10);
        textField.setBounds(154, 36, 96, 20);
        contentPane.add(textField);

        JButton btnConnect = new JButton("Connect");
        btnConnect.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    PORT = Integer.parseInt(textField.getText());
                    startServer();
                } catch (NumberFormatException ex) {
                    textArea.append("Error: Port must be a number.\n");
                }
            }
        });
        btnConnect.setBounds(260, 35, 89, 23);
        contentPane.add(btnConnect);

        JLabel lblNewLabel = new JLabel("Server");
        lblNewLabel.setBounds(173, 11, 49, 14);
        contentPane.add(lblNewLabel);

        textArea = new JTextArea();
        textArea.setBounds(30, 78, 462, 167);
        contentPane.add(textArea);
        textField.setText(String.valueOf(PORT));
    }

    private void startServer() {
        new Thread(() -> {
            try {
                ServerSocket serverSocket = new ServerSocket(PORT);
                textArea.append("Server started on port " + PORT + "\n");
                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    textArea.append("Client connected: " + clientSocket.getInetAddress() + "\n");
                    new Thread(() -> handleClient(clientSocket)).start();
                }
            } catch (Exception ex) {
                textArea.append("Error starting server: " + ex.getMessage() + "\n");
            }
        }).start(); 
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
                    response = "Invalid request";
                }
                writer.println(response);
            }

        } catch (Exception e) {
            textArea.append("Client error: " + e.getMessage() + "\n");
            e.printStackTrace();
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
            return "Error executing SELECT query: " + e.getMessage();
        }

        return result.toString();
    }

    private static String executeUpdate(String query) {
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {

            int rowsAffected = statement.executeUpdate(query);

            if (query.toUpperCase().startsWith("INSERT")) {
                return rowsAffected > 0 ? "Data inserted successfully!" : "No data was inserted.";
            } else if (query.toUpperCase().startsWith("UPDATE")) {
                return rowsAffected > 0 ? "Data updated successfully!" : "No data was updated.";
            } else if (query.toUpperCase().startsWith("DELETE")) {
                return rowsAffected > 0 ? "Data deleted successfully!" : "No data was deleted.";
            } else {
                return "Unknown operation.";
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return "Error executing operation: " + e.getMessage();
        }
    }
}
