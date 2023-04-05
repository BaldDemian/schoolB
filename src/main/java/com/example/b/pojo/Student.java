package com.example.b.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("STUDENT")
@XStreamAlias("学生")
public class Student {
    @TableId(value = "学号")
    @XStreamAlias("学号")
    private String sno;
    @TableField(value = "姓名")
    @XStreamAlias("姓名")
    private String name;
    @TableField(value = "性别")
    @XStreamAlias("性别")
    private String sex;
    @TableField(value = "专业")
    @XStreamAlias("专业")
    private String dept;
    @TableField(value = "密码")
    @XStreamAlias("密码")
    private String password;
}
