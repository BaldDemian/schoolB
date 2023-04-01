package com.example.b.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("ENROLL")
@XStreamAlias("选课")
public class Enroll {
    @TableField(value = "学号")
    @XStreamAlias("学生编号")
    private String sno;

    @TableField(value = "课程编号")
    @XStreamAlias("课程编号")
    private String cno;

    @TableField(value = "得分")
    @XStreamAlias("得分")
    private String grade;
}
