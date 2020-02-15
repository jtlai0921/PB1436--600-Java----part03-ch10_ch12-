package com.zzk;
import java.sql.*;
import javax.swing.JOptionPane;
public class DAO {
    private static DAO dao = new DAO(); // �ŧiDAO���O���R�A���
    /**
     * �غc��k�A���J��ƨ�Ʈw�X��
     */
    public DAO() {
        try {
            Class.forName("sun.jdbc.odbc.JdbcOdbcDriver"); // ���J��ƨ�Ʈw�X��
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, "��ƨ�Ʈw�X�ʸ��J���ѡC\n"
                    + e.getMessage());
        }
    }
    
    /**
     * ��o��ƨ�Ʈw�s������k
     * 
     * @return Connection
     */
    public static Connection getConn() {
        try {
            Connection conn = null; // �w�q��ƨ�Ʈw�s��
            String url = "jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)};DBQ=database/db_picture.mdb"; // ��ƨ�Ʈwdb_picture.mdb��URL
            String username = ""; // ��ƨ�Ʈw���ϥΪ̦W��
            String password = ""; // ��ƨ�Ʈw�K�X
            conn = DriverManager.getConnection(url, username, password); // �إ߳s��
            return conn; // �Ǧ^�s��
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "��ƨ�Ʈw�s�����ѡC\n" + e.getMessage());
            return null;
        }
    }
}