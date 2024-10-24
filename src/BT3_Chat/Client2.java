package BT3_Chat;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextArea;
import javax.swing.JButton;
import java.awt.Font;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;

public class Client2 extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField textField;
    private JTextArea textArea;
    private ChatClient chatClient;
    private static final int PORT = 6888;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                Client2 frame = new Client2();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public Client2() {
        chatClient = new ChatClientImpl();
        chatClient.connectToServer("localhost", PORT);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 537, 399);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setBounds(73, 36, 369, 235);
        contentPane.add(textArea);

        JButton btnSend = new JButton("Send");
        btnSend.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });
        btnSend.setFont(new Font("Tahoma", Font.PLAIN, 14));
        btnSend.setBounds(209, 328, 89, 34);
        contentPane.add(btnSend);

        textField = new JTextField();
        textField.setFont(new Font("Tahoma", Font.PLAIN, 12));
        textField.setBounds(73, 289, 369, 28);
        contentPane.add(textField);
        textField.setColumns(10);

        JLabel lblNewLabel = new JLabel("Client 2");
        lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
        lblNewLabel.setBounds(235, 11, 63, 14);
        contentPane.add(lblNewLabel);

        chatClient.startReceivingMessages(this::updateTextArea);
    }

    private void sendMessage() {
        String message = textField.getText();
        if (!message.isEmpty()) {
            textArea.append("Me: " + message + "\n");
            chatClient.sendMessage(message);
            textField.setText("");
        }
    }

    private void updateTextArea(String message) {
        if (!message.startsWith("Client2:")) {
            textArea.append(message + "\n");
        }
    }

    private class ChatClientImpl extends ChatClient {
        @Override
        protected String getClientName() {
            return "Client2";
        }
    }
}
