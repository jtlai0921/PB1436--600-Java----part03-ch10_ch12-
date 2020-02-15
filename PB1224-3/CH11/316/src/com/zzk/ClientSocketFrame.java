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
    private Image sendImg = null; // 宣告圖形對像
    private Image receiveImg = null; // 宣告圖形對像
    private SendImagePanel sendImagePanel = null; // 宣告圖形面板對像
    private ReceiveImagePanel receiveImagePanel = null; // 宣告圖形面板對像
    private File imgFile = null;// 宣告所選擇圖片的File對像
    private JTextField tf_path;
    private DataInputStream in = null; // 建立流對像
    private DataOutputStream out = null; // 建立流對像
    private Socket socket; // 宣告Socket對像
    private Container cc; // 宣告Container對像
    private long lengths = -1;// 圖片檔案的大小
    
    private void connect() { // 連接套接字方法
        try { // 捕捉例外
            socket = new Socket("localhost", 1978); // 實例化Socket對像
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
            long lengths = in.readLong();// 讀取圖片檔案的長度
            byte[] bt = new byte[(int) lengths];// 建立字節陣列
            for (int i = 0; i < bt.length; i++) {
                bt[i] = in.readByte();// 讀取字節資訊並儲存到字節陣列
            }
            receiveImg = new ImageIcon(bt).getImage();// 建立圖形對像
            receiveImagePanel.repaint();// 重新繪製圖形
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
        tf_path.setPreferredSize(new Dimension(140, 25));
        panel.add(tf_path);
        
        final JButton button = new JButton();
        button.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();// 建立檔案選擇器
                FileFilter filter = new FileNameExtensionFilter(
                        "圖形檔案（JPG/GIF/BMP)", "JPG", "JPEG", "GIF", "BMP");// 建立過濾器
                fileChooser.setFileFilter(filter);// 設定過濾器
                int flag = fileChooser.showOpenDialog(null);// 顯示開啟交談視窗
                if (flag == JFileChooser.APPROVE_OPTION) {
                    imgFile = fileChooser.getSelectedFile(); // 獲得勾選圖片的File對像
                }
                if (imgFile != null) {
                    tf_path.setText(imgFile.getAbsolutePath());// 圖片完整路徑
                    try {
                        sendImg = ImageIO.read(imgFile);// 建構BufferedImage對像
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
                sendImagePanel.repaint();// 呼叫paint()方法
            }
        });
        button.setText("選擇圖片");
        panel.add(button);
        
        final JButton button_1 = new JButton();
        button_1.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                try {
                    DataInputStream inStream = null;// 定義資料輸入流對像
                    if (imgFile != null) {
                        lengths = imgFile.length();// 獲得選擇圖片的大小
                        inStream = new DataInputStream(new FileInputStream(
                                imgFile));// 建立輸入流對像
                    } else {
                        JOptionPane.showMessageDialog(null, "還沒有選擇圖片檔案。");
                        return;
                    }
                    out.writeLong(lengths);// 將檔案的大小寫入輸出流
                    byte[] bt = new byte[(int) lengths];// 建立字節陣列
                    int len = -1;
                    while ((len = inStream.read(bt)) != -1) {// 將圖片檔案讀取到字節陣列
                        out.write(bt);// 將字節陣列寫入輸出流
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
        button_1.setText("發  送");
        panel.add(button_1);
        
        final JPanel panel_1 = new JPanel();
        panel_1.setLayout(new BorderLayout());
        getContentPane().add(panel_1, BorderLayout.CENTER);
        
        final JPanel panel_2 = new JPanel();
        panel_2.setLayout(new GridLayout(1, 0));
        panel_1.add(panel_2, BorderLayout.NORTH);
        
        final JLabel label_1 = new JLabel();
        label_1.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
        label_1.setText("客戶端選擇的要發送的圖片");
        panel_2.add(label_1);
        
        final JLabel label_2 = new JLabel();
        label_2.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
        label_2.setText("接收到服務器端發送的圖片     ");
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
    
    // 建立面板類別
    class SendImagePanel extends JPanel {
        public void paint(Graphics g) {
            if (sendImg != null) {
                g.clearRect(0, 0, this.getWidth(), this.getHeight());// 清除繪圖上下文的內容
                g.drawImage(sendImg, 0, 0, this.getWidth(), this.getHeight(),
                        this);// 繪製指定大小的圖片
            }
        }
    }
    
    // 建立面板類別
    class ReceiveImagePanel extends JPanel {
        public void paint(Graphics g) {
            if (receiveImg != null) {
                g.clearRect(0, 0, this.getWidth(), this.getHeight());// 清除繪圖上下文的內容
                g.drawImage(receiveImg, 0, 0, this.getWidth(),
                        this.getHeight(), this);// 繪製指定大小的圖片
            }
        }
    }
}
