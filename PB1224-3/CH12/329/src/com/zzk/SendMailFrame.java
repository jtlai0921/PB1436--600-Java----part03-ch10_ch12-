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
    private Session session;// �w�qSession�ﹳ
    private String sendHost = "localhost";// �w�q�o�e�l�󪺥D��
    private String sendProtocol="smtp";// �w�q�ϥΪ��o�e��w
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
        setTitle("�o�e�l����");
        getContentPane().setLayout(null);
        setBounds(100, 100, 439, 299);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        final JLabel label = new JLabel();
        label.setForeground(new Color(0, 0, 255));
        label.setFont(new Font("", Font.BOLD, 22));
        label.setText("�o�e�q�l�l��");
        label.setBounds(144, 10, 185, 24);
        getContentPane().add(label);

        final JLabel label_1 = new JLabel();
        label_1.setText("����H�a�}�G");
        label_1.setBounds(22, 42, 85, 18);
        getContentPane().add(label_1);

        tf_receive = new JTextField();
        tf_receive.setBounds(113, 40, 287, 22);
        getContentPane().add(tf_receive);

        final JLabel label_2 = new JLabel();
        label_2.setText("�o��H�a�}�G");
        label_2.setBounds(22, 68, 78, 18);
        getContentPane().add(label_2);

        tf_send = new JTextField();
        tf_send.setBounds(113, 66, 287, 22);
        getContentPane().add(tf_send);

        final JLabel label_3 = new JLabel();
        label_3.setText("�D    �D�G");
        label_3.setBounds(32, 92, 66, 18);
        getContentPane().add(label_3);

        tf_title = new JTextField();
        tf_title.setBounds(113, 94, 287, 22);
        getContentPane().add(tf_title);

        final JLabel label_4 = new JLabel();
        label_4.setText("��    ��G");
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
                String toAddr = tf_receive.getText().trim();// �u��s�b���ؼжl��a�}
                String title = tf_title.getText().trim();
                String text = ta_text.getText().trim();
                try {
                    sendMessage(fromAddr, toAddr, title, text);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });
        btn_send.setText("�o    �e");
        btn_send.setBounds(144, 225, 78, 28);
        getContentPane().add(btn_send);

        final JButton btn_exit = new JButton();
        btn_exit.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                System.exit(0);
            }
        });
        btn_exit.setText("�h    �X");
        btn_exit.setBounds(279, 225, 78, 28);
        getContentPane().add(btn_exit);
    }
    public void init() throws Exception {
        Properties props = new Properties();// �إ��ݩʹﹳ
        props.put("mail.transport.protocol", sendProtocol);// ���w�l��ǿ��w
        props.put("mail.smtp.class", "com.sun.mail.smtp.SMTPTransport");//���w�ǿ��w�ϥΪ����O
        props.put("mail.smtp.host", sendHost);// �w�q�o�e�l�󪺥D��
        session = Session.getDefaultInstance(props);// �إ�Session�ﹳ
    }
    /**
     * @param fromAddr �o�e��
     * @param toAddr ������
     * @param title �D�D
     * @param text ���e
     * @throws Exception �ҥ~
     */
    public void sendMessage(String fromAddr,String toAddr,String title,String text) throws Exception {
        Message msg = new MimeMessage(session);// �إ�Message�ﹳ
        InternetAddress[] toAddrs = InternetAddress.parse(toAddr,false);// �إ߱����誺InternetAddress�ﹳ
        msg.setRecipients(Message.RecipientType.TO, toAddrs);// ���w������
        msg.setSentDate(new Date());// ���w���o�e���
        msg.setSubject(title);// �]�w�D�D
        msg.setFrom(new InternetAddress(fromAddr));// ���w�o�e��
        msg.setText(text);// ���w�o�e���e
        Transport.send(msg);// �o�e�l��
        JOptionPane.showMessageDialog(null, "�l��o�e���\�C");
    }
}
