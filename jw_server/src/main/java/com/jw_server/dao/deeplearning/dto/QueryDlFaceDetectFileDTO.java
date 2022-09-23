package com.jw_server.dao.deeplearning.dto;

import com.jw_server.core.common.MyPageDTO;
import lombok.Data;

/**
 * Description: 检测文件查询
 * Author: jingwen
 * DATE: 2022/9/22 17:36
 */
@Data
public class QueryDlFaceDetectFileDTO extends MyPageDTO {

    private String fileName;

    private Integer detectStatus;
}
