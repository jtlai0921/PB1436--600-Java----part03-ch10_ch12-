package com.zzk;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import javax.swing.*;

public class ClientSocketFrame extends JFrame { // 建立類別繼承JFrame類別
    private JButton button;
    private JTextField tf_name;
    private JLabel label_1;
    private JLabel label;
    private JPanel panel;
    private ObjectInputStream in = null;// 建立流對像
    private ObjectOutputStream out = null;// 建立流對像
    private Socket socket;// 宣告Socket對像
    private JTextArea ta_info = new JTextArea();// 建立JtextArea對像
    private JTextField tf_id = new JTextField();// 建立JtextField對像
    private Container cc;// 宣告Container對像
    
    public ClientSocketFrame() { // 建構方法
        super(); // 呼叫父類別的建構方法
        setTitle("客戶端程式");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 373, 257);
        cc = this.getContentPane(); // 實例化對像
        final JScrollPane scrollPane = new JScrollPane();
        getContentPane().add(scrollPane, BorderLayout.CENTER);
        scrollPane.setViewportView(ta_info);
        getContentPane().add(getPanel(), BorderLayout.NORTH);
    }
    
    private void connect() { // 連接套接字方法
        ta_info.append("嘗試連接......\n"); // 純文字域中資訊資訊
        try { // 捕捉例外
            socket = new Socket("localhost", 1978); // 實例化Socket對像
            while (true){
                out = new ObjectOutputStream(socket.getOutputStream());
                in = new ObjectInputStream(socket.getInputStream());
            ta_info.append("完成連接。\n"); // 純文字域中提示資訊
                getClientInfo();
            }
        } catch (Exception e) {
            e.printStackTrace(); // 輸出例外資訊
        }
    }
    
    public static void main(String[] args) { // 主方法
        ClientSocketFrame clien = new ClientSocketFrame(); // 建立本例對像
        clien.setVisible(true); // 將窗體顯示
        clien.connect(); // 呼叫連接方法
    }
    private void getClientInfo() {
        try {
            while (true) { // 如果套接字是連接狀態
                Student stud = (Student)in.readObject();
                if (stud!=null)
                ta_info.append("接收到服務器發送的  編號為：" + stud.getId() + "  名稱為：" +stud.getName() + "\n"); // 獲得服務器資訊
            }
        } catch (Exception e) {
            e.printStackTrace();
        } 
        finally {
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
    /**
     * @return
     */
    protected JPanel getPanel() {
        if (panel == null) {
            panel = new JPanel();
            panel.add(getLabel());
            tf_id.setPreferredSize(new Dimension(70, 25));
            panel.add(tf_id);
            panel.add(getLabel_1());
            panel.add(getTf_name());
            panel.add(getButton());
        }
        return panel;
    }
    
    /**
     * @return
     */
    protected JLabel getLabel() {
        if (label == null) {
            label = new JLabel();
            label.setText("編號：");
        }
        return label;
    }
    /**
     * @return
     */
    protected JLabel getLabel_1() {
        if (label_1 == null) {
            label_1 = new JLabel();
            label_1.setText("名稱：");
        }
        return label_1;
    }
    /**
     * @return
     */
    protected JTextField getTf_name() {
        if (tf_name == null) {
            tf_name = new JTextField();
            tf_name.setPreferredSize(new Dimension(100, 25));
        }
        return tf_name;
    }
    /**
     * @return
     */
    protected JButton getButton() {
        if (button == null) {
            button = new JButton();
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
                    ta_info.append("客戶端發送的  編號是：" + tf_id.getText() + "  名稱是："+tf_name.getText()+"\n"); // 將純文字框中資訊顯示在純文字域中
                    tf_id.setText(null); // 將純文字框清空
                    tf_name.setText(null);
                }
            });
            button.setText("發  送");
        }
        return button;
    }
}
