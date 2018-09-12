package pers.li;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author:luofeng
 * @createTime : 2018/9/11 14:04
 */
@RestController
public class TestWeb {


    /**拆分service服务：jar包格式*/
    @Resource
    private TestService service;

    @RequestMapping("/test")
    public String  test(String name){
        String  test= service.test(name);
        return test;
    }


}
