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
 * @author �i���[
 *         ��o�������Ҧ�IP�a�}
 */
@SuppressWarnings("serial")
public class GainAllIpFrame extends JFrame {
    private JTextArea ta_allIp;
    static public Hashtable<String, String> pingMap; // �Ω��x�s��ping��IP�O�_������IP�����X
    
    /**
     * Launch the application
     * 
     * @param args
     */
    public static void main(String args[]) {
        GainAllIpFrame frame = new GainAllIpFrame();
        frame.setVisible(true);
    }
    
    public void gainAllIp() throws Exception {// ��o�Ҧ�IP�A����ܦb�¤�r�줤����k
        InetAddress host = InetAddress.getLocalHost();// ��o������InetAddress�ﹳ
        String hostAddress = host.getHostAddress();// ��o������IP�a�}
        int pos = hostAddress.lastIndexOf(".");// ��oIP�a�}���̫�@���I����m
        String wd = hostAddress.substring(0, pos + 1);// �糧����IP�i��I���A��o���q
        for (int i = 1; i <= 255; i++) { // �什�����IP�a�}�i���ˬd
            String ip = wd + i;// ����IP�a�}
            PingIpThread thread = new PingIpThread(ip);// �إ߽u�{�ﹳ
            thread.start();// �Ұʽu�{�ﹳ
        }
        Set<String> set = pingMap.keySet();// ��o���X���䪺Set�˵�
        Iterator<String> it = set.iterator();// ��o���N���ﹳ
        while (it.hasNext()) { // ���N�����������A�h����`����
            String key = it.next(); // ��o�U�@���䪺�W��
            String value = pingMap.get(key);// ��o���w�䪺��
            if (value.equals("true")) {
                ta_allIp.append(key + "\n");// �l�[���IP�a�}
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
        setTitle("��o�������Ҧ�IP�a�}");
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
                    JOptionPane.showMessageDialog(null, "���ε{���ҥ~�A�ЦA�դ@���C");
                }
            }
        });
        button_2.setText("��ܩҦ�IP");
        panel.add(button_2);
        
        final JButton button = new JButton();
        button.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                System.exit(0);
            }
        });
        button.setText("�h    �X");
        panel.add(button);
        
        final JButton button_1 = new JButton();
        button_1.setText("New JButton");
        panel.add(button_1);
        pingMap = new Hashtable<String, String>();
    }
    
    class PingIpThread extends Thread {// �P�_���wIP�O�_������IP���u�{�ﹳ
        public String ip; // ���IP�a�}�������ܼ�
        public PingIpThread(String ip) {// �ѼƬ��ݭn�P�_��IP�a�}
            this.ip = ip;
        }
        public void run() {
            try {
                // ��o��ping��IP�B�z�{�ǡA-w 280�O���ݨC���^�Ъ��O�ɮɶ��A-n 1�O�n�o�e���^���ШD��
                Process process = Runtime.getRuntime().exec(
                        "ping " + ip + " -w 280 -n 1");
                InputStream is = process.getInputStream();// ��o�B�z�{�Ǫ���J�y�ﹳ
                InputStreamReader isr = new InputStreamReader(is);// �إ�InputStreamReader�ﹳ
                BufferedReader in = new BufferedReader(isr);// �إ߽w�Ħr�Ŭy�ﹳ
                String line = in.readLine();// Ū����T
                while (line != null) {
                    if (line != null && !line.equals("")) {
                        if (line.substring(0, 2).equals("�Ӧ�")
                                || (line.length() > 10 && line.substring(0, 10)
                                        .equals("Reply from"))) {// �P�_�Oping�z�L��IP�a�}
                            pingMap.put(ip, "true");// �V���X���W�[IP
                        }
                    }
                    line = in.readLine();// �AŪ����T
                }
            } catch (IOException e) {
            }
        }
    }
}
