package com.zzk;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class DatabaseServerFrame extends JFrame {
    
    private JTextArea ta_info;
    private PrintWriter writer; // �ŧiPrintWriter���O�ﹳ
    private ServerSocket server; // �ŧiServerSocket�ﹳ
    private Socket socket; // �ŧiSocket�ﹳsocket
    
    public void getserver() {
        try {
            server = new ServerSocket(1978); // ��Ҥ�Socket�ﹳ
            ta_info.append("�A�Ⱦ��M���r�w�g�إߦ��\\n"); // ��X��T
            while (true) { // �p�G�M���r�O�s�����A
                ta_info.append("���ݫȤ�����s��......\n"); // ��X��T
                socket = server.accept(); // ��Ҥ�Socket�ﹳ
                writer = new PrintWriter(socket.getOutputStream(), true);// �إ߿�X�y�ﹳ
                getClientInfo(); // �I�sgetClientInfo()��k
            }
        } catch (Exception e) {
            e.printStackTrace(); // ��X�ҥ~��T
        }
    }
    
    private void getClientInfo() {
        try {
            BufferedReader reader; // �ŧiBufferedReader�ﹳ
            while (true) { // �p�G�M���r�O�s�����A
                reader = new BufferedReader(new InputStreamReader(socket
                        .getInputStream())); // ��Ҥ�BufferedReader�ﹳ
                String line = reader.readLine();// Ū���Ȥ�ݸ�T
                if (line != null) {
                    String[] value = new String[2];// �إ߰}�C
                    value[0] = line.substring(0, line.indexOf(":data:"));// ��o�m�W
                    value[1] = line.substring(line.indexOf(":data:") + 6);// ��o�~��
                    ta_info.append("������Ȥ�ݪ���T\n�m�W���G"+value[0]+" �~�֬��G"+value[1]+"�C\n");
                    try {
                        Connection conn = DAO.getConn();// ��o��ƨ�Ʈw�s��
                        String sql = "insert into tb_employee (name,age) values(?,?)";// �w�qSQL�ԭz
                        PreparedStatement ps = conn.prepareStatement(sql);// �إ�PreparedStatement��H�A�öǻ�SQL�ԭz
                        ps.setString(1, value[0]); // ����1�ӰѼƵ�����
                        ps.setInt(2, Integer.parseInt(value[1]));// ����2�ӰѼƵ�����
                        int flag = ps.executeUpdate(); // ����SQL�ԭz�A��o��s�O����
                        ps.close();// ����PreparedStatement�ﹳ
                        conn.close();// �����s��
                        if (flag > 0) {
                            ta_info.append("�æ��\�a�x�s���ƨ�Ʈw���C\n");
                            writer.println("�x�s���\�C");// �V�Ȥ�ݿ�X�x�s���\����T
                        } else {
                            writer.println("�x�s���ѡC\n");// �V�Ȥ�ݿ�X�x�s���\����T
                        }
                    } catch (SQLException ee) {
                        writer.println("�x�s���ѡC\n" + ee.getMessage());// �V�Ȥ�ݿ�X�x�s���\����T
                    }
                }
            }
        } catch (Exception e) {
            ta_info.append("�Ȥ�ݤw�h�X�C\n");
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
     * Launch the application
     * 
     * @param args
     */
    public static void main(String args[]) {
        DatabaseServerFrame frame = new DatabaseServerFrame();
        frame.setVisible(true);
        frame.getserver(); // �I�s��k
    }
    
    /**
     * Create the frame
     */
    public DatabaseServerFrame() {
        super();
        getContentPane().setLayout(null);
        setTitle("�A�Ⱦ��ݵ{��");
        setBounds(100, 100, 277, 263);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        final JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(10, 10, 241, 205);
        getContentPane().add(scrollPane);
        
        ta_info = new JTextArea();
        scrollPane.setViewportView(ta_info);
        //
    }
    
}
