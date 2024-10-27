package BT4_DB;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import javax.swing.*;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Font;

public class Client extends JFrame {
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField queryField;
    private JTextArea responseArea;
    private JTextField portField;
    private JButton btnConnect, btnSend;
    private Socket socket;
    private PrintWriter writer;
    private BufferedReader reader;
    private boolean isConnected = false;

    private static final String SERVER_HOST = "localhost"; 

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                Client frame = new Client();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public Client() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 500, 350);
        contentPane = new JPanel();
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblQuery = new JLabel("SQL Query:");
        lblQuery.setBounds(10, 59, 70, 14);
        contentPane.add(lblQuery);

        queryField = new JTextField();
        queryField.setBounds(90, 56, 250, 20);
        contentPane.add(queryField);
        queryField.setColumns(10);

        btnSend = new JButton("Send");
        btnSend.setBounds(350, 55, 70, 23);
        btnSend.setEnabled(false); // Disable until connected
        contentPane.add(btnSend);

        responseArea = new JTextArea();
        responseArea.setBounds(10, 100, 460, 200);
        contentPane.add(responseArea);

        JLabel lblPort = new JLabel("Port:");
        lblPort.setBounds(90, 14, 49, 14);
        contentPane.add(lblPort);

        portField = new JTextField("6767");
        portField.setBounds(149, 11, 96, 20);
        contentPane.add(portField);
        
        btnConnect = new JButton("Connect");
        btnConnect.setFont(new Font("Tahoma", Font.PLAIN, 14));
        btnConnect.setBounds(255, 5, 100, 28);
        contentPane.add(btnConnect);

        btnConnect.addActionListener(e -> {
            try {
                int port = Integer.parseInt(portField.getText());
                connectToServer(port);
            } catch (NumberFormatException ex) {
                responseArea.append("Please enter a valid port number.\n");
            }
        });

        btnSend.addActionListener(e -> {
            String query = queryField.getText();
            if (!query.isEmpty() && isConnected) {
                sendRequestToServer(query);
            } else {
                responseArea.append("Please enter a query and ensure connection.\n");
            }
        });
    }

    private void connectToServer(int port) {
        try {
            if (isConnected) {
                socket.close();
                isConnected = false;
            }

            socket = new Socket(SERVER_HOST, port);
            writer = new PrintWriter(socket.getOutputStream(), true);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            isConnected = true;
            btnSend.setEnabled(true);
            responseArea.append("Connected to server on port " + port + ".\n");

        } catch (Exception e) {
            responseArea.append("Connection error: " + e.getMessage() + "\n");
            isConnected = false;
            btnSend.setEnabled(false);
        }
    }

    private void sendRequestToServer(String query) {
        try {
            writer.println(query);
            String line;
            responseArea.append("Server response:\n");
            while ((line = reader.readLine()) != null) {
                if (line.isEmpty()) {  
                    break;
                }
                responseArea.append(line + "\n");
            }
        } catch (Exception e) {
            responseArea.append("Error: " + e.getMessage() + "\n");
        }
    }

}
