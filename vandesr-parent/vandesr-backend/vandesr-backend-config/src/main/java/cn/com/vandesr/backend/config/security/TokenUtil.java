package cn.com.vandesr.backend.config.security;

import cn.com.vandesr.backend.config.service.RedisService;
import cn.com.vandesr.backend.constInstance.CommonInstance;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.lang.Assert;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import netscape.javascript.JSObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

/**
 * @author: nj
 * @date: 2019/1/24:下午4:10
 */
@Component
public class TokenUtil extends JwtTokenUtil{

//    @Autowired
//    private RedisService redisService;

    /**
     * 从缓存中获取用户信息
     *
     * @param token
     * @return
     */
    @Override
    public UserDetails getUserDetails(String token) {
        try {
            if (token.startsWith(CommonInstance.TOKEN_PREFIX)) {
                token = token.replace(CommonInstance.TOKEN_PREFIX, "");
            }
            Claims claims = super.getClaimsFromToken(token);
            Assert.notNull(claims, "非法token");

            String loginName = claims.get("sub", String.class);
            Date created = claims.get("created", Date.class);
            Collection<? extends GrantedAuthority> authorities = claims.get("auth", Collection.class);
            String password = claims.get("password", String.class);
            if (null != loginName && null != password) {
                JwtUser jwtUser = new JwtUser();
                jwtUser.setAuthorities(authorities);
                jwtUser.setPassword(password);
                jwtUser.setUsername(loginName);

                return jwtUser;

            } else {
                return null;

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public JwtUser convertUser(JSONObject userObj) throws Exception{
        JwtUser jwtUser = new JwtUser();
        jwtUser.setUsername(userObj.optString("username", ""));
        jwtUser.setPassword(userObj.optString("password", ""));
        JSONArray authorities = userObj.getJSONArray("authorities");

        Collection<SimpleGrantedAuthority> authoritiesTmp = new HashSet<>();
        for (int i = 0; i < authorities.size(); i++) {
            JSONObject jsonObject = authorities.getJSONObject(i);
            String role = jsonObject.optString("authority", "");
            authoritiesTmp.add(new SimpleGrantedAuthority(role));
        }
        jwtUser.setAuthorities(authoritiesTmp);

        return jwtUser;
    }


}
