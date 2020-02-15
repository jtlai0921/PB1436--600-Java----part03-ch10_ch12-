package com.zzk;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ServerSocketFrame extends JFrame {
    private JTextField tf_send;
    private JTextArea ta_info;
    private PrintWriter writer; // �ŧiPrintWriter���O�ﹳ
    private BufferedReader reader; // �ŧiBufferedReader�ﹳ
    private ServerSocket server; // �ŧiServerSocket�ﹳ
    private Socket socket; // �ŧiSocket�ﹳsocket
    
    public void getserver() {
        try {
            server = new ServerSocket(1978); // ��Ҥ�Socket�ﹳ
            ta_info.append("�A�Ⱦ��M���r�w�g�إߦ��\\n"); // ��X��T
            while (true) { // �p�G�M���r�O�s�����A
                ta_info.append("���ݫȤ�����s��......\n"); // ��X��T
                socket = server.accept(); // ��Ҥ�Socket�ﹳ
                reader = new BufferedReader(new InputStreamReader(socket
                        .getInputStream())); // ��Ҥ�BufferedReader�ﹳ
                writer = new PrintWriter(socket.getOutputStream(), true);
                getClientInfo(); // �I�sgetClientInfo()��k
            }
        } catch (Exception e) {
            e.printStackTrace(); // ��X�ҥ~��T
        }
    }
    
    private void getClientInfo() {
        try {
            while (true) { // �p�G�M���r�O�s�����A
                String line = reader.readLine();
                if (line != null)
                    ta_info.append("������Ȥ���o�e����T�G" + line + "\n"); // ��o�Ȥ�ݸ�T
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
        frame.getserver(); // �I�s��k
    }
    
    public ServerSocketFrame() {
        super();
        setTitle("�A�Ⱦ��ݵ{��");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 379, 260);
        
        final JScrollPane scrollPane = new JScrollPane();
        getContentPane().add(scrollPane, BorderLayout.CENTER);
        
        ta_info = new JTextArea();
        scrollPane.setViewportView(ta_info);
        
        final JPanel panel = new JPanel();
        getContentPane().add(panel, BorderLayout.NORTH);
        
        final JLabel label = new JLabel();
        label.setText("�A�Ⱦ��o�e����T�G");
        panel.add(label);
        
        tf_send = new JTextField();
        tf_send.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                writer.println(tf_send.getText()); // �N�¤�r�ؤ���T�g�J�y
                ta_info.append("�A�Ⱦ��o�e����T�O�G" + tf_send.getText() + "\n"); // �N�¤�r�ؤ���T��ܦb�¤�r�줤
                tf_send.setText(""); // �N�¤�r�زM��
            }
        });
        tf_send.setPreferredSize(new Dimension(220, 25));
        panel.add(tf_send);
    }
}
