package BT2_Calculate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Stack;

public class Server {
    public static void main(String[] args) throws Exception {
        try (ServerSocket ss = new ServerSocket(6666)) {
            System.out.println("Server đang lắng nghe trên cổng 6666...");
            
            while (true) {
                Socket sk = ss.accept();
                System.out.println("Client đã kết nối.");
                
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(sk.getInputStream()));
                     PrintWriter writer = new PrintWriter(sk.getOutputStream(), true)) {
                     
                    String input;
                    while ((input = reader.readLine()) != null) {
                        System.out.println("Phép tính nhận được từ client: " + input);
                        double result=calculateExpression(input);
                        writer.println(result);
                    }
                } catch (IOException e) {
                    System.out.println("Lỗi trong khi xử lý client: " + e.getMessage());
                } finally {
                    sk.close(); 
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
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
