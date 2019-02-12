package cn.com.vandesr.backend.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.*;

/**
 * 公共配置；类
 * @author: nj
 * @date: 2019/1/24:下午2:33
 */
@EnableAspectJAutoProxy
@EnableAutoConfiguration
@Configuration
@ComponentScan(basePackages = {
        "cn.com.vandesr.backend.config",
        "cn.com.vandesr.redis.config"
})
public class BackendCommonConfig {

}
