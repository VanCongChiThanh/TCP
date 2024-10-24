package BT3_Chat;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;

public class ServerChat extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField textField;
    private static JTextArea textArea;
    private static ServerSocket serverSocket;
    private static final int PORT = 6888;
    private static List<PrintWriter> clientWriters = new ArrayList<>(); 

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                ServerChat frame = new ServerChat();
                frame.setVisible(true);
                startServer();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public ServerChat() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 524, 440);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        textArea = new JTextArea();
        textArea.setBounds(44, 57, 430, 211);
        contentPane.add(textArea);

        JLabel lblNewLabel = new JLabel("Server");
        lblNewLabel.setBounds(225, 32, 49, 14);
        contentPane.add(lblNewLabel);

        JButton btnSend = new JButton("Send");
        btnSend.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });
        btnSend.setBounds(204, 347, 89, 23);
        contentPane.add(btnSend);

        textField = new JTextField();
        textField.setBounds(44, 305, 430, 20);
        contentPane.add(textField);
        textField.setColumns(10);

 
    }

    private static void startServer() {
        new Thread(() -> {
            try {
                serverSocket = new ServerSocket(PORT);
                textArea.append("Server đang lắng nghe trên cổng "+PORT+"...\n");
                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    textArea.append("Client đã kết nối.\n");
                    new Thread(() -> handleClient(clientSocket)).start();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start(); 
    }

    private static void broadcastMessage(String message) {
        synchronized (clientWriters) {

            for (PrintWriter writer : clientWriters) {
                try {
                    writer.println(message);
                } catch (Exception e) {
                    e.printStackTrace();  
                }
            }

        }
    }
  
    private static void handleClient(Socket clientSocket) {
        PrintWriter writer = null;
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            writer = new PrintWriter(clientSocket.getOutputStream(), true);

            synchronized (clientWriters) {
                clientWriters.add(writer);
            }

            String message;
            while ((message = reader.readLine()) != null) {
                textArea.append(message + "\n");
                broadcastMessage(message);
            }
        } catch (IOException e) {
            textArea.append("Client ngắt kết nối.\n"); 
        } finally {
            if (writer != null) {
                synchronized (clientWriters) {
                    clientWriters.remove(writer);
                }
            }
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    private void sendMessage() {
        String message = textField.getText();
        if (!message.isEmpty()) {
        	textArea.append("Me:"+message+"\n");
            broadcastMessage("Server:" + message);
            textField.setText("");
        }
    }

}
