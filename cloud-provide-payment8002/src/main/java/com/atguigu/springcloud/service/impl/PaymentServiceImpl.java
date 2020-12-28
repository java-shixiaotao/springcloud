package com.atguigu.springcloud.service.impl;

import com.atguigu.springcloud.dao.PaymentDao;
import com.atguigu.springcloud.entity.Payment;
import com.atguigu.springcloud.service.PaymentService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Resource
    private PaymentDao paymentDao;

    @Override
    public int insert(Payment payment) {
        int insert = paymentDao.insert(payment);
        return insert;
    }

    @Override
    public Payment getPaymentById(Long id) {
        Payment payment = paymentDao.selectByPrimaryKey(id);
        return payment;
    }
}
