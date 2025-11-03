package com.example.service.impl;

import com.example.dao.StudentDAO;
import com.example.model.Payment;
import com.example.model.Student;
import com.example.service.FeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class FeeServiceImpl implements FeeService {

    private final StudentDAO studentDAO;

    @Autowired
    public FeeServiceImpl(StudentDAO studentDAO) {
        this.studentDAO = studentDAO;
    }

    @Override
    @Transactional
    public Payment processPayment(Student student, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Payment amount must be positive");
        }

        student.setBalance(student.getBalance().add(amount));
        studentDAO.update(student);

        return new Payment(student, amount, Payment.PaymentType.PAYMENT);
    }

    @Override
    @Transactional
    public Payment processRefund(Student student, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Refund amount must be positive");
        }

        if (student.getBalance().compareTo(amount) < 0) {
            throw new IllegalStateException("Insufficient balance for refund");
        }

        student.setBalance(student.getBalance().subtract(amount));
        studentDAO.update(student);

        return new Payment(student, amount, Payment.PaymentType.REFUND);
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getBalance(Student student) {
        return student.getBalance();
    }
}