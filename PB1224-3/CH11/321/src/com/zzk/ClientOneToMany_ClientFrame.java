package com.zzk;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ClientOneToMany_ClientFrame extends JFrame {
    private JTextArea ta_info;
    private JTextField tf_send;
    PrintWriter out;// �ŧi��X�y�ﹳ
    
    /**
     * Launch the application
     * 
     * @param args
     */
    public static void main(String args[]) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    ClientOneToMany_ClientFrame frame = new ClientOneToMany_ClientFrame();
                    frame.setVisible(true);
                    frame.createClientSocket();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    
    public void createClientSocket() {
        try {
            Socket socket = new Socket("localhost", 1978);// �إ߮M���r�ﹳ
            out = new PrintWriter(socket.getOutputStream(), true);// �إ߿�X�y�ﹳ
            new ClientThread(socket).start();// �إߨñҰʽu�{�ﹳ
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    class ClientThread extends Thread {
        Socket socket;
        
        public ClientThread(Socket socket) {
            this.socket = socket;
        }
        
        public void run() {
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        socket.getInputStream()));// �إ߿�J�y�ﹳ
                while (true) {
                    String info = in.readLine();// Ū����T
                    ta_info.append(info + "\n");// �b�¤�r�줤��ܸ�T
                    if (info.equals("88")) {
                        break;// �����u�{
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private void send() {
        String info = tf_send.getText();// ��o��J����T
        if (info.equals("")) {
            return;// �p�G�S��J��T�h�Ǧ^�A�Y���o�e
        }
        if (info.equals("88")) {
            System.exit(0);// �p�G�S��J��T�O88�A�h�h�X
        }
        out.println(info);// �o�e��T
        out.flush();// ��s��X�w�İ�
        tf_send.setText(null);// �M�ů¤�r��
    }
    /**
     * Create the frame
     */
    public ClientOneToMany_ClientFrame() {
        super();
        setTitle("�Ȥ�ݤ@��h�q�T�X�X�Ȥ�ݵ{��");
        setBounds(100, 100, 385, 266);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        final JPanel panel = new JPanel();
        getContentPane().add(panel, BorderLayout.SOUTH);
        
        final JLabel label = new JLabel();
        label.setText("��J��Ѥ��e�G");
        panel.add(label);
        
        tf_send = new JTextField();
        tf_send.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                send();// �I�s��k�o�e��T
            }
        });
        tf_send.setPreferredSize(new Dimension(180, 25));
        panel.add(tf_send);
        
        final JButton button = new JButton();
        button.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                send();// �I�s��k�o�e��T
            }
        });
        button.setText("�o  �e");
        panel.add(button);
        
        final JScrollPane scrollPane = new JScrollPane();
        getContentPane().add(scrollPane, BorderLayout.CENTER);
        
        ta_info = new JTextArea();
        scrollPane.setViewportView(ta_info);
        //
    }
    
}
