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
        Properties propperties = System.getProperties();// ��o�t���ݩʹﹳ
        propperties.setProperty("http.proxyHost", proxyAddress);// �]�wHTTP�A�ȨϥΪ��N�z�A�Ⱦ��a�}
        propperties.setProperty("http.proxyPort", "80");// �]�wHTTP�A�ȨϥΪ��N�z�A�Ⱦ��q�T��
        propperties.setProperty("https.proxyHost", proxyAddress);// �]�w�w��HTTP�A�ȨϥΪ��N�z�A�Ⱦ��a�}
        propperties.setProperty("https.proxyPort", "443");// �]�w�w��HTTP�A�ȨϥΪ��N�z�A�Ⱦ��q�T��
        propperties.setProperty("ftp.proxyHost", proxyAddress);// �]�wFTP�s�����N�z�A�Ⱦ��D��
        propperties.setProperty("ftp.proxyPort", "21");// �]�wFTP�s�����N�z�A�Ⱦ��q�T��
        propperties.setProperty("socks.ProxyHost", proxyAddress);// �]�wsocks�N�z�A�Ⱦ����a�}
        propperties.setProperty("socks.ProxyPort", "1080");// �]�wsocks�N�z�A�Ⱦ����q�T��
    }
    
    public void removeProxyInfo() {
        Properties propperties = System.getProperties();// ��o�t���ݩʹﹳ
        propperties.remove("http.proxyHost");// �M��HTTP�A�ȨϥΪ��N�z�A�Ⱦ��a�}
        propperties.remove("http.proxyPort");// �M��HTTP�A�ȨϥΪ��N�z�A�Ⱦ��q�T��
        propperties.remove("https.proxyHost");// �M���w��HTTP�A�ȨϥΪ��N�z�A�Ⱦ��a�}
        propperties.remove("https.proxyPort");// �M���w��HTTP�A�ȨϥΪ��N�z�A�Ⱦ��q�T��
        propperties.remove("ftp.proxyHost");// �M��FTP�s�����N�z�A�Ⱦ��D��
        propperties.remove("ftp.proxyPort");// �M��FTP�s�����N�z�A�Ⱦ��q�T��
        propperties.remove("socksProxyHost");// �M��socks�N�z�A�Ⱦ����a�}
        propperties.remove("socksProxyPort");// �M��socks�N�z�A�Ⱦ����q�T��
    }
    
    /**
     * �ϥ�HTTP�s��
     * @param accessAddress �ݭn�s�����a�}
     * @throws Exception �ߥX�ҥ~
     */
    public void useHttpAccess(String accessAddress) throws Exception{
        URL url = new URL(accessAddress);// �إ�URL�ﹳ
        URLConnection urlConn = url.openConnection(); // �۰ʩI�s�t�γ]�w��HTTP�N�z�A�Ⱦ��A�ö}�ҳs��
        Scanner scanner = new Scanner(urlConn.getInputStream(),"utf8");// �z�L�y�إ߱��˹ﹳ
        StringBuffer sb = new StringBuffer();// �إߦr�ž��֨��O����
        while (scanner.hasNextLine()) {// �p�G���˾�������T
            sb.append(scanner.nextLine()+"\n");// Ū���N�z�A�Ⱦ��W���������e�A�üW�[��r��֨��O���餤
        }
        if (sb!=null) {
            ta_info.append(sb.toString());// ��ܺ������e
        }
    }
    
    /**
     * Create the frame
     */
    public ProxySelectorFrame() {
        super();
        setTitle("�ϥ�ProxySelector��ܥN�z�A�Ⱦ�");
        getContentPane().setLayout(null);
        setBounds(100, 100, 419, 309);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        final JLabel label = new JLabel();
        label.setText("�п�J�N�z�A�Ⱦ��a�}�G");
        label.setBounds(21, 21, 151, 18);
        getContentPane().add(label);
        
        tf_proxyAddress = new JTextField();
        tf_proxyAddress.setBounds(167, 19, 218, 22);
        getContentPane().add(tf_proxyAddress);
        
        final JLabel label_1 = new JLabel();
        label_1.setText("��J�n�s�����������}�A�M��T�{");
        label_1.setBounds(21, 45, 218, 18);
        getContentPane().add(label_1);
        
        tf_accessAddress = new JTextField();
        tf_accessAddress.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                try {
                    String proxyAddress = tf_proxyAddress.getText().trim();// �N�z�A�Ⱦ��a�}
                    setProxyInfo(proxyAddress);// �]�w�����N�z
                    String accessAddress = tf_accessAddress.getText().trim();// ��o�ݭn�s�������}
                    useHttpAccess(accessAddress);// �I�s��k�A�i��http�s��
                    removeProxyInfo();// �M�������N�z
                } catch (Exception ex) {
                    
                }
            }
        });
        tf_accessAddress.setBounds(21, 69, 364, 22);
        getContentPane().add(tf_accessAddress);
        
        final JLabel label_2 = new JLabel();
        label_2.setText("�s�����G");
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