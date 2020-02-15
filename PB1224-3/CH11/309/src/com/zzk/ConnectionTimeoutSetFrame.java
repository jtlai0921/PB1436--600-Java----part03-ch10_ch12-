package com.zzk;

import java.awt.BorderLayout;
import java.io.*;
import java.net.*;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

@SuppressWarnings("serial")
public class ConnectionTimeoutSetFrame extends JFrame {
    private JTextArea ta_info;
    private ServerSocket server; // �ŧiServerSocket�ﹳ
    public void getserver() {
        try {
            server = new ServerSocket(1978); // ��Ҥ�Socket�ﹳ
            server.setSoTimeout(100);// �]�w�s���O�ɮɶ���10��
            ta_info.append("�A�Ⱦ��M���r�w�g�إߦ��\\n"); // ��X��T
            while (true) { // �p�G�M���r�O�s�����A
                ta_info.append("���ݫȤ�����s��......\n"); // ��X��T
                server.accept();// ���ݫȤ���s��
            }
        } catch (SocketTimeoutException e) {
            ta_info.append("�s���O��......");
            JOptionPane.showMessageDialog(null, "�s���O��......");
        } catch (IOException e) {
            e.printStackTrace();
        } 
    }
    public static void main(String[] args) { // �D��k
    	ConnectionTimeoutSetFrame frame = new ConnectionTimeoutSetFrame(); // �إߥ����O�ﹳ
        frame.setVisible(true);
        frame.getserver(); // �I�s��k
    }
    public ConnectionTimeoutSetFrame() {
        super();
        setTitle("�]�w���ݳs�����O�ɮɶ�");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 336, 257);
        
        final JScrollPane scrollPane = new JScrollPane();
        getContentPane().add(scrollPane, BorderLayout.CENTER);
        
        ta_info = new JTextArea();
        scrollPane.setViewportView(ta_info);
    }
}
