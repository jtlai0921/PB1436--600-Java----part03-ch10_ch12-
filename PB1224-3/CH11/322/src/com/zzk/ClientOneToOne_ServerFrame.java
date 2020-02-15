package com.zzk;

import java.awt.BorderLayout;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class ClientOneToOne_ServerFrame extends JFrame {
    private JTextArea ta_info;
    private ServerSocket server; // 宣告ServerSocket對像
    private Socket socket; // 宣告Socket對像socket
    private Hashtable<String, Socket> map = new Hashtable<String, Socket>();// 用於儲存連接到服務器的使用者和客戶端套接字對像
    
    public void createSocket() {
        try {
            server = new ServerSocket(1978);
            while (true) {
                ta_info.append("等待新客戶連接......\n");
                socket = server.accept();// 建立套接字對像
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
                    String key = "";
                    if (info.startsWith("使用者：")) {// 增加登入使用者到客戶端列表
                        key = info.substring(3, info.length());// 獲得使用者名稱並作為鍵使用
                        map.put(key, socket);// 增加鍵值對
                        Set<String> set = map.keySet();// 獲得集合中所有鍵的Set檢視
                        Iterator<String> keyIt = set.iterator();// 獲得所有鍵的迭代器
                        while (keyIt.hasNext()) {
                            String receiveKey = keyIt.next();// 獲得表示接收資訊的鍵
                            Socket s = map.get(receiveKey);// 獲得與該鍵對應的套接字對像
                            PrintWriter out = new PrintWriter(s
                                    .getOutputStream(), true);// 建立輸出流對像
                            Iterator<String> keyIt1 = set.iterator();// 獲得所有鍵的迭代器
                            while (keyIt1.hasNext()) {
                                String receiveKey1 = keyIt1.next();// 獲得鍵，用於向客戶端增加使用者列表
                                out.println(receiveKey1);// 發送資訊
                                out.flush();// 更新輸出緩衝區
                            }
                        }
                        
                    } else {// 轉發接收的訊息
                        key = info.substring(info.indexOf("：發送給：") + 5, info
                                .indexOf("：的資訊是："));// 獲得接收方的key值,即接收方的使用者名稱
                        String sendUser = info.substring(0, info
                                .indexOf("：發送給："));// 獲得發送方的key值,即發送方的使用者名稱
                        Set<String> set = map.keySet();// 獲得集合中所有鍵的Set檢視
                        Iterator<String> keyIt = set.iterator();// 獲得所有鍵的迭代器
                        while (keyIt.hasNext()) {
                            String receiveKey = keyIt.next();// 獲得表示接收資訊的鍵
                            if (key.equals(receiveKey)
                                    && !sendUser.equals(receiveKey)) {// 如果是發送方，但不是使用者本身
                                Socket s = map.get(receiveKey);// 獲得與該鍵對應的套接字對像
                                PrintWriter out = new PrintWriter(s
                                        .getOutputStream(), true);// 建立輸出流對像
                                
                                out.println("MSG:"+info);// 發送資訊
                                out.flush();// 更新輸出緩衝區
                            }
                        }
                    }
                }
            } catch (IOException e) {
                ta_info.append(socket + "已經退出。\n");
            }
        }
    }
    
    /**
     * Launch the application
     * 
     * @param args
     */
    public static void main(String args[]) {
        ClientOneToOne_ServerFrame frame = new ClientOneToOne_ServerFrame();
        frame.setVisible(true);
        frame.createSocket();
    }
    
    /**
     * Create the frame
     */
    public ClientOneToOne_ServerFrame() {
        super();
        setTitle("客戶端一對一通訊——服務器端程式");
        setBounds(100, 100, 385, 266);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        final JScrollPane scrollPane = new JScrollPane();
        getContentPane().add(scrollPane, BorderLayout.CENTER);
        
        ta_info = new JTextArea();
        scrollPane.setViewportView(ta_info);
    }
}
