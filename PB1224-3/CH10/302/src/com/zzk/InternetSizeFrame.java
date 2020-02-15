package com.zzk;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.net.URLConnection;
import javax.swing.JButton;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class InternetSizeFrame extends JFrame {
    
    private JTextField tf_size;
    private JTextField tf_address;
    /**
     * Launch the application
     * @param args
     */
    public static void main(String args[]) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    InternetSizeFrame frame = new InternetSizeFrame();
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
    public InternetSizeFrame() {
        super();
        setTitle("��o�����귽���j�p");
        getContentPane().setLayout(null);
        setBounds(100, 100, 391, 223);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        final JLabel label = new JLabel();
        label.setText("��J���}�G");
        label.setBounds(10, 77, 79, 18);
        getContentPane().add(label);

        tf_address = new JTextField();
        tf_address.setBounds(82, 75, 286, 22);
        getContentPane().add(tf_address);

        final JButton button = new JButton();
        button.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                String address = tf_address.getText().trim();// ��o��J�����}
                try{
                    long len = netSourceSize(address);// �I�s��k��o�����귽���j�p
                    tf_size.setText(String.valueOf(len)+" �r�`");// �b�¤�r�ؤ���ܺ����귽���j�p
                }catch(Exception ex){
                    ex.printStackTrace();
                }
            }
        });
        button.setText("��o�j�p");
        button.setBounds(282, 123, 86, 28);
        getContentPane().add(button);

        final JLabel label_1 = new JLabel();
        label_1.setText("�����귽���j�p���G");
        label_1.setBounds(10, 128, 127, 18);
        getContentPane().add(label_1);

        tf_size = new JTextField();
        tf_size.setBounds(131, 126, 145, 22);
        getContentPane().add(tf_size);

        final JLabel label_2 = new JLabel();
        label_2.setForeground(new Color(0, 0, 255));
        label_2.setFont(new Font("", Font.BOLD, 22));
        label_2.setText("��o�����귽���j�p");
        label_2.setBounds(82, 8, 239, 44);
        getContentPane().add(label_2);
    }
    public long netSourceSize(String sUrl) throws Exception{
        URL url = new URL(sUrl);  // �إ�URL�ﹳ
        URLConnection urlConn = url.openConnection();          // ��o�����s���ﹳ
        urlConn.connect();                                     // �}�Ҩ�url�ѦҸ귽���q�T�챵
        return urlConn.getContentLength();                     // �H�r�`�����Ǧ^�귽���j�p
    }
}
