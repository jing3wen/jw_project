package com.jw_server.dao.deeplearning.dto;

import com.jw_server.core.common.MyPageDTO;
import lombok.Data;

/**
 * Description:
 * Author: jingwen
 * DATE: 2022/9/13 21:00
 */
@Data
public class QueryDlFaceDataDTO extends MyPageDTO {

    private String faceName;
}
