package com.zzk;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import javax.swing.JButton;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class SingleThreadDownloadFrame extends JFrame {
    
    private JTextField tf_address;
    /**
     * Launch the application
     * @param args
     */
    public static void main(String args[]) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    SingleThreadDownloadFrame frame = new SingleThreadDownloadFrame();
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
    public SingleThreadDownloadFrame() {
        super();
        getContentPane().setLayout(null);
        setTitle("�����귽����u�{�U��");
        setBounds(100, 100, 500, 237);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        final JLabel label = new JLabel();
        label.setText("�����귽�����}�G");
        label.setBounds(10, 88, 118, 18);
        getContentPane().add(label);

        tf_address = new JTextField();
        tf_address.setBounds(117, 86, 357, 22);
        getContentPane().add(tf_address);

        final JButton button = new JButton();
        button.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                String address = tf_address.getText().trim();// ��o���}
                download(address);  // �U���ɮ�
            }
        });
        button.setText("�����}�l�U��");
        button.setBounds(41, 144, 145, 28);
        getContentPane().add(button);

        final JButton button_1 = new JButton();
        button_1.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                tf_address.setText(null);// �M���¤�r�ؤ��e
                tf_address.requestFocus();// �¤�r����o�J�I
            }
        });
        button_1.setText("�M    ��");
        button_1.setBounds(204, 144, 106, 28);
        getContentPane().add(button_1);

        final JButton button_2 = new JButton();
        button_2.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                System.exit(0);
            }
        });
        button_2.setText("�h    �X");
        button_2.setBounds(328, 144, 106, 28);
        getContentPane().add(button_2);

        final JLabel label_1 = new JLabel();
        label_1.setForeground(new Color(0, 0, 255));
        label_1.setFont(new Font("", Font.BOLD, 24));
        label_1.setText("�����귽����u�{�U��");
        label_1.setBounds(117, 21, 301, 48);
        getContentPane().add(label_1);
    }
    public void download(String urlAddr){         // �q���w���}�U���ɮ�
        try {
            URL url = new URL(urlAddr);    // �إ�URL�ﹳ
            URLConnection urlConn = url.openConnection();  // ��o�s���ﹳ
            urlConn.connect();                           // �}�Ҩ�url�ѦҸ귽���q�T�챵
            InputStream in = urlConn.getInputStream() ;      // ��o��J�y�ﹳ
            String filePath = url.getFile();                  // ��o������|
            int pos = filePath.lastIndexOf("/");              // ��o���|���̫�@�ӱ׺b����m
            String fileName = filePath.substring(pos+1);      // �I���ɮצW
            FileOutputStream out = new FileOutputStream("C:/"+fileName);  // �إ߿�X�y�ﹳ
            byte[] bytes = new byte[1024];                 // �ŧi�s��U�����e���r�`�}�C
            int len = in.read(bytes);                       // �q��J�y��Ū�����e
            while (len != -1){
                out.write(bytes,0,len);                     // �NŪ�������e�g���X�y
                len = in.read(bytes);                      // �~��q��J�y��Ū�����e
            }
            out.close();          // ������X�y
            in.close();           // ������J�y
            JOptionPane.showMessageDialog(null, "�U������");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
