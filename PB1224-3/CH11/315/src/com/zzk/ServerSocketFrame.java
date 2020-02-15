package com.zzk;

import java.awt.BorderLayout;
import java.awt.Dimension;
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
    
    private JTextField tf_name;
    private JTextField tf_id;
    private JTextArea ta_info;
    private ObjectOutputStream out = null; // 建立流對像
    private ObjectInputStream in = null; // 建立流對像
    private ServerSocket server; // 宣告ServerSocket對像
    private Socket socket; // 宣告Socket對像socket
    
    public void getserver() {
        try {
            server = new ServerSocket(1978); // 實例化Socket對像
            ta_info.append("服務器套接字已經建立成功\n"); // 輸出資訊
            while (true) { // 如果套接字是連接狀態
                ta_info.append("等待客戶機的連接......\n"); // 輸出資訊
                socket = server.accept(); // 實例化Socket對像
                ta_info.append("客戶機連接成功\n"); // 輸出資訊
                out = new ObjectOutputStream(socket.getOutputStream());
                in = new ObjectInputStream(socket.getInputStream());
                getClientInfo(); // 呼叫getClientInfo()方法
            }
        } catch (Exception e) {
            e.printStackTrace(); // 輸出例外資訊
        }
    }
    
    private void getClientInfo() {
        try {
            while (true) { // 如果套接字是連接狀態
                Student stud = (Student)in.readObject();
                if (stud!=null)
                ta_info.append("接收到客戶機發送的  編號為：" + stud.getId() + "  名稱為：" +stud.getName() + "\n"); // 獲得客戶端資訊
            }
        } catch (Exception e) {
            ta_info.append("客戶端已退出。\n"); // 輸出例外資訊
        } finally {
            try {
                if (in != null) {
                    in.close();// 關閉流
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
        frame.getserver(); // 呼叫方法
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
        getContentPane().add(panel, BorderLayout.NORTH);

        final JLabel label = new JLabel();
        label.setText("編號：");
        panel.add(label);

        tf_id = new JTextField();
        tf_id.setPreferredSize(new Dimension(70,25));
        panel.add(tf_id);

        final JLabel label_1 = new JLabel();
        label_1.setText("名稱：");
        panel.add(label_1);

        tf_name = new JTextField();
        tf_name.setPreferredSize(new Dimension(100,25));
        panel.add(tf_name);

        final JButton button = new JButton();
        button.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                Student stud = new Student();
                stud.setId(tf_id.getText());
                stud.setName(tf_name.getText());
                try {
                    out.writeObject(stud);
                } catch (IOException e1) {
                    e1.printStackTrace();
                } 
                ta_info.append("服務器發送的  編號是：" + tf_id.getText() + "  名稱是："+tf_name.getText()+"\n"); // 將純文字框中資訊顯示在純文字域中
                tf_id.setText(null); // 將純文字框清空
                tf_name.setText(null);
            }
        });
        button.setText("發  送");
        panel.add(button);
    }
}
