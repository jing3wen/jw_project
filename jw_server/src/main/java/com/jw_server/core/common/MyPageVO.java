package com.jw_server.core.common;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.*;
import org.apache.poi.ss.formula.functions.T;

import java.util.List;

/**
 * Description: mybatis-plus分页数据二次封装
 * Author: jingwen
 * DATE: 2022/9/2 17:20
 */
@Data
public class MyPageVO<T> {

    private Long pages;

    private Long current;

    private Long size;

    private Long total;

    private List<T> records;

    public MyPageVO setMyPageVO(Long pages, Long current, Long size, Long total,List<T> records) {
        MyPageVO myPageVO = new MyPageVO();
        myPageVO.setPages(pages);
        myPageVO.setCurrent(current);
        myPageVO.setSize(size);
        myPageVO.setTotal(total);
        myPageVO.setRecords(records);
        return myPageVO;
    }


}
