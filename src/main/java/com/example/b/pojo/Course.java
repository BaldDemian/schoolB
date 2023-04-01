package com.example.b.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("COURSE")
@XStreamAlias("课程")
public class Course {
    @TableId(value = "编号")
    @XStreamAlias("编号")
    private String cno;
    @TableField(value = "名称")
    @XStreamAlias("名称")
    private String cName;
    @TableField(value = "课时")
    @XStreamAlias("课时")
    private String time;
    @TableField(value = "学分")
    @XStreamAlias("学分")
    private String points;
    @TableField(value = "老师")
    @XStreamAlias("老师")
    private String teacher;
    @TableField(value = "地点")
    @XStreamAlias("地点")
    private String place;
    @TableField(value = "共享")
    @XStreamAlias("共享")
    private String shared;
}
