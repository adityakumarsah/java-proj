package com.example.service;

import com.example.model.Payment;
import com.example.model.Student;
import java.math.BigDecimal;

public interface FeeService {
    Payment processPayment(Student student, BigDecimal amount);
    Payment processRefund(Student student, BigDecimal amount);
    BigDecimal getBalance(Student student);
}