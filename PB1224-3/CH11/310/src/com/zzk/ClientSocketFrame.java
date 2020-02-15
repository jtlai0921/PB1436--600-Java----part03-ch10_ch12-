package com.zzk;

import java.awt.BorderLayout;
import java.net.*;
import javax.swing.*;
import javax.swing.JScrollPane;

public class ClientSocketFrame extends JFrame { // 建立類別繼承JFrame類別
    private Socket socket; // 宣告Socket對像
    private JTextArea ta = new JTextArea(); // 建立JtextArea對像
    
    public ClientSocketFrame() { // 建構方法
        super(); // 呼叫父類別的建構方法
        setTitle("獲得Socket資訊");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 351, 257);
        final JScrollPane scrollPane = new JScrollPane();
        getContentPane().add(scrollPane, BorderLayout.CENTER);
        scrollPane.setViewportView(ta);
    }
    
    private void connect() { // 連接套接字方法
        ta.append("嘗試連接......\n"); // 純文字域中資訊資訊
        try { // 捕捉例外
            socket = new Socket("localhost", 1978); // 實例化Socket對像
            ta.append("完成連接。\n"); // 純文字域中提示資訊
            InetAddress netAddress = socket.getInetAddress();// 獲得遠端服務器的地址
            String netIp = netAddress.getHostAddress();// 獲得遠端服務器的IP地址 
            int netPort = socket.getPort();// 獲得遠端服務器的通訊埠號
            InetAddress localAddress = socket.getLocalAddress();// 獲得客戶端的地址
            String localIp = localAddress.getHostAddress();// 獲得客戶端的IP地址
            int localPort = socket.getLocalPort();// 獲得客戶端的通訊埠號
            ta.append("遠端服務器的IP地址：" + netIp + "\n");
            ta.append("遠端服務器的通訊埠號：" + netPort + "\n");
            ta.append("客戶機本機的IP地址：" + localIp + "\n");
            ta.append("客戶機本機的通訊埠號：" + localPort + "\n");
        } catch (Exception e) {
            e.printStackTrace(); // 輸出例外資訊
        }
    }
    
    public static void main(String[] args) { // 主方法
        ClientSocketFrame clien = new ClientSocketFrame(); // 建立本例對像
        clien.setVisible(true); // 將窗體顯示
        clien.connect(); // 呼叫連接方法
    }
}
