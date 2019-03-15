package cn.com.vandesr.admin;

import cn.com.vandesr.backend.config.BackendCommonConfig;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author: nj
 * @date: 2019/1/24:下午2:35
 */
@Slf4j

@EnableEurekaClient
@SpringBootApplication
@EnableTransactionManagement
@MapperScan("cn.com.vandesr.admin.mapper")

@ComponentScan({
        "cn.com.vandesr.admin",
        "cn.com.vandesr.backend.config"
})

public class AdminBootstrap {
    public static void main(String[] args) {
        log.info("admin 启动");
        try {
            SpringApplication.run(AdminBootstrap.class, args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
