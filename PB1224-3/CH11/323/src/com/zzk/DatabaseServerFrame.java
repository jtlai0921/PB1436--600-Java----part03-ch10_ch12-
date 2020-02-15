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
    private PrintWriter writer; // 宣告PrintWriter類別對像
    private ServerSocket server; // 宣告ServerSocket對像
    private Socket socket; // 宣告Socket對像socket
    
    public void getserver() {
        try {
            server = new ServerSocket(1978); // 實例化Socket對像
            ta_info.append("服務器套接字已經建立成功\n"); // 輸出資訊
            while (true) { // 如果套接字是連接狀態
                ta_info.append("等待客戶機的連接......\n"); // 輸出資訊
                socket = server.accept(); // 實例化Socket對像
                writer = new PrintWriter(socket.getOutputStream(), true);// 建立輸出流對像
                getClientInfo(); // 呼叫getClientInfo()方法
            }
        } catch (Exception e) {
            e.printStackTrace(); // 輸出例外資訊
        }
    }
    
    private void getClientInfo() {
        try {
            BufferedReader reader; // 宣告BufferedReader對像
            while (true) { // 如果套接字是連接狀態
                reader = new BufferedReader(new InputStreamReader(socket
                        .getInputStream())); // 實例化BufferedReader對像
                String line = reader.readLine();// 讀取客戶端資訊
                if (line != null) {
                    String[] value = new String[2];// 建立陣列
                    value[0] = line.substring(0, line.indexOf(":data:"));// 獲得姓名
                    value[1] = line.substring(line.indexOf(":data:") + 6);// 獲得年齡
                    ta_info.append("接收到客戶端的資訊\n姓名為："+value[0]+" 年齡為："+value[1]+"。\n");
                    try {
                        Connection conn = DAO.getConn();// 獲得資料函數庫連接
                        String sql = "insert into tb_employee (name,age) values(?,?)";// 定義SQL敘述
                        PreparedStatement ps = conn.prepareStatement(sql);// 建立PreparedStatement對象，並傳遞SQL敘述
                        ps.setString(1, value[0]); // 為第1個參數給予值
                        ps.setInt(2, Integer.parseInt(value[1]));// 為第2個參數給予值
                        int flag = ps.executeUpdate(); // 執行SQL敘述，獲得更新記錄數
                        ps.close();// 關閉PreparedStatement對像
                        conn.close();// 關閉連接
                        if (flag > 0) {
                            ta_info.append("並成功地儲存到資料函數庫中。\n");
                            writer.println("儲存成功。");// 向客戶端輸出儲存成功的資訊
                        } else {
                            writer.println("儲存失敗。\n");// 向客戶端輸出儲存成功的資訊
                        }
                    } catch (SQLException ee) {
                        writer.println("儲存失敗。\n" + ee.getMessage());// 向客戶端輸出儲存成功的資訊
                    }
                }
            }
        } catch (Exception e) {
            ta_info.append("客戶端已退出。\n");
        } finally {
            try {
                if (socket != null) {
                    socket.close(); // 關閉套接字
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
        frame.getserver(); // 呼叫方法
    }
    
    /**
     * Create the frame
     */
    public DatabaseServerFrame() {
        super();
        getContentPane().setLayout(null);
        setTitle("服務器端程式");
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