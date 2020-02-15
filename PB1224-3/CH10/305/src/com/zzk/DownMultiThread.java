package com.zzk;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.swing.JOptionPane;

public class DownMultiThread implements Runnable{
    private String sUrl = "";// 網絡資源地址
    private File desFile;// 需要寫入的目標檔案對像
    private long startPos;// 寫入的開始位置
    private long endPos;// 寫入的結束位置
    /**
     * @param sUrl 網絡資源地址
     * @param file 需要寫入的目標檔案對像
     * @param startPos 寫入的開始位置
     * @param endPos 寫入的結束位置
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
            URL url = new URL(sUrl);// 建立下載資源的URL對像
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();// 開啟連接對像
            conn.setRequestProperty("User-Agent", "NetFox");// 設定請求屬性
            String rangeProperty = "bytes="+startPos+"-";// 定義範圍屬性
            if (endPos > 0){
                rangeProperty = "bytes="+startPos+"-" + endPos;// 調整範圍屬性的值
            }
            conn.setRequestProperty("RANGE", rangeProperty);// 指定範圍屬性
            RandomAccessFile out = new RandomAccessFile(desFile, "rw");// 建立讀取寫的流對像
            out.seek(startPos);// 指定讀寫的開始標記
            InputStream in = conn.getInputStream();// 獲得網絡資源的輸入流對像
            BufferedInputStream bin = new BufferedInputStream(in);// 建立輸入緩衝流對像
            byte[] buff = new byte[2048];// 建立字節陣列
            int len = -1;// 宣告存放讀取字節數的變數
            len=bin.read(buff);// 讀取到內容並增加到字節陣列
            while (len!=-1){
                out.write(buff,0,len);// 寫入磁碟檔案
                len=bin.read(buff);// 讀取到內容並增加到字節陣列
            }
            out.close();// 關閉流
            bin.close();// 關閉流
            conn.disconnect();// 斷開連接
        }catch(Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }
}
