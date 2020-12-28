package com.atguigu.springcloud.lb.impl;

import com.atguigu.springcloud.lb.LoadBalancer;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class MyLoadBalancer implements LoadBalancer {


    private AtomicInteger atomicInteger = new AtomicInteger(0);

    public final int getAndIncrement() {
        int current;
        int next;
        do {
            current = this.atomicInteger.get();
            next = current > 2147483647 ? 0 : current + 1;
        } while (!this.atomicInteger.compareAndSet(current, next));
        System.out.println("------------------当前是第"+next+"次请求");
        return next;
    }


    @Override
    public ServiceInstance instances(List<ServiceInstance> serviceInstanceList) {
        int i = getAndIncrement() % serviceInstanceList.size();
        ServiceInstance serviceInstance = serviceInstanceList.get(i);
        return serviceInstance;
    }
}
