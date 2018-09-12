package pers.li.web;

import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pers.li.DubboService;

/**
 * @author:luofeng
 * @createTime : 2018/9/12 11:57
 */
@RestController
public class dubboWeb {

    @Reference(version = "1.0",application = "")
    private DubboService service;

    @RequestMapping("/test")
    public String test(String name){
        return service.test(name);
    }
}
