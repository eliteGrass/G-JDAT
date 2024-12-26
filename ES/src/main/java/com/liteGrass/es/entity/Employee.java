package com.liteGrass.es.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * @Description 人员信息
 * @Author liteGrass
 * @Date 2024/12/16 22:07
 */
@Data
@AllArgsConstructor
@Document(indexName = "employee")
public class Employee {
    @Id
    private Long id;
    @Field(type = FieldType.Keyword)
    private String name;
    @Field(type = FieldType.Integer)
    private int sex;
    @Field(type = FieldType.Integer, index = false)
    private int age;
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String address;

    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String remark;

}
