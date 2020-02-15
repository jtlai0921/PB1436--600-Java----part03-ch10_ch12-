package com.zzk;

import java.awt.BorderLayout;
import java.net.*;
import javax.swing.*;
import javax.swing.JScrollPane;

public class ClientSocketFrame extends JFrame { // �إ����O�~��JFrame���O
    private Socket socket; // �ŧiSocket�ﹳ
    private JTextArea ta = new JTextArea(); // �إ�JtextArea�ﹳ
    
    public ClientSocketFrame() { // �غc��k
        super(); // �I�s�����O���غc��k
        setTitle("��oSocket��T");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 351, 257);
        final JScrollPane scrollPane = new JScrollPane();
        getContentPane().add(scrollPane, BorderLayout.CENTER);
        scrollPane.setViewportView(ta);
    }
    
    private void connect() { // �s���M���r��k
        ta.append("���ճs��......\n"); // �¤�r�줤��T��T
        try { // �����ҥ~
            socket = new Socket("localhost", 1978); // ��Ҥ�Socket�ﹳ
            ta.append("�����s���C\n"); // �¤�r�줤���ܸ�T
            InetAddress netAddress = socket.getInetAddress();// ��o���ݪA�Ⱦ����a�}
            String netIp = netAddress.getHostAddress();// ��o���ݪA�Ⱦ���IP�a�} 
            int netPort = socket.getPort();// ��o���ݪA�Ⱦ����q�T��
            InetAddress localAddress = socket.getLocalAddress();// ��o�Ȥ�ݪ��a�}
            String localIp = localAddress.getHostAddress();// ��o�Ȥ�ݪ�IP�a�}
            int localPort = socket.getLocalPort();// ��o�Ȥ�ݪ��q�T��
            ta.append("���ݪA�Ⱦ���IP�a�}�G" + netIp + "\n");
            ta.append("���ݪA�Ⱦ����q�T�𸹡G" + netPort + "\n");
            ta.append("�Ȥ��������IP�a�}�G" + localIp + "\n");
            ta.append("�Ȥ���������q�T�𸹡G" + localPort + "\n");
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