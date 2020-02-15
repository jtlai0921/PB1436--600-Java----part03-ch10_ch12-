package com.zzk;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ServerSocketFrame extends JFrame {
    private JTextField tf_send;
    private JTextArea ta_info;
    private ServerSocket server; // �ŧiServerSocket�ﹳ
    private Socket socket; // �ŧiSocket�ﹳsocket
    private Vector<Socket> vector = new Vector<Socket>();// �Ω��x�s�s����A�Ⱦ����Ȥ�ݮM���r�ﹳ
    private int counts = 0;// �Ω�O���s�����Ȥ�H��
    
    public void getServer() {
        try {
            server = new ServerSocket(1978); // ��Ҥ�Socket�ﹳ
            ta_info.append("�A�Ⱦ��M���r�w�g�إߦ��\\n"); // ��X��T
            while (true) { // �p�G�M���r�O�s�����A
                socket = server.accept(); // ��Ҥ�Socket�ﹳ
                counts++;
                ta_info.append("��" + counts + "�ӫȤ�s�����\\n"); // ��X��T
                PrintWriter out = new PrintWriter(socket.getOutputStream(),
                        true);
                out.println(String.valueOf(counts - 1));// �V�Ȥ�ݵo�e�M���r����
                vector.add(socket);// �x�s�Ȥ�ݮM���r�ﹳ
                new ServerThread(socket).start();// �إߨñҰʽu�{��
            }
        } catch (Exception e) {
            e.printStackTrace(); // ��X�ҥ~��T
        }
    }
    
    class ServerThread extends Thread {
        Socket socket = null; // �إ�Socket�ﹳ
        BufferedReader reader; // �ŧiBufferedReader�ﹳ
        
        public ServerThread(Socket socket) { // �غc��k
            this.socket = socket;
        }
        
        public void run() {
            try {
                if (socket != null) {
                    reader = new BufferedReader(new InputStreamReader(socket
                            .getInputStream())); // ��Ҥ�BufferedReader�ﹳ
                    int index = -1;// �x�s�h�X���Ȥ�ݯ��ޭ�
                    try {
                        while (true) { // �p�G�M���r�O�s�����A
                            String line = reader.readLine();// Ū���Ȥ�ݸ�T
                            try {
                                index = Integer.parseInt(line);// ��o�h�X���Ȥ�ݯ��ޭ�
                            } catch (Exception ex) {
                                index = -1;
                            }
                            if (line != null) {
                                ta_info.append("������Ȥ���o�e����T�G" + line + "\n"); // ��o�Ȥ�ݸ�T
                            }
                        }
                    } catch (Exception e) {
                        if (index != -1) {
                            vector.set(index, null);// �N�h�X���Ȥ�ݮM���r�]�w��null
                            ta_info.append("��" + (index + 1) + "�ӫȤ�ݤw�g�h�X�C\n"); // ��X�ҥ~��T
                        }
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
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
    }
    
    private void writeInfo(PrintWriter writer, String text) {
        writer.println(text);
    }
    
    public static void main(String[] args) { // �D��k
        ServerSocketFrame frame = new ServerSocketFrame(); // �إߥ����O�ﹳ
        frame.setVisible(true);// ��ܵ���
        frame.getServer(); // �I�s��k
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
        getContentPane().add(panel, BorderLayout.SOUTH);
        
        final JLabel label = new JLabel();
        label.setText("�A�Ⱦ��o�e����T�G");
        panel.add(label);
        
        tf_send = new JTextField();
        tf_send.setPreferredSize(new Dimension(150, 25));
        panel.add(tf_send);
        
        final JButton button = new JButton();
        button.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                for (int i = 0; i < vector.size(); i++) {
                    Socket socket = vector.get(i);// ��o�s�����\���M���r�ﹳ
                    PrintWriter writer;
                    try {
                        if (socket != null && !socket.isClosed()) {
                            writer = new PrintWriter(socket.getOutputStream(),
                                    true);// �إ߿�X�y�ﹳ
                            writeInfo(writer, tf_send.getText()); // �N�¤�r�ؤ���T�g�J�y
                        }
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
                ta_info.append("�A�Ⱦ��o�e����T�O�G" + tf_send.getText() + "\n"); // �N�¤�r�ؤ���T��ܦb�¤�r�줤
                tf_send.setText(""); // �N�¤�r�زM��
            }
        });
        button.setText("�o  �e");
        panel.add(button);
        
        final JPanel panel_1 = new JPanel();
        getContentPane().add(panel_1, BorderLayout.NORTH);
        
        final JLabel label_1 = new JLabel();
        label_1.setForeground(new Color(0, 0, 255));
        label_1.setFont(new Font("", Font.BOLD, 22));
        label_1.setText("�@��h�q�T�X�X�A�Ⱦ��ݵ{��");
        panel_1.add(label_1);
    }
}
