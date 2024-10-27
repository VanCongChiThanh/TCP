package BT1_String;
import java.awt.EventQueue;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.awt.event.ActionEvent;

public class Server extends JFrame {
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField txtPort;
    private JTextArea textArea;
    private int PORT = 1234;
    private ServerSocket serverSocket;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                Server frame = new Server();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public Server() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblNewLabel_1 = new JLabel("PORT");
        lblNewLabel_1.setBounds(80, 39, 49, 14);
        contentPane.add(lblNewLabel_1);

        txtPort = new JTextField();
        txtPort.setColumns(10);
        txtPort.setBounds(134, 36, 96, 20);
        contentPane.add(txtPort);

        JButton btnConnect = new JButton("Connect");
        btnConnect.addActionListener(e -> {
            try {
                PORT = Integer.parseInt(txtPort.getText());
                startServer();
            } catch (NumberFormatException ex) {
                textArea.append("Port must be a valid number.\n");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        btnConnect.setBounds(240, 35, 89, 23);
        contentPane.add(btnConnect);

        JLabel lblNewLabel = new JLabel("Server");
        lblNewLabel.setBounds(153, 11, 49, 14);
        contentPane.add(lblNewLabel);

        textArea = new JTextArea();
        textArea.setBounds(10, 78, 416, 167);
        contentPane.add(textArea);
        txtPort.setText(String.valueOf(PORT));
    }

    private void startServer() throws IOException {
        new Thread(() -> {
            try {
                serverSocket = new ServerSocket(PORT);
                textArea.append("Server is running in port:"+PORT+"\n");
                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    textArea.append("Client connected: " + clientSocket.getInetAddress() + "\n");
                    ConvertString cs = new ConvertString(clientSocket);
                    cs.start();
                }
            } catch (IOException e) {
            	textArea.append("Error starting server: " + e.getMessage() + "\n");
            }
        }).start();
    }

}

class ConvertString extends Thread {
    private Socket socket;

    public ConvertString(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)) {

            String input = reader.readLine();
            String response = "Chu hoa: " + input.toUpperCase() + "\n" +
                              "Chu thuong: " + input.toLowerCase() + "\n" +
                              "Chu vua hoa vua thuong: " + swapCase(input) + "\n" +
                              "Chuoi dao nguoc: " + new StringBuilder(input).reverse() + "\n" +
                              "So tu: " + countWords(input) + "\n" +
                              "So nguyen am: " + countVowels(input) + "\n";

            writer.println(response);
        } catch (IOException e) {
            System.out.println("Error handling client: " + e.getMessage());
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String swapCase(String input) {
        StringBuilder swapped = new StringBuilder();
        for (char c : input.toCharArray()) {
            swapped.append(Character.isUpperCase(c) ? Character.toLowerCase(c) : Character.toUpperCase(c));
        }
        return swapped.toString();
    }

    private int countWords(String input) {
        return (input == null || input.isEmpty()) ? 0 : input.trim().split("\\s+").length;
    }

    private int countVowels(String input) {
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
