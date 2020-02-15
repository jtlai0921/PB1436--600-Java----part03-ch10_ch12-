package com.zzk;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FileDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.Properties;
import java.util.Vector;

import javax.mail.Address;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeUtility;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import com.sun.mail.imap.IMAPFolder;

public class ReceiveMailFrame extends JFrame {
    private JTextArea ta_receive;
    protected Session session;// 宣告Session對像
    protected Store store;// 宣告Store對像
    private String receiveHost = "localhost";// 接收主機
    private String receiveProtocol = "imap";// 接收協定
    private String username = "zzk";// 使用者名稱
    private String password = "zzk";// 密碼
    private Vector<String> readedEmailUIDVector = new Vector<String>();// 儲存已讀郵件UID的向量
    private ObjectOutputStream objectOut = null;// 宣告對像輸出流
    private ObjectInputStream objectIn = null;// 宣告對像輸入流
    private int noReadNums = 0;// 記錄未讀郵件數
    
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
        IMAPFolder folder = (IMAPFolder) store.getFolder("inbox");// 獲得inbox郵件夾的IMAPFolder對像
        if (folder == null) {
            throw new Exception("不存在inbox郵件夾。");
        }
        folder.open(Folder.READ_ONLY);// 以只讀方式開啟郵件夾
        Message[] messages = folder.getMessages();// 獲得郵件夾中的所有郵件
        for (int i = 0; i < messages.length; i++) {
            long uid = folder.getUID(messages[i]);// 獲得郵件的UID
            try {
                objectIn = new ObjectInputStream(new FileInputStream("c:/readedEmail.txt"));// 建立輸入流對像
                Object readObj = objectIn.readObject();// 讀取資訊
                if (readObj == null) { // 檔案中未儲存已讀檔案的UID，說明是未讀郵件
                    readedEmailUIDVector.add(String.valueOf(uid));// 將讀取郵件的UID增加到向量中
                    objectOut = new ObjectOutputStream(new FileOutputStream("c:/readedEmail.txt"));// 建立輸出流對像
                    objectOut.writeObject(readedEmailUIDVector);// 將已讀郵件UID的向量寫入磁碟
                    // 讀取郵件資訊
                    noReadNums++;
                    readMessage(messages[i], folder, i);// 呼叫readMessage()方法，讀取郵件資訊
                } else {
                    boolean readedFlag = false; // 為false表示未讀
                    readedEmailUIDVector = (Vector<String>) readObj;
                    for (int j = 0; j < readedEmailUIDVector.size(); j++) {
                        if (String.valueOf(uid).equals(
                                readedEmailUIDVector.get(j))) {
                            readedFlag = true; // 標記為true，表示已讀
                            break;
                        }
                    }
                    if (readedFlag) {
                        continue;
                    } else {
                        readedEmailUIDVector.add(String.valueOf(uid));// 將讀取郵件的UID增加到向量中
                        objectOut = new ObjectOutputStream(new FileOutputStream("c:/readedEmail.txt"));// 建立輸出流對像
                        objectOut.writeObject(readedEmailUIDVector);// 將已讀郵件UID的向量寫入磁碟
                     // 讀取郵件資訊
                        noReadNums++;
                        readMessage(messages[i], folder, i);// 呼叫readMessage()方法，讀取郵件資訊
                    }
                }
                // /
            } catch (Exception ex) {
                readedEmailUIDVector.add(String.valueOf(uid));// 將讀取郵件的UID增加到向量中
                objectOut = new ObjectOutputStream(new FileOutputStream("c:/readedEmail.txt"));// 建立輸出流對像
                objectOut.writeObject(readedEmailUIDVector);// 將已讀郵件UID的向量寫入磁碟
             // 讀取郵件資訊
                noReadNums++;
                readMessage(messages[i], folder, i);// 呼叫readMessage()方法，讀取郵件資訊
            }
            
        }
        ta_receive.append("您共收到" + folder.getMessageCount() + "個郵件。\n");
        if (noReadNums > 0) {
            ta_receive.append("其中上述" + noReadNums + "個是新郵件。\n\n");
        } else {
            ta_receive.append("沒有未讀郵件。\n\n");
        }
        folder.close(false);// 關閉郵件夾，但不刪除郵件
        store.close();// 關閉Store對像
        if (objectOut != null)
            objectOut.close();
        if (objectIn != null)
            objectIn.close();
    }
    
    public void readMessage(Message message, IMAPFolder folder, int i) throws Exception {
        Object content = message.getContent();// 獲得郵件內容
        if (content instanceof Multipart) {// 有附屬應用程式執行的程式碼
            ta_receive.append("----第" + (i + 1) + "個郵件----\n");
            ta_receive.append("主題：" + folder.getMessage(i + 1).getSubject() + "\n");// 主題
            Multipart mPart = (Multipart) content;// 建立Multipart對像
            ta_receive.append("正文：" + mPart.getBodyPart(0).getContent() + "\n");// 正文
            ta_receive.append("發送日期：" + folder.getMessage(i + 1).getSentDate() + "\n");// 發送日期
            Address[] ias = folder.getMessage(i + 1).getFrom();// 發件人地址
            ta_receive.append("發件人：" + ias[0] + "\n");
            Address[] iasTo = folder.getMessage(i + 1).getAllRecipients();// 收件人地址
            ta_receive.append("收件人：" + iasTo[0] + "\n\n");
            try {
                String fileName = mPart.getBodyPart(1).getFileName();// 獲得檔案名
                ta_receive.append("接收到一個名為「" + MimeUtility.decodeText(fileName) + "」的附屬應用程式\n\n");// 獲得附屬應用程式檔案名
                InputStream in = mPart.getBodyPart(1).getInputStream();// 獲得輸入流對像
                FileDialog dialog = new FileDialog(ReceiveMailFrame.this, "儲存");// 建立交談視窗
                dialog.setMode(FileDialog.SAVE);// 設定交談視窗為儲存交談視窗
                dialog.setFile(MimeUtility.decodeText(fileName));// 設定交談視窗顯示的檔案名
                dialog.setVisible(true);// 顯示儲存交談視窗
                String path = dialog.getDirectory();// 獲得檔案的儲存路徑
                String saveFileName = dialog.getFile();// 獲得儲存的檔案名
                if (path == null || saveFileName == null) {
                    return;
                }
                OutputStream out = new BufferedOutputStream(new FileOutputStream(path + "/" + saveFileName));// 建立輸出流對像
                int len = -1;
                while ((len = in.read()) != -1) {// 讀取內容，如果沒到檔案尾則執行循環體
                    out.write(len);// 寫入檔案
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
}
