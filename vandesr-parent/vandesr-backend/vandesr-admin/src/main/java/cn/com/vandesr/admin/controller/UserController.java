package cn.com.vandesr.admin.controller;

import cn.com.vandesr.admin.service.UserService;
import cn.com.vandesr.backend.config.security.TokenUtil;
import cn.com.vandesr.backend.config.service.RedisService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: nj
 * @date: 2019/1/24:下午2:32
 */
@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private TokenUtil tokenUtil;
    @Autowired
    RedisService redisService;
    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping(value = "/login")
    public Map<String, Object> login(@RequestBody JSONObject jsonObject) {
        Map<String, Object> map = new HashMap<>(6);
        String token = "";
        Boolean isSuccess = false;
        String responseMsg = "请求失败";
        Integer responseCode = -1;
        boolean isContinue = true;
        try {


            String userName = jsonObject.has("userName") ? jsonObject.getString("userName") : "";
//            String email = jsonObject.has("userName") ? jsonObject.getString("userName") : "";
            String password = jsonObject.has("password") ? jsonObject.getString("password") : "";
            if (StringUtils.isEmpty(userName)) {
                responseMsg = "用户名不能为空";
                isContinue = false;
            }

            if (isContinue && StringUtils.isEmpty(password)) {
                responseMsg = "密码不能为空";
                isContinue = false;
            }

            UsernamePasswordAuthenticationToken upToken = new UsernamePasswordAuthenticationToken(userName, password);
            Authentication authentication = authenticationManager.authenticate(upToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserDetails userDetails = (UserDetails)authentication.getPrincipal();
            token = tokenUtil.generateToken(userDetails);
            redisService.setValue("auth_user_info_" + userName, JSONObject.fromObject(userDetails).toString(), 24 * 3600 * 1000L);
            map.put("token", token);

        } catch (Exception e) {
            responseMsg = e.getMessage();
            e.printStackTrace();

        }

        map.put("responseMsg", responseMsg);
        return map;

    }
}
