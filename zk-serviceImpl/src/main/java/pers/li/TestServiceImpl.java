package pers.li;

import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author:luofeng
 * @createTime : 2018/9/11 14:04
 */
@Service("testService")
public class TestServiceImpl implements TestService {


    @Override
    public String test(String name) {
        return Objects.isNull(name)+"";
    }

//    public static void main(String[] args) {
//        System.out.println(1111111);
//    }
}
