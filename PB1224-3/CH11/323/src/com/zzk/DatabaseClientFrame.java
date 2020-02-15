package com.zzk;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

public class DatabaseClientFrame extends JFrame {
    
    private JTextArea ta_result;
    private JTextField tf_age;
    private JTextField tf_name;
    private PrintWriter writer; // �ŧiPrintWriter���O�ﹳ
    private BufferedReader reader; // �ŧiBufferedReader�ﹳ
    private Socket socket; // �ŧiSocket�ﹳ
    
    /**
     * Launch the application
     * 
     * @param args
     */
    public static void main(String args[]) {
        DatabaseClientFrame frame = new DatabaseClientFrame();
        frame.setVisible(true);
        frame.connect();
    }
    
    private void connect() { // �s���M���r��k
        ta_result.append("���ճs��......\n"); // �¤�r�줤��T��T
        try { // �����ҥ~
            socket = new Socket("localhost", 1978); // ��Ҥ�Socket�ﹳ
            while (true) {
                writer = new PrintWriter(socket.getOutputStream(), true);// ��Ҥ�PrintWriter�ﹳ
                reader = new BufferedReader(new InputStreamReader(socket
                        .getInputStream())); // ��Ҥ�BufferedReader�ﹳ
                ta_result.append("�����s���C\n"); // �¤�r�줤���ܸ�T
                getServerInfo();// �I�s��kŪ���A�Ⱦ���T
            }
        } catch (Exception e) {
            e.printStackTrace(); // ��X�ҥ~��T
        }
    }
    
    private void getServerInfo() {
        try {
            while (true) { // �p�G�M���r�O�s�����A
                if (reader != null) {
                    String line = reader.readLine();// Ū���A�Ⱦ���T
                    if (line != null) {
                        ta_result.append("������A�Ⱦ��o�e����T�G " + line + "\n"); // ��o�A�Ⱦ���T
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
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
    public DatabaseClientFrame() {
        super();
        getContentPane().setLayout(null);
        setTitle("�Ȥ�ݵ{��");
        setBounds(100, 100, 277, 263);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        final JLabel label = new JLabel();
        label.setText("�W  �١G");
        label.setBounds(10, 12, 66, 18);
        getContentPane().add(label);
        
        final JLabel label_1 = new JLabel();
        label_1.setText("�~  �֡G");
        label_1.setBounds(10, 38, 66, 18);
        getContentPane().add(label_1);
        
        tf_name = new JTextField();
        tf_name.setBounds(56, 10, 194, 22);
        getContentPane().add(tf_name);
        
        tf_age = new JTextField();
        tf_age.setBounds(56, 36, 194, 22);
        getContentPane().add(tf_age);
        
        final JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBorder(new TitledBorder(null, "��ܪA�Ⱦ��ݪ����X��T",
                TitledBorder.DEFAULT_JUSTIFICATION,
                TitledBorder.DEFAULT_POSITION, null, null));
        panel.setBounds(10, 91, 240, 124);
        getContentPane().add(panel);
        
        final JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(10, 21, 220, 93);
        panel.add(scrollPane);
        
        ta_result = new JTextArea();
        scrollPane.setViewportView(ta_result);
        
        final JButton button = new JButton();
        button.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                String name = tf_name.getText().trim();// ��o�m�W
                String age = tf_age.getText().trim();// ��o�~��
                if (name == null || name.equals("") || age == null || age.equals("")) {
                    JOptionPane.showMessageDialog(null, "�m�W�M�~�֤��ର�šC");
                    return;
                }
                try {
                    Integer.parseInt(age);// �p�G�~�֤��O�Ʀr�N�|�o�ͨҥ~
                }catch(Exception ex) {
                    JOptionPane.showMessageDialog(null, "�~�֥������Ʀr�C");
                    return;
                }
                String info = name + ":data:" + age;// �ϥΦr��":data:"�s���m�W�M�~��
                writer.println(info);// �V�A�Ⱦ��o�e��T
            }
        });
        button.setText("�O    �s");
        button.setBounds(41, 62, 72, 23);
        getContentPane().add(button);
        
        final JButton button_1 = new JButton();
        button_1.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                System.exit(0);
            }
        });
        button_1.setText("�h    �X");
        button_1.setBounds(148, 62, 72, 23);
        getContentPane().add(button_1);
        //
    }
    
}
