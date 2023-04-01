package com.example.b.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("USER")
public class User {
    @TableId(value = "账户名")
    private String name;

    @TableField(value = "密码")
    private String password;

    @TableField(value = "级别")
    private Integer auth;

    @TableField(value = "客体")
    private String student; // 关联的学生学号，注意要先录入学生记录再录入对应的账户
}
