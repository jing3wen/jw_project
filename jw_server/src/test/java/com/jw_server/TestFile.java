package com.jw_server;

import java.io.File;

/**
 * Description:
 * Author: jingwen
 * DATE: 2022/9/14 9:33
 */
public class TestFile {

    public static void main(String[] args) {

        File oldFile = new File("E:\\IDEA\\project\\jw_project\\static\\upload\\deep_learning\\face_detect\\face_database\\defined_face\\lixian_2.png");
        File newFile = new File("E:\\IDEA\\project\\jw_project\\static\\upload\\deep_learning\\face_detect\\face_database\\undefined_face\\lixian_2.png");

        oldFile.renameTo(newFile);

//        System.out.println(FileNameUtil.getName("/static/upload/deep_learning/face_detect/face_database/123/123.jpg"));

    }
}
