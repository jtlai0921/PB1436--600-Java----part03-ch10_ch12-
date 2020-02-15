package com.zzk;

import java.awt.BorderLayout;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class ClientOneToOne_ServerFrame extends JFrame {
    private JTextArea ta_info;
    private ServerSocket server; // �ŧiServerSocket�ﹳ
    private Socket socket; // �ŧiSocket�ﹳsocket
    private Hashtable<String, Socket> map = new Hashtable<String, Socket>();// �Ω��x�s�s����A�Ⱦ����ϥΪ̩M�Ȥ�ݮM���r�ﹳ
    
    public void createSocket() {
        try {
            server = new ServerSocket(1978);
            while (true) {
                ta_info.append("���ݷs�Ȥ�s��......\n");
                socket = server.accept();// �إ߮M���r�ﹳ
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
                    String key = "";
                    if (info.startsWith("�ϥΪ̡G")) {// �W�[�n�J�ϥΪ̨�Ȥ�ݦC��
                        key = info.substring(3, info.length());// ��o�ϥΪ̦W�٨ç@����ϥ�
                        map.put(key, socket);// �W�[��ȹ�
                        Set<String> set = map.keySet();// ��o���X���Ҧ��䪺Set�˵�
                        Iterator<String> keyIt = set.iterator();// ��o�Ҧ��䪺���N��
                        while (keyIt.hasNext()) {
                            String receiveKey = keyIt.next();// ��o��ܱ�����T����
                            Socket s = map.get(receiveKey);// ��o�P����������M���r�ﹳ
                            PrintWriter out = new PrintWriter(s
                                    .getOutputStream(), true);// �إ߿�X�y�ﹳ
                            Iterator<String> keyIt1 = set.iterator();// ��o�Ҧ��䪺���N��
                            while (keyIt1.hasNext()) {
                                String receiveKey1 = keyIt1.next();// ��o��A�Ω�V�Ȥ�ݼW�[�ϥΪ̦C��
                                out.println(receiveKey1);// �o�e��T
                                out.flush();// ��s��X�w�İ�
                            }
                        }
                        
                    } else {// ��o�������T��
                        key = info.substring(info.indexOf("�G�o�e���G") + 5, info
                                .indexOf("�G����T�O�G"));// ��o�����誺key��,�Y�����誺�ϥΪ̦W��
                        String sendUser = info.substring(0, info
                                .indexOf("�G�o�e���G"));// ��o�o�e�誺key��,�Y�o�e�誺�ϥΪ̦W��
                        Set<String> set = map.keySet();// ��o���X���Ҧ��䪺Set�˵�
                        Iterator<String> keyIt = set.iterator();// ��o�Ҧ��䪺���N��
                        while (keyIt.hasNext()) {
                            String receiveKey = keyIt.next();// ��o��ܱ�����T����
                            if (key.equals(receiveKey)
                                    && !sendUser.equals(receiveKey)) {// �p�G�O�o�e��A�����O�ϥΪ̥���
                                Socket s = map.get(receiveKey);// ��o�P����������M���r�ﹳ
                                PrintWriter out = new PrintWriter(s
                                        .getOutputStream(), true);// �إ߿�X�y�ﹳ
                                
                                out.println("MSG:"+info);// �o�e��T
                                out.flush();// ��s��X�w�İ�
                            }
                        }
                    }
                }
            } catch (IOException e) {
                ta_info.append(socket + "�w�g�h�X�C\n");
            }
        }
    }
    
    /**
     * Launch the application
     * 
     * @param args
     */
    public static void main(String args[]) {
        ClientOneToOne_ServerFrame frame = new ClientOneToOne_ServerFrame();
        frame.setVisible(true);
        frame.createSocket();
    }
    
    /**
     * Create the frame
     */
    public ClientOneToOne_ServerFrame() {
        super();
        setTitle("�Ȥ�ݤ@��@�q�T�X�X�A�Ⱦ��ݵ{��");
        setBounds(100, 100, 385, 266);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        final JScrollPane scrollPane = new JScrollPane();
        getContentPane().add(scrollPane, BorderLayout.CENTER);
        
        ta_info = new JTextArea();
        scrollPane.setViewportView(ta_info);
    }
}
