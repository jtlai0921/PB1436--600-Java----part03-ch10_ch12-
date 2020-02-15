package com.zzk;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ClientSocketFrame extends JFrame {
    private PrintWriter writer; // �ŧiPrintWriter���O�ﹳ
    private BufferedReader reader; // �ŧiBufferedReader�ﹳ
    private Socket socket; // �ŧiSocket�ﹳ
    private JTextArea ta_info; // �إ�JtextArea�ﹳ
    private JTextField tf_send; // �إ�JtextField�ﹳ
    private int index = -1;
    private void connect() { // �s���M���r��k
        ta_info.append("���ճs��......\n"); // �¤�r�줤��T��T
        try { // �����ҥ~
            socket = new Socket("localhost", 1978); // ��Ҥ�Socket�ﹳ
            while (true) {
                writer = new PrintWriter(socket.getOutputStream(), true);// �إ߿�X�y�ﹳ
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream())); // ��Ҥ�BufferedReader�ﹳ
                index = Integer.parseInt(reader.readLine());// ��o�Ȥ�n�J�A�Ⱦ������ޭ�
                ta_info.append("�A�O��"+(index+1)+"�ӧ����s�����ϥΪ̡C\n"); // �¤�r�줤���ܸ�T
                getServerInfo();
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
    
    private void getServerInfo() {
        try {
            while (true) { // �p�G�M���r�O�s�����A
                if (reader != null) {
                    String line = reader.readLine();// Ū���A�Ⱦ��o�e����T
                    if (line != null){
                        ta_info.append("������A�Ⱦ��o�e����T�G" + line + "\n"); // ��o�Ȥ�ݸ�T
                    }
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
     * Create the frame
     */
    public ClientSocketFrame() {
        super();
        addWindowListener(new WindowAdapter() {
            public void windowClosing(final WindowEvent e) {
                writer.println(String.valueOf(index));// �V�A�Ⱦ��ݵo�e�h�X�Ȥ᪺���ޭ�
            }
        });
        setTitle("�Ȥ�ݵ{��");
        setBounds(100, 100, 361, 257);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        final JPanel panel = new JPanel();
        getContentPane().add(panel, BorderLayout.NORTH);

        final JLabel label = new JLabel();
        label.setForeground(new Color(0, 0, 255));
        label.setFont(new Font("", Font.BOLD, 22));
        label.setText("�@��h�q�T�X�X�Ȥ�ݵ{��");
        panel.add(label);

        final JPanel panel_1 = new JPanel();
        getContentPane().add(panel_1, BorderLayout.SOUTH);

        final JLabel label_1 = new JLabel();
        label_1.setText("�Ȥ�ݵo�e����T�G");
        panel_1.add(label_1);

        tf_send = new JTextField();
        tf_send.setPreferredSize(new Dimension(140, 25));
        panel_1.add(tf_send);

        final JButton button = new JButton();
        button.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                writer.println(tf_send.getText()); // �N�¤�r�ؤ���T�g�J�y
                ta_info.append("�Ȥ�ݵo�e����T�O�G" + tf_send.getText()
                        + "\n"); // �N�¤�r�ؤ���T��ܦb�¤�r�줤
                tf_send.setText(""); // �N�¤�r�زM��
            }
        });
        button.setText("�o  �e");
        panel_1.add(button);

        final JScrollPane scrollPane = new JScrollPane();
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        ta_info = new JTextArea();
        scrollPane.setViewportView(ta_info);
        //
    }
    
}
