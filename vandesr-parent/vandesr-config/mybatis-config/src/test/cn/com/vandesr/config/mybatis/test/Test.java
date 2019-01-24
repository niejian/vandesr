package cn.com.vandesr.config.mybatis.test;

import cn.com.vandesr.config.mybatis.entity.DemoUser;
import cn.com.vandesr.config.mybatis.mapper.UserMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @author: nj
 * @date: 2019/1/21:下午2:09
 */
@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class Test {

    @Autowired
    private UserMapper userMapper;

    @org.junit.Test
    public void getUser() {
        QueryWrapper<DemoUser> queryWrapper = new QueryWrapper<>();
        List<DemoUser> demoUsers = this.userMapper.selectList(queryWrapper);
        log.info(demoUsers.toString());
    }
}


