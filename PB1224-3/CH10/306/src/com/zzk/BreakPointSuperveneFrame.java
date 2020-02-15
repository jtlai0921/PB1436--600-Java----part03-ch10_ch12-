package com.zzk;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class BreakPointSuperveneFrame extends JFrame {
    private JTextField tf_totalLength;
    private JTextField tf_residuaryLength;
    private JTextField tf_readToPos;
    private JTextField tf_address;
    private JTextField tf_endPos;
    private JTextField tf_startPos;
    private String urlAddress = "";// 用於儲存網絡資源的地址
    private long totalLength = 0;// 儲存網絡資源的大小，以字節為單位
    private long readToPos = 0;// 儲存上次讀取到的位置
    private long residuaryLength = 0;// 儲存未讀內容的大小
    
    /**
     * Launch the application
     * 
     * @param args
     */
    public static void main(String args[]) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    BreakPointSuperveneFrame frame = new BreakPointSuperveneFrame();
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
    public BreakPointSuperveneFrame() {
        super();
        getContentPane().setLayout(null);
        setTitle("下載網絡資源的斷點續傳");
        setBounds(100, 100, 514, 238);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        tf_startPos = new JTextField();
        tf_startPos.setBounds(80, 165, 113, 22);
        getContentPane().add(tf_startPos);
        
        final JLabel label = new JLabel();
        label.setText("起始位置：");
        label.setBounds(10, 167, 74, 18);
        getContentPane().add(label);
        
        final JLabel label_1 = new JLabel();
        label_1.setText("結束位置：");
        label_1.setBounds(199, 167, 74, 18);
        getContentPane().add(label_1);
        
        tf_endPos = new JTextField();
        tf_endPos.setBounds(267, 165, 117, 22);
        getContentPane().add(tf_endPos);
        
        final JLabel label_2 = new JLabel();
        label_2.setText("網絡資源的地址：");
        label_2.setBounds(10, 52, 113, 18);
        getContentPane().add(label_2);
        
        tf_address = new JTextField();
        tf_address.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                try {
                    urlAddress = tf_address.getText().trim();
                    URL url = new URL(urlAddress);// 獲得網絡資源的URL
                    HttpURLConnection connection = (HttpURLConnection) url
                            .openConnection();// 獲得連接對像
                    connection.connect();// 連接網絡資源
                    totalLength = connection.getContentLength();// 獲得網絡資源的長度
                    connection.disconnect();// 斷開連接
                    tf_totalLength.setText(String.valueOf(totalLength));// 顯示總長度
                    tf_readToPos.setText("0");// 顯示上次讀取到的位置
                    residuaryLength = totalLength;// 未讀內容為檔案總長度
                    tf_residuaryLength.setText(String.valueOf(residuaryLength));// 顯示未讀內容
                } catch (MalformedURLException e1) {
                    e1.printStackTrace();
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
                
            }
        });
        tf_address.setBounds(119, 50, 365, 22);
        getContentPane().add(tf_address);
        
        final JLabel label_3 = new JLabel();
        label_3.setForeground(new Color(0, 0, 255));
        label_3.setFont(new Font("", Font.BOLD, 14));
        label_3.setText("輸入網絡資源的地址並確認，可以獲得網絡資源的大小。");
        label_3.setBounds(10, 10, 384, 22);
        getContentPane().add(label_3);
        
        final JLabel label_4 = new JLabel();
        label_4.setForeground(new Color(128, 0, 0));
        label_4.setText("網絡資源的大小為");
        label_4.setBounds(10, 76, 113, 38);
        getContentPane().add(label_4);
        
        final JLabel label_5 = new JLabel();
        label_5.setText("上次讀取到");
        label_5.setBounds(10, 123, 74, 18);
        getContentPane().add(label_5);
        
        tf_readToPos = new JTextField();
        tf_readToPos.setBounds(80, 121, 113, 22);
        tf_readToPos.setEnabled(false);
        getContentPane().add(tf_readToPos);
        
        final JLabel label_6 = new JLabel();
        label_6.setText("字節處，還剩");
        label_6.setBounds(202, 123, 87, 18);
        getContentPane().add(label_6);
        
        tf_residuaryLength = new JTextField();
        tf_residuaryLength.setBounds(285, 120, 117, 22);
        tf_residuaryLength.setEnabled(false);
        getContentPane().add(tf_residuaryLength);
        
        final JLabel label_7 = new JLabel();
        label_7.setText("字節未讀。");
        label_7.setBounds(404, 123, 80, 18);
        getContentPane().add(label_7);
        
        final JLabel label_4_1 = new JLabel();
        label_4_1.setForeground(new Color(128, 0, 0));
        label_4_1.setText("個字節。");
        label_4_1.setBounds(404, 76, 80, 38);
        getContentPane().add(label_4_1);
        
        tf_totalLength = new JTextField();
        tf_totalLength.setBounds(119, 84, 283, 22);
        tf_totalLength.setEnabled(false);
        getContentPane().add(tf_totalLength);
        
        final JButton button = new JButton();
        button.setBounds(395, 162, 89, 28);
        getContentPane().add(button);
        button.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                if (totalLength == 0) {
                    JOptionPane.showMessageDialog(null,
                            "沒有網絡資源。\n\n請輸入正確的網址，然後確認。");
                    return;
                }
                long startPos = 0;// 起始位置
                long endPos = 0;// 結束位置
                try {
                    startPos = Long.parseLong(tf_startPos.getText().trim());// 起始位置
                    endPos = Long.parseLong(tf_endPos.getText().trim());// 結束位置
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "輸入的起始位置或結束位置不正確。");
                    return;
                }
                readToPos = endPos;// 記錄讀取到的位置
                residuaryLength = totalLength - readToPos;// 記錄未讀內容的大小
                tf_readToPos.setText(String.valueOf(readToPos));// 顯示讀取到的位置
                tf_residuaryLength.setText(String.valueOf(residuaryLength));// 顯示未讀字節數
                tf_startPos.setText(String.valueOf(readToPos));// 設定下一個讀取點的開始位置
                tf_endPos.setText(String.valueOf(totalLength));// 設定下一個讀取點的結束位置
                tf_endPos.requestFocus();// 使結束位置純文字框獲得焦點
                tf_endPos.selectAll();// 選擇結束位置純文字框中的全部內容，方便輸入結束位置值
                download(startPos, endPos);// 呼叫方法進行下載
            }
        });
        button.setText("開始下載");
    }
    
    public void download(long startPosition, long endPosition) {
        try {
            URL url = new URL(urlAddress);// 獲得網絡資源的URL
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();// 獲得連接對像
            connection.setRequestProperty("User-Agent", "NetFox");// 設定請求屬性
            String rangeProperty = "bytes=" + startPosition + "-";// 定義請求範圍屬性
            if (endPosition > 0) {
                rangeProperty += endPosition;// 調整請求範圍屬性
            }
            connection.setRequestProperty("RANGE", rangeProperty);// 設定請求範圍屬性
            connection.connect();// 連接網絡資源
            InputStream in = connection.getInputStream();// 獲得輸入流對像
            String file = url.getFile();// 獲得檔案對像
            String name = file.substring(file.lastIndexOf('/') + 1);// 獲得檔案名
            FileOutputStream out = new FileOutputStream("c:/" + name, true);// 建立輸出流對像,儲存下載的資源
            byte[] buff = new byte[2048];// 建立字節陣列
            int len = 0;// 定義儲存讀取內容長度的變數
            len = in.read(buff);// 讀取內容
            while (len != -1) {
                out.write(buff, 0, len);// 寫入磁碟
                len = in.read(buff);// 讀取內容
            }
            out.close();// 關閉流
            in.close();// 關閉流
            connection.disconnect();// 斷開連接
            if (readToPos > 0 && readToPos == totalLength) {
                JOptionPane.showMessageDialog(null, "完成網絡資源的下載。\n單擊「確定」按鈕退出程式。");
                System.exit(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
