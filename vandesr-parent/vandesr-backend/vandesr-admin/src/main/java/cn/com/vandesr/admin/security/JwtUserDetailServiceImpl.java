package cn.com.vandesr.admin.security;

import cn.com.vandesr.admin.entity.DemoUser;
import cn.com.vandesr.admin.mapper.UserMapper;
import cn.com.vandesr.backend.config.security.JwtUser;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author: nj
 * @date: 2019/1/24:下午4:27
 */
@Service
public class JwtUserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    /**
     * Locates the user based on the username. In the actual implementation, the search
     * may possibly be case sensitive, or case insensitive depending on how the
     * implementation instance is configured. In this case, the <code>UserDetails</code>
     * object that comes back may have a username that is of a different case than what
     * was actually requested..
     *
     * @param username the username identifying the user whose data is required.
     * @return a fully populated user record (never <code>null</code>)
     * @throws UsernameNotFoundException if the user could not be found or the user has no
     *                                   GrantedAuthority
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        QueryWrapper<DemoUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", username);
        List<DemoUser> demoUsers = userMapper.selectList(queryWrapper);
        if (CollectionUtils.isEmpty(demoUsers)) {
            throw new UsernameNotFoundException("用户名不存在");

        } else {
            DemoUser user = demoUsers.get(0);
            //获取密码。
            String userPassword = user.getUserPassword();
            Set<GrantedAuthority> authorities = new HashSet<>();
            return new JwtUser(username, userPassword, authorities);
        }


    }
}
