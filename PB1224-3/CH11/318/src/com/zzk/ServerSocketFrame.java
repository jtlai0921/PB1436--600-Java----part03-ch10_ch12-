package com.zzk;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;
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

public class ServerSocketFrame extends JFrame {
    private JTextArea ta_info;
    private File file = null;// �ŧi�ҿ�ܹϤ���File�ﹳ
    private JTextField tf_path;
    private DataOutputStream out = null; // �إ߬y�ﹳ
    private DataInputStream in = null; // �إ߬y�ﹳ
    private ServerSocket server; // �ŧiServerSocket�ﹳ
    private Socket socket; // �ŧiSocket�ﹳsocket
    private long lengths = -1; // �Ϥ��ɮת��j�p
    private String fileName = null;
    
    public void getServer() {
        try {
            server = new ServerSocket(1978); // ��Ҥ�Socket�ﹳ
            ta_info.append("�A�Ⱦ��M���r�w�g�إߦ��\\n"); // ��X��T
            ta_info.append("���ݫȤ�����s��......\n"); // ��X��T
            socket = server.accept(); // ��Ҥ�Socket�ﹳ
            ta_info.append("�Ȥ���s�����\......\n"); // ��X��T
            while (true) { // �p�G�M���r�O�s�����A
                if (socket != null && !socket.isClosed()) {
                    out = new DataOutputStream(socket.getOutputStream());// ��o��X�y�ﹳ
                    in = new DataInputStream(socket.getInputStream());// ��o��J�y�ﹳ
                    getClientInfo(); // �I�sgetClientInfo()��k
                } else {
                    socket = server.accept(); // ��Ҥ�Socket�ﹳ
                }
            }
        } catch (Exception e) {
            e.printStackTrace(); // ��X�ҥ~��T
        }
    }
    
    private void getClientInfo() {
        try {
            String name = in.readUTF();// Ū���ɮצW
            long lengths = in.readLong();// Ū���ɮת�����
            byte[] bt = new byte[(int) lengths];// �إߦr�`�}�C
            for (int i = 0; i < bt.length; i++) {
                bt[i] = in.readByte();// Ū���r�`��T���x�s��r�`�}�C
            }
            FileDialog dialog = new FileDialog(ServerSocketFrame.this, "�x�s");// �إߥ�͵���
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
    
    public static void main(String[] args) { // �D��k
        ServerSocketFrame frame = new ServerSocketFrame(); // �إߥ����O�ﹳ
        frame.setVisible(true);
        frame.getServer(); // �I�s��k
    }
    
    public ServerSocketFrame() {
        super();
        setTitle("�A�Ⱦ��ݵ{��");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 379, 260);
        
        final JPanel panel = new JPanel();
        getContentPane().add(panel, BorderLayout.NORTH);
        
        final JLabel label = new JLabel();
        label.setText("���|�G");
        panel.add(label);
        
        tf_path = new JTextField();
        tf_path.setPreferredSize(new Dimension(140, 25));
        panel.add(tf_path);
        
        final JButton button_1 = new JButton();
        button_1.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();// �إ��ɮ׿�ܾ�
                FileFilter filter = new FileNameExtensionFilter(
                        "���T�ɮס]AVI/MPG/DAT/RM)", "AVI", "MPG", "DAT", "RM");// �إ߹L�o��
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
        button_1.setText("��ܵ��T");
        panel.add(button_1);
        
        final JButton button = new JButton();
        button.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                try {
                    DataInputStream inStream = null;// �w�q��ƿ�J�y�ﹳ
                    if (file != null) {
                        lengths = file.length();// ��o�ҿ�ܵ��T�ɮת��j�p
                        inStream = new DataInputStream(
                                new FileInputStream(file));// �إ߿�J�y�ﹳ
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
        button.setText("�o  �e");
        panel.add(button);
        
        final JScrollPane scrollPane = new JScrollPane();
        getContentPane().add(scrollPane, BorderLayout.CENTER);
        
        ta_info = new JTextArea();
        scrollPane.setViewportView(ta_info);
    }
    
}
