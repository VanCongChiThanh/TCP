package BT2_Calculate;
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
import java.util.Stack;
import java.awt.event.ActionEvent;

public class Server extends JFrame {
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField txtPort;
    private JTextArea textArea;
    private int PORT = 1233;
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
            double response = calculateExpression(input);

            writer.println(response);
        } catch (Exception e) {
            System.out.println("Error handling client: " + e.getMessage());
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static double calculateExpression(String expression) throws Exception {
        char[] tokens = expression.toCharArray();
        Stack<Double> values = new Stack<>(); 
        Stack<Character> operators = new Stack<>(); 

        for (int i = 0; i < tokens.length; i++) {
            if (tokens[i] == ' ') {
                continue;
            }
            if (Character.isDigit(tokens[i])) {
                StringBuilder sb = new StringBuilder();
                while (i < tokens.length && (Character.isDigit(tokens[i]) || tokens[i] == '.')) {
                    sb.append(tokens[i++]);
                }
                values.push(Double.parseDouble(sb.toString()));
                i--; 
            }
            else if (tokens[i] == '(') {
                operators.push(tokens[i]);
            }
            else if (tokens[i] == ')') {
                while (operators.peek() != '(') {
                    values.push(applyOperation(operators.pop(), values.pop(), values.pop()));
                }
                operators.pop(); 
            }
            else if (isOperator(tokens[i])) {
                while (!operators.isEmpty() && precedence(operators.peek()) >= precedence(tokens[i])) {
                    values.push(applyOperation(operators.pop(), values.pop(), values.pop()));
                }
                operators.push(tokens[i]);
            }
        }
        while (!operators.isEmpty()) {
            values.push(applyOperation(operators.pop(), values.pop(), values.pop()));
        }

        return values.pop();
    }

    private static double applyOperation(char operator, double b, double a) throws Exception {
        switch (operator) {
            case '+':
                return a + b;
            case '-':
                return a - b;
            case '*':
                return a * b;
            case '/':
                if (b == 0) throw new Exception("Không thể chia cho 0");
                return a / b;
        }
        return 0;
    }

    private static int precedence(char operator) {
        switch (operator) {
            case '+':
            case '-':
                return 1;
            case '*':
            case '/':
                return 2;
        }
        return 0;
    }
    private static boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/';
    }
}
