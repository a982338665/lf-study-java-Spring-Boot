package pers.li.servicefeign.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import pers.li.servicefeign.service.impl.helloServiceHystric;

/**
 * create by lishengbo on 2018-07-16 16:11
 */
@FeignClient(value = "eureka-client-01",fallback = helloServiceHystric.class)
public interface helloService {

    @RequestMapping(value = "/hi",method = RequestMethod.GET)
    String sayHiFromClientOne(@RequestParam(value = "name") String name);

}
