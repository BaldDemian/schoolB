package com.example.b.controller;

import com.example.b.mapper.EnrollMapper;
import com.example.b.pojo.Enroll;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class EnrollController {
    XStream xStream = new XStream(new StaxDriver());
    @Autowired
    private EnrollMapper enrollMapper;

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
        enrollMapper.insert(enroll);
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
}
