package com.jw_server.dao.blog.entity;

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
 * Description 
 * Author jingwen
 * Date 2023-02-09 14:24:46
 **/
@Data
@TableName("blog_message")
@ApiModel(value = "BlogMessage对象", description = "")
public class BlogMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("留言ID")
    @TableId(value = "message_id", type = IdType.AUTO)
    private Integer messageId;

    @ApiModelProperty("昵称")
    private String messageNickname;

    @ApiModelProperty("留言地址")
    private String messageAvatar;

    @ApiModelProperty("留言邮箱")
    private String messageEmail;

    @ApiModelProperty("留言内容")
    private String messageContent;

    @ApiModelProperty("留言审核状态（1表示通过，0表示未审核）")
    private String messageCheck;

    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;


}
