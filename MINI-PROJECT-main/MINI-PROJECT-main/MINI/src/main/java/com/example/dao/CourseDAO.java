package com.example.dao;

import com.example.model.Course;
import java.util.List;
import java.util.Optional;

public interface CourseDAO {
    Course save(Course course);
    Optional<Course> findById(Long id);
    List<Course> findAll();
    void update(Course course);
    void delete(Course course);
}