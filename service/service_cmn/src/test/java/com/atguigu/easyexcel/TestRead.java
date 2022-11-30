package com.atguigu.easyexcel;

import com.alibaba.excel.EasyExcel;

public class TestRead {
    public static void main(String[] args) {
        // configure the path and file name
        String filename = "C:\\workspace\\atguigu\\yygh_parent\\_easyexcel\\01.xlsx";
        // method invocation
        EasyExcel.read(filename, UserData.class, new ExcelListener()).sheet().doRead();
    }
}
