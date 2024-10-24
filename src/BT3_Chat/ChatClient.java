package BT3_Chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public abstract class ChatClient {
    protected Socket socket;
    protected PrintWriter writer;
    protected BufferedReader reader;

    protected void connectToServer(String host, int port) {
        try {
            socket = new Socket(host, port);
            writer = new PrintWriter(socket.getOutputStream(), true);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void sendMessage(String message) {
        if (!message.isEmpty()) {
            writer.println(getClientName() + ": " + message);
        }
    }

    protected abstract String getClientName(); 

    public void startReceivingMessages(ReceiveMessageListener listener) {
        new Thread(() -> {
            try {
                String response;
                while ((response = reader.readLine()) != null) {
                    listener.onMessageReceived(response);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public interface ReceiveMessageListener {
        void onMessageReceived(String message);
    }
}
