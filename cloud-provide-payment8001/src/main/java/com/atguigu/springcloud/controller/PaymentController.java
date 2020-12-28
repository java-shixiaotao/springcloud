package com.atguigu.springcloud.controller;

import com.atguigu.springcloud.entity.CommonResult;
import com.atguigu.springcloud.entity.Payment;
import com.atguigu.springcloud.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@RestController
@Slf4j
public class PaymentController {
    @Resource
    private PaymentService paymentService;
    @Value("${server.port}")
    private String serverPort;
    @Resource
    private DiscoveryClient discoveryClient;

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
        log.info("查询结果===" + paymentById+ "ffff,serverPort" + serverPort);
        CommonResult<Payment> result = new CommonResult<>();
        if (Objects.nonNull(paymentById)) {
            result.setCode(200);
            result.setMessage("查询成功,serverPort" + serverPort);
            result.setData(paymentById);
        }else {
            result.setCode(500);
            result.setMessage("查询失败,serverPort" + serverPort);
        }
        return result;
    }

    @GetMapping(value = "/payment/discovery")
    public Object discovery(){
        List<String> services = discoveryClient.getServices();
        for (String service : services){
        log.info("******element" + service);
        }

        List<ServiceInstance> instances = discoveryClient.getInstances("cloud-payment-service");
        for (ServiceInstance instance: instances) {
            log.info(instance.getServiceId()+ "\t" + instance.getHost() + "\t" + instance.getPort() + "\t" + instance.getUri());
        }

        return discoveryClient;
    }


    @GetMapping(value = "/payment/lb")
    public String getPaymentLB(){
        return serverPort;
    }


    @GetMapping(value = "/payment/feign/timeout")
    public String paymentFeignTimeout(){
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return serverPort;
    }
}
