package com.verge.parking;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.Collections;

public class CodeGenerator {
    public static void main(String[] args) {
        FastAutoGenerator.create("jdbc:mysql://localhost:3306/smart_parking", "root", "123456")
                .globalConfig(builder -> {
                    builder.author("Verge") // 设置作者
                            .outputDir("C:\\Users\\Verge\\workspace"); // 指定输出目录
                })
                .packageConfig(builder -> {
                    builder.parent("com.verge.parking") // 设置父包名
                            .pathInfo(Collections.singletonMap(OutputFile.xml, "C:\\Users\\Verge\\workspace")); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder.addInclude("parking_order", "parking_place", "user");
                })
                .templateEngine(new FreemarkerTemplateEngine())
                .execute();
    }
}
