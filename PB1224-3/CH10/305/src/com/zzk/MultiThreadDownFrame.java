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
        setTitle("�����귽���h�u�{�U��");
        setBounds(100, 100, 482, 189);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        final JButton button = new JButton();
        button.setBounds(10, 95, 106, 28);
        getContentPane().add(button);
        button.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                try {
                    String address = tf_address.getText();// ��o�����귽�a�}
                    download(address, "c:\\01.flv", 2);// �I�sdownload()��k,�N�U���������귽�x�s��Ϻ�
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });
        button.setText("�U    ��");
        
        final JLabel label = new JLabel();
        label.setText("�����귽���a�}�G");
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
        button_1.setText("�M    ��");
        button_1.setBounds(179, 95, 106, 28);
        getContentPane().add(button_1);
        
        final JButton button_2 = new JButton();
        button_2.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                System.exit(0);
            }
        });
        button_2.setText("�h    �X");
        button_2.setBounds(349, 95, 106, 28);
        getContentPane().add(button_2);
    }
    
    public void download(String url, String dest, int threadNum)
            throws Exception {
        URL downURL = new URL(url);// �إߺ����귽��URL
        HttpURLConnection conn = (HttpURLConnection) downURL.openConnection();// �}�Һ����䱵
        long fileLength = -1;// �Ω��x�s�ɮת��ת��ܼ�
        int stateFlagCode = conn.getResponseCode();// ��o�s�����A�аO�{���X
        if (stateFlagCode == 200) {// �����s�����`
            fileLength = conn.getContentLength();// ��o�ɮת�����
            conn.disconnect();// ���������s��
        }
        if (fileLength > 0) {
            long byteCounts = fileLength / threadNum + 1;// �p��C�ӽu�{���r�`��
            File file = new File(dest);// �إߥؼ��ɮת�File�ﹳ
            int i = 0;
            while (i < threadNum) {
                long startPosition = byteCounts * i;// �w�q�}�l��m
                long endPosition = byteCounts * (i + 1);// �w�q������m
                if (i == threadNum - 1) {
                    DownMultiThread fileThread = new DownMultiThread(url, file,
                            startPosition, 0);// �إ�DownMultiThread�u�{�����
                    new Thread(fileThread).start();// �Ұʽu�{�ﹳ
                } else {
                    DownMultiThread fileThread = new DownMultiThread(url, file,
                            startPosition, endPosition);// �إ�DownMultiThread�u�{�����
                    new Thread(fileThread).start();// �Ұʽu�{�ﹳ
                }
                i++;
            }
            JOptionPane.showMessageDialog(null, "���������귽���U���C");
        }
    }
}
