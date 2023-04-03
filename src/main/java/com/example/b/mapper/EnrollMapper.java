package com.example.b.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.b.pojo.Enroll;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface EnrollMapper extends BaseMapper<Enroll> {
    @Delete("delete from ENROLL where 课程编号 = #{cno} and 学号 = #{sno}")
    // 删掉一条选课记录，(cno, sno)是联合主键
    void deleteByCnoSno(@Param("cno") String cno, @Param("sno") String sno);

    @Select("select * from ENROLL where 学号 = #{sno}")
    List<Enroll> findEnrollBySno(@Param("sno") String sno);
}
