package com.example.b.controller;

import com.example.b.mapper.CourseMapper;
import com.example.b.pojo.Course;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CourseController {
    XStream xStream = new XStream(new StaxDriver());

    @Autowired
    private CourseMapper courseMapper;

    @GetMapping("/courses")
    public String getAllCourses(){
        xStream.processAnnotations(Course.class);
        return xStream.toXML(courseMapper.selectList(null));
    }

    @PostMapping("/courses/add")
    public void addCourse(@RequestBody String courseXml){
        xStream.processAnnotations(Course.class);
        Course course = (Course) xStream.fromXML(courseXml);
        courseMapper.insert(course);
    }

    @PostMapping("/courses/delete")
    public void deleteCourse(@RequestBody String courseXml){
        xStream.processAnnotations(Course.class);
        Course course = (Course) xStream.fromXML(courseXml);
        courseMapper.deleteById(course.getCno());
    }

    @PostMapping("/courses/update")
    public void updateCourse(@RequestBody String courseXml){
        xStream.processAnnotations(Course.class);
        Course course = (Course) xStream.fromXML(courseXml);
        courseMapper.updateById(course);
    }

    @GetMapping("/courses/searchByCno")
    public String searchByCno(String courseXml){
        xStream.processAnnotations(Course.class);
        Course course = (Course) xStream.fromXML(courseXml);
        return xStream.toXML(courseMapper.findByCno(course.getCno()));
    }

    @PostMapping("/courses/sendSharedCourse")
    public String sendSharedCourse(@RequestBody String courseXml){
        xStream.processAnnotations(Course.class);
        Course sharedCourse = (Course) xStream.fromXML(courseXml);
        sharedCourse.setShared("1");
        courseMapper.updateById(sharedCourse);
        return xStream.toXML(courseMapper.findByCno(sharedCourse.getCno()));
    }

    @PostMapping("/courses/receiveSharedCourse")
    public void receiveSharedCourse(@RequestBody String courseXml){
        addCourse(courseXml);
    }
}
