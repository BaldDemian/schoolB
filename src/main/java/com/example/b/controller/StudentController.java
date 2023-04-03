package com.example.b.controller;

import com.example.b.mapper.StudentMapper;
import com.example.b.pojo.Student;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class StudentController {
    XStream xStream = new XStream(new StaxDriver());
    @Autowired
    private StudentMapper studentMapper;

    @GetMapping("/students")
    public String getAllStudents() {
        xStream.processAnnotations(Student.class);
        List<Student> students = studentMapper.selectList(null);
        System.out.println(students.size());
        return xStream.toXML(students);
    }

    @GetMapping("/students/get_students_count")
    public String getStudentCnt() {
        xStream.processAnnotations(Student.class);
        return xStream.toXML(studentMapper.selectList(null).size());
    }

    @PostMapping("/students/add")
    public void addStudent(@RequestBody String studentXML) {
        xStream.processAnnotations(Student.class);
        Student student = (Student) xStream.fromXML(studentXML);
        studentMapper.insert(student);
    }

    @PostMapping("/students/delete")
    public void deleteStudent(@RequestBody String studentXML) {
        xStream.processAnnotations(Student.class);
        Student student = (Student) xStream.fromXML(studentXML);
        studentMapper.deleteById(student.getName());
    }

    @PostMapping("/students/update")
    public void updateStudent(@RequestBody String studentXml) {
        xStream.processAnnotations(Student.class);
        Student student = (Student) xStream.fromXML(studentXml);
        studentMapper.updateById(student); //todo
    }
}
