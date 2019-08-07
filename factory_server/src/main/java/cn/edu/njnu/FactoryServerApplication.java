package cn.edu.njnu;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
@MapperScan("cn.edu.njnu.dao")
public class FactoryServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(FactoryServerApplication.class, args);
    }

}
