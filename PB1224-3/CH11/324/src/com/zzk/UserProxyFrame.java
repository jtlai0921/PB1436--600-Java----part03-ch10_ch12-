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
    private Proxy proxy;// �w�q�N�z
    private URL url;// �w�qURL�ﹳ
    private URLConnection urlConn;// �w�q�s���ﹳ
    private Scanner scanner;// �b�������z�L�N�zŪ����ƪ����˾�
    
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
        url = new URL(accessAddress);// �إ�URL�ﹳ
        proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyAddress,
                proxyPort));// �إߥN�z
        urlConn = url.openConnection(proxy);// �z�L�N�z�}�ҳs��
        scanner = new Scanner(urlConn.getInputStream(), "UTF8");// �z�L�y�إ߱��˾�
        ta_show.setText(null);// �M�ů¤�r�쪺���e
        StringBuffer sb = new StringBuffer();// �إߦr��֨��O����
        while (scanner.hasNextLine()) {// �P�_���˾��O�_�����
            String line = scanner.nextLine();// �q���˾���o�@����
            sb.append(line + "\n");// �V�r��֨��O����W�[��T
        }
        if (sb != null) {
            ta_show.append(sb.toString());// �b�¤�r�줤��ܸ�T
        }
    }
    
    /**
     * Create the frame
     */
    public UserProxyFrame() {
        super();
        getContentPane().setLayout(null);
        setTitle("�ϥ�Proxy�إߥN�z�A�Ⱦ�");
        setBounds(100, 100, 448, 334);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        final JLabel label = new JLabel();
        label.setText("�N�z�A�Ⱦ����a�}�G");
        label.setBounds(10, 10, 129, 18);
        getContentPane().add(label);
        
        tf_proxyAddress = new JTextField();
        tf_proxyAddress.setBounds(145, 8, 277, 22);
        getContentPane().add(tf_proxyAddress);
        
        final JLabel label_1 = new JLabel();
        label_1.setText("�N�z�A�Ⱦ����q�T�𸹡G");
        label_1.setBounds(10, 38, 136, 18);
        getContentPane().add(label_1);
        
        tf_proxyPort = new JTextField();
        tf_proxyPort.setBounds(145, 36, 277, 22);
        getContentPane().add(tf_proxyPort);
        
        final JLabel label_2 = new JLabel();
        label_2.setText("��J�n�s�����������}�A�M��T�{");
        label_2.setBounds(10, 65, 231, 18);
        getContentPane().add(label_2);
        
        tf_accessAddress = new JTextField();
        tf_accessAddress.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                try {
                    String proxyAddress = tf_proxyAddress.getText().trim();// �N�z�A�Ⱦ����a�}
                    int proxyPort = Integer.parseInt(tf_proxyPort.getText().trim());// �N�z�A�Ⱦ����q�T��
                    String accessAddress = tf_accessAddress.getText().trim();// �ݭn�}�Ҫ��������}
                    accessUrl(proxyAddress, proxyPort, accessAddress);// �I�s��k�A�ϥΥN�z�s������
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "��J����T���~�C");
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
        label_3.setText("�s�����G");
        label_3.setBounds(10, 113, 193, 18);
        getContentPane().add(label_3);
    }
    
}
