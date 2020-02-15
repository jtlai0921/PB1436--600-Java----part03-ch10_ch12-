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
    private File file = null;// 宣告所選擇視訊的File對像
    private JTextField tf_path;
    private DataInputStream in = null; // 建立流對像
    private DataOutputStream out = null; // 建立流對像
    private Socket socket; // 宣告Socket對像
    private long lengths = -1;// 圖片檔案的大小
    private String fileName = null;
    
    private void connect() { // 連接套接字方法
        ta_info.append("嘗試連接......\n"); // 純文字域中資訊資訊
        try { // 捕捉例外
            socket = new Socket("localhost", 1978); // 實例化Socket對像
            ta_info.append("完成連接。\n"); // 純文字域中提示資訊
            while (true) {
                if (socket != null && !socket.isClosed()) {
                    out = new DataOutputStream(socket.getOutputStream());// 獲得輸出流對像
                    in = new DataInputStream(socket.getInputStream());// 獲得輸入流對像
                    getServerInfo();// 呼叫getServerInfo()方法
                } else {
                    socket = new Socket("localhost", 1978); // 實例化Socket對像
                }
            }
        } catch (Exception e) {
            e.printStackTrace(); // 輸出例外資訊
        }
    }
    
    public static void main(String[] args) { // 主方法
        ClientSocketFrame clien = new ClientSocketFrame(); // 建立本例對像
        clien.setVisible(true); // 將窗體顯示
        clien.connect(); // 呼叫連接方法
    }
    
    private void getServerInfo() {
        try {
            String name = in.readUTF();// 讀取檔案名
            long lengths = in.readLong();// 讀取檔案的長度
            byte[] bt = new byte[(int) lengths];// 建立字節陣列
            for (int i = 0; i < bt.length; i++) {
                bt[i] = in.readByte();// 讀取字節資訊並儲存到字節陣列
            }
            FileDialog dialog = new FileDialog(ClientSocketFrame.this, "儲存");// 建立交談視窗
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
    
    /**
     * Create the frame
     */
    public ClientSocketFrame() {
        super();
        setTitle("客戶端程式");
        setBounds(100, 100, 373, 257);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        final JPanel panel = new JPanel();
        getContentPane().add(panel, BorderLayout.NORTH);

        final JLabel label = new JLabel();
        label.setText("路徑：");
        panel.add(label);

        tf_path = new JTextField();
        tf_path.setPreferredSize(new Dimension(140,25));
        panel.add(tf_path);

        final JButton button = new JButton();
        button.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();// 建立檔案選擇器
                FileFilter filter = new FileNameExtensionFilter("視訊檔案（AVI/MPG/DAT/RM)", "AVI", "MPG", "DAT", "RM");// 建立過濾器
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
        button.setText("選擇視訊");
        panel.add(button);

        final JButton button_1 = new JButton();
        button_1.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                try {
                    DataInputStream inStream = null;// 定義資料輸入流對像
                    if (file != null) {
                        lengths = file.length();// 獲得所選擇視訊檔案的大小
                        inStream = new DataInputStream(new FileInputStream(file));// 建立輸入流對像
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
        button_1.setText("發  送");
        panel.add(button_1);

        final JScrollPane scrollPane = new JScrollPane();
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        ta_info = new JTextArea();
        scrollPane.setViewportView(ta_info);
        //
    }
}
