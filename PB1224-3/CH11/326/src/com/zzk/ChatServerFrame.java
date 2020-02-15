package com.zzk;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class ChatServerFrame extends JFrame {
    private JTextArea ta_info;
    private ServerSocket server; // �ŧiServerSocket�ﹳ
    private Socket socket; // �ŧiSocket�ﹳsocket
    private Hashtable<String, Socket> map = new Hashtable<String, Socket>();// �Ω��x�s�s����A�Ⱦ����ϥΪ̩M�Ȥ�ݮM���r�ﹳ
    
    public void createSocket() {
        try {
            server = new ServerSocket(1982);// �إߪA�Ⱦ��M���r�ﹳ
            while (true) {
                ta_info.append("���ݷs�Ȥ�s��......\n");
                socket = server.accept();// ��o�M���r�ﹳ
                ta_info.append("�Ȥ�ݳs�����\�C" + socket + "\n");
                new ServerThread(socket).start();// �إߨñҰʽu�{�ﹳ
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    class ServerThread extends Thread {
        Socket socket;
        public ServerThread(Socket socket) {
            this.socket = socket;
        }
        public void run() {
            try {
                ObjectInputStream ins = new ObjectInputStream(socket
                        .getInputStream());
                while (true) {
                    Vector v = null;
                    try {
                        v = (Vector) ins.readObject();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    if (v != null && v.size() > 0) {
                        for (int i = 0; i < v.size(); i++) {
                            String info = (String) v.get(i);// Ū����T
                            String key = "";
                            if (info.startsWith("�ϥΪ̡G")) {// �W�[�n�J�ϥΪ̨�Ȥ�ݦC��
                                key = info.substring(3, info.length());// ��o�ϥΪ̦W�٨ç@����ϥ�
                                map.put(key, socket);// �W�[��ȹ�
                                Set<String> set = map.keySet();// ��o���X���Ҧ��䪺Set�˵�
                                Iterator<String> keyIt = set.iterator();// ��o�Ҧ��䪺���N��
                                while (keyIt.hasNext()) {
                                    String receiveKey = keyIt.next();// ��o��ܱ�����T����
                                    Socket s = map.get(receiveKey);// ��o�P����������M���r�ﹳ
                                    PrintWriter out = new PrintWriter(s
                                            .getOutputStream(), true);// �إ߿�X�y�ﹳ
                                    Iterator<String> keyIt1 = set.iterator();// ��o�Ҧ��䪺���N��
                                    while (keyIt1.hasNext()) {
                                        String receiveKey1 = keyIt1.next();// ��o��A�Ω�V�Ȥ�ݼW�[�ϥΪ̦C��
                                        out.println(receiveKey1);// �o�e��T
                                        out.flush();// ��s��X�w�İ�
                                    }
                                }
                            } else if (info.startsWith("�h�X�G")) {
                                key = info.substring(3);// ��o�h�X�ϥΪ̪���
                                map.remove(key);// �W�[��ȹ�
                                Set<String> set = map.keySet();// ��o���X���Ҧ��䪺Set�˵�
                                Iterator<String> keyIt = set.iterator();// ��o�Ҧ��䪺���N��
                                while (keyIt.hasNext()) {
                                    String receiveKey = keyIt.next();// ��o��ܱ�����T����
                                    Socket s = map.get(receiveKey);// ��o�P����������M���r�ﹳ
                                    PrintWriter out = new PrintWriter(s
                                            .getOutputStream(), true);// �إ߿�X�y�ﹳ
                                    out.println("�h�X�G" + key);// �o�e��T
                                    out.flush();// ��s��X�w�İ�
                                }
                            } else {// ��o�������T��
                                key = info.substring(info.indexOf("�G�o�e���G") + 5,
                                        info.indexOf("�G����T�O�G"));// ��o�����誺key��,�Y�����誺�ϥΪ̦W��
                                String sendUser = info.substring(0, info
                                        .indexOf("�G�o�e���G"));// ��o�o�e�誺key��,�Y�o�e�誺�ϥΪ̦W��
                                Set<String> set = map.keySet();// ��o���X���Ҧ��䪺Set�˵�
                                Iterator<String> keyIt = set.iterator();// ��o�Ҧ��䪺���N��
                                while (keyIt.hasNext()) {
                                    String receiveKey = keyIt.next();// ��o��ܱ�����T����
                                    if (key.equals(receiveKey) && !sendUser.equals(receiveKey)) {// �P�����ϥΪ̬ۦP�A�����O�o�e�ϥΪ�
                                        Socket s = map.get(receiveKey);// ��o�P����������M���r�ﹳ
                                        PrintWriter out = new PrintWriter(s.getOutputStream(), true);// �إ߿�X�y�ﹳ
                                        out.println("MSG:" + info);// �o�e��T
                                        out.flush();// ��s��X�w�İ�
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (IOException e) {
                ta_info.append(socket + "�w�g�h�X�C\n");
            }
        }
    }
    
    public static void main(String args[]) {
        ChatServerFrame frame = new ChatServerFrame();
        frame.setVisible(true);
        frame.createSocket();
    }
    
    /**
     * Create the frame
     */
    public ChatServerFrame() {
        super();
        addWindowListener(new WindowAdapter() {
            public void windowIconified(final WindowEvent e) {
                setVisible(false);
            }
        });
        setTitle("��ѫǪA�Ⱦ���");
        setBounds(100, 100, 385, 266);
        
        final JScrollPane scrollPane = new JScrollPane();
        getContentPane().add(scrollPane, BorderLayout.CENTER);
        
        ta_info = new JTextArea();
        scrollPane.setViewportView(ta_info);
        
        //�u�@�C
        if (SystemTray.isSupported()){                                      // �P�_�O�_�䴩�t�Τu�@�C
            URL url=ChatServerFrame.class.getResource("server.png");          // ��o�Ϥ��Ҧb��URL
            ImageIcon icon = new ImageIcon(url);                            // ��Ҥƹϧιﹳ
            Image image=icon.getImage();                                    // ��oImage�ﹳ
            TrayIcon trayIcon=new TrayIcon(image);                          // �إߤu�@�C�ϼ�
            trayIcon.addMouseListener(new MouseAdapter(){                   // ���u�@�C�W�[���Ф����d
                public void mouseClicked(MouseEvent e){                     // ���Шƥ�
                    if (e.getClickCount()==2){                              // �P�_�O�_�����F����
                        showFrame();                                    // �I�s��k��ܵ���
                    }
                }
            });
            trayIcon.setToolTip("�t�Τu�@�C");                                    // �W�[�u�㴣�ܯ¤�r
            PopupMenu popupMenu=new PopupMenu();                    // �إߥX�{���
            MenuItem exit=new MenuItem("�h�X");                           // �إ߿�涵
            exit.addActionListener(new ActionListener() {                   // �W�[�ƥ��ť��
                public void actionPerformed(final ActionEvent arg0) {
                    System.exit(0);                                     // �h�X�t��
                }
            });
            popupMenu.add(exit);                                        // ���X�{���W�[��涵
            trayIcon.setPopupMenu(popupMenu);                           // ���u�@�C�ϼХ[�X�{��u
            SystemTray systemTray=SystemTray.getSystemTray();           // ��o�t�Τu�@�C�ﹳ
            try{
                systemTray.add(trayIcon);                               // ���t�Τu�@�C�[�u�@�C�ϼ�
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
    public void showFrame(){
        this.setVisible(true);                                              // ��ܵ���
        this.setState(Frame.NORMAL);
    }
}
