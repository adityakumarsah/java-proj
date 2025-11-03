package com.example.dao.impl;

import com.example.dao.CourseDAO;
import com.example.model.Course;
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
public class CourseDAOImpl implements CourseDAO {

    private final SessionFactory sessionFactory;

    @Autowired
    public CourseDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    private Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public Course save(Course course) {
        getSession().save(course);
        return course;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Course> findById(Long id) {
        return Optional.ofNullable(getSession().get(Course.class, id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Course> findAll() {
        CriteriaQuery<Course> criteriaQuery = getSession().getCriteriaBuilder().createQuery(Course.class);
        criteriaQuery.from(Course.class);
        return getSession().createQuery(criteriaQuery).getResultList();
    }

    @Override
    public void update(Course course) {
        getSession().update(course);
    }

    @Override
    public void delete(Course course) {
        getSession().delete(course);
    }
}