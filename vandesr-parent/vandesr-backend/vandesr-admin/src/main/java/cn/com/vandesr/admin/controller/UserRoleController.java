package cn.com.vandesr.admin.controller;/**
 * Created by niejian on 2019/8/17.
 */

import cn.com.vandesr.admin.entity.VandesrRole;
import cn.com.vandesr.admin.entity.VandesrUser;
import cn.com.vandesr.admin.service.IVandesrUserRoleService;
import cn.com.vandesr.admin.service.IVandesrUserService;
import cn.com.vandesr.backend.common.dto.BaseResponseDto;
import cn.com.vandesr.backend.common.instance.CommonInstance;
import cn.com.vandesr.backend.common.util.CommonFunction;
import cn.com.vandesr.backend.config.security.JwtUser;
import cn.com.vandesr.backend.config.security.TokenUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @author niejian
 * @date 2019/8/17
 */
@CrossOrigin
@Slf4j
@RestController
@RequestMapping(value = "/userRole")
public class UserRoleController {

    @Autowired
    private IVandesrUserRoleService userRoleService;
    @Autowired
    private IVandesrUserService userService;
    @Autowired
    private TokenUtil tokenUtil;

    @PostMapping(value = "/list")
    public BaseResponseDto<List<VandesrRole>> getUserRoleList(@RequestBody JSONObject jsonObject) {
        BaseResponseDto<Boolean> baseResponseDto = new BaseResponseDto<>();
        Boolean isSuccess = CommonInstance.FAIL;
        String responseMsg = CommonInstance.FAIL_MSG;
        Integer responseCode = CommonInstance.FAIL_CODE;
        Boolean isContinue = true;
        List<VandesrRole> roleList = new ArrayList<>();

        try {
            String userId = jsonObject.optString("userId", null);
            if (StringUtils.isEmpty(userId)) {
                isContinue = false;
                responseMsg = "请选择用户信息";
            }

            if (isContinue) {
                roleList = this.userRoleService.getRoleList(userId);
                responseMsg = CommonInstance.SUCCESS_MSG;
                responseCode = CommonInstance.SUCCESS_CODE;
                isSuccess = CommonInstance.SUCCESS;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return baseResponseDto.success(isSuccess)
                .responseMsg(responseMsg)
                .responseCode(responseCode)
                .data(roleList);
    }


    //updateUserRole
    @PostMapping(value = "/updateUserRole")
    public BaseResponseDto<String> updateUserRole(@RequestBody JSONObject jsonObject, HttpServletRequest request) {
        BaseResponseDto<Boolean> baseResponseDto = new BaseResponseDto<>();
        Boolean isSuccess = CommonInstance.FAIL;
        String responseMsg = CommonInstance.FAIL_MSG;
        Integer responseCode = CommonInstance.FAIL_CODE;
        Boolean isContinue = true;

        try {
            String userId = jsonObject.optString("userId", null);
            if (StringUtils.isEmpty(userId)) {
                isContinue = false;
                responseMsg = "请选择用户信息";
            }

            JSONArray roleIds = jsonObject.optJSONArray("roleIds");
            if (null == roleIds) {
                roleIds = new JSONArray();
            }

            VandesrUser user = null;
            String token = request.getHeader("Authorization");
            if (!StringUtils.isEmpty(token)) {
                //Bearer
                token = token.substring(7);
            }

            String username = tokenUtil.getUsernameFromToken(token);
            if (isContinue) {
                QueryWrapper<VandesrUser> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("id", userId).eq("delete_flag", 0);
                List<VandesrUser> users = this.userService.list(queryWrapper);
                if (CollectionUtils.isEmpty(users)) {
                    isContinue = false;
                    responseMsg = "该用户不存在或者已删除，请刷新用户列表重试";
                } else {
                    user = users.get(0);
                }

            }

            if (isContinue) {
                this.userRoleService.updateUserRole(user, roleIds, username);

                responseMsg = CommonInstance.SUCCESS_MSG;
                responseCode = CommonInstance.SUCCESS_CODE;
                isSuccess = CommonInstance.SUCCESS;
            }
        } catch (Exception e) {
            CommonFunction.genErrorMessage(log, e);
            e.printStackTrace();
        }


        return baseResponseDto.success(isSuccess)
                .responseMsg(responseMsg)
                .responseCode(responseCode)
                .data(responseMsg);
    }


}
