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
    private String receiveHost = "localhost";// 接收主機
    private String receiveProtocol = "imap";// 接收協定
    private String username = "zzk";// 使用者名稱
    private String password = "zzk";// 密碼
    
    public static void main(String args[]) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    ReceiveMailFrame frame = new ReceiveMailFrame();
                    frame.init();// 呼叫初始化方法
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
        setTitle("接收郵件窗體");
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
                    receiveMessage();// 呼叫接收資訊的方法
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });
        button.setText("接收郵件並下載附屬應用程式");
        panel.add(button);
        
        final JButton button_1 = new JButton();
        button_1.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                System.exit(0);
            }
        });
        button_1.setText("退出系統");
        panel.add(button_1);
    }
    
    public void init() throws Exception {
        Properties props = new Properties();// 宣告Properties對像
        props.put("mail.store.protocol", receiveProtocol);// 指定接收協定
        props.put("mail.imap.class", "com.sun.mail.imap.IMAPStore");// 指定使用Store進行接收
        session = Session.getDefaultInstance(props);// 獲得Session對像
        store = session.getStore(receiveProtocol);// 獲得Store對像
        store.connect(receiveHost, username, password);// 連接接收服務器
    }
    
    public void receiveMessage() throws Exception {
        Folder folder = store.getFolder("inbox");// 獲得inbox郵件夾的Folder對像
        if (folder == null) {
            throw new Exception("不存在inbox郵件夾。");
        }
        folder.open(Folder.READ_ONLY);// 以只讀方式開啟郵件夾
        ta_receive.append("您共收到" + folder.getMessageCount() + "個電子郵件。\n\n");
        Message[] messages = folder.getMessages();// 獲得郵件夾中的所有郵件
        for (int i = 0; i < messages.length; i++) {
            Object content = messages[i].getContent();// 獲得郵件內容
            if (content instanceof Multipart) {// 有附屬應用程式執行的程式碼
                ta_receive.append("----第" + (i + 1) + "個郵件----\n");
                ta_receive.append("主題：" + folder.getMessage(i + 1).getSubject() + "\n");// 主題
                Multipart mPart = (Multipart) content;// 轉為Multipart對像
                ta_receive.append("正文：" + mPart.getBodyPart(0).getContent() + "\n");// 正文內容
                ta_receive.append("發送日期：" + folder.getMessage(i + 1).getSentDate() + "\n");// 發送日期
                Address[] ias = folder.getMessage(i + 1).getFrom();// 發件人地址
                ta_receive.append("發件人：" + ias[0] + "\n");
                Address[] iasTo = folder.getMessage(i + 1).getAllRecipients();// 收件人地址
                ta_receive.append("收件人：" + iasTo[0] + "\n\n");
                try {
                    String fileName = mPart.getBodyPart(1).getFileName();// 獲得檔案名
                    ta_receive.append("接收到一個名為「" + MimeUtility.decodeText(fileName) + "」的附屬應用程式\n");
                    InputStream in = mPart.getBodyPart(1).getInputStream();
                    FileDialog dialog = new FileDialog(ReceiveMailFrame.this, "儲存");// 建立交談視窗
                    dialog.setMode(FileDialog.SAVE);// 設定交談視窗為儲存交談視窗
                    dialog.setFile(MimeUtility.decodeText(fileName));
                    dialog.setVisible(true);// 顯示儲存交談視窗
                    String path = dialog.getDirectory();// 獲得檔案的儲存路徑
                    String saveFileName = dialog.getFile();// 獲得儲存的檔案名
                    if (path == null || saveFileName == null) {
                        return;
                    }
                    OutputStream out = new BufferedOutputStream(new FileOutputStream(path + "/" + saveFileName));// 建立儲存附屬應用程式檔案的輸出流對像
                    int len = -1;
                    while ((len = in.read()) != -1) {
                        out.write(len);// 向輸出流寫入資料
                    }
                    out.close();
                    in.close();
                } catch (Exception ex) {
                }
            } else {// 沒有附屬應用程式執行的程式碼
                ta_receive.append("----第" + (i + 1) + "個郵件----\n");
                ta_receive.append("主題：" + folder.getMessage(i + 1).getSubject() + "\n");// 主題
                ta_receive.append("正文：" + folder.getMessage(i + 1).getContent() + "\n");// 正文
                ta_receive.append("發送日期：" + folder.getMessage(i + 1).getSentDate() + "\n");// 發送日期
                Address[] ias = folder.getMessage(i + 1).getFrom();// 發件人地址
                ta_receive.append("發件人：" + ias[0] + "\n");
                Address[] iasTo = folder.getMessage(i + 1).getAllRecipients();// 收件人地址
                ta_receive.append("收件人：" + iasTo[0] + "\n\n");
            }
        }
        folder.close(false);// 關閉郵件夾，但不刪除郵件
        store.close();// 關閉Store對像
    }
    
    public void receiveText(Message message) {
        
    }
}
