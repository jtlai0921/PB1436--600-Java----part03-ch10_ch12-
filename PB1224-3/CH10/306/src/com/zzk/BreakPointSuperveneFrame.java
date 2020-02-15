package com.zzk;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class BreakPointSuperveneFrame extends JFrame {
    private JTextField tf_totalLength;
    private JTextField tf_residuaryLength;
    private JTextField tf_readToPos;
    private JTextField tf_address;
    private JTextField tf_endPos;
    private JTextField tf_startPos;
    private String urlAddress = "";// �Ω��x�s�����귽���a�}
    private long totalLength = 0;// �x�s�����귽���j�p�A�H�r�`�����
    private long readToPos = 0;// �x�s�W��Ū���쪺��m
    private long residuaryLength = 0;// �x�s��Ū���e���j�p
    
    /**
     * Launch the application
     * 
     * @param args
     */
    public static void main(String args[]) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    BreakPointSuperveneFrame frame = new BreakPointSuperveneFrame();
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
    public BreakPointSuperveneFrame() {
        super();
        getContentPane().setLayout(null);
        setTitle("�U�������귽���_�I���");
        setBounds(100, 100, 514, 238);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        tf_startPos = new JTextField();
        tf_startPos.setBounds(80, 165, 113, 22);
        getContentPane().add(tf_startPos);
        
        final JLabel label = new JLabel();
        label.setText("�_�l��m�G");
        label.setBounds(10, 167, 74, 18);
        getContentPane().add(label);
        
        final JLabel label_1 = new JLabel();
        label_1.setText("������m�G");
        label_1.setBounds(199, 167, 74, 18);
        getContentPane().add(label_1);
        
        tf_endPos = new JTextField();
        tf_endPos.setBounds(267, 165, 117, 22);
        getContentPane().add(tf_endPos);
        
        final JLabel label_2 = new JLabel();
        label_2.setText("�����귽���a�}�G");
        label_2.setBounds(10, 52, 113, 18);
        getContentPane().add(label_2);
        
        tf_address = new JTextField();
        tf_address.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                try {
                    urlAddress = tf_address.getText().trim();
                    URL url = new URL(urlAddress);// ��o�����귽��URL
                    HttpURLConnection connection = (HttpURLConnection) url
                            .openConnection();// ��o�s���ﹳ
                    connection.connect();// �s�������귽
                    totalLength = connection.getContentLength();// ��o�����귽������
                    connection.disconnect();// �_�}�s��
                    tf_totalLength.setText(String.valueOf(totalLength));// ����`����
                    tf_readToPos.setText("0");// ��ܤW��Ū���쪺��m
                    residuaryLength = totalLength;// ��Ū���e���ɮ��`����
                    tf_residuaryLength.setText(String.valueOf(residuaryLength));// ��ܥ�Ū���e
                } catch (MalformedURLException e1) {
                    e1.printStackTrace();
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
                
            }
        });
        tf_address.setBounds(119, 50, 365, 22);
        getContentPane().add(tf_address);
        
        final JLabel label_3 = new JLabel();
        label_3.setForeground(new Color(0, 0, 255));
        label_3.setFont(new Font("", Font.BOLD, 14));
        label_3.setText("��J�����귽���a�}�ýT�{�A�i�H��o�����귽���j�p�C");
        label_3.setBounds(10, 10, 384, 22);
        getContentPane().add(label_3);
        
        final JLabel label_4 = new JLabel();
        label_4.setForeground(new Color(128, 0, 0));
        label_4.setText("�����귽���j�p��");
        label_4.setBounds(10, 76, 113, 38);
        getContentPane().add(label_4);
        
        final JLabel label_5 = new JLabel();
        label_5.setText("�W��Ū����");
        label_5.setBounds(10, 123, 74, 18);
        getContentPane().add(label_5);
        
        tf_readToPos = new JTextField();
        tf_readToPos.setBounds(80, 121, 113, 22);
        tf_readToPos.setEnabled(false);
        getContentPane().add(tf_readToPos);
        
        final JLabel label_6 = new JLabel();
        label_6.setText("�r�`�B�A�ٳ�");
        label_6.setBounds(202, 123, 87, 18);
        getContentPane().add(label_6);
        
        tf_residuaryLength = new JTextField();
        tf_residuaryLength.setBounds(285, 120, 117, 22);
        tf_residuaryLength.setEnabled(false);
        getContentPane().add(tf_residuaryLength);
        
        final JLabel label_7 = new JLabel();
        label_7.setText("�r�`��Ū�C");
        label_7.setBounds(404, 123, 80, 18);
        getContentPane().add(label_7);
        
        final JLabel label_4_1 = new JLabel();
        label_4_1.setForeground(new Color(128, 0, 0));
        label_4_1.setText("�Ӧr�`�C");
        label_4_1.setBounds(404, 76, 80, 38);
        getContentPane().add(label_4_1);
        
        tf_totalLength = new JTextField();
        tf_totalLength.setBounds(119, 84, 283, 22);
        tf_totalLength.setEnabled(false);
        getContentPane().add(tf_totalLength);
        
        final JButton button = new JButton();
        button.setBounds(395, 162, 89, 28);
        getContentPane().add(button);
        button.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                if (totalLength == 0) {
                    JOptionPane.showMessageDialog(null,
                            "�S�������귽�C\n\n�п�J���T�����}�A�M��T�{�C");
                    return;
                }
                long startPos = 0;// �_�l��m
                long endPos = 0;// ������m
                try {
                    startPos = Long.parseLong(tf_startPos.getText().trim());// �_�l��m
                    endPos = Long.parseLong(tf_endPos.getText().trim());// ������m
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "��J���_�l��m�ε�����m�����T�C");
                    return;
                }
                readToPos = endPos;// �O��Ū���쪺��m
                residuaryLength = totalLength - readToPos;// �O����Ū���e���j�p
                tf_readToPos.setText(String.valueOf(readToPos));// ���Ū���쪺��m
                tf_residuaryLength.setText(String.valueOf(residuaryLength));// ��ܥ�Ū�r�`��
                tf_startPos.setText(String.valueOf(readToPos));// �]�w�U�@��Ū���I���}�l��m
                tf_endPos.setText(String.valueOf(totalLength));// �]�w�U�@��Ū���I��������m
                tf_endPos.requestFocus();// �ϵ�����m�¤�r����o�J�I
                tf_endPos.selectAll();// ��ܵ�����m�¤�r�ؤ����������e�A��K��J������m��
                download(startPos, endPos);// �I�s��k�i��U��
            }
        });
        button.setText("�}�l�U��");
    }
    
    public void download(long startPosition, long endPosition) {
        try {
            URL url = new URL(urlAddress);// ��o�����귽��URL
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();// ��o�s���ﹳ
            connection.setRequestProperty("User-Agent", "NetFox");// �]�w�ШD�ݩ�
            String rangeProperty = "bytes=" + startPosition + "-";// �w�q�ШD�d���ݩ�
            if (endPosition > 0) {
                rangeProperty += endPosition;// �վ�ШD�d���ݩ�
            }
            connection.setRequestProperty("RANGE", rangeProperty);// �]�w�ШD�d���ݩ�
            connection.connect();// �s�������귽
            InputStream in = connection.getInputStream();// ��o��J�y�ﹳ
            String file = url.getFile();// ��o�ɮ׹ﹳ
            String name = file.substring(file.lastIndexOf('/') + 1);// ��o�ɮצW
            FileOutputStream out = new FileOutputStream("c:/" + name, true);// �إ߿�X�y�ﹳ,�x�s�U�����귽
            byte[] buff = new byte[2048];// �إߦr�`�}�C
            int len = 0;// �w�q�x�sŪ�����e���ת��ܼ�
            len = in.read(buff);// Ū�����e
            while (len != -1) {
                out.write(buff, 0, len);// �g�J�Ϻ�
                len = in.read(buff);// Ū�����e
            }
            out.close();// �����y
            in.close();// �����y
            connection.disconnect();// �_�}�s��
            if (readToPos > 0 && readToPos == totalLength) {
                JOptionPane.showMessageDialog(null, "���������귽���U���C\n�����u�T�w�v���s�h�X�{���C");
                System.exit(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
