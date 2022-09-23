package com.jw_server.dao.deeplearning.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Description 人脸库
 * Author jingwen
 * Date 2022-09-13 17:21:26
 **/
@Data
@TableName("dl_face_database")
@ApiModel(value = "DlFaceDatabase对象", description = "人脸库")
public class DlFaceDatabase implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("父id")
    private Integer parentId;

    @ApiModelProperty("人脸名")
    private String faceName;

    @ApiModelProperty("人脸性别")
    private String faceSex;

    @ApiModelProperty("人脸类别")
    private String faceType;

    @ApiModelProperty("图片名称")
    private String imageName;

    @ApiModelProperty("图片地址")
    private String imageAddress;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("状态(1正常 0停用)")
    private String status;

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
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    @ApiModelProperty("更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;


}
