package com.zzk;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.swing.JOptionPane;

public class DownMultiThread implements Runnable{
    private String sUrl = "";// �����귽�a�}
    private File desFile;// �ݭn�g�J���ؼ��ɮ׹ﹳ
    private long startPos;// �g�J���}�l��m
    private long endPos;// �g�J��������m
    /**
     * @param sUrl �����귽�a�}
     * @param file �ݭn�g�J���ؼ��ɮ׹ﹳ
     * @param startPos �g�J���}�l��m
     * @param endPos �g�J��������m
     */
    public DownMultiThread(String sUrl,File desFile,long startPos,long endPos) {
        this.sUrl = sUrl;
        this.desFile = desFile;
        this.startPos = startPos;
        this.endPos = endPos;
    }
    @Override
    public void run() {
        try {
            URL url = new URL(sUrl);// �إߤU���귽��URL�ﹳ
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();// �}�ҳs���ﹳ
            conn.setRequestProperty("User-Agent", "NetFox");// �]�w�ШD�ݩ�
            String rangeProperty = "bytes="+startPos+"-";// �w�q�d���ݩ�
            if (endPos > 0){
                rangeProperty = "bytes="+startPos+"-" + endPos;// �վ�d���ݩʪ���
            }
            conn.setRequestProperty("RANGE", rangeProperty);// ���w�d���ݩ�
            RandomAccessFile out = new RandomAccessFile(desFile, "rw");// �إ�Ū���g���y�ﹳ
            out.seek(startPos);// ���wŪ�g���}�l�аO
            InputStream in = conn.getInputStream();// ��o�����귽����J�y�ﹳ
            BufferedInputStream bin = new BufferedInputStream(in);// �إ߿�J�w�Ĭy�ﹳ
            byte[] buff = new byte[2048];// �إߦr�`�}�C
            int len = -1;// �ŧi�s��Ū���r�`�ƪ��ܼ�
            len=bin.read(buff);// Ū���줺�e�üW�[��r�`�}�C
            while (len!=-1){
                out.write(buff,0,len);// �g�J�Ϻ��ɮ�
                len=bin.read(buff);// Ū���줺�e�üW�[��r�`�}�C
            }
            out.close();// �����y
            bin.close();// �����y
            conn.disconnect();// �_�}�s��
        }catch(Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }
}
