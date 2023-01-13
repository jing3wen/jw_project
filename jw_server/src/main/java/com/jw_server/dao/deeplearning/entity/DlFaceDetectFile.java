package com.jw_server.dao.deeplearning.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Description 检测人脸文件表
 * Author jingwen
 * Date 2022-09-22 17:31:09
 **/
@Data
@TableName("dl_face_detect_file")
@ApiModel(value = "DlFaceDetectFile对象", description = "检测人脸文件表")
public class DlFaceDetectFile implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("检测文件ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("检测文件名")
    private String fileName;

    @ApiModelProperty("检测文件地址")
    private String fileAddress;

    @ApiModelProperty("检测文件类别")
    private String fileType;

    @ApiModelProperty("检测状态(0:未检测, 1:检测中, 2:检测完成, -1:检测失败)")
    private Integer detectStatus;

    @ApiModelProperty("是否保存检测结果(0:否, 1:是)")
    private String saveResult;

    @ApiModelProperty("检测结果文件地址")
    private String resultFileAddress;

    @ApiModelProperty("检测结果描述")
    private String resultMsg;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("是否删除(0代表存在 1代表删除)")
    private String isDeleted;

    @ApiModelProperty("创建者")
    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @ApiModelProperty("更新者")
    private String updateBy;

    @ApiModelProperty("更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;


}
