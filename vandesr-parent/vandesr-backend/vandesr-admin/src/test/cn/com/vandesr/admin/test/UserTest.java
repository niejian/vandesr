package cn.com.vandesr.admin.test;

import cn.com.vandesr.admin.entity.DemoUser;
import cn.com.vandesr.admin.entity.VandesrUser;
import cn.com.vandesr.admin.service.IVandesrUserService;
import cn.com.vandesr.admin.service.UserService;
import cn.com.vandesr.admin.vo.MenuVo;
import cn.com.vandesr.backend.config.security.JwtUser;
import cn.com.vandesr.backend.config.security.TokenUtil;
import cn.com.vandesr.backend.config.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.codec.digest.Md5Crypt;
import org.apache.tomcat.util.security.MD5Encoder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

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
//    @Autowired
//    private RedisService redisService;
    @Autowired
    private IVandesrUserService vandesrUserService;

    @Autowired
    private TokenUtil tokenUtil;

//    public static void main(String[] args) {
//        String str = "qq123123";
//        System.out.println(str.getBytes().length);
//        System.out.println(DigestUtils.md5DigestAsHex(str.getBytes()));
//    }

    @Ignore
    @Test
    public void addUser() throws Exception{
//        DemoUser user = new DemoUser();
//        user.setCreateDate(new Date());
//        user.setUserPassword(passwordEncoder.encode("qq123123"));
//        user.setUserName("niejian");
//        user.setUserId("100100113");
//        this.userService.addUser(user);
        String password = DigestUtils.md5DigestAsHex("qq123123".getBytes());
        this.vandesrUserService.addUser("code4fun", "code4fun@qq.com", password);
    }


    @Ignore
    @Test
    public void getUserClaim() {
        String token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJjb2RlNGZ1bkBxcS5jb20iLCJwYXNzd29yZCI6IiQyYSQxMCRhU0tmNVRlc01vMWVMaW9HRW9mTDF1TjdjaURCM3BaTThKZUVBSE1ZWm8vTUlJNE94c1dtVyIsImV4cCI6MTU2MzAwMzE2OCwiYXV0aCI6W10sImNyZWF0ZWQiOjE1NjI5MTY3Njg1NDN9.lK08DUaFKfJMRXJhKlrz968gEYpRRw9DwUDZ-OwNNqoegGri6OoqYsS50o701PNKJuzrMhFtEHzQHXAKjKm7pg";
        JwtUser userDetails = (JwtUser) this.tokenUtil.getUserDetails(token);
        System.out.println(userDetails.toString());

    }



    @Test
    public void getUserMenuTree() throws Exception{
        List<MenuVo> menuVoList = this.vandesrUserService.getUserMenuByUserId(1);
        log.info("==========");
        log.info(JSONArray.fromObject(menuVoList).toString());
        log.info("==========");

    }
}
