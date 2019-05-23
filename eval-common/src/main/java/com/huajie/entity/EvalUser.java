package com.huajie.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * 系统中的user当前登录用户对象
 */
@Data
public class EvalUser implements Serializable {
    private static final long serialVersionUID = 42L;

    private String userid;
    private String username;
    private String orgId; //过滤字段1
    private String level; //过滤字段2

}
