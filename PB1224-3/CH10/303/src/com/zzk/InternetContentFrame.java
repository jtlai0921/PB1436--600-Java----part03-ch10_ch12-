package com.zzk;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class InternetContentFrame extends JFrame {
    
    private JTextArea ta_content;
    private JTextField tf_address;
    /**
     * Launch the application
     * @param args
     */
    public static void main(String args[]) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    InternetContentFrame frame = new InternetContentFrame();
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
    public InternetContentFrame() {
        super();
        setTitle("解析網頁中的內容");
        setBounds(100, 100, 484, 375);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        final JPanel panel = new JPanel();
        getContentPane().add(panel, BorderLayout.NORTH);

        final JLabel label = new JLabel();
        label.setText("輸入網址：");
        panel.add(label);

        tf_address = new JTextField();
        tf_address.setPreferredSize(new Dimension(260,25));
        panel.add(tf_address);

        final JButton button = new JButton();
        button.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                String address = tf_address.getText().trim();// 獲得輸入的網址
                Collection urlCollection = getURLCollection(address);// 呼叫方法，獲得網頁內容的集合對像
                Iterator it = urlCollection.iterator();  // 獲得集合的迭代器對像
                while(it.hasNext()){
                    ta_content.append((String)it.next()+"\n");       // 在純文字域中顯示解析的內容
                }
            }
        });
        button.setText("解析網頁");
        panel.add(button);

        final JScrollPane scrollPane = new JScrollPane();
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        ta_content = new JTextArea();
        ta_content.setFont(new Font("", Font.BOLD, 14));
        scrollPane.setViewportView(ta_content);
        //
    }
    public Collection<String> getURLCollection(String urlString){
        URL url = null;                             // 宣告URL
        URLConnection conn = null;                  // 宣告URLConnection
        Collection<String> urlCollection = new ArrayList<String>(); // 建立集合對像
        try{
            url = new URL(urlString);               // 建立URL對像
            conn = url.openConnection();            // 獲得連接對像
            conn.connect();                         // 開啟到url參考資源的通訊鏈接
            InputStream is = conn.getInputStream(); // 獲得流對像
            InputStreamReader in = new InputStreamReader(is,"UTF-8"); // 轉為字符流
            BufferedReader br = new BufferedReader(in); // 建立緩衝流對像
            String nextLine = br.readLine();            // 讀取資訊，解析網頁
            while (nextLine !=null){
                urlCollection.add(nextLine);   // 解析網頁的全部內容，增加到集合中
                nextLine = br.readLine();      // 讀取資訊，解析網頁
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return urlCollection;
    }

}
