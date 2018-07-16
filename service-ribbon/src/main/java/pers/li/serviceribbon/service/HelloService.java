package pers.li.serviceribbon.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class HelloService {

    @Autowired
    RestTemplate restTemplate;

    //断路器注解
    @HystrixCommand(fallbackMethod = "hiError")
    public String hiService(String name) {
        //使用注册到Eureka服务中心的客户端，由客户端分配具体调用哪个服务
        return restTemplate.getForObject("http://EUREKA-CLIENT-01/hi?name="+name,String.class);
    }

    public String hiError(String name) {
        return "hi,"+name+",sorry,error!";
    }

}