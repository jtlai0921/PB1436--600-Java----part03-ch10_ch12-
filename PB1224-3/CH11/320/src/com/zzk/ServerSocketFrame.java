package com.zzk;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;
import java.util.Vector;
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
    private ServerSocket server; // 宣告ServerSocket對像
    private Socket socket; // 宣告Socket對像socket
    private Vector<Socket> vector = new Vector<Socket>();// 用於儲存連接到服務器的客戶端套接字對像
    private int counts = 0;// 用於記錄連接的客戶人數
    
    public void getServer() {
        try {
            server = new ServerSocket(1978); // 實例化Socket對像
            ta_info.append("服務器套接字已經建立成功\n"); // 輸出資訊
            while (true) { // 如果套接字是連接狀態
                socket = server.accept(); // 實例化Socket對像
                counts++;
                ta_info.append("第" + counts + "個客戶連接成功\n"); // 輸出資訊
                PrintWriter out = new PrintWriter(socket.getOutputStream(),
                        true);
                out.println(String.valueOf(counts - 1));// 向客戶端發送套接字索引
                vector.add(socket);// 儲存客戶端套接字對像
                new ServerThread(socket).start();// 建立並啟動線程式
            }
        } catch (Exception e) {
            e.printStackTrace(); // 輸出例外資訊
        }
    }
    
    class ServerThread extends Thread {
        Socket socket = null; // 建立Socket對像
        BufferedReader reader; // 宣告BufferedReader對像
        
        public ServerThread(Socket socket) { // 建構方法
            this.socket = socket;
        }
        
        public void run() {
            try {
                if (socket != null) {
                    reader = new BufferedReader(new InputStreamReader(socket
                            .getInputStream())); // 實例化BufferedReader對像
                    int index = -1;// 儲存退出的客戶端索引值
                    try {
                        while (true) { // 如果套接字是連接狀態
                            String line = reader.readLine();// 讀取客戶端資訊
                            try {
                                index = Integer.parseInt(line);// 獲得退出的客戶端索引值
                            } catch (Exception ex) {
                                index = -1;
                            }
                            if (line != null) {
                                ta_info.append("接收到客戶機發送的資訊：" + line + "\n"); // 獲得客戶端資訊
                            }
                        }
                    } catch (Exception e) {
                        if (index != -1) {
                            vector.set(index, null);// 將退出的客戶端套接字設定為null
                            ta_info.append("第" + (index + 1) + "個客戶端已經退出。\n"); // 輸出例外資訊
                        }
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
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
    }
    
    private void writeInfo(PrintWriter writer, String text) {
        writer.println(text);
    }
    
    public static void main(String[] args) { // 主方法
        ServerSocketFrame frame = new ServerSocketFrame(); // 建立本類別對像
        frame.setVisible(true);// 顯示窗體
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
                for (int i = 0; i < vector.size(); i++) {
                    Socket socket = vector.get(i);// 獲得連接成功的套接字對像
                    PrintWriter writer;
                    try {
                        if (socket != null && !socket.isClosed()) {
                            writer = new PrintWriter(socket.getOutputStream(),
                                    true);// 建立輸出流對像
                            writeInfo(writer, tf_send.getText()); // 將純文字框中資訊寫入流
                        }
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
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
        label_1.setText("一對多通訊——服務器端程式");
        panel_1.add(label_1);
    }
}
