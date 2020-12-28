package com.atguigu.springcloud.controller;

import com.atguigu.springcloud.entity.CommonResult;
import com.atguigu.springcloud.entity.Payment;
import com.atguigu.springcloud.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Objects;

@RestController
@Slf4j
public class PaymentController {
    @Resource
    private PaymentService paymentService;

    @Value("${server.port}")
    private String serverPort;

    @PostMapping("/insertPayment")
    public CommonResult<Payment> insertPayment(@RequestBody Payment payment){
        int insert = paymentService.insert(payment);
        log.info("插入结果===" + insert);
        CommonResult<Payment> result = new CommonResult<>();
        if (insert == 1) {
            result.setCode(200);
            result.setMessage("添加成功");
        }else {
            result.setCode(500);
            result.setMessage("添加失败");
        }
        return result;
    }



    @GetMapping(value = "/getPaymentById/{id}")
    public CommonResult<Payment> getPaymentById(@PathVariable("id") Long id){
        Payment paymentById = paymentService.getPaymentById(id);
        log.info("查询结果===" + paymentById+ "ffff" + "serverPort" + serverPort);
        CommonResult<Payment> result = new CommonResult<>();
        if (Objects.nonNull(paymentById)) {
            result.setCode(200);
            result.setMessage("查询成功" + "serverPort" + serverPort);
            result.setData(paymentById);
        }else {
            result.setCode(500);
            result.setMessage("查询失败 serverPort" + serverPort);
        }
        return result;
    }


    @GetMapping(value = "/payment/lb")
    public String getPaymentLB(){
        return serverPort;
    }

}
