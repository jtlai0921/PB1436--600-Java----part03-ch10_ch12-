package com.zzk;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FileDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import javax.imageio.ImageIO;
import javax.mail.Address;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeUtility;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class ReceiveMailFrame extends JFrame {
    private JTextArea ta_receive;
    protected Session session;
    protected Store store;
    private String receiveHost = "localhost";// �����D��
    private String receiveProtocol = "imap";// ������w
    private String username = "zzk";// �ϥΪ̦W��
    private String password = "zzk";// �K�X
    
    public static void main(String args[]) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    ReceiveMailFrame frame = new ReceiveMailFrame();
                    frame.init();// �I�s��l�Ƥ�k
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    
    public ReceiveMailFrame() {
        super();
        getContentPane().setLayout(new BorderLayout());
        setTitle("�����l����");
        setBounds(100, 100, 386, 314);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        final JScrollPane scrollPane = new JScrollPane();
        getContentPane().add(scrollPane, BorderLayout.CENTER);
        
        ta_receive = new JTextArea();
        scrollPane.setViewportView(ta_receive);
        
        final JPanel panel = new JPanel();
        getContentPane().add(panel, BorderLayout.SOUTH);
        
        final JButton button = new JButton();
        button.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                try {
                    receiveMessage();// �I�s������T����k
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });
        button.setText("�����l��äU���������ε{��");
        panel.add(button);
        
        final JButton button_1 = new JButton();
        button_1.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                System.exit(0);
            }
        });
        button_1.setText("�h�X�t��");
        panel.add(button_1);
    }
    
    public void init() throws Exception {
        Properties props = new Properties();// �ŧiProperties�ﹳ
        props.put("mail.store.protocol", receiveProtocol);// ���w������w
        props.put("mail.imap.class", "com.sun.mail.imap.IMAPStore");// ���w�ϥ�Store�i�汵��
        session = Session.getDefaultInstance(props);// ��oSession�ﹳ
        store = session.getStore(receiveProtocol);// ��oStore�ﹳ
        store.connect(receiveHost, username, password);// �s�������A�Ⱦ�
    }
    
    public void receiveMessage() throws Exception {
        Folder folder = store.getFolder("inbox");// ��oinbox�l�󧨪�Folder�ﹳ
        if (folder == null) {
            throw new Exception("���s�binbox�l�󧨡C");
        }
        folder.open(Folder.READ_ONLY);// �H�uŪ�覡�}�Ҷl��
        ta_receive.append("�z�@����" + folder.getMessageCount() + "�ӹq�l�l��C\n\n");
        Message[] messages = folder.getMessages();// ��o�l�󧨤����Ҧ��l��
        for (int i = 0; i < messages.length; i++) {
            Object content = messages[i].getContent();// ��o�l�󤺮e
            if (content instanceof Multipart) {// ���������ε{�����檺�{���X
                ta_receive.append("----��" + (i + 1) + "�Ӷl��----\n");
                ta_receive.append("�D�D�G" + folder.getMessage(i + 1).getSubject() + "\n");// �D�D
                Multipart mPart = (Multipart) content;// �ରMultipart�ﹳ
                ta_receive.append("����G" + mPart.getBodyPart(0).getContent() + "\n");// ���夺�e
                ta_receive.append("�o�e����G" + folder.getMessage(i + 1).getSentDate() + "\n");// �o�e���
                Address[] ias = folder.getMessage(i + 1).getFrom();// �o��H�a�}
                ta_receive.append("�o��H�G" + ias[0] + "\n");
                Address[] iasTo = folder.getMessage(i + 1).getAllRecipients();// ����H�a�}
                ta_receive.append("����H�G" + iasTo[0] + "\n\n");
                try {
                    String fileName = mPart.getBodyPart(1).getFileName();// ��o�ɮצW
                    ta_receive.append("������@�ӦW���u" + MimeUtility.decodeText(fileName) + "�v���������ε{��\n");
                    InputStream in = mPart.getBodyPart(1).getInputStream();
                    FileDialog dialog = new FileDialog(ReceiveMailFrame.this, "�x�s");// �إߥ�͵���
                    dialog.setMode(FileDialog.SAVE);// �]�w��͵������x�s��͵���
                    dialog.setFile(MimeUtility.decodeText(fileName));
                    dialog.setVisible(true);// ����x�s��͵���
                    String path = dialog.getDirectory();// ��o�ɮת��x�s���|
                    String saveFileName = dialog.getFile();// ��o�x�s���ɮצW
                    if (path == null || saveFileName == null) {
                        return;
                    }
                    OutputStream out = new BufferedOutputStream(new FileOutputStream(path + "/" + saveFileName));// �إ��x�s�������ε{���ɮת���X�y�ﹳ
                    int len = -1;
                    while ((len = in.read()) != -1) {
                        out.write(len);// �V��X�y�g�J���
                    }
                    out.close();
                    in.close();
                } catch (Exception ex) {
                }
            } else {// �S���������ε{�����檺�{���X
                ta_receive.append("----��" + (i + 1) + "�Ӷl��----\n");
                ta_receive.append("�D�D�G" + folder.getMessage(i + 1).getSubject() + "\n");// �D�D
                ta_receive.append("����G" + folder.getMessage(i + 1).getContent() + "\n");// ����
                ta_receive.append("�o�e����G" + folder.getMessage(i + 1).getSentDate() + "\n");// �o�e���
                Address[] ias = folder.getMessage(i + 1).getFrom();// �o��H�a�}
                ta_receive.append("�o��H�G" + ias[0] + "\n");
                Address[] iasTo = folder.getMessage(i + 1).getAllRecipients();// ����H�a�}
                ta_receive.append("����H�G" + iasTo[0] + "\n\n");
            }
        }
        folder.close(false);// �����l�󧨡A�����R���l��
        store.close();// ����Store�ﹳ
    }
    
    public void receiveText(Message message) {
        
    }
}
