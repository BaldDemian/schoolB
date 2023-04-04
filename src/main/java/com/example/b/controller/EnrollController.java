package com.example.b.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.b.mapper.CourseMapper;
import com.example.b.mapper.EnrollMapper;
import com.example.b.mapper.StudentMapper;
import com.example.b.pojo.Course;
import com.example.b.pojo.Enroll;
import com.example.b.pojo.Resp;
import com.example.b.pojo.Student;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@RestController
public class EnrollController {
    XStream xStream = new XStream(new StaxDriver());
    @Autowired
    public RestTemplate restTemplate;
    @Autowired
    private EnrollMapper enrollMapper;
    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private CourseController courseController;

    @GetMapping("/courses_selection")
    public String getAllCoursesSelectionTable(){
        xStream.processAnnotations(Enroll.class);
        return xStream.toXML(enrollMapper.selectList(null));
    }

    @GetMapping("/courses_selection/add")
    public String addCoursesSelectionTable(@RequestParam String courses_selectionXml){
        System.out.println("frontend return: " + courses_selectionXml);
        xStream.processAnnotations(Enroll.class);
        Enroll enroll = (Enroll) xStream.fromXML(courses_selectionXml);
        System.out.println(enroll);
        // 判断课程号是否以2开头，如果以2开头，表示是本院系的课，直接添加一条选课记录
        String cno = enroll.getCno();
        if (cno.charAt(0) == '2') {
            enrollMapper.insert(enroll);
            // 返回结果至前端
            xStream.processAnnotations(Resp.class);
            Resp resp = new Resp("选课成功");
            return xStream.toXML(resp);
        } else {
            // 不是以2开头，向集成服务器发送选课请求
            String url = "http://localhost:8081/AaskShare?courseXml={value}&studentXml={value}&from={value}&to={value}";
            Course course = new Course();
            course.setCno(cno); // course中仅含课程编号
            Student student = studentMapper.selectById(enroll.getSno());
            xStream.processAnnotations(Course.class);
            String courseXML = xStream.toXML(course);
            xStream.processAnnotations(Student.class);
            String studentXML = xStream.toXML(student);
            String from = "B";
            String to = "";
            if (cno.charAt(0) == '1') {
                // 请求共享A的课
                to = "A";
            } else {
                to = "C";
            }
            String resp = restTemplate.getForObject(url, String.class, courseXML, studentXML, from, to);
            if (!resp.contains("<null>")) {
                // 选课成功
                Resp r = new Resp("选课成功");
                xStream.processAnnotations(Resp.class);
                return xStream.toXML(r);
            } else {
                // 选课失败，表明目标课程不允许共享，向前端返回结果
                xStream.processAnnotations(Resp.class);
                Resp r = new Resp("选课失败，目标课程不允许共享");
                return xStream.toXML(r);
            }
            //return null;
        }
    }
    @GetMapping("/courses/selection/handle_share_request")
    public String handleShareRequest(@RequestParam String course_selectionXml, @RequestParam String studentXml) {
        // 检查目标课程是否支持共享
        System.out.println(course_selectionXml);
        System.out.println(studentXml);
        xStream.processAnnotations(Enroll.class);
        Enroll enroll = (Enroll) xStream.fromXML(course_selectionXml);
        System.out.println(enroll);
        Course course = courseMapper.selectById(enroll.getCno());
        System.out.println(course);
        if (course.getShared().equals("0")) {
            // 不支持共享
            Resp resp = new Resp("课程不支持共享");
            xStream.processAnnotations(Resp.class);
            return xStream.toXML(resp);
        } else {
            // 可以共享，将学生和对应选课记录插入本地数据库
            xStream.processAnnotations(Student.class);
            Student student = (Student) xStream.fromXML(studentXml);
            studentMapper.insert(student);
            enrollMapper.insert(enroll);
            xStream.processAnnotations(Resp.class);
            Resp resp = new Resp("选课成功");
            return xStream.toXML(resp);
        }
    }

    @PostMapping("/courses_selection/delete")
    public void deleteCoursesSelectionTable(@RequestBody String courses_selectionXml){
        xStream.processAnnotations(Enroll.class);
        Enroll enroll = (Enroll) xStream.fromXML(courses_selectionXml);
        enrollMapper.deleteByCnoSno(enroll.getCno(), enroll.getSno());
    }

    @PostMapping("/courses_selection/update")
    public void updateCoursesSelectionTable(@RequestBody String courses_selectionXml){
        xStream.processAnnotations(Enroll.class);
        Enroll enroll = (Enroll) xStream.fromXML(courses_selectionXml);
        enrollMapper.updateById(enroll);
    }

    @PostMapping("/courses_selection/find_by_sno")
    public String findEnrollBySno(@RequestParam String studentXML) {
        System.out.println(studentXML);
        xStream.processAnnotations(Student.class);
        Student student = (Student) xStream.fromXML(studentXML);
        QueryWrapper<Enroll> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("学号", student.getSno());
        List<Enroll> enrollList = enrollMapper.selectList(queryWrapper);
        // 查出对应的Course
        List<Course> courseList = new ArrayList<>();
        for (Enroll enroll: enrollList) {
            String cno = enroll.getCno();
            Course course = courseMapper.selectById(cno);
            courseList.add(course);
        }
        xStream.processAnnotations(Course.class);
        return xStream.toXML(courseList);

    }
}
