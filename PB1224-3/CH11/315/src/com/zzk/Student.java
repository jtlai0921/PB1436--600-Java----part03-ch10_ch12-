package com.zzk;

import java.io.Serializable;

public class Student implements Serializable {  // �ǦC�ƹﹳ���O
    private String id;                  // ���O�������ܼ�
    private String name;// ���O�������ܼ�
    public String getId() { // �����ܼƪ�getter��k
        return id;
    }
    public void setId(String id) {// �����ܼƪ�setter��k
        this.id = id;
    }
    public String getName() {// �����ܼƪ�getter��k
        return name;
    }
    public void setName(String name) {// �����ܼƪ�setter��k
        this.name = name;
    }
}
