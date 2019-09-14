package cn.com.vandesr.admin.controller;/**
 * Created by niejian on 2019/9/14.
 */

import cn.com.vandesr.admin.entity.VandesrRoleMenu;
import cn.com.vandesr.admin.service.IVandesrRoleMenuService;
import cn.com.vandesr.admin.vo.MenuVo;
import cn.com.vandesr.backend.common.CommonFunction;
import cn.com.vandesr.backend.common.dto.BaseResponseDto;
import cn.com.vandesr.backend.common.instance.CommonInstance;
import cn.com.vandesr.backend.config.security.JwtUser;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author niejian
 * @date 2019/9/14
 */

@Slf4j
@CrossOrigin
@RestController
@RequestMapping(value = "/user/roleMenu")
public class RoleMenuController {

    @Autowired
    private IVandesrRoleMenuService roleMenuService;

    @PostMapping(value = "/getMenuByRoleId")
    public BaseResponseDto<List<VandesrRoleMenu>> getMenuByRoleId(@RequestBody JSONObject jsonObject) {
        BaseResponseDto<List<MenuVo>> baseResponseDto = new BaseResponseDto<>();
        Boolean isSuccess = false;
        String responseMsg = "请求失败";
        Integer responseCode = -1;
        Boolean isContinue = true;
        CommonFunction.beforeProcess(log, jsonObject);
        List<VandesrRoleMenu> list = new ArrayList<>();

        try {
            Integer roleId = jsonObject.optInt("roleId", -1);
            if (roleId < 0) {
                isContinue = false;
                responseMsg = "请选择一个菜单信息";
            }

            if (isContinue) {
                list = this.roleMenuService.getMenuByRoleId(roleId);
                responseMsg = CommonInstance.SUCCESS_MSG;
                responseCode = CommonInstance.SUCCESS_CODE;
                isSuccess = CommonInstance.SUCCESS;
            }

        } catch (Exception e) {
            CommonFunction.genErrorMessage(log, e);
            e.printStackTrace();
        }

        return baseResponseDto.responseCode(responseCode)
                .responseMsg(responseMsg)
                .success(isSuccess)
                .data(list);
    }

    @PostMapping(value = "/saveRoleMenu")
    public BaseResponseDto<String> saveRoleMenu(@RequestBody JSONObject jsonObject) {
        BaseResponseDto<String> baseResponseDto = new BaseResponseDto<>();
        Boolean isSuccess = false;
        String responseMsg = "请求失败";
        Integer responseCode = -1;
        Boolean isContinue = true;
        CommonFunction.beforeProcess(log, jsonObject);

        try {

            // 获取当前登录人信息
            JwtUser loginUser = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Assert.notNull(loginUser, "登录过期，请重新登录");
            String loginAccount = loginUser.getUsername();

            Integer roleId = jsonObject.optInt("roleId", -1);

            if (roleId < 0) {
                isContinue = false;
                responseMsg = "请选择一个菜单信息";
            }

            JSONArray datas = jsonObject.optJSONArray("data");
            if (null == datas) {
                datas = new JSONArray();
            }

            if (isContinue) {
                this.roleMenuService.saveRoleMenus(roleId, loginAccount, datas);

                responseMsg = CommonInstance.SUCCESS_MSG;
                responseCode = CommonInstance.SUCCESS_CODE;
                isSuccess = CommonInstance.SUCCESS;
            }

        } catch (Exception e) {

            responseMsg = e.getMessage();
            CommonFunction.genErrorMessage(log, e);
            e.printStackTrace();
        }

        return baseResponseDto.responseCode(responseCode)
                .responseMsg(responseMsg)
                .success(isSuccess)
                .data(responseMsg);
    }

    @PostMapping(value = "/getUserMenuTree")
    public BaseResponseDto<List<MenuVo>> getUserMenuTree() {
        BaseResponseDto<List<MenuVo>> baseResponseDto = new BaseResponseDto<>();
        Boolean isSuccess = false;
        String responseMsg = "请求失败";
        Integer responseCode = -1;
        Boolean isContinue = true;
        List<MenuVo> list = new ArrayList<>();
        CommonFunction.beforeProcess(log);

        try {

            // 获取当前登录人信息
            JwtUser loginUser = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Assert.notNull(loginUser, "登录过期，请重新登录");
            String loginAccount = loginUser.getUsername();


            if (isContinue) {

                responseMsg = CommonInstance.SUCCESS_MSG;
                responseCode = CommonInstance.SUCCESS_CODE;
                isSuccess = CommonInstance.SUCCESS;
            }

        } catch (Exception e) {

            responseMsg = e.getMessage();
            CommonFunction.genErrorMessage(log, e);
            e.printStackTrace();
        }

        return baseResponseDto.responseCode(responseCode)
                .responseMsg(responseMsg)
                .success(isSuccess)
                .data(list);
    }
}
