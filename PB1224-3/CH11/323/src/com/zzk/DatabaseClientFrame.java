package com.zzk;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

public class DatabaseClientFrame extends JFrame {
    
    private JTextArea ta_result;
    private JTextField tf_age;
    private JTextField tf_name;
    private PrintWriter writer; // 宣告PrintWriter類別對像
    private BufferedReader reader; // 宣告BufferedReader對像
    private Socket socket; // 宣告Socket對像
    
    /**
     * Launch the application
     * 
     * @param args
     */
    public static void main(String args[]) {
        DatabaseClientFrame frame = new DatabaseClientFrame();
        frame.setVisible(true);
        frame.connect();
    }
    
    private void connect() { // 連接套接字方法
        ta_result.append("嘗試連接......\n"); // 純文字域中資訊資訊
        try { // 捕捉例外
            socket = new Socket("localhost", 1978); // 實例化Socket對像
            while (true) {
                writer = new PrintWriter(socket.getOutputStream(), true);// 實例化PrintWriter對像
                reader = new BufferedReader(new InputStreamReader(socket
                        .getInputStream())); // 實例化BufferedReader對像
                ta_result.append("完成連接。\n"); // 純文字域中提示資訊
                getServerInfo();// 呼叫方法讀取服務器資訊
            }
        } catch (Exception e) {
            e.printStackTrace(); // 輸出例外資訊
        }
    }
    
    private void getServerInfo() {
        try {
            while (true) { // 如果套接字是連接狀態
                if (reader != null) {
                    String line = reader.readLine();// 讀取服務器資訊
                    if (line != null) {
                        ta_result.append("接收到服務器發送的資訊： " + line + "\n"); // 獲得服務器資訊
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (socket != null) {
                    socket.close(); // 關閉套接字
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Create the frame
     */
    public DatabaseClientFrame() {
        super();
        getContentPane().setLayout(null);
        setTitle("客戶端程式");
        setBounds(100, 100, 277, 263);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        final JLabel label = new JLabel();
        label.setText("名  稱：");
        label.setBounds(10, 12, 66, 18);
        getContentPane().add(label);
        
        final JLabel label_1 = new JLabel();
        label_1.setText("年  齡：");
        label_1.setBounds(10, 38, 66, 18);
        getContentPane().add(label_1);
        
        tf_name = new JTextField();
        tf_name.setBounds(56, 10, 194, 22);
        getContentPane().add(tf_name);
        
        tf_age = new JTextField();
        tf_age.setBounds(56, 36, 194, 22);
        getContentPane().add(tf_age);
        
        final JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBorder(new TitledBorder(null, "顯示服務器端的反饋資訊",
                TitledBorder.DEFAULT_JUSTIFICATION,
                TitledBorder.DEFAULT_POSITION, null, null));
        panel.setBounds(10, 91, 240, 124);
        getContentPane().add(panel);
        
        final JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(10, 21, 220, 93);
        panel.add(scrollPane);
        
        ta_result = new JTextArea();
        scrollPane.setViewportView(ta_result);
        
        final JButton button = new JButton();
        button.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                String name = tf_name.getText().trim();// 獲得姓名
                String age = tf_age.getText().trim();// 獲得年齡
                if (name == null || name.equals("") || age == null || age.equals("")) {
                    JOptionPane.showMessageDialog(null, "姓名和年齡不能為空。");
                    return;
                }
                try {
                    Integer.parseInt(age);// 如果年齡不是數字就會發生例外
                }catch(Exception ex) {
                    JOptionPane.showMessageDialog(null, "年齡必須為數字。");
                    return;
                }
                String info = name + ":data:" + age;// 使用字串":data:"連接姓名和年齡
                writer.println(info);// 向服務器發送資訊
            }
        });
        button.setText("保    存");
        button.setBounds(41, 62, 72, 23);
        getContentPane().add(button);
        
        final JButton button_1 = new JButton();
        button_1.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                System.exit(0);
            }
        });
        button_1.setText("退    出");
        button_1.setBounds(148, 62, 72, 23);
        getContentPane().add(button_1);
        //
    }
    
}
