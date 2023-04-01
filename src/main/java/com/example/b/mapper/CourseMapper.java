package com.example.b.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.b.pojo.Course;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CourseMapper extends BaseMapper<Course> {
}
