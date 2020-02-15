package com.zzk;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ServerSocketFrame extends JFrame {
    private JTextField tf_send;
    private JTextArea ta_info;
    private PrintWriter writer; // 宣告PrintWriter類別對像
    private BufferedReader reader; // 宣告BufferedReader對像
    private ServerSocket server; // 宣告ServerSocket對像
    private Socket socket; // 宣告Socket對像socket
    
    public void getServer() {
        try {
            server = new ServerSocket(1978); // 實例化Socket對像
            ta_info.append("服務器套接字已經建立成功\n"); // 輸出資訊
            while (true) { // 如果套接字是連接狀態
                ta_info.append("等待客戶機的連接......\n"); // 輸出資訊
                socket = server.accept(); // 實例化Socket對像
                reader = new BufferedReader(new InputStreamReader(socket
                        .getInputStream())); // 實例化BufferedReader對像
                writer = new PrintWriter(socket.getOutputStream(), true);
                getClientInfo(); // 呼叫getClientInfo()方法
            }
        } catch (Exception e) {
            e.printStackTrace(); // 輸出例外資訊
        }
    }
    
    private void getClientInfo() {
        try {
            while (true) {
                String line = reader.readLine();// 讀取客戶端發送的資訊
                if (line != null)
                    ta_info.append("接收到客戶機發送的資訊：" + line + "\n"); // 顯示客戶端發送的資訊
            }
        } catch (Exception e) {
            ta_info.append("客戶端已退出。\n"); // 輸出例外資訊
        } finally {
            try {
                if (reader != null) {
                    reader.close();// 關閉流
                }
                if (socket != null) {
                    socket.close(); // 關閉套接字
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    public static void main(String[] args) { // 主方法
        ServerSocketFrame frame = new ServerSocketFrame(); // 建立本類別對像
        frame.setVisible(true);
        frame.getServer(); // 呼叫方法
    }
    
    public ServerSocketFrame() {
        super();
        setTitle("服務器端程式");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 379, 260);
        
        final JScrollPane scrollPane = new JScrollPane();
        getContentPane().add(scrollPane, BorderLayout.CENTER);
        
        ta_info = new JTextArea();
        scrollPane.setViewportView(ta_info);
        
        final JPanel panel = new JPanel();
        getContentPane().add(panel, BorderLayout.SOUTH);
        
        final JLabel label = new JLabel();
        label.setText("服務器發送的資訊：");
        panel.add(label);
        
        tf_send = new JTextField();
        tf_send.setPreferredSize(new Dimension(150, 25));
        panel.add(tf_send);

        final JButton button = new JButton();
        button.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                writer.println(tf_send.getText()); // 將純文字框中資訊寫入流
                ta_info.append("服務器發送的資訊是：" + tf_send.getText() + "\n"); // 將純文字框中資訊顯示在純文字域中
                tf_send.setText(""); // 將純文字框清空
            }
        });
        button.setText("發  送");
        panel.add(button);

        final JPanel panel_1 = new JPanel();
        getContentPane().add(panel_1, BorderLayout.NORTH);

        final JLabel label_1 = new JLabel();
        label_1.setForeground(new Color(0, 0, 255));
        label_1.setFont(new Font("", Font.BOLD, 22));
        label_1.setText("一對一通訊——服務器端程式");
        panel_1.add(label_1);
    }
}
