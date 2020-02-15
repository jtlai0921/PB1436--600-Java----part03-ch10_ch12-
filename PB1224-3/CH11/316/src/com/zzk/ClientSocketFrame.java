package com.zzk;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

public class ClientSocketFrame extends JFrame {
    private Image sendImg = null; // �ŧi�ϧιﹳ
    private Image receiveImg = null; // �ŧi�ϧιﹳ
    private SendImagePanel sendImagePanel = null; // �ŧi�ϧέ��O�ﹳ
    private ReceiveImagePanel receiveImagePanel = null; // �ŧi�ϧέ��O�ﹳ
    private File imgFile = null;// �ŧi�ҿ�ܹϤ���File�ﹳ
    private JTextField tf_path;
    private DataInputStream in = null; // �إ߬y�ﹳ
    private DataOutputStream out = null; // �إ߬y�ﹳ
    private Socket socket; // �ŧiSocket�ﹳ
    private Container cc; // �ŧiContainer�ﹳ
    private long lengths = -1;// �Ϥ��ɮת��j�p
    
    private void connect() { // �s���M���r��k
        try { // �����ҥ~
            socket = new Socket("localhost", 1978); // ��Ҥ�Socket�ﹳ
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
            long lengths = in.readLong();// Ū���Ϥ��ɮת�����
            byte[] bt = new byte[(int) lengths];// �إߦr�`�}�C
            for (int i = 0; i < bt.length; i++) {
                bt[i] = in.readByte();// Ū���r�`��T���x�s��r�`�}�C
            }
            receiveImg = new ImageIcon(bt).getImage();// �إ߹ϧιﹳ
            receiveImagePanel.repaint();// ���sø�s�ϧ�
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
        tf_path.setPreferredSize(new Dimension(140, 25));
        panel.add(tf_path);
        
        final JButton button = new JButton();
        button.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();// �إ��ɮ׿�ܾ�
                FileFilter filter = new FileNameExtensionFilter(
                        "�ϧ��ɮס]JPG/GIF/BMP)", "JPG", "JPEG", "GIF", "BMP");// �إ߹L�o��
                fileChooser.setFileFilter(filter);// �]�w�L�o��
                int flag = fileChooser.showOpenDialog(null);// ��ܶ}�ҥ�͵���
                if (flag == JFileChooser.APPROVE_OPTION) {
                    imgFile = fileChooser.getSelectedFile(); // ��o�Ŀ�Ϥ���File�ﹳ
                }
                if (imgFile != null) {
                    tf_path.setText(imgFile.getAbsolutePath());// �Ϥ�������|
                    try {
                        sendImg = ImageIO.read(imgFile);// �غcBufferedImage�ﹳ
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
                sendImagePanel.repaint();// �I�spaint()��k
            }
        });
        button.setText("��ܹϤ�");
        panel.add(button);
        
        final JButton button_1 = new JButton();
        button_1.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                try {
                    DataInputStream inStream = null;// �w�q��ƿ�J�y�ﹳ
                    if (imgFile != null) {
                        lengths = imgFile.length();// ��o��ܹϤ����j�p
                        inStream = new DataInputStream(new FileInputStream(
                                imgFile));// �إ߿�J�y�ﹳ
                    } else {
                        JOptionPane.showMessageDialog(null, "�٨S����ܹϤ��ɮסC");
                        return;
                    }
                    out.writeLong(lengths);// �N�ɮת��j�p�g�J��X�y
                    byte[] bt = new byte[(int) lengths];// �إߦr�`�}�C
                    int len = -1;
                    while ((len = inStream.read(bt)) != -1) {// �N�Ϥ��ɮ�Ū����r�`�}�C
                        out.write(bt);// �N�r�`�}�C�g�J��X�y
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
        button_1.setText("�o  �e");
        panel.add(button_1);
        
        final JPanel panel_1 = new JPanel();
        panel_1.setLayout(new BorderLayout());
        getContentPane().add(panel_1, BorderLayout.CENTER);
        
        final JPanel panel_2 = new JPanel();
        panel_2.setLayout(new GridLayout(1, 0));
        panel_1.add(panel_2, BorderLayout.NORTH);
        
        final JLabel label_1 = new JLabel();
        label_1.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
        label_1.setText("�Ȥ�ݿ�ܪ��n�o�e���Ϥ�");
        panel_2.add(label_1);
        
        final JLabel label_2 = new JLabel();
        label_2.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
        label_2.setText("������A�Ⱦ��ݵo�e���Ϥ�     ");
        panel_2.add(label_2);
        
        final JPanel imgPanel = new JPanel();
        sendImagePanel = new SendImagePanel();
        receiveImagePanel = new ReceiveImagePanel();
        imgPanel.add(sendImagePanel);
        imgPanel.add(receiveImagePanel);
        final GridLayout gridLayout = new GridLayout(1, 0);
        gridLayout.setVgap(6);
        imgPanel.setLayout(gridLayout);
        panel_1.add(imgPanel, BorderLayout.CENTER);
        //
    }
    
    // �إ߭��O���O
    class SendImagePanel extends JPanel {
        public void paint(Graphics g) {
            if (sendImg != null) {
                g.clearRect(0, 0, this.getWidth(), this.getHeight());// �M��ø�ϤW�U�媺���e
                g.drawImage(sendImg, 0, 0, this.getWidth(), this.getHeight(),
                        this);// ø�s���w�j�p���Ϥ�
            }
        }
    }
    
    // �إ߭��O���O
    class ReceiveImagePanel extends JPanel {
        public void paint(Graphics g) {
            if (receiveImg != null) {
                g.clearRect(0, 0, this.getWidth(), this.getHeight());// �M��ø�ϤW�U�媺���e
                g.drawImage(receiveImg, 0, 0, this.getWidth(),
                        this.getHeight(), this);// ø�s���w�j�p���Ϥ�
            }
        }
    }
}
