package com.example.dao;

import com.example.model.Student;
import java.util.List;
import java.util.Optional;

public interface StudentDAO {
    Student save(Student student);
    Optional<Student> findById(Long id);
    List<Student> findAll();
    void update(Student student);
    void delete(Student student);
}