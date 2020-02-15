package com.zzk;

import java.awt.BorderLayout;
import java.net.*;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class ServerSocketFrame extends JFrame {
    private JTextArea ta_info;
    private ServerSocket server; // �ŧiServerSocket�ﹳ
    private Socket socket; // �ŧiSocket�ﹳsocket
    
    public ServerSocketFrame() {
        super();
        setTitle("����Socket�w��");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 278, 185);
        final JScrollPane scrollPane = new JScrollPane();
        getContentPane().add(scrollPane, BorderLayout.CENTER);
        
        ta_info = new JTextArea();
        scrollPane.setViewportView(ta_info);
    }
    
    public void getserver() {
        try {
            server = new ServerSocket(1978); // ��Ҥ�Socket�ﹳ
            ta_info.append("�A�Ⱦ��M���r�w�g�إߦ��\\n"); // ��X��T
            while (true) { // �p�G�M���r�O�s�����A
                ta_info.append("���ݫȤ�����s��......\n"); // ��X��T
                ta_info.append("�p�G�s�����\�N�|����Socket�w��......\n"); // ��X��T
                socket = server.accept(); // ��Ҥ�Socket�ﹳ
                socket.setTcpNoDelay(true);// ����Socket�w�ġA���@��ƶǿ�t��
                ta_info.append("�w�g����Socket�w��......\n"); // ��X��T
            }
        } catch (Exception e) {
            e.printStackTrace(); // ��X�ҥ~��T
        }
    }
    
    public static void main(String[] args) { // �D��k
        ServerSocketFrame frame = new ServerSocketFrame(); // �إߥ����O�ﹳ
        frame.setVisible(true);
        frame.getserver(); // �I�s��k
    }

}
