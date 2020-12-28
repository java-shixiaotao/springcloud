package com.atguigu.springcloud.service;

import com.atguigu.springcloud.entity.Payment;

public interface PaymentService {

    int insert(Payment payment);

    Payment getPaymentById(Long id);
}
