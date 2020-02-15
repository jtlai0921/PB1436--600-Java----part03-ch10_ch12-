package com.zzk;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ClientOneToMany_ClientFrame extends JFrame {
    private JTextArea ta_info;
    private JTextField tf_send;
    PrintWriter out;// 宣告輸出流對像
    
    /**
     * Launch the application
     * 
     * @param args
     */
    public static void main(String args[]) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    ClientOneToMany_ClientFrame frame = new ClientOneToMany_ClientFrame();
                    frame.setVisible(true);
                    frame.createClientSocket();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    
    public void createClientSocket() {
        try {
            Socket socket = new Socket("localhost", 1978);// 建立套接字對像
            out = new PrintWriter(socket.getOutputStream(), true);// 建立輸出流對像
            new ClientThread(socket).start();// 建立並啟動線程對像
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    class ClientThread extends Thread {
        Socket socket;
        
        public ClientThread(Socket socket) {
            this.socket = socket;
        }
        
        public void run() {
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        socket.getInputStream()));// 建立輸入流對像
                while (true) {
                    String info = in.readLine();// 讀取資訊
                    ta_info.append(info + "\n");// 在純文字域中顯示資訊
                    if (info.equals("88")) {
                        break;// 結束線程
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private void send() {
        String info = tf_send.getText();// 獲得輸入的資訊
        if (info.equals("")) {
            return;// 如果沒輸入資訊則傳回，即不發送
        }
        if (info.equals("88")) {
            System.exit(0);// 如果沒輸入資訊是88，則退出
        }
        out.println(info);// 發送資訊
        out.flush();// 更新輸出緩衝區
        tf_send.setText(null);// 清空純文字框
    }
    /**
     * Create the frame
     */
    public ClientOneToMany_ClientFrame() {
        super();
        setTitle("客戶端一對多通訊——客戶端程式");
        setBounds(100, 100, 385, 266);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        final JPanel panel = new JPanel();
        getContentPane().add(panel, BorderLayout.SOUTH);
        
        final JLabel label = new JLabel();
        label.setText("輸入聊天內容：");
        panel.add(label);
        
        tf_send = new JTextField();
        tf_send.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                send();// 呼叫方法發送資訊
            }
        });
        tf_send.setPreferredSize(new Dimension(180, 25));
        panel.add(tf_send);
        
        final JButton button = new JButton();
        button.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                send();// 呼叫方法發送資訊
            }
        });
        button.setText("發  送");
        panel.add(button);
        
        final JScrollPane scrollPane = new JScrollPane();
        getContentPane().add(scrollPane, BorderLayout.CENTER);
        
        ta_info = new JTextArea();
        scrollPane.setViewportView(ta_info);
        //
    }
    
}
