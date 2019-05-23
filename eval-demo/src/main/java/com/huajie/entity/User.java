package com.huajie.entity;

import lombok.Data;

@Data
public class User {
    private String name;
    private String age;
    private String orgId;
    private String level;

    public User(String name, String age, String orgId,String level) {
        this.name = name;
        this.age = age;
        this.orgId = orgId;
        this.level = level;
    }
}
