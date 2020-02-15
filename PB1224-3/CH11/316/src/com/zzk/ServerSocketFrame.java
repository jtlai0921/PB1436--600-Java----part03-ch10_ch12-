package com.zzk;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

public class ServerSocketFrame extends JFrame {
    private Image sendImg = null; // �ŧi�ϧιﹳ
    private Image receiveImg = null; // �ŧi�ϧιﹳ
    private SendImagePanel sendImagePanel = null; // �ŧi�ϧέ��O�ﹳ
    private ReceiveImagePanel receiveImagePanel = null; // �ŧi�ϧέ��O�ﹳ
    private File imgFile = null;// �ŧi�ҿ�ܹϤ���File�ﹳ
    private JTextField tf_path;
    private DataOutputStream out = null; // �إ߬y�ﹳ
    private DataInputStream in = null; // �إ߬y�ﹳ
    private ServerSocket server; // �ŧiServerSocket�ﹳ
    private Socket socket; // �ŧiSocket�ﹳsocket
    private long lengths = -1; // �Ϥ��ɮת��j�p
    public void getServer() {
        try {
            server = new ServerSocket(1978); // ��Ҥ�Socket�ﹳ
            while (true) { // �p�G�M���r�O�s�����A
                socket = server.accept(); // ��Ҥ�Socket�ﹳ
                out = new DataOutputStream(socket.getOutputStream());// ��o��X�y�ﹳ
                in = new DataInputStream(socket.getInputStream());// ��o��J�y�ﹳ
                getClientInfo(); // �I�sgetClientInfo()��k
            }
        } catch (Exception e) {
            e.printStackTrace(); // ��X�ҥ~��T
        }
    }
    
    private void getClientInfo() {
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
        
        sendImagePanel = new SendImagePanel();
        receiveImagePanel = new ReceiveImagePanel();
        final JButton button_1 = new JButton();
        button_1.addActionListener(new ActionListener() {
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
        button_1.setText("��ܹϤ�");
        panel.add(button_1);
        
        final JButton button = new JButton();
        button.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                try {
                    DataInputStream inStream = null;// �w�q��ƿ�J�y�ﹳ
                    if (imgFile != null) {
                        lengths = imgFile.length();// ��o��ܹϤ����j�p
                        inStream = new DataInputStream(new FileInputStream(imgFile));// �إ߿�J�y�ﹳ
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
        button.setText("�o  �e");
        panel.add(button);
        
        final JPanel panel_1 = new JPanel();
        panel_1.setLayout(new BorderLayout());
        getContentPane().add(panel_1, BorderLayout.CENTER);
        
        final JPanel panel_2 = new JPanel();
        panel_2.setLayout(new GridLayout(1, 0));
        final FlowLayout flowLayout = new FlowLayout();
        flowLayout.setAlignment(FlowLayout.LEFT);
        panel_2.setLayout(flowLayout);
        panel_1.add(panel_2, BorderLayout.NORTH);
        
        final JLabel label_1 = new JLabel();
        label_1.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
        label_1.setText("�A�Ⱦ��ݿ�ܪ��n�o�e���Ϥ�  ");
        panel_2.add(label_1);
        
        final JLabel label_2 = new JLabel();
        label_2.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
        label_2.setText("������Ȥ�ݵo�e���Ϥ�       ");
        panel_2.add(label_2);
        
        final JPanel imgPanel = new JPanel();
        final GridLayout gridLayout = new GridLayout(1, 0);
        gridLayout.setVgap(10);
        imgPanel.setLayout(gridLayout);
        panel_1.add(imgPanel, BorderLayout.CENTER);
        imgPanel.add(sendImagePanel);
        imgPanel.add(receiveImagePanel);
        sendImagePanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
        receiveImagePanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
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