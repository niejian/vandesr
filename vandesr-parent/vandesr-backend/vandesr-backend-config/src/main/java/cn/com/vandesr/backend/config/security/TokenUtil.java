package cn.com.vandesr.backend.config.security;

import cn.com.vandesr.backend.config.service.RedisService;
import net.sf.json.JSONObject;
import netscape.javascript.JSObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

/**
 * @author: nj
 * @date: 2019/1/24:下午4:10
 */
@Component
public class TokenUtil extends JwtTokenUtil{

    @Autowired
    private RedisService redisService;

    /**
     * 从缓存中获取用户信息
     *
     * @param token
     * @return
     */
    @Override
    public UserDetails getUserDetails(String token) {
        String userName = getUsernameFromToken(token);
        try {
            String userInfo = (String)redisService.getValue("auth_user_info_" + userName, String.class);
            return (JwtUser)JSONObject.toBean(JSONObject.fromObject(userInfo), JwtUser.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
