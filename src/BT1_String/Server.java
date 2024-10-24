package BT1_String;

import java.awt.EventQueue;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class Server extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
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

	/**
	 * Create the frame.
	 */
	public Server() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		
		ResponseToClient();
	}
	public void ResponseToClient() {
		try {
			ServerSocket ss=new ServerSocket(6867);
			
		    while(true) {
		    	Socket sk=ss.accept();
		    	 
		    	BufferedReader reader=new BufferedReader(new InputStreamReader(sk.getInputStream()));
		    	
		    	PrintWriter writer=new PrintWriter(sk.getOutputStream(),true);
		    	

	            String input = reader.readLine();
	            
	            String response = "Chu hoa: " + input.toUpperCase() + "\n" +
	                              "Chu thuong: " + input.toLowerCase() + "\n" +
	                              "Chu vua hoa vua thuong: " + swapCase(input) + "\n"+
	                              "Chuoi dao nguoc: " + new StringBuilder(input).reverse()+"\n"+
	                              "So tu: " + countWords(input) + "\n" +
	                              "So nguyen am: " + countVowels(input) + "\n";
	            
	            writer.println(response);
	            sk.close(); 
		    		
		    }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
    private String swapCase(String input) {
        StringBuilder swapped = new StringBuilder();
        for (char c : input.toCharArray()) {
            if (Character.isUpperCase(c)) {
                swapped.append(Character.toLowerCase(c));
            } else if (Character.isLowerCase(c)) {
                swapped.append(Character.toUpperCase(c));
            } else {
                swapped.append(c); 
            }
        }
        return swapped.toString();
    }
    private int countWords(String input) {
        if (input == null || input.isEmpty()) {
            return 0;
        }
        String[] words = input.trim().split("\\s+");
        return words.length;
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
