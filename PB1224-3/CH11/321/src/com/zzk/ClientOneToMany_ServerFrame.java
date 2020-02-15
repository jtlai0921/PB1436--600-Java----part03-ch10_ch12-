package com.zzk;

import java.awt.BorderLayout;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class ClientOneToMany_ServerFrame extends JFrame {
    private JTextArea ta_info;
    private ServerSocket server; // �ŧiServerSocket�ﹳ
    private Socket socket; // �ŧiSocket�ﹳsocket
    private Vector<Socket> vector = new Vector<Socket>();// �Ω��x�s�s����A�Ⱦ����Ȥ�ݮM���r�ﹳ
    
    public void createSocket() {
        try {
            server = new ServerSocket(1978);
            while (true) {
                ta_info.append("���ݷs�Ȥ�s��......\n");
                socket = server.accept();// �إ߮M���r�ﹳ
                vector.add(socket);// �N�M���r�ﹳ�W�[��V�q�ﹳ��
                ta_info.append("�Ȥ�ݳs�����\�C" + socket + "\n");
                new ServerThread(socket).start();// �إߨñҰʽu�{�ﹳ
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    class ServerThread extends Thread {
        Socket socket;
        public ServerThread(Socket socket) {
            this.socket = socket;
        }
        public void run() {
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        socket.getInputStream()));// �إ߿�J�y�ﹳ
                while (true) {
                    String info = in.readLine();// Ū����T
                    for (Socket s : vector) {// �ˬd�Ҧ��Ȥ�ݮM���r�ﹳ
                        if (s != socket) {// �p�G���O�o�e��T���M���r�ﹳ
                            PrintWriter out = new PrintWriter(s
                                    .getOutputStream(), true);// �إ߿�X�y�ﹳ
                            out.println(info);// �o�e��T
                            out.flush();// ��s��X�w�İ�
                        }
                    }
                }
            } catch (IOException e) {
                ta_info.append(socket + "�w�g�h�X�C\n");
                vector.remove(socket);// �����h�X���Ȥ�ݮM���r
            }
        }
    }
    
    /**
     * Launch the application
     * 
     * @param args
     */
    public static void main(String args[]) {
        ClientOneToMany_ServerFrame frame = new ClientOneToMany_ServerFrame();
        frame.setVisible(true);
        frame.createSocket();
    }
    
    /**
     * Create the frame
     */
    public ClientOneToMany_ServerFrame() {
        super();
        setTitle("�Ȥ�ݤ@��h�q�T�X�X�A�Ⱦ��ݵ{��");
        setBounds(100, 100, 385, 266);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        final JScrollPane scrollPane = new JScrollPane();
        getContentPane().add(scrollPane, BorderLayout.CENTER);
        
        ta_info = new JTextArea();
        scrollPane.setViewportView(ta_info);
    }
}
