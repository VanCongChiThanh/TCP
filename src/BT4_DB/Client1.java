package BT4_DB;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

public class Client1 extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextArea textArea;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                Client1 frame = new Client1();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public Client1() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 554, 381);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        
        textArea = new JTextArea();
        textArea.setBounds(60, 60, 434, 250);
        contentPane.add(textArea);
        
        JButton btnFetchData = new JButton("Lấy thông tin bảng user");
        btnFetchData.setBounds(192, 26, 149, 23);
        btnFetchData.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fetchData();
            }
        });
        contentPane.add(btnFetchData);
    }

    private void fetchData() {
        try (Socket socket = new Socket("localhost", 3333);
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
             
            writer.println("GET_USERS"); 
            String response;
            StringBuilder result = new StringBuilder();

            while ((response = reader.readLine()) != null) {
                result.append(response).append("\n");
            }

            textArea.setText(result.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
