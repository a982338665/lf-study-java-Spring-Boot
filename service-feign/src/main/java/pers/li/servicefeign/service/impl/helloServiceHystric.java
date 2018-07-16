package pers.li.servicefeign.service.impl;

import org.springframework.stereotype.Component;
import pers.li.servicefeign.service.helloService;

/**
 * create by lishengbo on 2018-07-16 17:05
 */
@Component
public class helloServiceHystric implements helloService  {
    @Override
    public String sayHiFromClientOne(String name) {
       return  "sorry "+name;
    }
}
