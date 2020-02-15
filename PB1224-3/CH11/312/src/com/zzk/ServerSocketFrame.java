package com.zzk;

import java.awt.BorderLayout;
import java.net.*;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class ServerSocketFrame extends JFrame {
    private JTextArea ta_info;
    private ServerSocket server; // 宣告ServerSocket對像
    private Socket socket; // 宣告Socket對像socket
    
    public ServerSocketFrame() {
        super();
        setTitle("關閉Socket緩衝");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 278, 185);
        final JScrollPane scrollPane = new JScrollPane();
        getContentPane().add(scrollPane, BorderLayout.CENTER);
        
        ta_info = new JTextArea();
        scrollPane.setViewportView(ta_info);
    }
    
    public void getserver() {
        try {
            server = new ServerSocket(1978); // 實例化Socket對像
            ta_info.append("服務器套接字已經建立成功\n"); // 輸出資訊
            while (true) { // 如果套接字是連接狀態
                ta_info.append("等待客戶機的連接......\n"); // 輸出資訊
                ta_info.append("如果連接成功就會關閉Socket緩衝......\n"); // 輸出資訊
                socket = server.accept(); // 實例化Socket對像
                socket.setTcpNoDelay(true);// 關閉Socket緩衝，提昇資料傳輸速度
                ta_info.append("已經關閉Socket緩衝......\n"); // 輸出資訊
            }
        } catch (Exception e) {
            e.printStackTrace(); // 輸出例外資訊
        }
    }
    
    public static void main(String[] args) { // 主方法
        ServerSocketFrame frame = new ServerSocketFrame(); // 建立本類別對像
        frame.setVisible(true);
        frame.getserver(); // 呼叫方法
    }

}
