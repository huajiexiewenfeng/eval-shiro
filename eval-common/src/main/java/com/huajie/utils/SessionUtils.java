package com.huajie.utils;

import com.huajie.entity.EvalUser;

import javax.servlet.http.HttpServletRequest;

/**
 * session工具类
 * */
public class SessionUtils {

    /**
     * 从session中获取当前登录用户对象
     * 这里也只是举个例子，可以根据实际情况自己修改
     * */
    public static EvalUser getSessionEvalUser(HttpServletRequest request) {
        return (EvalUser)request.getSession().getAttribute("user");
    }

}
