package com.zzk;

import java.awt.BorderLayout;
import java.net.*;
import javax.swing.*;
import javax.swing.JScrollPane;

public class ClientSocketFrame extends JFrame { // 建立類別繼承JFrame類別
    private Socket socket; // 宣告Socket對像
    private JTextArea ta_info = new JTextArea(); // 建立JtextArea對像
    
    public ClientSocketFrame() { // 建構方法
        super(); // 呼叫父類別的建構方法
        setTitle("建立客戶端套接字");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 254, 166);
        final JScrollPane scrollPane = new JScrollPane();
        getContentPane().add(scrollPane, BorderLayout.CENTER);
        scrollPane.setViewportView(ta_info);
    }
    
    private void connect() { // 連接套接字方法
        ta_info.append("嘗試連接......\n"); // 純文字域中資訊資訊
        try { // 捕捉例外
            socket = new Socket("127.0.0.1", 1978); // 實例化Socket對像
            ta_info.append("完成連接。\n"); // 純文字域中提示資訊
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