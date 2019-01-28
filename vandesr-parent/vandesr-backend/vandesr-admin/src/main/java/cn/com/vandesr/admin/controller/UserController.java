package cn.com.vandesr.admin.controller;

import cn.com.vandesr.admin.entity.VandesrUser;
import cn.com.vandesr.admin.service.IVandesrUserService;
import cn.com.vandesr.admin.service.UserService;
import cn.com.vandesr.admin.vo.VandesrUserVo;
import cn.com.vandesr.backend.config.exception.AccountNotFountException;
import cn.com.vandesr.backend.config.security.JwtUser;
import cn.com.vandesr.backend.config.security.TokenUtil;
import cn.com.vandesr.backend.config.service.RedisService;
import cn.com.vandesr.backend.config.web.BaseResponse;
import cn.com.vandesr.backend.config.web.BaseResponseExt;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import net.sf.json.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

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
    @Autowired
    private IVandesrUserService vandesrUserService;


    @PostMapping(value = "/register")
    public BaseResponse register(@RequestBody JSONObject jsonObject) {
        BaseResponse response = new BaseResponse();
        boolean isContinue = true;

        try {
            String email = jsonObject.optString("email", "");
            if (StringUtils.isEmpty(email)) {
                isContinue = false;
                response.setResponseCode(1101);
                response.setResponseMsg("请填写邮箱");

            }

            String userName = jsonObject.optString("userName", "");
            if (isContinue && StringUtils.isEmpty(userName)) {
                isContinue = false;
                response.setResponseCode(1103);
                response.setResponseMsg("请填写账号昵称");
            }

            String pwd = jsonObject.optString("pwd", "");

            if (StringUtils.isEmpty(pwd) && isContinue) {
                isContinue = false;
                response.setResponseCode(1102);
                response.setResponseMsg("请填写密码");
            }

            if (isContinue) {
                vandesrUserService.addUser(userName, email, pwd);
            }

        } catch (Exception e) {
            response.setResponseMsg(e.getMessage());
            e.printStackTrace();
        }



        return response;
    }

    @PostMapping(value = "/login")
    public Map<String, Object> login(@RequestBody JSONObject jsonObject) {
        Map<String, Object> map = new HashMap<>(6);
        String token = "";
        Boolean isSuccess = false;
        String responseMsg = "请求失败";
        Integer responseCode = -1;
        boolean isContinue = true;
        try {


            String email = jsonObject.has("email") ? jsonObject.getString("email") : "";
//            String email = jsonObject.has("userName") ? jsonObject.getString("userName") : "";
            String password = jsonObject.has("password") ? jsonObject.getString("password") : "";
            if (StringUtils.isEmpty(email)) {
                responseMsg = "登陆账号不能为空";
                isContinue = false;
            }

            if (isContinue && StringUtils.isEmpty(password)) {
                responseMsg = "密码不能为空";
                isContinue = false;
            }

            if (isContinue) {
                VandesrUser user = getUserByLoginAccount(email);
                if (null == user) {
                    throw new AccountNotFountException("登陆账号不存在");
                }
            }

            if (isContinue) {
                UsernamePasswordAuthenticationToken upToken = new UsernamePasswordAuthenticationToken(email, password);
                Authentication authentication = authenticationManager.authenticate(upToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                UserDetails userDetails = (UserDetails)authentication.getPrincipal();
                token = tokenUtil.generateToken(userDetails);
                redisService.setValue("auth_user_info_" + email, JSONObject.fromObject(userDetails).toString(), 24 * 3600 * 1000L);
                map.put("token", token);
                isSuccess = true;
                responseMsg = "请求成功";
                responseCode = 0;
            }


        } catch (Exception e) {
            if (e instanceof BadCredentialsException) {
                responseMsg = "密码错误";
            } else if (e instanceof UsernameNotFoundException) {
                responseMsg = "账号不存在";
            } else {
                responseMsg = e.getMessage();
            }

            e.printStackTrace();

        }

        map.put("responseMsg", responseMsg);
        map.put("isSuccess", isSuccess);
        map.put("responseCode", responseCode);
        return map;

    }

    private VandesrUser getUserByLoginAccount(String userName) throws Exception {

        QueryWrapper<VandesrUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email", userName)
                .eq("delete_flag", 0);
        //通过登陆账号查询用户信息
        VandesrUser user = vandesrUserService.getOne(queryWrapper);

        return user;
    }

    @PostMapping("/getUserInfo")
    public BaseResponseExt<VandesrUserVo> getUserInfo(HttpServletRequest request) {
        BaseResponseExt<VandesrUserVo> baseResponseExt = new BaseResponseExt();
        String token = request.getHeader("Authorization");
        if (!StringUtils.isEmpty(token)) {
            //Bearer
            token = token.substring(7);
        }

        JwtUser jwtUser = (JwtUser) this.tokenUtil.getUserDetails(token);

        String username = tokenUtil.getUsernameFromToken(token);
        //
        VandesrUserVo user = new VandesrUserVo();

        try {
            VandesrUser vandesrUser = this.getUserByLoginAccount(username);
            BeanUtils.copyProperties(vandesrUser, user);
            Collection<? extends GrantedAuthority> authorities = jwtUser.getAuthorities();
            List<String> roleList = new ArrayList<>();
            authorities.forEach(authority -> {
                roleList.add(authority.getAuthority());
            });
            user.setRoles(roleList);

            baseResponseExt.setData(user);
        } catch (Exception e) {
            baseResponseExt.setData(null);
            baseResponseExt.setResponseCode(-1);
            baseResponseExt.setResponseMsg(e.getMessage());
            baseResponseExt.setSuccess(false);
            e.printStackTrace();
        }


        return baseResponseExt;
    }

}
