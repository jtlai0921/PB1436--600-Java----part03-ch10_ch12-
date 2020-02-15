package com.zzk;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import javax.swing.JButton;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class SingleThreadDownloadFrame extends JFrame {
    
    private JTextField tf_address;
    /**
     * Launch the application
     * @param args
     */
    public static void main(String args[]) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    SingleThreadDownloadFrame frame = new SingleThreadDownloadFrame();
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
    public SingleThreadDownloadFrame() {
        super();
        getContentPane().setLayout(null);
        setTitle("網絡資源的單線程下載");
        setBounds(100, 100, 500, 237);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        final JLabel label = new JLabel();
        label.setText("網絡資源的網址：");
        label.setBounds(10, 88, 118, 18);
        getContentPane().add(label);

        tf_address = new JTextField();
        tf_address.setBounds(117, 86, 357, 22);
        getContentPane().add(tf_address);

        final JButton button = new JButton();
        button.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                String address = tf_address.getText().trim();// 獲得網址
                download(address);  // 下載檔案
            }
        });
        button.setText("單擊開始下載");
        button.setBounds(41, 144, 145, 28);
        getContentPane().add(button);

        final JButton button_1 = new JButton();
        button_1.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                tf_address.setText(null);// 清除純文字框內容
                tf_address.requestFocus();// 純文字框獲得焦點
            }
        });
        button_1.setText("清    空");
        button_1.setBounds(204, 144, 106, 28);
        getContentPane().add(button_1);

        final JButton button_2 = new JButton();
        button_2.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                System.exit(0);
            }
        });
        button_2.setText("退    出");
        button_2.setBounds(328, 144, 106, 28);
        getContentPane().add(button_2);

        final JLabel label_1 = new JLabel();
        label_1.setForeground(new Color(0, 0, 255));
        label_1.setFont(new Font("", Font.BOLD, 24));
        label_1.setText("網絡資源的單線程下載");
        label_1.setBounds(117, 21, 301, 48);
        getContentPane().add(label_1);
    }
    public void download(String urlAddr){         // 從指定網址下載檔案
        try {
            URL url = new URL(urlAddr);    // 建立URL對像
            URLConnection urlConn = url.openConnection();  // 獲得連接對像
            urlConn.connect();                           // 開啟到url參考資源的通訊鏈接
            InputStream in = urlConn.getInputStream() ;      // 獲得輸入流對像
            String filePath = url.getFile();                  // 獲得完整路徑
            int pos = filePath.lastIndexOf("/");              // 獲得路徑中最後一個斜槓的位置
            String fileName = filePath.substring(pos+1);      // 截取檔案名
            FileOutputStream out = new FileOutputStream("C:/"+fileName);  // 建立輸出流對像
            byte[] bytes = new byte[1024];                 // 宣告存放下載內容的字節陣列
            int len = in.read(bytes);                       // 從輸入流中讀取內容
            while (len != -1){
                out.write(bytes,0,len);                     // 將讀取的內容寫到輸出流
                len = in.read(bytes);                      // 繼續從輸入流中讀取內容
            }
            out.close();          // 關閉輸出流
            in.close();           // 關閉輸入流
            JOptionPane.showMessageDialog(null, "下載完畢");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
