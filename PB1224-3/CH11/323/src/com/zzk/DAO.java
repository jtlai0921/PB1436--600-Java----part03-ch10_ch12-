package com.zzk;
import java.sql.*;
import javax.swing.JOptionPane;
public class DAO {
    private static DAO dao = new DAO(); // 宣告DAO類別的靜態實例
    /**
     * 建構方法，載入資料函數庫驅動
     */
    public DAO() {
        try {
            Class.forName("sun.jdbc.odbc.JdbcOdbcDriver"); // 載入資料函數庫驅動
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, "資料函數庫驅動載入失敗。\n"
                    + e.getMessage());
        }
    }
    
    /**
     * 獲得資料函數庫連接的方法
     * 
     * @return Connection
     */
    public static Connection getConn() {
        try {
            Connection conn = null; // 定義資料函數庫連接
            String url = "jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)};DBQ=database/db_picture.mdb"; // 資料函數庫db_picture.mdb的URL
            String username = ""; // 資料函數庫的使用者名稱
            String password = ""; // 資料函數庫密碼
            conn = DriverManager.getConnection(url, username, password); // 建立連接
            return conn; // 傳回連接
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "資料函數庫連接失敗。\n" + e.getMessage());
            return null;
        }
    }
}