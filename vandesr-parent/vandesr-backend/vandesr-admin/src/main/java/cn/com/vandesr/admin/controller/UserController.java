package cn.com.vandesr.admin.controller;

import cn.com.vandesr.admin.entity.VandesrUser;
import cn.com.vandesr.admin.service.IVandesrUserService;
import cn.com.vandesr.admin.service.UserService;
import cn.com.vandesr.admin.vo.MenuRouterVo;
import cn.com.vandesr.admin.vo.MenuVo;
import cn.com.vandesr.admin.vo.RoleMenuVo;
import cn.com.vandesr.admin.vo.VandesrUserVo;
import cn.com.vandesr.backend.common.CommonFunction;
import cn.com.vandesr.backend.config.aop.LogAspect;
import cn.com.vandesr.backend.common.dto.BaseResponseDto;
import cn.com.vandesr.backend.common.instance.CommonInstance;
import cn.com.vandesr.backend.config.exception.AccountNotFountException;
import cn.com.vandesr.backend.config.security.JwtUser;
import cn.com.vandesr.backend.config.security.TokenUtil;
import cn.com.vandesr.backend.config.web.BaseResponse;
import cn.com.vandesr.backend.config.web.BaseResponseExt;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author: nj
 * @date: 2019/1/24:下午2:32
 */
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private TokenUtil tokenUtil;
//    @Autowired
//    RedisService redisService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private IVandesrUserService vandesrUserService;


    @Transactional
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

//    @LogAspect
    @PostMapping(value = "/login")
    public BaseResponseDto                <Map<String, Object>> login(@RequestBody JSONObject jsonObject) {
        Map<String, Object> map = new HashMap<>(2);
        BaseResponseDto<Map<String, Object>> responseDto = new BaseResponseDto<>();
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
            VandesrUser user = null;
            if (isContinue) {
                user = getUserByLoginAccount(email);
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
                map.put("token", token);
                // 获取菜单信息
                List<MenuVo> menuVoList = this.vandesrUserService.getMenuTreeByUserId(user.getId());
                if (menuVoList == null) {
                    menuVoList = new ArrayList<>();
                }
                // 前端需要菜单树作为构建侧边的数据基础
                map.put("menusTree", menuVoList);
                // 所有的叶子节点作为路由
                List<MenuRouterVo> leafMenus = this.vandesrUserService.getLeafMenuByUserId(user.getId());
                if (null == leafMenus) {
                    leafMenus = new ArrayList<>();
                }
                map.put("leafMenus", leafMenus);

                // 返回用户信息
                map.put("user", user);

                responseMsg = CommonInstance.SUCCESS_MSG;
                responseCode = CommonInstance.SUCCESS_CODE;
                isSuccess = CommonInstance.SUCCESS;
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


        return  responseDto.success(isSuccess).responseCode(responseCode).responseMsg(responseMsg).data(map);

    }

    private VandesrUser getUserByLoginAccount(String userName) throws Exception {

        QueryWrapper<VandesrUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email", userName)
                .eq("delete_flag", 0);
        //通过登陆账号查询用户信息
        VandesrUser user = vandesrUserService.getOne(queryWrapper);

        return user;
    }

    /**
     * 获取登陆用户的信息
     * @param request
     * @return
     */
    @LogAspect
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

            //获取用户菜单信息
            List<MenuVo> menuVoList = this.vandesrUserService.getUserMenuByUserId(vandesrUser.getId());
//            for (int i = 0; i < menuVoList.size(); i++) {
//                if (!CollectionUtils.isEmpty(menuVoList.get(i).getChildren())) {
//                    menuVoList.get(i).setHasChildren(true);
//                }
//            }

            user.setUserMenus(menuVoList);
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

    @PreAuthorize("hasRole('sysadmin')")
    @PostMapping(value = "/getUsers")
    public BaseResponseDto<IPage<VandesrUser>> getUsers(@RequestBody JSONObject jsonObject) {
        BaseResponseDto<VandesrUser> baseResponseDto = new BaseResponseDto<>();

        List<VandesrUser> list = new ArrayList<>();
        Boolean isSuccess = CommonInstance.FAIL;
        String responseMsg = CommonInstance.FAIL_MSG;
        Integer responseCode = CommonInstance.FAIL_CODE;
        IPage<VandesrUser> userIPage = null;

        try {

            JSONObject page = jsonObject.getJSONObject("page");
            String userName = jsonObject.optString("userName", null);
            String loginName = jsonObject.optString("loginName", null);
            String email = jsonObject.optString("email", null);
            int pageNum = page.optInt("pageNum", 1);
            int pageSize = page.optInt("pageSize", 10);
            Page queryPage = new Page(pageNum, pageSize);
            // 分页获取用户信息
            userIPage = this.vandesrUserService.getUsers(userName, loginName, email, queryPage);
            if (null == userIPage) {
                userIPage = new Page<>();
            }

            responseMsg = CommonInstance.SUCCESS_MSG;
            responseCode = CommonInstance.SUCCESS_CODE;
            isSuccess = CommonInstance.SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            CommonFunction.genErrorMessage(log, e);
        }

        return baseResponseDto.success(isSuccess)
                .responseMsg(responseMsg)
                .responseCode(responseCode)
                .data(userIPage);
    }

}
