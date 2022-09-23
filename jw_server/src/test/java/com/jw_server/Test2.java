package com.jw_server;

/**
 * Description:
 * Author: jingwen
 * DATE: 2022/8/30 12:31
 */
public class Test2 {

    public static void tryExceptions(){
        Integer i=0;
        if(i==0){
            throw new RuntimeException("第一层 i==0");
        }

    }

    public static void main(String[] args) {

        try {
            tryExceptions();
        }
//        catch(RuntimeException e){
//            throw e;
//        }
        catch(Exception e){
            throw new RuntimeException("第二层");
        }


    }
}
