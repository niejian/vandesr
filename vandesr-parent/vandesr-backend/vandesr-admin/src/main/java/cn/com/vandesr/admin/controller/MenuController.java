package cn.com.vandesr.admin.controller;

import cn.com.vandesr.admin.entity.VandesrMenu;
import cn.com.vandesr.admin.service.IVandesrMenuService;
import cn.com.vandesr.admin.service.IVandesrUserService;
import cn.com.vandesr.admin.vo.MenuRouterVo;
import cn.com.vandesr.admin.vo.MenuVo;
import cn.com.vandesr.backend.common.CommonFunction;
import cn.com.vandesr.backend.common.dto.BaseResponseDto;
import cn.com.vandesr.backend.common.instance.CommonInstance;
import cn.com.vandesr.backend.config.security.JwtUser;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import sun.plugin.javascript.JSObject;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
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
                    menu = this.menuService.getMenuByMenuNameOrMenuCode(menuName, menuCode);
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



    @PostMapping(value = "/addMenuInfo")
    public BaseResponseDto<String> addMenuInfo( @RequestBody JSONObject jsonObject) {
        BaseResponseDto<String> baseResponseDto = new BaseResponseDto<>();
        Boolean isSuccess = false;
        String responseMsg = "请求失败";
        Integer responseCode = -1;
        Boolean isContinue = true;
        CommonFunction.beforeProcess(log, jsonObject);

        try {

            // 获取当前登录人信息
            JwtUser loginUser = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (null == loginUser) {
                isContinue = false;
                responseMsg = "登录过期，请重新登录";
            }

            String loginUserName = "";
            if (isContinue) {
                loginUserName = loginUser.getUsername();
            }
            boolean hasChildren = jsonObject.optBoolean("hasChildren", false);
            String icon = jsonObject.optString("icon", null);

            String menuCode = jsonObject.optString("menuCode", null);
            if (null == menuCode) {
                isContinue = false;
                responseMsg = "菜单编码不能为空";
            }

            if (isContinue) {
                VandesrMenu q = this.menuService.getMenuByMenuNameOrMenuCode(null, menuCode);
                if (null != q) {
                    isContinue = false;
                    responseMsg = "菜单编码已存在，请重新输入";
                }

            }

            String menuName = null;
            if (isContinue) {
                menuName = jsonObject.optString("name", null);
                if (null == menuName) {
                    isContinue = false;
                    responseMsg = "菜单名称不能为空";
                }
            }

            if (isContinue) {
                VandesrMenu q = this.menuService.getMenuByMenuNameOrMenuCode(menuName, null);
                if (null != q) {
                    isContinue = false;
                    responseMsg = "菜单名称已存在，请重新输入";
                }
            }

            String parentId = null;

            if (isContinue) {
                parentId = jsonObject.optString("parentId", null);
                if (null == parentId) {
                    isContinue = false;
                    responseMsg = "请选择上级菜单";
                }
            }

            String parentIds = null;
            if (isContinue) {
                parentIds = jsonObject.optString("parentIds", null);
                if (null == parentId) {
                    isContinue = false;
                    responseMsg = "请选择上级菜单";
                }
            }

            String router = null;

            if (isContinue) {
                router = jsonObject.optString("routerPath", null);
                if (null == router) {
                    isContinue = false;
                    responseMsg = "访问路由不能为空";
                }
            }

            String path = null;
            if (isContinue) {
                path = jsonObject.optString("path", null);
                if (null == path) {
                    isContinue = false;
                    responseMsg = "菜单路径不能为空";
                }
            }


            if (isContinue) {
                VandesrMenu menu = VandesrMenu.builder().menuCode(menuCode).menuIcon(icon)
                        .menuName(menuName)
                        .menuUrl(path)
                        .createDate(new Date())
                        .createUserCode(loginUserName)
                        .createUserName(loginUserName)
                        .deleteFlag(0)
                        .leaf(!hasChildren)
                        .parentId(Integer.parseInt(parentId))
                        .parentIds(parentIds + "," + parentId)
                        .router(router)
                        .updateDate(new Date())
                        .updateUserCode(loginUserName)
                        .updateUserName(loginUserName)
                        .build();

                if (null != menu) {
                    this.menuService.addMenu(menu);
                    responseMsg = CommonInstance.SUCCESS_MSG;
                    responseCode = CommonInstance.SUCCESS_CODE;
                    isSuccess = CommonInstance.SUCCESS;
                }



            }

        } catch (Exception e) {
            CommonFunction.genErrorMessage(log, e);
            e.printStackTrace();
        }

        return baseResponseDto.responseCode(responseCode)
                .responseMsg(responseMsg)
                .success(isSuccess)
                .data(responseMsg);
    }


}
