package pers.li.impl;

import com.alibaba.dubbo.config.annotation.Service;
import pers.li.DubboService;

/**
 * @author:luofeng
 * @createTime : 2018/9/12 11:01
 */
//此处注解使用dubbo下的：创建此类型对象，作为服务提供者发布出去==原来的xml文件配置
@Service(version = "1.0",application = "${dubbo.application.id}",protocol = "${dubbo.protocol.id}",registry = "${dubbo.registry.id}",register = true)
public class DubboServiceImpl implements DubboService {

    @Override
    public String test(String name) {
        return "result:-->"+name;
    }
}
