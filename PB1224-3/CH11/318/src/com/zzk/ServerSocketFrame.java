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
    private File file = null;// 宣告所選擇圖片的File對像
    private JTextField tf_path;
    private DataOutputStream out = null; // 建立流對像
    private DataInputStream in = null; // 建立流對像
    private ServerSocket server; // 宣告ServerSocket對像
    private Socket socket; // 宣告Socket對像socket
    private long lengths = -1; // 圖片檔案的大小
    private String fileName = null;
    
    public void getServer() {
        try {
            server = new ServerSocket(1978); // 實例化Socket對像
            ta_info.append("服務器套接字已經建立成功\n"); // 輸出資訊
            ta_info.append("等待客戶機的連接......\n"); // 輸出資訊
            socket = server.accept(); // 實例化Socket對像
            ta_info.append("客戶機連接成功......\n"); // 輸出資訊
            while (true) { // 如果套接字是連接狀態
                if (socket != null && !socket.isClosed()) {
                    out = new DataOutputStream(socket.getOutputStream());// 獲得輸出流對像
                    in = new DataInputStream(socket.getInputStream());// 獲得輸入流對像
                    getClientInfo(); // 呼叫getClientInfo()方法
                } else {
                    socket = server.accept(); // 實例化Socket對像
                }
            }
        } catch (Exception e) {
            e.printStackTrace(); // 輸出例外資訊
        }
    }
    
    private void getClientInfo() {
        try {
            String name = in.readUTF();// 讀取檔案名
            long lengths = in.readLong();// 讀取檔案的長度
            byte[] bt = new byte[(int) lengths];// 建立字節陣列
            for (int i = 0; i < bt.length; i++) {
                bt[i] = in.readByte();// 讀取字節資訊並儲存到字節陣列
            }
            FileDialog dialog = new FileDialog(ServerSocketFrame.this, "儲存");// 建立交談視窗
            dialog.setMode(FileDialog.SAVE);// 設定交談視窗為儲存交談視窗
            dialog.setFile(name);
            dialog.setVisible(true);// 顯示儲存交談視窗
            String path = dialog.getDirectory();// 獲得檔案的儲存路徑
            
            String newFileName = dialog.getFile();// 獲得儲存的檔案名
            if (path == null || newFileName == null) {
                return;
            }
            String pathAndName = path + "\\" + newFileName;// 檔案的完整路徑
            FileOutputStream fOut = new FileOutputStream(pathAndName);
            fOut.write(bt);
            fOut.flush();
            fOut.close();
            ta_info.append("檔案接收完畢。");
        } catch (Exception e) {
        } finally {
            try {
                if (in != null) {
                    in.close();// 關閉流
                }
                if (socket != null) {
                    socket.close(); // 關閉套接字
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    public static void main(String[] args) { // 主方法
        ServerSocketFrame frame = new ServerSocketFrame(); // 建立本類別對像
        frame.setVisible(true);
        frame.getServer(); // 呼叫方法
    }
    
    public ServerSocketFrame() {
        super();
        setTitle("服務器端程式");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 379, 260);
        
        final JPanel panel = new JPanel();
        getContentPane().add(panel, BorderLayout.NORTH);
        
        final JLabel label = new JLabel();
        label.setText("路徑：");
        panel.add(label);
        
        tf_path = new JTextField();
        tf_path.setPreferredSize(new Dimension(140, 25));
        panel.add(tf_path);
        
        final JButton button_1 = new JButton();
        button_1.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();// 建立檔案選擇器
                FileFilter filter = new FileNameExtensionFilter(
                        "視訊檔案（AVI/MPG/DAT/RM)", "AVI", "MPG", "DAT", "RM");// 建立過濾器
                fileChooser.setFileFilter(filter);// 設定過濾器
                int flag = fileChooser.showOpenDialog(null);// 顯示開啟交談視窗
                if (flag == JFileChooser.APPROVE_OPTION) {
                    file = fileChooser.getSelectedFile(); // 獲得勾選視訊檔案的File對像
                }
                if (file != null) {
                    tf_path.setText(file.getAbsolutePath());// 視訊檔案的完整路徑
                    fileName = file.getName();// 獲得視訊檔案的名稱
                }
            }
        });
        button_1.setText("選擇視訊");
        panel.add(button_1);
        
        final JButton button = new JButton();
        button.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                try {
                    DataInputStream inStream = null;// 定義資料輸入流對像
                    if (file != null) {
                        lengths = file.length();// 獲得所選擇視訊檔案的大小
                        inStream = new DataInputStream(
                                new FileInputStream(file));// 建立輸入流對像
                    } else {
                        JOptionPane.showMessageDialog(null, "還沒有選擇視訊檔案。");
                        return;
                    }
                    out.writeUTF(fileName);// 寫入視訊檔案名
                    out.writeLong(lengths);// 將檔案的大小寫入輸出流
                    byte[] bt = new byte[(int) lengths];// 建立字節陣列
                    int len = -1;
                    while ((len = inStream.read(bt)) != -1) {// 將視訊檔案讀取到字節陣列
                        out.write(bt);// 將字節陣列寫入輸出流
                    }
                    out.flush();
                    out.close();
                    ta_info.append("檔案發送完畢。");
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
        button.setText("發  送");
        panel.add(button);
        
        final JScrollPane scrollPane = new JScrollPane();
        getContentPane().add(scrollPane, BorderLayout.CENTER);
        
        ta_info = new JTextArea();
        scrollPane.setViewportView(ta_info);
    }
    
}
