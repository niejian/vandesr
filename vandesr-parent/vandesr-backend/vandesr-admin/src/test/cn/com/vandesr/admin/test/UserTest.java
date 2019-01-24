package cn.com.vandesr.admin.test;

import cn.com.vandesr.admin.entity.DemoUser;
import cn.com.vandesr.admin.service.UserService;
import cn.com.vandesr.backend.config.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

/**
 * @author: nj
 * @date: 2019/1/24:下午2:40
 */
@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class UserTest {

    @Autowired
    private UserService userService;
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    @Autowired
    private RedisService redisService;

    @Ignore
    @Test
    public void addUser() {
        DemoUser user = new DemoUser();
        user.setCreateDate(new Date());
        user.setUserPassword(passwordEncoder.encode("qq123123"));
        user.setUserName("niejian");
        user.setUserId("100100113");
        this.userService.addUser(user);
    }

    @Ignore
    @Test
    public void getUser()  throws Exception{
        String user = (String) redisService.getValue("auth_user_info_100100113", String.class);
        JSONObject json = JSONObject.fromObject(user);
        log.info("---------------");
        log.info(json.toString());
    }
}
