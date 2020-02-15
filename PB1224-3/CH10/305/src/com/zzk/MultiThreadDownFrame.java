package com.zzk;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class MultiThreadDownFrame extends JFrame {
    private JTextField tf_address;
    
    /**
     * Launch the application
     * 
     * @param args
     */
    public static void main(String args[]) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    MultiThreadDownFrame frame = new MultiThreadDownFrame();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    
    public MultiThreadDownFrame() {
        super();
        getContentPane().setLayout(null);
        setTitle("網絡資源的多線程下載");
        setBounds(100, 100, 482, 189);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        final JButton button = new JButton();
        button.setBounds(10, 95, 106, 28);
        getContentPane().add(button);
        button.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                try {
                    String address = tf_address.getText();// 獲得網絡資源地址
                    download(address, "c:\\01.flv", 2);// 呼叫download()方法,將下載的網絡資源儲存到磁碟
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });
        button.setText("下    載");
        
        final JLabel label = new JLabel();
        label.setText("網絡資源的地址：");
        label.setBounds(10, 44, 106, 18);
        getContentPane().add(label);
        
        tf_address = new JTextField();
        tf_address.setBounds(114, 42, 341, 22);
        getContentPane().add(tf_address);
        
        final JButton button_1 = new JButton();
        button_1.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                tf_address.setText(null);
            }
        });
        button_1.setText("清    空");
        button_1.setBounds(179, 95, 106, 28);
        getContentPane().add(button_1);
        
        final JButton button_2 = new JButton();
        button_2.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                System.exit(0);
            }
        });
        button_2.setText("退    出");
        button_2.setBounds(349, 95, 106, 28);
        getContentPane().add(button_2);
    }
    
    public void download(String url, String dest, int threadNum)
            throws Exception {
        URL downURL = new URL(url);// 建立網絡資源的URL
        HttpURLConnection conn = (HttpURLConnection) downURL.openConnection();// 開啟網絡邊接
        long fileLength = -1;// 用於儲存檔案長度的變數
        int stateFlagCode = conn.getResponseCode();// 獲得連接狀態標記程式碼
        if (stateFlagCode == 200) {// 網絡連接正常
            fileLength = conn.getContentLength();// 獲得檔案的長度
            conn.disconnect();// 取消網絡連接
        }
        if (fileLength > 0) {
            long byteCounts = fileLength / threadNum + 1;// 計算每個線程的字節數
            File file = new File(dest);// 建立目標檔案的File對像
            int i = 0;
            while (i < threadNum) {
                long startPosition = byteCounts * i;// 定義開始位置
                long endPosition = byteCounts * (i + 1);// 定義結束位置
                if (i == threadNum - 1) {
                    DownMultiThread fileThread = new DownMultiThread(url, file,
                            startPosition, 0);// 建立DownMultiThread線程的實例
                    new Thread(fileThread).start();// 啟動線程對像
                } else {
                    DownMultiThread fileThread = new DownMultiThread(url, file,
                            startPosition, endPosition);// 建立DownMultiThread線程的實例
                    new Thread(fileThread).start();// 啟動線程對像
                }
                i++;
            }
            JOptionPane.showMessageDialog(null, "完成網絡資源的下載。");
        }
    }
}
