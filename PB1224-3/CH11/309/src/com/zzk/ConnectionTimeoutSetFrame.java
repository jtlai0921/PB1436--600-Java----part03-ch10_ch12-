package com.zzk;

import java.awt.BorderLayout;
import java.io.*;
import java.net.*;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

@SuppressWarnings("serial")
public class ConnectionTimeoutSetFrame extends JFrame {
    private JTextArea ta_info;
    private ServerSocket server; // 宣告ServerSocket對像
    public void getserver() {
        try {
            server = new ServerSocket(1978); // 實例化Socket對像
            server.setSoTimeout(100);// 設定連接逾時時間為10秒
            ta_info.append("服務器套接字已經建立成功\n"); // 輸出資訊
            while (true) { // 如果套接字是連接狀態
                ta_info.append("等待客戶機的連接......\n"); // 輸出資訊
                server.accept();// 等待客戶機連接
            }
        } catch (SocketTimeoutException e) {
            ta_info.append("連接逾時......");
            JOptionPane.showMessageDialog(null, "連接逾時......");
        } catch (IOException e) {
            e.printStackTrace();
        } 
    }
    public static void main(String[] args) { // 主方法
    	ConnectionTimeoutSetFrame frame = new ConnectionTimeoutSetFrame(); // 建立本類別對像
        frame.setVisible(true);
        frame.getserver(); // 呼叫方法
    }
    public ConnectionTimeoutSetFrame() {
        super();
        setTitle("設定等待連接的逾時時間");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 336, 257);
        
        final JScrollPane scrollPane = new JScrollPane();
        getContentPane().add(scrollPane, BorderLayout.CENTER);
        
        ta_info = new JTextArea();
        scrollPane.setViewportView(ta_info);
    }
}
