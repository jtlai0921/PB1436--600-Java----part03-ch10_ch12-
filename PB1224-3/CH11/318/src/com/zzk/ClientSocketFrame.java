package com.zzk;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

public class ClientSocketFrame extends JFrame {
    private JTextArea ta_info;
    private File file = null;// �ŧi�ҿ�ܵ��T��File�ﹳ
    private JTextField tf_path;
    private DataInputStream in = null; // �إ߬y�ﹳ
    private DataOutputStream out = null; // �إ߬y�ﹳ
    private Socket socket; // �ŧiSocket�ﹳ
    private long lengths = -1;// �Ϥ��ɮת��j�p
    private String fileName = null;
    
    private void connect() { // �s���M���r��k
        ta_info.append("���ճs��......\n"); // �¤�r�줤��T��T
        try { // �����ҥ~
            socket = new Socket("localhost", 1978); // ��Ҥ�Socket�ﹳ
            ta_info.append("�����s���C\n"); // �¤�r�줤���ܸ�T
            while (true) {
                if (socket != null && !socket.isClosed()) {
                    out = new DataOutputStream(socket.getOutputStream());// ��o��X�y�ﹳ
                    in = new DataInputStream(socket.getInputStream());// ��o��J�y�ﹳ
                    getServerInfo();// �I�sgetServerInfo()��k
                } else {
                    socket = new Socket("localhost", 1978); // ��Ҥ�Socket�ﹳ
                }
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
            String name = in.readUTF();// Ū���ɮצW
            long lengths = in.readLong();// Ū���ɮת�����
            byte[] bt = new byte[(int) lengths];// �إߦr�`�}�C
            for (int i = 0; i < bt.length; i++) {
                bt[i] = in.readByte();// Ū���r�`��T���x�s��r�`�}�C
            }
            FileDialog dialog = new FileDialog(ClientSocketFrame.this, "�x�s");// �إߥ�͵���
            dialog.setMode(FileDialog.SAVE);// �]�w��͵������x�s��͵���
            dialog.setFile(name);
            dialog.setVisible(true);// ����x�s��͵���
            String path = dialog.getDirectory();// ��o�ɮת��x�s���|
            String newFileName = dialog.getFile();// ��o�x�s���ɮצW
            if (path == null || newFileName == null) {
                return;
            }
            String pathAndName = path + "\\" + newFileName;// �ɮת�������|
            FileOutputStream fOut = new FileOutputStream(pathAndName);
            fOut.write(bt);
            fOut.flush();
            fOut.close();
            ta_info.append("�ɮױ��������C");
        } catch (Exception e) {
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
    
    /**
     * Create the frame
     */
    public ClientSocketFrame() {
        super();
        setTitle("�Ȥ�ݵ{��");
        setBounds(100, 100, 373, 257);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        final JPanel panel = new JPanel();
        getContentPane().add(panel, BorderLayout.NORTH);

        final JLabel label = new JLabel();
        label.setText("���|�G");
        panel.add(label);

        tf_path = new JTextField();
        tf_path.setPreferredSize(new Dimension(140,25));
        panel.add(tf_path);

        final JButton button = new JButton();
        button.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();// �إ��ɮ׿�ܾ�
                FileFilter filter = new FileNameExtensionFilter("���T�ɮס]AVI/MPG/DAT/RM)", "AVI", "MPG", "DAT", "RM");// �إ߹L�o��
                fileChooser.setFileFilter(filter);// �]�w�L�o��
                int flag = fileChooser.showOpenDialog(null);// ��ܶ}�ҥ�͵���
                if (flag == JFileChooser.APPROVE_OPTION) {
                    file = fileChooser.getSelectedFile(); // ��o�Ŀ���T�ɮת�File�ﹳ
                }
                if (file != null) {
                    tf_path.setText(file.getAbsolutePath());// ���T�ɮת�������|
                    fileName = file.getName();// ��o���T�ɮת��W��
                }
            }
        });
        button.setText("��ܵ��T");
        panel.add(button);

        final JButton button_1 = new JButton();
        button_1.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                try {
                    DataInputStream inStream = null;// �w�q��ƿ�J�y�ﹳ
                    if (file != null) {
                        lengths = file.length();// ��o�ҿ�ܵ��T�ɮת��j�p
                        inStream = new DataInputStream(new FileInputStream(file));// �إ߿�J�y�ﹳ
                    } else {
                        JOptionPane.showMessageDialog(null, "�٨S����ܵ��T�ɮסC");
                        return;
                    }
                    out.writeUTF(fileName);// �g�J���T�ɮצW
                    out.writeLong(lengths);// �N�ɮת��j�p�g�J��X�y
                    byte[] bt = new byte[(int) lengths];// �إߦr�`�}�C
                    int len = -1;
                    while ((len = inStream.read(bt)) != -1) {// �N���T�ɮ�Ū����r�`�}�C
                        out.write(bt);// �N�r�`�}�C�g�J��X�y
                    }
                    out.flush();
                    out.close();
                    ta_info.append("�ɮ׵o�e�����C");
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
        button_1.setText("�o  �e");
        panel.add(button_1);

        final JScrollPane scrollPane = new JScrollPane();
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        ta_info = new JTextArea();
        scrollPane.setViewportView(ta_info);
        //
    }
}
