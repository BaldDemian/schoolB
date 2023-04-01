package com.example.b.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.b.pojo.Course;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CourseMapper extends BaseMapper<Course> {
    @Select("select * from \"COURSE\"")
    List<Course> findAllCourses();
    @Select("select * from \"COURSE\" where 编号 = #{cno}")
    Course findByCno(@Param("cno") String cno);
}
