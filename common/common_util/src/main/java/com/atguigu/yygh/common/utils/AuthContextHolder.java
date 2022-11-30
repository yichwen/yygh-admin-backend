package com.atguigu.yygh.common.utils;

import com.atguigu.yygh.common.helper.JwtHelper;

import javax.servlet.http.HttpServletRequest;

// 获取当前用户信息工具类
public class AuthContextHolder {

    public static Long getUserId(HttpServletRequest request) {
        String token = request.getHeader("token");
        return JwtHelper.getUserId(token);
    }

    public static String getUserName(HttpServletRequest request) {
        String token = request.getHeader("token");
        return JwtHelper.getUserName(token);
    }

}
