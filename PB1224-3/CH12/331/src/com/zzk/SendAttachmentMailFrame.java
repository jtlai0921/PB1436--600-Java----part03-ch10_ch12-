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
        setTitle("�o�e�a�������ε{�����l��");
        getContentPane().setLayout(null);
        setBounds(100, 100, 439, 358);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        final JLabel label = new JLabel();
        label.setForeground(new Color(0, 0, 255));
        label.setFont(new Font("", Font.BOLD, 22));
        label.setText("�o�e�a�������ε{�����l��");
        label.setBounds(113, 10, 217, 24);
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
        scrollPane.setBounds(113, 128, 287, 76);
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
        btn_send.setBounds(225, 282, 85, 28);
        getContentPane().add(btn_send);

        final JButton btn_exit = new JButton();
        btn_exit.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                System.exit(0);
            }
        });
        btn_exit.setText("�h    �X");
        btn_exit.setBounds(316, 282, 84, 28);
        getContentPane().add(btn_exit);

        final JButton button = new JButton();
        button.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser(); // �إ��ɮץ�͵���
                int returnValue = fileChooser.showOpenDialog(null);// �}���ɮ׿�ܥ�͵���
                if (returnValue == JFileChooser.APPROVE_OPTION) { // �P�_�O�_��ܤF�ɮ�
                    File file = fileChooser.getSelectedFile(); // ��o�ɮ׹ﹳ
                    if (file.length() / 1024.0 / 1024 > 50.0) {
                        JOptionPane.showMessageDialog(null, "�п�ܤp�󵥩�50MB���ɮסC");
                        return;
                    }
                    filePathAndName = file.getAbsolutePath();// ��o�ɮת�������|�M�ɮצW
                    ta_attachment.append(file.getName());// ��ܪ������ε{���ɮת��W��
                }
            }
        });
        button.setText("�W�[�������ε{��");
        button.setBounds(113, 282, 106, 28);
        getContentPane().add(button);

        final JLabel label_5 = new JLabel();
        label_5.setText("��    ��G");
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
     * @param fromAddr �o�e��a�}
     * @param toAddr ������a�}
     * @param title �D�D
     * @param text �¤�r���e
     * @throws Exception �ҥ~
     */
    public void sendMessage(String fromAddr,String toAddr,String title,String text) throws Exception {
        Message msg = new MimeMessage(session);// �إ�Message�ﹳ
        InternetAddress[] toAddrs = InternetAddress.parse(toAddr,false);// ������a�}
        msg.setRecipients(Message.RecipientType.TO, toAddrs);// ���w������
        msg.setSentDate(new Date());// �]�w�o�e���
        msg.setSubject(title);// �]�w�D�D
        msg.setFrom(new InternetAddress(fromAddr));// �]�w�o�e�a�}
        Multipart multipart = new MimeMultipart();// �i�H�W�[�������e��Multipart�ﹳ
        MimeBodyPart mimeBodyPartText = new MimeBodyPart();// �W�[���媺MimeBodyPart�ﹳ
        mimeBodyPartText.setText(text);// ���w����
        multipart.addBodyPart(mimeBodyPartText);// �W�[��Multipart�ﹳ�W
        if (filePathAndName!=null && !filePathAndName.equals("")){
            MimeBodyPart mimeBodyPartAdjunct = new MimeBodyPart();// �W�[�������ε{����MimeBodyPart�ﹳ
            FileDataSource fileDataSource = new FileDataSource(filePathAndName);// �إߪ������ε{����FileDataSource�ﹳ
            mimeBodyPartAdjunct.setDataHandler(new DataHandler(fileDataSource));// ���w���
            mimeBodyPartAdjunct.setDisposition(Part.ATTACHMENT);// ���w�W�[�����e�O�������ε{��
            String name = fileDataSource.getName();
            mimeBodyPartAdjunct.setFileName(MimeUtility.encodeText(name, "GBK", null));// ���w�������ε{���ɮת��W��
            multipart.addBodyPart(mimeBodyPartAdjunct);// �W�[��Multipart�ﹳ�W
        }
        msg.setContent(multipart);// �]�w�l�󤺮e
        Transport.send(msg);// �o�e�l��
        filePathAndName = null;
        JOptionPane.showMessageDialog(null, "�l��o�e���\�C");
    }
}
