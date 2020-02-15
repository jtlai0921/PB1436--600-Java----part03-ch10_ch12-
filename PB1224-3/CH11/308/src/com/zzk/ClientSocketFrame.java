package com.zzk;

import java.awt.BorderLayout;
import java.net.*;
import javax.swing.*;
import javax.swing.JScrollPane;

public class ClientSocketFrame extends JFrame { // �إ����O�~��JFrame���O
    private Socket socket; // �ŧiSocket�ﹳ
    private JTextArea ta_info = new JTextArea(); // �إ�JtextArea�ﹳ
    
    public ClientSocketFrame() { // �غc��k
        super(); // �I�s�����O���غc��k
        setTitle("�إ߫Ȥ�ݮM���r");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 254, 166);
        final JScrollPane scrollPane = new JScrollPane();
        getContentPane().add(scrollPane, BorderLayout.CENTER);
        scrollPane.setViewportView(ta_info);
    }
    
    private void connect() { // �s���M���r��k
        ta_info.append("���ճs��......\n"); // �¤�r�줤��T��T
        try { // �����ҥ~
            socket = new Socket("127.0.0.1", 1978); // ��Ҥ�Socket�ﹳ
            ta_info.append("�����s���C\n"); // �¤�r�줤���ܸ�T
        } catch (Exception e) {
            e.printStackTrace(); // ��X�ҥ~��T
        }
    }
    
    public static void main(String[] args) { // �D��k
        ClientSocketFrame clien = new ClientSocketFrame(); // �إߥ��ҹﹳ
        clien.setVisible(true); // �N�������
        clien.connect(); // �I�s�s����k
    }
}
