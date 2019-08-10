package cn.edu.njnu;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableEurekaClient
@MapperScan("cn.edu.njnu.dao")
@EnableCaching
@EnableFeignClients
@EnableTransactionManagement
public class FactoryServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(FactoryServerApplication.class, args);
    }

}
