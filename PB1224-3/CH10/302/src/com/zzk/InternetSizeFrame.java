package com.zzk;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.net.URLConnection;
import javax.swing.JButton;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class InternetSizeFrame extends JFrame {
    
    private JTextField tf_size;
    private JTextField tf_address;
    /**
     * Launch the application
     * @param args
     */
    public static void main(String args[]) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    InternetSizeFrame frame = new InternetSizeFrame();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    
    /**
     * Create the frame
     */
    public InternetSizeFrame() {
        super();
        setTitle("獲得網絡資源的大小");
        getContentPane().setLayout(null);
        setBounds(100, 100, 391, 223);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        final JLabel label = new JLabel();
        label.setText("輸入網址：");
        label.setBounds(10, 77, 79, 18);
        getContentPane().add(label);

        tf_address = new JTextField();
        tf_address.setBounds(82, 75, 286, 22);
        getContentPane().add(tf_address);

        final JButton button = new JButton();
        button.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                String address = tf_address.getText().trim();// 獲得輸入的網址
                try{
                    long len = netSourceSize(address);// 呼叫方法獲得網絡資源的大小
                    tf_size.setText(String.valueOf(len)+" 字節");// 在純文字框中顯示網絡資源的大小
                }catch(Exception ex){
                    ex.printStackTrace();
                }
            }
        });
        button.setText("獲得大小");
        button.setBounds(282, 123, 86, 28);
        getContentPane().add(button);

        final JLabel label_1 = new JLabel();
        label_1.setText("網絡資源的大小為：");
        label_1.setBounds(10, 128, 127, 18);
        getContentPane().add(label_1);

        tf_size = new JTextField();
        tf_size.setBounds(131, 126, 145, 22);
        getContentPane().add(tf_size);

        final JLabel label_2 = new JLabel();
        label_2.setForeground(new Color(0, 0, 255));
        label_2.setFont(new Font("", Font.BOLD, 22));
        label_2.setText("獲得網絡資源的大小");
        label_2.setBounds(82, 8, 239, 44);
        getContentPane().add(label_2);
    }
    public long netSourceSize(String sUrl) throws Exception{
        URL url = new URL(sUrl);  // 建立URL對像
        URLConnection urlConn = url.openConnection();          // 獲得網絡連接對像
        urlConn.connect();                                     // 開啟到url參考資源的通訊鏈接
        return urlConn.getContentLength();                     // 以字節為單位傳回資源的大小
    }
}
