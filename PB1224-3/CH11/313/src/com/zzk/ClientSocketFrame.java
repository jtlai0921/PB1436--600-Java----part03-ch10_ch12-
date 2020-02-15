package com.zzk;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import javax.swing.*;

public class ClientSocketFrame extends JFrame { // �إ����O�~��JFrame���O
    private JLabel label;
    private JPanel panel;
    private PrintWriter writer; // �ŧiPrintWriter���O�ﹳ
    private BufferedReader reader; // �ŧiBufferedReader�ﹳ
    private Socket socket; // �ŧiSocket�ﹳ
    private JTextArea ta_info = new JTextArea(); // �إ�JtextArea�ﹳ
    private JTextField tf_send = new JTextField(); // �إ�JtextField�ﹳ
    
    public ClientSocketFrame() { // �غc��k
        super(); // �I�s�����O���غc��k
        setTitle("�Ȥ�ݵ{��");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 361, 257);
        final JScrollPane scrollPane = new JScrollPane();
        getContentPane().add(scrollPane, BorderLayout.CENTER);
        scrollPane.setViewportView(ta_info);
        getContentPane().add(getPanel(), BorderLayout.NORTH);
    }
    
    private void connect() { // �s���M���r��k
        ta_info.append("���ճs��......\n"); // �¤�r�줤��T��T
        try { // �����ҥ~
            socket = new Socket("localhost", 1978); // ��Ҥ�Socket�ﹳ
            while (true) {
                writer = new PrintWriter(socket.getOutputStream(), true);
                reader = new BufferedReader(new InputStreamReader(socket
                        .getInputStream())); // ��Ҥ�BufferedReader�ﹳ
                ta_info.append("�����s���C\n"); // �¤�r�줤���ܸ�T
                getClientInfo();
            }
        } catch (Exception e) {
            e.printStackTrace(); // ��X�ҥ~��T
        }
    }
    
    public static void main(String[] args) { // �D��k
        ClientSocketFrame clien = new ClientSocketFrame(); // �إߥ��ҹﹳ
        clien.setVisible(true); // �N�������
        clien.connect(); // �I�s�s����k
    }
    
    private void getClientInfo() {
        try {
            while (true) { // �p�G�M���r�O�s�����A
                if (reader != null) {
                    String line = reader.readLine();
                    if (line != null)
                        ta_info.append("������A�Ⱦ��o�e����T�G" + line + "\n"); // ��o�Ȥ�ݸ�T
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    
                    reader.close();// �����y
                    
                }
                if (socket != null) {
                    socket.close(); // �����M���r
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * @return
     */
    protected JPanel getPanel() {
        if (panel == null) {
            panel = new JPanel();
            panel.add(getLabel());
            tf_send.setPreferredSize(new Dimension(200, 25));
            panel.add(tf_send);
            tf_send.addActionListener(new ActionListener() { // �j�w�ƥ�
                        public void actionPerformed(ActionEvent e) {
                            writer.println(tf_send.getText()); // �N�¤�r�ؤ���T�g�J�y
                            ta_info.append("�Ȥ�ݵo�e����T�O�G" + tf_send.getText()
                                    + "\n"); // �N�¤�r�ؤ���T��ܦb�¤�r�줤
                            tf_send.setText(""); // �N�¤�r�زM��
                        }
                    });
        }
        return panel;
    }
    
    /**
     * @return
     */
    protected JLabel getLabel() {
        if (label == null) {
            label = new JLabel();
            label.setText("�Ȥ�ݵo�e����T�G");
        }
        return label;
    }
}
