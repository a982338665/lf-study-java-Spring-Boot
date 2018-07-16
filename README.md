# Spring-Boot


微服务-springcloud:为开发人员提供了快速构建分布式系统的一些工具:

    配置管理、
    服务发现、
    断路器、
    路由、
    微代理、
    事件总线、
    全局锁、
    决策竞选、
    分布式会话
    ++其他

**1.服务注册中心-Eureka** 

    ·服务注册和发现模块
    1.eureka-server创建：
        右键工程->
        创建model-> 
        选择spring initialir->
        选择cloud discovery->
        eureka server->完成

        --向EurekaServerApplication添加注解@EnableEurekaServer表明是一个eureka服务
        --添加配置文件:application.yml
    --------------------
    2.eureka-client-01创建：同上
        --注解@EnableEurekaClient 表明自己是一个eurekaclient.
        --配置文件中注明自己的服务注册中心的地址，application.yml配置文件
        --打开http://localhost:8761 ，即eureka server 的网址,可以看见注册的服务
        --打开http://localhost:8762/hi?name=forezp，返回接口访问内容
        
**2.ribbon+restTemplate-服务消费者**

    1.在微服务架构中，业务都会被拆分成一个独立的服务，服务与服务的通讯是基于http restful的。
      Spring cloud有两种服务调用方式，一种是ribbon+restTemplate，另一种是feign。
    2.ribbon是一个负载均衡客户端，可以很好的控制htt和tcp的一些行为。Feign默认集成了ribbon。
    ——————————————————————
    1.基于以上项目复制eureka-client-01创建eureka-client-02并修改yml中端口为8763并启动
    2.spring.application.name不修改，仍为eureka-client-01：
      由于服务之间是根据此名称进行相互调用，所以此时表示
      8763,8762对外提供一个服务，服务名为eureka-client-01=====>等同于一个小的集群
    ——————————————————————
    3.建一个服务消费者：service-ribbon
        --1.启动类中添加注解和方法：
            1.@EnableDiscoveryClient
            2.通过@LoadBalanced注解表明这个restRemplate开启负载均衡的功能
              @Bean
              @LoadBalanced
              RestTemplate restTemplate() {
                return new RestTemplate();
              }
        --2.编写测试类service和controller：主要调用是因为：service中的RestTemplate
        --3.浏览器上多次访问http://localhost:8764/hi?name=forezp，浏览器交替显示：
            ·hi forezp,i am from port:8762
            ·hi forezp,i am from port:8763
        --4.这说明当我们通过调用restTemplate.getForObject(“http://SERVICE-HI/hi?name=“+name,String.class)
            方法时,已经做了负载均衡，访问了不同的端口的服务实例
    4.场景总结：
        -1.一个服务注册中心，eureka-server,端口为8761
        -2.向服务注册中心eureka-server注册两个客户端服务：名称为eureka-client-01包含服务：8762/8763
        -3.向服务注册中心eureka-server注册消费者服务：sercvice-ribbon端口为8764,
        -4.当sercvice-ribbon通过restTemplate调用eureka-client-01的hi接口时，
           因为用ribbon进行了负载均衡，会轮流的调用eureka-client-01：8762和8763 两个端口的hi接口；
            
**3.Feign-服务消费者**       

    1.Feign是一个声明式的伪Http客户端，它使得写Http客户端变得更简单。
      使用Feign，只需要创建一个接口并注解。它具有可插拔的注解特性，可使用Feign 注解和JAX-RS注解。
      Feign支持可插拔的编码器和解码器。Feign默认集成了Ribbon，并和Eureka结合，默认实现了负载均衡的效果
    2.简单理解：
        ·Feign 采用的是基于接口的注解
        ·Feign 整合了ribbon   
    ——————————————————————
    3.建一个服务消费者：serice-feign 
        1.yml指定程序名为service-feign，端口号为8765
        2.启动类加上@EnableFeignClients注解开启Feign的功能（@EnableDiscoveryClient@EnableFeignClients）
          注意：
            ·@EnableFeignClients在高版本中不在原来位置，需要引入新位置，在pom中有体现
        3.编写测试类service和controller：
          ·feign基于接口-创建serviceTest
          
**4.断路器（Hystrix）**    

    1.微服务架构中，根据业务来拆分成一个个的服务，服务与服务之间可以相互调用（RPC）
        在Spring Cloud可以用RestTemplate+Ribbon和Feign来调用。为了保证其高可用，
        单个服务通常会集群部署。由于网络原因或者自身的原因，服务并不能保证100%可用，
        如果单个服务出现问题，调用这个服务就会出现线程阻塞，此时若有大量的请求涌入，
        Servlet容器的线程资源会被消耗完毕，导致服务瘫痪。服务与服务之间的依赖性，
        故障会传播，会对整个微服务系统造成灾难性的严重后果，这就是服务故障的“雪崩”效应。
        为了解决这个问题，业界提出了断路器模型。
    2.Netflix开源了Hystrix组件，实现了断路器模式，SpringCloud对这一组件进行了整合。 
        在微服务架构中，一个请求需要调用多个服务是非常常见的，较底层的服务如果出现故障，
        会导致连锁故障。当对特定的服务的调用的不可用达到一个阀值（Hystric 是5秒20次） 
        断路器将会被打开。断路打开后，可用避免连锁故障，fallback方法可以直接返回一个固定值
    3.ribbon使用断路器：
        1.添加断路器依赖：spring-cloud-starter-hystrix
        2.启动类加注解@EnableHystrix开启Hystrix（@EnableDiscoveryClient和@EnableHystrix）
        3.改造HelloService类，在hiService方法上加上@HystrixCommand注解
          注意：
          该注解对该方法创建了熔断器的功能，并指定了fallbackMethod熔断方法，
          熔断方法直接返回了一个字符串，字符串为”hi,”+name+”,sorry,error!”，
        4.断路测试：
          ·启动ribben访问
          ·关闭eureka-client-01，再访问，会得到短路由返回值
          ·eureka-client-01工程不可用的时候，service-ribbon调用 service-hi的API接口时，
           会执行快速失败，直接返回一组字符串，而不是等待响应超时，这很好的控制了容器的线程阻塞
    4.Feign中使用断路器：
        1.Feign是自带断路器的，在D版本的Spring Cloud中，它没有默认打开。
          需要在配置文件中配置打开：feign.hystrix.enabled=true
        2.在@FeignClient接口的注解中加上fallback的指定类helloServiceHystric
        3.helloServiceHystric需要实现helloService 接口，并注入到Ioc容器中（组件注解）
        4.断路测试-同上
    5.Hystrix Dashboard (断路器：Hystrix 仪表盘)：
        --仪表盘添加+ribbon和feign相同
        --以ribbon为例：
        1.添加依赖——
        2.主程序启动类中加入@EnableHystrixDashboard注解，开启hystrixDashboard
        3.访问http://localhost:8764/hystrix
          第一个文本框输入http://localhost:8764/hystrix.stream
        4.点击monitor stream，进入下一个界面
        5.访问：http://localhost:8764/hi?name=forezp，查看监控界面
       
**微服务监控聚合参考：**
**https://blog.csdn.net/jrn1012/article/details/77837744**