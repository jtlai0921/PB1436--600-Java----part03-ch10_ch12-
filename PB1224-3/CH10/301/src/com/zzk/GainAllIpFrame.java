package com.zzk;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * @author 張振坤
 *         獲得內網的所有IP地址
 */
@SuppressWarnings("serial")
public class GainAllIpFrame extends JFrame {
    private JTextArea ta_allIp;
    static public Hashtable<String, String> pingMap; // 用於儲存所ping的IP是否為內網IP的集合
    
    /**
     * Launch the application
     * 
     * @param args
     */
    public static void main(String args[]) {
        GainAllIpFrame frame = new GainAllIpFrame();
        frame.setVisible(true);
    }
    
    public void gainAllIp() throws Exception {// 獲得所有IP，並顯示在純文字域中的方法
        InetAddress host = InetAddress.getLocalHost();// 獲得本機的InetAddress對像
        String hostAddress = host.getHostAddress();// 獲得本機的IP地址
        int pos = hostAddress.lastIndexOf(".");// 獲得IP地址中最後一個點的位置
        String wd = hostAddress.substring(0, pos + 1);// 對本機的IP進行截取，獲得網段
        for (int i = 1; i <= 255; i++) { // 對局域網的IP地址進行檢查
            String ip = wd + i;// 產生IP地址
            PingIpThread thread = new PingIpThread(ip);// 建立線程對像
            thread.start();// 啟動線程對像
        }
        Set<String> set = pingMap.keySet();// 獲得集合中鍵的Set檢視
        Iterator<String> it = set.iterator();// 獲得迭代器對像
        while (it.hasNext()) { // 迭代器中有元素，則執行循環體
            String key = it.next(); // 獲得下一個鍵的名稱
            String value = pingMap.get(key);// 獲得指定鍵的值
            if (value.equals("true")) {
                ta_allIp.append(key + "\n");// 追加顯示IP地址
            }
        }
    }
    
    /**
     * Create the frame
     */
    public GainAllIpFrame() {
        super();
        addWindowListener(new WindowAdapter() {
            public void windowOpened(final WindowEvent e) {
                try {
                    gainAllIp();
                    ta_allIp.setText(null);
                } catch (Exception e1) {
                    ta_allIp.setText(null);
                }
            }
        });
        setTitle("獲得內網的所有IP地址");
        setBounds(400, 200, 270, 375);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        final JScrollPane scrollPane = new JScrollPane();
        getContentPane().add(scrollPane, BorderLayout.CENTER);
        
        ta_allIp = new JTextArea();
        scrollPane.setViewportView(ta_allIp);
        
        final JPanel panel = new JPanel();
        getContentPane().add(panel, BorderLayout.NORTH);
        
        final JButton button_2 = new JButton();
        button_2.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                try {
                    ta_allIp.setText(null);
                    gainAllIp();
                } catch (Exception e1) {
                    ta_allIp.setText(null);
                    JOptionPane.showMessageDialog(null, "應用程式例外，請再試一次。");
                }
            }
        });
        button_2.setText("顯示所有IP");
        panel.add(button_2);
        
        final JButton button = new JButton();
        button.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                System.exit(0);
            }
        });
        button.setText("退    出");
        panel.add(button);
        
        final JButton button_1 = new JButton();
        button_1.setText("New JButton");
        panel.add(button_1);
        pingMap = new Hashtable<String, String>();
    }
    
    class PingIpThread extends Thread {// 判斷指定IP是否為內網IP的線程對像
        public String ip; // 表示IP地址的成員變數
        public PingIpThread(String ip) {// 參數為需要判斷的IP地址
            this.ip = ip;
        }
        public void run() {
            try {
                // 獲得所ping的IP處理程序，-w 280是等待每次回覆的逾時時間，-n 1是要發送的回應請求數
                Process process = Runtime.getRuntime().exec(
                        "ping " + ip + " -w 280 -n 1");
                InputStream is = process.getInputStream();// 獲得處理程序的輸入流對像
                InputStreamReader isr = new InputStreamReader(is);// 建立InputStreamReader對像
                BufferedReader in = new BufferedReader(isr);// 建立緩衝字符流對像
                String line = in.readLine();// 讀取資訊
                while (line != null) {
                    if (line != null && !line.equals("")) {
                        if (line.substring(0, 2).equals("來自")
                                || (line.length() > 10 && line.substring(0, 10)
                                        .equals("Reply from"))) {// 判斷是ping透過的IP地址
                            pingMap.put(ip, "true");// 向集合中增加IP
                        }
                    }
                    line = in.readLine();// 再讀取資訊
                }
            } catch (IOException e) {
            }
        }
    }
}
