package cn.com.vandesr.admin.controller;

import cn.com.vandesr.admin.entity.VandesrMenu;
import cn.com.vandesr.admin.service.IVandesrMenuService;
import cn.com.vandesr.admin.service.IVandesrUserService;
import cn.com.vandesr.admin.vo.MenuRouterVo;
import cn.com.vandesr.admin.vo.MenuVo;
import cn.com.vandesr.backend.common.CommonFunction;
import cn.com.vandesr.backend.common.dto.BaseResponseDto;
import cn.com.vandesr.backend.common.instance.CommonInstance;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import sun.plugin.javascript.JSObject;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * 菜单管理
 * @author niejian
 * @date 2019/8/31
 */

@Slf4j
@CrossOrigin
@RestController
@RequestMapping(value = "/user/menu/")
public class MenuController {

    @Autowired
    private IVandesrUserService userService;
    @Autowired
    private IVandesrMenuService menuService;

    @PreAuthorize("hasRole('sysadmin')")
    @PostMapping(value = "getSystemMenus")
    public BaseResponseDto<List<MenuVo>> getSystemMenus(@RequestBody JSONObject jsonObject,
                                                        HttpServletRequest request) {
        BaseResponseDto<List<MenuVo>> baseResponseDto = new BaseResponseDto<>();
        Boolean isSuccess = false;
        String responseMsg = "请求失败";
        Integer responseCode = -1;
        Boolean isContinue = true;
        CommonFunction.beforeProcess(log, jsonObject);
        List<MenuVo> list = new ArrayList<>();

        try {

            list = this.userService.getSystemMenuTree();
            responseMsg = CommonInstance.SUCCESS_MSG;
            responseCode = CommonInstance.SUCCESS_CODE;
            isSuccess = CommonInstance.SUCCESS;
        } catch (Exception e) {
            CommonFunction.genErrorMessage(log, e);
            e.printStackTrace();
        }

        return baseResponseDto.responseCode(responseCode)
                .responseMsg(responseMsg)
                .success(isSuccess)
                .data(list);
    }

    @PostMapping(value = "getMenuInfo")
    public BaseResponseDto<VandesrMenu> getMenuInfo(@RequestBody JSONObject jsonObject) {
        BaseResponseDto<VandesrMenu> baseResponseDto = new BaseResponseDto<>();
        Boolean isSuccess = false;
        String responseMsg = "请求失败";
        Integer responseCode = -1;
        Boolean isContinue = true;
        VandesrMenu menu = null;
        CommonFunction.beforeProcess(log, jsonObject);

        try {
            String menuId = jsonObject.optString("id", null);
            String menuCode = jsonObject.optString("menuCode", null);
            String menuName = jsonObject.optString("menuName", null);
            if (null == menuId && null == menuCode && null == menuName) {
                isContinue = false;
                responseMsg = "";
            }



            if (isContinue) {
                if (null != menuId) {
                    menu = this.menuService.getById(menuId);
                } else {
                    QueryWrapper<VandesrMenu> queryWrapper = new QueryWrapper<>();
                    if (null != menuCode) {
                        queryWrapper.eq("menu_code", menuCode);

                    }

                    if (null != menuName) {
                        queryWrapper.eq("menu_name", menuName);
                    }

                    List<VandesrMenu> list1 = this.menuService.list(queryWrapper);
                    if (!CollectionUtils.isEmpty(list1)) {
                        menu = list1.get(0);
                    }
                }
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
                .data(menu);
    }


}
