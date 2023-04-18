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

    @GetMapping("/courses_selection")
    public String getAllCoursesSelectionTable(){
        xStream.processAnnotations(Enroll.class);
        return xStream.toXML(enrollMapper.selectList(null));
    }

    @GetMapping("/courses_selection/add")
    public void addCoursesSelectionTable(@RequestParam String courses_selectionXml){
        System.out.println("frontend return: " + courses_selectionXml);
        xStream.processAnnotations(Enroll.class);
        Enroll enroll = (Enroll) xStream.fromXML(courses_selectionXml);
        // 判断课程号是否以2开头，如果以2开头，表示是本院系的课，直接添加一条选课记录
        String cno = enroll.getCno();
        if (cno.charAt(0) == '2') {
            QueryWrapper<Enroll> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("学号", enroll.getSno());
            queryWrapper.eq("课程编号", enroll.getCno());
            if (enrollMapper.selectOne(queryWrapper) != null) {
                return;
            }
            enrollMapper.insert(enroll);
        } else {
            // 不是以2开头，向集成服务器发送选课请求
            String url = "http://localhost:8081/integration/httpTest/?studentXml={value}&courses_selectionXml={value}&curr={value}&transTo={value}";
            Student student = studentMapper.selectById(enroll.getSno());
            xStream.processAnnotations(Student.class);
            String studentXML = xStream.toXML(student);
            String from = "b";
            String to = "";
            if (cno.charAt(0) == '1') {
                // 请求共享A的课
                to = "a";
            } else {
                to = "c";
            }
            String resp = restTemplate.getForObject(url, String.class, studentXML, courses_selectionXml, from, to);
        }
    }
    @GetMapping("/courses_selection/handle_share_request")
    public String handleShareRequest(@RequestParam String course_selectionXml, @RequestParam String studentXml) {
        // 检查目标课程是否支持共享
        xStream.processAnnotations(Enroll.class);
        Enroll enroll = (Enroll) xStream.fromXML(course_selectionXml);
        Course course = courseMapper.selectById(enroll.getCno());
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

    @GetMapping("/courses_selection/delete")
    public void deleteCoursesSelectionTable(@RequestParam String courses_selectionXml){
        xStream.processAnnotations(Enroll.class);
        Enroll enroll = (Enroll) xStream.fromXML(courses_selectionXml);
        // 校验课程号
        String cno = enroll.getCno();
        if (cno.charAt(0) == '2') {
            // 本院系的课，直接删除
            QueryWrapper<Enroll> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("课程编号", enroll.getCno());
            queryWrapper.eq("学号", enroll.getSno());
            enrollMapper.delete(queryWrapper);
            // 校验一下学号，如果不是本学院的学生，且该学生在本学院没有选课了，删除该学生
            String sno = enroll.getSno();
            if (sno.charAt(0) != '2') {
                // 不是本学院的学生
                // 查一下该学生在本学院是否还有选课记录
                QueryWrapper<Enroll> queryWrapper1 = new QueryWrapper<>();
                queryWrapper1.eq("学号", sno);
                List<Enroll> enrollList = enrollMapper.selectList(queryWrapper1);
                if (enrollList.size() == 0) {
                    studentMapper.deleteById(sno);
                }
            }
        } else {
            String url = "http://localhost:8081/integration/httpTestDelete/?studentXml={value}&courses_selectionXml={value}&curr={value}&transTo={value}";
            String from = "b";
            String to;
            if (cno.charAt(0) == '1') {
                to = "a";
            } else {
                to = "c";
            }
            Student student = studentMapper.selectById(enroll.getSno());
            xStream.processAnnotations(Student.class);
            String studentXML = xStream.toXML(student);
            String resp = restTemplate.getForObject(url, String.class, studentXML, courses_selectionXml, from, to);
        }
    }

    @GetMapping("/courses_selection/update")
    public void updateCoursesSelectionTable(@RequestParam String courses_selectionXml){
        xStream.processAnnotations(Enroll.class);
        Enroll enroll = (Enroll) xStream.fromXML(courses_selectionXml);
        enrollMapper.updateById(enroll);
    }

    @GetMapping("/courses_selection/find_by_sno")
    public String findEnrollBySno(@RequestParam String studentXml) {
        System.out.println(studentXml);
        xStream.processAnnotations(Student.class);
        Student student = (Student) xStream.fromXML(studentXml);
        QueryWrapper<Enroll> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("学号", student.getSno());
        List<Enroll> enrollList = enrollMapper.selectList(queryWrapper);
        List<Course> courseList = new ArrayList<>();
        for (Enroll enroll: enrollList) {
            String cno = enroll.getCno();
            Course course = courseMapper.selectById(cno);
            courseList.add(course);
        }
        xStream.processAnnotations(Course.class);
        return xStream.toXML(courseList);
    }

    @GetMapping("/courses_selection/searchBySno")
    public String searchBySno(@RequestParam String courses_selectionXml){
        xStream.processAnnotations(Enroll.class);
        Enroll enroll = (Enroll) xStream.fromXML(courses_selectionXml);
        System.out.println(enroll);
        QueryWrapper<Enroll> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("学号", enroll.getSno());
        List<Enroll> enrollList = enrollMapper.selectList(queryWrapper);
        System.out.println(enrollList);
        return xStream.toXML(enrollList);

    }
}
