package com.zzk;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.net.URLConnection;
import java.util.Properties;
import java.util.Scanner;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ProxySelectorFrame extends JFrame {
    
    private JTextArea ta_info;
    private JTextField tf_accessAddress;
    private JTextField tf_proxyAddress;
    
    /**
     * Launch the application
     * 
     * @param args
     */
    public static void main(String args[]) {
        ProxySelectorFrame frame = new ProxySelectorFrame();
        frame.setVisible(true);
    }
    
    public void setProxyInfo(String proxyAddress) {
        Properties propperties = System.getProperties();// 獲得系統屬性對像
        propperties.setProperty("http.proxyHost", proxyAddress);// 設定HTTP服務使用的代理服務器地址
        propperties.setProperty("http.proxyPort", "80");// 設定HTTP服務使用的代理服務器通訊埠
        propperties.setProperty("https.proxyHost", proxyAddress);// 設定安全HTTP服務使用的代理服務器地址
        propperties.setProperty("https.proxyPort", "443");// 設定安全HTTP服務使用的代理服務器通訊埠
        propperties.setProperty("ftp.proxyHost", proxyAddress);// 設定FTP存取的代理服務器主機
        propperties.setProperty("ftp.proxyPort", "21");// 設定FTP存取的代理服務器通訊埠
        propperties.setProperty("socks.ProxyHost", proxyAddress);// 設定socks代理服務器的地址
        propperties.setProperty("socks.ProxyPort", "1080");// 設定socks代理服務器的通訊埠
    }
    
    public void removeProxyInfo() {
        Properties propperties = System.getProperties();// 獲得系統屬性對像
        propperties.remove("http.proxyHost");// 清除HTTP服務使用的代理服務器地址
        propperties.remove("http.proxyPort");// 清除HTTP服務使用的代理服務器通訊埠
        propperties.remove("https.proxyHost");// 清除安全HTTP服務使用的代理服務器地址
        propperties.remove("https.proxyPort");// 清除安全HTTP服務使用的代理服務器通訊埠
        propperties.remove("ftp.proxyHost");// 清除FTP存取的代理服務器主機
        propperties.remove("ftp.proxyPort");// 清除FTP存取的代理服務器通訊埠
        propperties.remove("socksProxyHost");// 清除socks代理服務器的地址
        propperties.remove("socksProxyPort");// 清除socks代理服務器的通訊埠
    }
    
    /**
     * 使用HTTP存取
     * @param accessAddress 需要存取的地址
     * @throws Exception 拋出例外
     */
    public void useHttpAccess(String accessAddress) throws Exception{
        URL url = new URL(accessAddress);// 建立URL對像
        URLConnection urlConn = url.openConnection(); // 自動呼叫系統設定的HTTP代理服務器，並開啟連接
        Scanner scanner = new Scanner(urlConn.getInputStream(),"utf8");// 透過流建立掃瞄對像
        StringBuffer sb = new StringBuffer();// 建立字符器快取記憶體
        while (scanner.hasNextLine()) {// 如果掃瞄器中有資訊
            sb.append(scanner.nextLine()+"\n");// 讀取代理服務器上的網頁內容，並增加到字串快取記憶體中
        }
        if (sb!=null) {
            ta_info.append(sb.toString());// 顯示網頁內容
        }
    }
    
    /**
     * Create the frame
     */
    public ProxySelectorFrame() {
        super();
        setTitle("使用ProxySelector選擇代理服務器");
        getContentPane().setLayout(null);
        setBounds(100, 100, 419, 309);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        final JLabel label = new JLabel();
        label.setText("請輸入代理服務器地址：");
        label.setBounds(21, 21, 151, 18);
        getContentPane().add(label);
        
        tf_proxyAddress = new JTextField();
        tf_proxyAddress.setBounds(167, 19, 218, 22);
        getContentPane().add(tf_proxyAddress);
        
        final JLabel label_1 = new JLabel();
        label_1.setText("輸入要存取的網站網址，然後確認");
        label_1.setBounds(21, 45, 218, 18);
        getContentPane().add(label_1);
        
        tf_accessAddress = new JTextField();
        tf_accessAddress.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                try {
                    String proxyAddress = tf_proxyAddress.getText().trim();// 代理服務器地址
                    setProxyInfo(proxyAddress);// 設定本機代理
                    String accessAddress = tf_accessAddress.getText().trim();// 獲得需要存取的網址
                    useHttpAccess(accessAddress);// 呼叫方法，進行http存取
                    removeProxyInfo();// 清除本機代理
                } catch (Exception ex) {
                    
                }
            }
        });
        tf_accessAddress.setBounds(21, 69, 364, 22);
        getContentPane().add(tf_accessAddress);
        
        final JLabel label_2 = new JLabel();
        label_2.setText("存取結果");
        label_2.setBounds(21, 97, 75, 18);
        getContentPane().add(label_2);
        
        final JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(21, 121, 358, 140);
        getContentPane().add(scrollPane);
        
        ta_info = new JTextArea();
        scrollPane.setViewportView(ta_info);
        //
    }
    

}
