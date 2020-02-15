package com.zzk;

import java.io.Serializable;

public class Student implements Serializable {  // 序列化對像類別
    private String id;                  // 類別的成員變數
    private String name;// 類別的成員變數
    public String getId() { // 成員變數的getter方法
        return id;
    }
    public void setId(String id) {// 成員變數的setter方法
        this.id = id;
    }
    public String getName() {// 成員變數的getter方法
        return name;
    }
    public void setName(String name) {// 成員變數的setter方法
        this.name = name;
    }
}
