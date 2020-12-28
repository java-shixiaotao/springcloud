package com.atguigu.springcloud.controller;

import com.atguigu.springcloud.entity.CommonResult;
import com.atguigu.springcloud.entity.Payment;
import com.atguigu.springcloud.lb.impl.MyLoadBalancer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.net.URI;
import java.util.List;

@RestController
@Slf4j
public class OrderController {

    public static final String PAYMENT_URL = "http://CLOUD-PAYMENT-SERVICE";

    @Resource
    private RestTemplate restTemplate;
    @Resource
    private DiscoveryClient discoveryClient;
    @Resource
    private MyLoadBalancer myLoadBalancer;


    @GetMapping("/consumer/insertPayment")
    public CommonResult<Payment> create(Payment payment){
        return restTemplate.postForObject(PAYMENT_URL + "/insertPayment",payment,CommonResult.class);
    }



    @GetMapping(value = "/consumer/getPaymentById/getForObject/{id}")
    public CommonResult<Payment> getPaymentById(@PathVariable("id") Long id){
      return  restTemplate.getForObject(PAYMENT_URL + "/getPaymentById/" + id,CommonResult.class);
    }


    @GetMapping(value = "/consumer/getPaymentById/getForEntity/{id}")
    public CommonResult<Payment> getPaymentById2(@PathVariable("id") Long id){
        ResponseEntity<CommonResult> entity  = restTemplate.getForEntity(PAYMENT_URL + "/getPaymentById/" + id,CommonResult.class);
        if(entity.getStatusCode().is2xxSuccessful()){
            return entity.getBody();
        }else {
            return  new CommonResult<>(444,"操作失败");
        }
    }


    @GetMapping(value = "/consumer/payment/lb")
    public String getPaymentLB(){
        List<ServiceInstance> instances = discoveryClient.getInstances("CLOUD-PAYMENT-SERVICE");
        ServiceInstance instances1 = myLoadBalancer.instances(instances);
        URI uri = instances1.getUri();
        String forObject = restTemplate.getForObject(uri + "/payment/lb", String.class);
        return forObject;
    }



}
