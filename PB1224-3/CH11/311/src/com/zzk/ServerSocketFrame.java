package com.zzk;

import java.awt.BorderLayout;
import java.io.*;
import java.net.*;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class ServerSocketFrame extends JFrame {
    private JTextArea ta_info;// �ŧi�¤�r��,�Ω���ܳs����T�M�����쪺��T
    private BufferedReader reader; // �ŧiBufferedReader�ﹳ
    private ServerSocket server; // �ŧiServerSocket�ﹳ
    private Socket socket; // �ŧiSocket�ﹳsocket
    
    public void getServer() {
        try {
            server = new ServerSocket(1978); // ��Ҥ�Socket�ﹳ
            ta_info.append("�A�Ⱦ��M���r�w�g�إߦ��\\n"); // ��X��T
            while (true) { // �p�G�M���r�O�s�����A
                ta_info.append("���ݫȤ�����s��......\n"); // ��X��T
                socket = server.accept(); // ��Ҥ�Socket�ﹳ
                ta_info.append("�s�����\�C\n"); // ��X��T
                reader = new BufferedReader(new InputStreamReader(socket
                        .getInputStream())); // ��Ҥ�BufferedReader�ﹳ
                getClientInfo(); // �I�sgetClientInfo()��k
            }
        } catch (Exception e) {
            e.printStackTrace(); // ��X�ҥ~��T
        }
    }
    
    private void getClientInfo() {
        try {
            while (true) { // �p�G�M���r�O�s�����A
                ta_info.append("������Ȥ���o�e����T�G" + reader.readLine() + "\n"); // ��o�Ȥ�ݸ�T
            }
        } catch (Exception e) {
            ta_info.append("�Ȥ�ݤw�h�X�C\n"); // ��X�ҥ~��T
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
    
    public static void main(String[] args) { // �D��k
        ServerSocketFrame frame = new ServerSocketFrame(); // �إߥ����O�ﹳ
        frame.setVisible(true);
        frame.getServer(); // �I�s��k
    }
    
    public ServerSocketFrame() {
        super();
        setTitle("�A�Ⱦ��ݵ{��");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 336, 257);
        
        final JScrollPane scrollPane = new JScrollPane();
        getContentPane().add(scrollPane, BorderLayout.CENTER);
        
        ta_info = new JTextArea();
        scrollPane.setViewportView(ta_info);
    }
}
