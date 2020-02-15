package com.zzk;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ServerSocketFrame extends JFrame {
    
    private JTextField tf_name;
    private JTextField tf_id;
    private JTextArea ta_info;
    private ObjectOutputStream out = null; // �إ߬y�ﹳ
    private ObjectInputStream in = null; // �إ߬y�ﹳ
    private ServerSocket server; // �ŧiServerSocket�ﹳ
    private Socket socket; // �ŧiSocket�ﹳsocket
    
    public void getserver() {
        try {
            server = new ServerSocket(1978); // ��Ҥ�Socket�ﹳ
            ta_info.append("�A�Ⱦ��M���r�w�g�إߦ��\\n"); // ��X��T
            while (true) { // �p�G�M���r�O�s�����A
                ta_info.append("���ݫȤ�����s��......\n"); // ��X��T
                socket = server.accept(); // ��Ҥ�Socket�ﹳ
                ta_info.append("�Ȥ���s�����\\n"); // ��X��T
                out = new ObjectOutputStream(socket.getOutputStream());
                in = new ObjectInputStream(socket.getInputStream());
                getClientInfo(); // �I�sgetClientInfo()��k
            }
        } catch (Exception e) {
            e.printStackTrace(); // ��X�ҥ~��T
        }
    }
    
    private void getClientInfo() {
        try {
            while (true) { // �p�G�M���r�O�s�����A
                Student stud = (Student)in.readObject();
                if (stud!=null)
                ta_info.append("������Ȥ���o�e��  �s�����G" + stud.getId() + "  �W�٬��G" +stud.getName() + "\n"); // ��o�Ȥ�ݸ�T
            }
        } catch (Exception e) {
            ta_info.append("�Ȥ�ݤw�h�X�C\n"); // ��X�ҥ~��T
        } finally {
            try {
                if (in != null) {
                    in.close();// �����y
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
        label.setText("�s���G");
        panel.add(label);

        tf_id = new JTextField();
        tf_id.setPreferredSize(new Dimension(70,25));
        panel.add(tf_id);

        final JLabel label_1 = new JLabel();
        label_1.setText("�W�١G");
        panel.add(label_1);

        tf_name = new JTextField();
        tf_name.setPreferredSize(new Dimension(100,25));
        panel.add(tf_name);

        final JButton button = new JButton();
        button.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                Student stud = new Student();
                stud.setId(tf_id.getText());
                stud.setName(tf_name.getText());
                try {
                    out.writeObject(stud);
                } catch (IOException e1) {
                    e1.printStackTrace();
                } 
                ta_info.append("�A�Ⱦ��o�e��  �s���O�G" + tf_id.getText() + "  �W�٬O�G"+tf_name.getText()+"\n"); // �N�¤�r�ؤ���T��ܦb�¤�r�줤
                tf_id.setText(null); // �N�¤�r�زM��
                tf_name.setText(null);
            }
        });
        button.setText("�o  �e");
        panel.add(button);
    }
}
