package com.zzk;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import javax.swing.*;

public class ClientSocketFrame extends JFrame { // �إ����O�~��JFrame���O
    private JButton button;
    private JTextField tf_name;
    private JLabel label_1;
    private JLabel label;
    private JPanel panel;
    private ObjectInputStream in = null;// �إ߬y�ﹳ
    private ObjectOutputStream out = null;// �إ߬y�ﹳ
    private Socket socket;// �ŧiSocket�ﹳ
    private JTextArea ta_info = new JTextArea();// �إ�JtextArea�ﹳ
    private JTextField tf_id = new JTextField();// �إ�JtextField�ﹳ
    private Container cc;// �ŧiContainer�ﹳ
    
    public ClientSocketFrame() { // �غc��k
        super(); // �I�s�����O���غc��k
        setTitle("�Ȥ�ݵ{��");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 373, 257);
        cc = this.getContentPane(); // ��Ҥƹﹳ
        final JScrollPane scrollPane = new JScrollPane();
        getContentPane().add(scrollPane, BorderLayout.CENTER);
        scrollPane.setViewportView(ta_info);
        getContentPane().add(getPanel(), BorderLayout.NORTH);
    }
    
    private void connect() { // �s���M���r��k
        ta_info.append("���ճs��......\n"); // �¤�r�줤��T��T
        try { // �����ҥ~
            socket = new Socket("localhost", 1978); // ��Ҥ�Socket�ﹳ
            while (true){
                out = new ObjectOutputStream(socket.getOutputStream());
                in = new ObjectInputStream(socket.getInputStream());
            ta_info.append("�����s���C\n"); // �¤�r�줤���ܸ�T
                getClientInfo();
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
    private void getClientInfo() {
        try {
            while (true) { // �p�G�M���r�O�s�����A
                Student stud = (Student)in.readObject();
                if (stud!=null)
                ta_info.append("������A�Ⱦ��o�e��  �s�����G" + stud.getId() + "  �W�٬��G" +stud.getName() + "\n"); // ��o�A�Ⱦ���T
            }
        } catch (Exception e) {
            e.printStackTrace();
        } 
        finally {
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
    /**
     * @return
     */
    protected JPanel getPanel() {
        if (panel == null) {
            panel = new JPanel();
            panel.add(getLabel());
            tf_id.setPreferredSize(new Dimension(70, 25));
            panel.add(tf_id);
            panel.add(getLabel_1());
            panel.add(getTf_name());
            panel.add(getButton());
        }
        return panel;
    }
    
    /**
     * @return
     */
    protected JLabel getLabel() {
        if (label == null) {
            label = new JLabel();
            label.setText("�s���G");
        }
        return label;
    }
    /**
     * @return
     */
    protected JLabel getLabel_1() {
        if (label_1 == null) {
            label_1 = new JLabel();
            label_1.setText("�W�١G");
        }
        return label_1;
    }
    /**
     * @return
     */
    protected JTextField getTf_name() {
        if (tf_name == null) {
            tf_name = new JTextField();
            tf_name.setPreferredSize(new Dimension(100, 25));
        }
        return tf_name;
    }
    /**
     * @return
     */
    protected JButton getButton() {
        if (button == null) {
            button = new JButton();
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
                    ta_info.append("�Ȥ�ݵo�e��  �s���O�G" + tf_id.getText() + "  �W�٬O�G"+tf_name.getText()+"\n"); // �N�¤�r�ؤ���T��ܦb�¤�r�줤
                    tf_id.setText(null); // �N�¤�r�زM��
                    tf_name.setText(null);
                }
            });
            button.setText("�o  �e");
        }
        return button;
    }
}
