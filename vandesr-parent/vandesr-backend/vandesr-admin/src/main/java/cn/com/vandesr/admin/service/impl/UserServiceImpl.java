package cn.com.vandesr.admin.service.impl;

import cn.com.vandesr.admin.entity.DemoUser;
import cn.com.vandesr.admin.mapper.UserMapper;
import cn.com.vandesr.admin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author: nj
 * @date: 2019/1/24:下午2:42
 */
@Service(value = "userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Override
    public void addUser(DemoUser demoUser) {
        userMapper.insert(demoUser);

    }
}
