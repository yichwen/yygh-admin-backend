package com.atguigu.easyexcel;

import com.alibaba.excel.EasyExcel;

import java.util.ArrayList;
import java.util.List;

public class TestWrite {
    public static void main(String[] args) {
        // create a list
        List<UserData> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            UserData userData = new UserData();
            userData.setUid(i);
            userData.setUsername("lucy" + i);
            list.add(userData);
        }

        // configure the path and file name
        String filename = "C:\\workspace\\atguigu\\yygh_parent\\_easyexcel\\01.xlsx";
        // method invocation
        EasyExcel.write(filename, UserData.class)
            .sheet("用户信息")
            .doWrite(list);
    }
}
