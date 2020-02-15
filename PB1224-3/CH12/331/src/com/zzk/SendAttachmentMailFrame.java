package com.zzk;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Date;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class SendAttachmentMailFrame extends JFrame {
    private JTextArea ta_attachment;
    private JTextArea ta_text;
    private JTextField tf_title;
    private JTextField tf_send;
    private JTextField tf_receive;
    private Session session;
    private String sendHost = "localhost";
    private String sendProtocol="smtp";
    private String filePathAndName = null;
    public static void main(String args[]) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    SendAttachmentMailFrame frame = new SendAttachmentMailFrame();
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
    public SendAttachmentMailFrame() {
        super();
        setTitle("發送帶附屬應用程式的郵件");
        getContentPane().setLayout(null);
        setBounds(100, 100, 439, 358);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        final JLabel label = new JLabel();
        label.setForeground(new Color(0, 0, 255));
        label.setFont(new Font("", Font.BOLD, 22));
        label.setText("發送帶附屬應用程式的郵件");
        label.setBounds(113, 10, 217, 24);
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
        scrollPane.setBounds(113, 128, 287, 76);
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
        btn_send.setBounds(225, 282, 85, 28);
        getContentPane().add(btn_send);

        final JButton btn_exit = new JButton();
        btn_exit.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                System.exit(0);
            }
        });
        btn_exit.setText("退    出");
        btn_exit.setBounds(316, 282, 84, 28);
        getContentPane().add(btn_exit);

        final JButton button = new JButton();
        button.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser(); // 建立檔案交談視窗
                int returnValue = fileChooser.showOpenDialog(null);// 開啟檔案選擇交談視窗
                if (returnValue == JFileChooser.APPROVE_OPTION) { // 判斷是否選擇了檔案
                    File file = fileChooser.getSelectedFile(); // 獲得檔案對像
                    if (file.length() / 1024.0 / 1024 > 50.0) {
                        JOptionPane.showMessageDialog(null, "請選擇小於等於50MB的檔案。");
                        return;
                    }
                    filePathAndName = file.getAbsolutePath();// 獲得檔案的完整路徑和檔案名
                    ta_attachment.append(file.getName());// 顯示附屬應用程式檔案的名稱
                }
            }
        });
        button.setText("增加附屬應用程式");
        button.setBounds(113, 282, 106, 28);
        getContentPane().add(button);

        final JLabel label_5 = new JLabel();
        label_5.setText("附    件：");
        label_5.setBounds(32, 210, 66, 18);
        getContentPane().add(label_5);

        final JScrollPane scrollPane_1 = new JScrollPane();
        scrollPane_1.setBounds(112, 213, 287, 63);
        getContentPane().add(scrollPane_1);

        ta_attachment = new JTextArea();
        scrollPane_1.setViewportView(ta_attachment);
    }
    public void init() throws Exception {
        Properties props = new Properties();
        props.put("mail.transport.protocol", sendProtocol);
        props.put("mail.smtp.class", "com.sun.mail.smtp.SMTPTransport");
        props.put("mail.smtp.host", sendHost);
        session = Session.getDefaultInstance(props);
    }
    /**
     * @param fromAddr 發送方地址
     * @param toAddr 接收方地址
     * @param title 主題
     * @param text 純文字內容
     * @throws Exception 例外
     */
    public void sendMessage(String fromAddr,String toAddr,String title,String text) throws Exception {
        Message msg = new MimeMessage(session);// 建立Message對像
        InternetAddress[] toAddrs = InternetAddress.parse(toAddr,false);// 接收方地址
        msg.setRecipients(Message.RecipientType.TO, toAddrs);// 指定接收方
        msg.setSentDate(new Date());// 設定發送日期
        msg.setSubject(title);// 設定主題
        msg.setFrom(new InternetAddress(fromAddr));// 設定發送地址
        Multipart multipart = new MimeMultipart();// 可以增加複雜內容的Multipart對像
        MimeBodyPart mimeBodyPartText = new MimeBodyPart();// 增加正文的MimeBodyPart對像
        mimeBodyPartText.setText(text);// 指定正文
        multipart.addBodyPart(mimeBodyPartText);// 增加到Multipart對像上
        if (filePathAndName!=null && !filePathAndName.equals("")){
            MimeBodyPart mimeBodyPartAdjunct = new MimeBodyPart();// 增加附屬應用程式的MimeBodyPart對像
            FileDataSource fileDataSource = new FileDataSource(filePathAndName);// 建立附屬應用程式的FileDataSource對像
            mimeBodyPartAdjunct.setDataHandler(new DataHandler(fileDataSource));// 指定資料
            mimeBodyPartAdjunct.setDisposition(Part.ATTACHMENT);// 指定增加的內容是附屬應用程式
            String name = fileDataSource.getName();
            mimeBodyPartAdjunct.setFileName(MimeUtility.encodeText(name, "GBK", null));// 指定附屬應用程式檔案的名稱
            multipart.addBodyPart(mimeBodyPartAdjunct);// 增加到Multipart對像上
        }
        msg.setContent(multipart);// 設定郵件內容
        Transport.send(msg);// 發送郵件
        filePathAndName = null;
        JOptionPane.showMessageDialog(null, "郵件發送成功。");
    }
}
