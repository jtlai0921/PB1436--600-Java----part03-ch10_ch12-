package com.zzk;

import java.awt.BorderLayout;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class ClientOneToMany_ServerFrame extends JFrame {
    private JTextArea ta_info;
    private ServerSocket server; // 宣告ServerSocket對像
    private Socket socket; // 宣告Socket對像socket
    private Vector<Socket> vector = new Vector<Socket>();// 用於儲存連接到服務器的客戶端套接字對像
    
    public void createSocket() {
        try {
            server = new ServerSocket(1978);
            while (true) {
                ta_info.append("等待新客戶連接......\n");
                socket = server.accept();// 建立套接字對像
                vector.add(socket);// 將套接字對像增加到向量對像中
                ta_info.append("客戶端連接成功。" + socket + "\n");
                new ServerThread(socket).start();// 建立並啟動線程對像
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    class ServerThread extends Thread {
        Socket socket;
        public ServerThread(Socket socket) {
            this.socket = socket;
        }
        public void run() {
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        socket.getInputStream()));// 建立輸入流對像
                while (true) {
                    String info = in.readLine();// 讀取資訊
                    for (Socket s : vector) {// 檢查所有客戶端套接字對像
                        if (s != socket) {// 如果不是發送資訊的套接字對像
                            PrintWriter out = new PrintWriter(s
                                    .getOutputStream(), true);// 建立輸出流對像
                            out.println(info);// 發送資訊
                            out.flush();// 更新輸出緩衝區
                        }
                    }
                }
            } catch (IOException e) {
                ta_info.append(socket + "已經退出。\n");
                vector.remove(socket);// 移除退出的客戶端套接字
            }
        }
    }
    
    /**
     * Launch the application
     * 
     * @param args
     */
    public static void main(String args[]) {
        ClientOneToMany_ServerFrame frame = new ClientOneToMany_ServerFrame();
        frame.setVisible(true);
        frame.createSocket();
    }
    
    /**
     * Create the frame
     */
    public ClientOneToMany_ServerFrame() {
        super();
        setTitle("客戶端一對多通訊——服務器端程式");
        setBounds(100, 100, 385, 266);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        final JScrollPane scrollPane = new JScrollPane();
        getContentPane().add(scrollPane, BorderLayout.CENTER);
        
        ta_info = new JTextArea();
        scrollPane.setViewportView(ta_info);
    }
}
