package com.jw_server.core.utils;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.fill.Column;
import com.baomidou.mybatisplus.generator.fill.Property;

import java.util.Collections;

/**
 * @author jingwen
 * @Description mybatis-plus代码生成器
 * @DATE 2022/8/17 8:53
 */
public class CodeGenerator {

    private static void generateCode(){
        FastAutoGenerator.create("jdbc:mysql://localhost:3306/jw_project?serverTimezone=GMT%2b8", "你的用户名", "你的密码")
                .globalConfig(builder -> {
                    builder.author("jingwen") // 设置作者
                            .enableSwagger() // 开启 swagger 模式
                            .fileOverride() // 覆盖已生成文件
                            .outputDir("E:\\IDEA\\project\\jw_project\\jw_server\\src\\main\\java")  // 指定输出目录
                            .commentDate("yyyy-MM-dd HH:mm:ss");   //注释时间格式
                })
                .packageConfig(builder -> {
                    builder.parent("com.jw_server") // 设置父包名
                            .moduleName(null) // 设置父包模块名
                            .controller("controller.deeplearning")
                            .entity("dao.deeplearning.entity")
                            .mapper("dao.deeplearning.mapper")
                            .service("service.deeplearning")
                            .serviceImpl("service.deeplearning.impl")
                            .pathInfo(Collections.singletonMap(OutputFile.mapperXml, "E:\\IDEA\\project\\jw_project\\jw_server\\src\\main\\resources\\mapper\\deeplearning\\")); // 设置mapperXml生成路径
                }).
                strategyConfig(builder -> {
                    builder.controllerBuilder()
                            .enableRestStyle();  //开启生成@RestController 控制
                    builder.entityBuilder()
                            .enableLombok()  //entity类添加@lombok
                            .addTableFills(new Column("create_by", FieldFill.INSERT))
                            .addTableFills(new Column("create_time", FieldFill.INSERT))
                            .addTableFills(new Property("update_by", FieldFill.INSERT_UPDATE))
                            .addTableFills(new Property("update_time", FieldFill.INSERT_UPDATE))
                            ;
                    builder.mapperBuilder()
                            .enableMapperAnnotation();  //mapper添加@Mapper
                    builder.addInclude("dl_face_detect_file");// 设置需要生成的表名

//                            .addTablePrefix("t_", "sys_"); // 设置过滤表前缀
                })
                .execute();
    }




    public static void main(String[] args) {
        generateCode();
    }
}
