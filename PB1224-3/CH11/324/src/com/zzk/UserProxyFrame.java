package com.zzk;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class UserProxyFrame extends JFrame {
    
    private JTextArea ta_show;
    private JTextField tf_accessAddress;
    private JTextField tf_proxyPort;
    private JTextField tf_proxyAddress;
    private Proxy proxy;// 定義代理
    private URL url;// 定義URL對像
    private URLConnection urlConn;// 定義連接對像
    private Scanner scanner;// 在網絡中透過代理讀取資料的掃瞄器
    
    /**
     * Launch the application
     * 
     * @param args
     */
    public static void main(String args[]) {
        UserProxyFrame frame = new UserProxyFrame();
        frame.setVisible(true);
    }
    
    public void accessUrl(String proxyAddress, int proxyPort,
            String accessAddress) throws Exception {
        url = new URL(accessAddress);// 建立URL對像
        proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyAddress,
                proxyPort));// 建立代理
        urlConn = url.openConnection(proxy);// 透過代理開啟連接
        scanner = new Scanner(urlConn.getInputStream(), "UTF8");// 透過流建立掃瞄器
        ta_show.setText(null);// 清空純文字域的內容
        StringBuffer sb = new StringBuffer();// 建立字串快取記憶體
        while (scanner.hasNextLine()) {// 判斷掃瞄器是否有資料
            String line = scanner.nextLine();// 從掃瞄器獲得一行資料
            sb.append(line + "\n");// 向字串快取記憶體增加資訊
        }
        if (sb != null) {
            ta_show.append(sb.toString());// 在純文字域中顯示資訊
        }
    }
    
    /**
     * Create the frame
     */
    public UserProxyFrame() {
        super();
        getContentPane().setLayout(null);
        setTitle("使用Proxy建立代理服務器");
        setBounds(100, 100, 448, 334);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        final JLabel label = new JLabel();
        label.setText("代理服務器的地址：");
        label.setBounds(10, 10, 129, 18);
        getContentPane().add(label);
        
        tf_proxyAddress = new JTextField();
        tf_proxyAddress.setBounds(145, 8, 277, 22);
        getContentPane().add(tf_proxyAddress);
        
        final JLabel label_1 = new JLabel();
        label_1.setText("代理服務器的通訊埠號：");
        label_1.setBounds(10, 38, 136, 18);
        getContentPane().add(label_1);
        
        tf_proxyPort = new JTextField();
        tf_proxyPort.setBounds(145, 36, 277, 22);
        getContentPane().add(tf_proxyPort);
        
        final JLabel label_2 = new JLabel();
        label_2.setText("輸入要存取的網站網址，然後確認");
        label_2.setBounds(10, 65, 231, 18);
        getContentPane().add(label_2);
        
        tf_accessAddress = new JTextField();
        tf_accessAddress.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                try {
                    String proxyAddress = tf_proxyAddress.getText().trim();// 代理服務器的地址
                    int proxyPort = Integer.parseInt(tf_proxyPort.getText().trim());// 代理服務器的通訊埠
                    String accessAddress = tf_accessAddress.getText().trim();// 需要開啟的網站網址
                    accessUrl(proxyAddress, proxyPort, accessAddress);// 呼叫方法，使用代理存取網站
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "輸入的資訊有誤。");
                }
            }
        });
        tf_accessAddress.setBounds(10, 85, 412, 22);
        getContentPane().add(tf_accessAddress);
        
        final JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(10, 137, 412, 149);
        getContentPane().add(scrollPane);
        
        ta_show = new JTextArea();
        scrollPane.setViewportView(ta_show);
        
        final JLabel label_3 = new JLabel();
        label_3.setText("存取結果");
        label_3.setBounds(10, 113, 193, 18);
        getContentPane().add(label_3);
    }
    
}
