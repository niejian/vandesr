package cn.com.vandesr.admin.service.impl;

import cn.com.vandesr.admin.entity.VandesrUser;
import cn.com.vandesr.admin.mapper.VandesrUserMapper;
import cn.com.vandesr.admin.service.IVandesrUserService;
import cn.com.vandesr.backend.config.utils.SnowflakeIdWorker;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author code4fun
 * @since 2019-01-25
 */
@Service
public class VandesrUserServiceImpl extends ServiceImpl<VandesrUserMapper, VandesrUser> implements IVandesrUserService {


    @Autowired
    private PasswordEncoder passwordEncoder;

    SnowflakeIdWorker idWorker = new SnowflakeIdWorker(0, 0);


    /**
     * 注册
     *
     * @param email
     * @param password
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addUser(String userName, String email, String password) throws Exception {

        VandesrUser user = this.getUserByEmail(email);
        if (null != user) {
            throw new Exception("email账号已存在");
        }
        Date date = new Date();
        user = new VandesrUser();
        user.setCreateDate(date);
        user.setUpdateDate(date);
        user.setUserName(userName);
        user.setLoginName(email);
        password = passwordEncoder.encode(password);
        user.setPwd(password);

        user.setUserCode("U" + idWorker.nextId());
        user.setEmail(email);
        this.save(user);
    }

    /**
     * 判断邮箱是否唯一
     *
     * @param email
     * @return
     * @throws Exception
     */
    @Override
    public VandesrUser getUserByEmail(String email) throws Exception {
        QueryWrapper<VandesrUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email", email)
                .eq("delete_flag", 0);
        VandesrUser vandesrUser = this.getOne(queryWrapper);
        return vandesrUser;
    }

}
