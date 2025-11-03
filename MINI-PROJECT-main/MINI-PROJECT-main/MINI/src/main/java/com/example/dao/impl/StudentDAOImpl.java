package com.example.dao.impl;

import com.example.dao.StudentDAO;
import com.example.model.Student;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaQuery;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class StudentDAOImpl implements StudentDAO {

    private final SessionFactory sessionFactory;

    @Autowired
    public StudentDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    private Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public Student save(Student student) {
        getSession().save(student);
        return student;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Student> findById(Long id) {
        return Optional.ofNullable(getSession().get(Student.class, id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Student> findAll() {
        CriteriaQuery<Student> criteriaQuery = getSession().getCriteriaBuilder().createQuery(Student.class);
        criteriaQuery.from(Student.class);
        return getSession().createQuery(criteriaQuery).getResultList();
    }

    @Override
    public void update(Student student) {
        getSession().update(student);
    }

    @Override
    public void delete(Student student) {
        getSession().delete(student);
    }
}