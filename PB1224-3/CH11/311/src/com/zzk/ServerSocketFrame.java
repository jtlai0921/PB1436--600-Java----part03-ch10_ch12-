package com.zzk;

import java.awt.BorderLayout;
import java.io.*;
import java.net.*;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class ServerSocketFrame extends JFrame {
    private JTextArea ta_info;// 宣告純文字域,用於顯示連接資訊和接收到的資訊
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
                ta_info.append("連接成功。\n"); // 輸出資訊
                reader = new BufferedReader(new InputStreamReader(socket
                        .getInputStream())); // 實例化BufferedReader對像
                getClientInfo(); // 呼叫getClientInfo()方法
            }
        } catch (Exception e) {
            e.printStackTrace(); // 輸出例外資訊
        }
    }
    
    private void getClientInfo() {
        try {
            while (true) { // 如果套接字是連接狀態
                ta_info.append("接收到客戶機發送的資訊：" + reader.readLine() + "\n"); // 獲得客戶端資訊
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
        setBounds(100, 100, 336, 257);
        
        final JScrollPane scrollPane = new JScrollPane();
        getContentPane().add(scrollPane, BorderLayout.CENTER);
        
        ta_info = new JTextArea();
        scrollPane.setViewportView(ta_info);
    }
}
