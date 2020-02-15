package com.zzk;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class SendMailFrame extends JFrame {
    private JTextArea ta_text;
    private JTextField tf_title;
    private JTextField tf_send;
    private JTextField tf_receive;
    private Session session;// 定義Session對像
    private String sendHost = "localhost";// 定義發送郵件的主機
    private String sendProtocol="smtp";// 定義使用的發送協定
    public static void main(String args[]) {
    	EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    SendMailFrame frame = new SendMailFrame();
                    frame.init();
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
    public SendMailFrame() {
        super();
        setTitle("發送郵件窗體");
        getContentPane().setLayout(null);
        setBounds(100, 100, 439, 299);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        final JLabel label = new JLabel();
        label.setForeground(new Color(0, 0, 255));
        label.setFont(new Font("", Font.BOLD, 22));
        label.setText("發送電子郵件");
        label.setBounds(144, 10, 185, 24);
        getContentPane().add(label);

        final JLabel label_1 = new JLabel();
        label_1.setText("收件人地址：");
        label_1.setBounds(22, 42, 85, 18);
        getContentPane().add(label_1);

        tf_receive = new JTextField();
        tf_receive.setBounds(113, 40, 287, 22);
        getContentPane().add(tf_receive);

        final JLabel label_2 = new JLabel();
        label_2.setText("發件人地址：");
        label_2.setBounds(22, 68, 78, 18);
        getContentPane().add(label_2);

        tf_send = new JTextField();
        tf_send.setBounds(113, 66, 287, 22);
        getContentPane().add(tf_send);

        final JLabel label_3 = new JLabel();
        label_3.setText("主    題：");
        label_3.setBounds(32, 92, 66, 18);
        getContentPane().add(label_3);

        tf_title = new JTextField();
        tf_title.setBounds(113, 94, 287, 22);
        getContentPane().add(tf_title);

        final JLabel label_4 = new JLabel();
        label_4.setText("正    文：");
        label_4.setBounds(34, 128, 66, 18);
        getContentPane().add(label_4);

        final JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(113, 128, 287, 91);
        getContentPane().add(scrollPane);

        ta_text = new JTextArea();
        scrollPane.setViewportView(ta_text);

        final JButton btn_send = new JButton();
        btn_send.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                String fromAddr = tf_send.getText().trim();
                String toAddr = tf_receive.getText().trim();// 真實存在的目標郵件地址
                String title = tf_title.getText().trim();
                String text = ta_text.getText().trim();
                try {
                    sendMessage(fromAddr, toAddr, title, text);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });
        btn_send.setText("發    送");
        btn_send.setBounds(144, 225, 78, 28);
        getContentPane().add(btn_send);

        final JButton btn_exit = new JButton();
        btn_exit.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                System.exit(0);
            }
        });
        btn_exit.setText("退    出");
        btn_exit.setBounds(279, 225, 78, 28);
        getContentPane().add(btn_exit);
    }
    public void init() throws Exception {
        Properties props = new Properties();// 建立屬性對像
        props.put("mail.transport.protocol", sendProtocol);// 指定郵件傳輸協定
        props.put("mail.smtp.class", "com.sun.mail.smtp.SMTPTransport");//指定傳輸協定使用的類別
        props.put("mail.smtp.host", sendHost);// 定義發送郵件的主機
        session = Session.getDefaultInstance(props);// 建立Session對像
    }
    /**
     * @param fromAddr 發送者
     * @param toAddr 接收者
     * @param title 主題
     * @param text 內容
     * @throws Exception 例外
     */
    public void sendMessage(String fromAddr,String toAddr,String title,String text) throws Exception {
        Message msg = new MimeMessage(session);// 建立Message對像
        InternetAddress[] toAddrs = InternetAddress.parse(toAddr,false);// 建立接收方的InternetAddress對像
        msg.setRecipients(Message.RecipientType.TO, toAddrs);// 指定接收方
        msg.setSentDate(new Date());// 指定接發送日期
        msg.setSubject(title);// 設定主題
        msg.setFrom(new InternetAddress(fromAddr));// 指定發送者
        msg.setText(text);// 指定發送內容
        Transport.send(msg);// 發送郵件
        JOptionPane.showMessageDialog(null, "郵件發送成功。");
    }
}
