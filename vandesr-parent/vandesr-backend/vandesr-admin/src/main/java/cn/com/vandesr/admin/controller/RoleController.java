package cn.com.vandesr.admin.controller;

import cn.com.vandesr.admin.entity.VandesrMenu;
import cn.com.vandesr.admin.entity.VandesrRole;
import cn.com.vandesr.admin.entity.VandesrRoleMenu;
import cn.com.vandesr.admin.service.IVandesrRoleMenuService;
import cn.com.vandesr.admin.service.IVandesrRoleService;
import cn.com.vandesr.admin.vo.RoleMenuVo;
import cn.com.vandesr.backend.common.instance.CommonInstance;
import cn.com.vandesr.backend.config.aop.LogAspect;
import cn.com.vandesr.backend.common.dto.BaseResponseDto;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: nj
 * @date: 2019-07-26:11:08
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/user/role/")
@Slf4j
public class RoleController {

    @Autowired
    private IVandesrRoleService roleService;
    @Autowired
    private IVandesrRoleMenuService roleMenuService;

    @PreAuthorize("hasRole('sysadmin')")
//    @LogAspect
    @PostMapping(value = "list")
    public BaseResponseDto<List<RoleMenuVo>> roleList(@RequestBody JSONObject jsonObject) {
        BaseResponseDto<List<RoleMenuVo>> responseDto = new BaseResponseDto<>();
        List<RoleMenuVo> list = new ArrayList<>();
        Boolean isSuccess = false;
        String responseMsg = "请求失败";
        Integer responseCode = -1;
        IPage<VandesrRole> vandesrRoleIPage = null;
        try {
            JSONObject page = jsonObject.getJSONObject("page");
            JSONObject queryRole = jsonObject.getJSONObject("role");

            int pageNum = page.optInt("pageNum", 1);
            int pageSize = page.optInt("pageSize", 10);
            String roleCode = queryRole.optString("roleCode", "");
            String roleName = queryRole.optString("roleName", "");
            vandesrRoleIPage = this.roleService.getRoleList(roleCode, roleName, pageNum, pageSize);
            if (null == vandesrRoleIPage) {
                vandesrRoleIPage = new Page<>();
            }



            // 获取所有角色信息
            List<VandesrRole> roleList = roleService.list();
            if (null != roleList) {
                for (VandesrRole role : roleList) {
                    RoleMenuVo vo = new RoleMenuVo();
                    BeanUtils.copyProperties(role, vo);
                    //
//                    QueryWrapper<VandesrRoleMenu> qw = new QueryWrapper<>();
//                    qw.eq("role_id", role.getId());
//                    List<VandesrRoleMenu> roleMenuList = this.roleMenuService.list(qw);
//                    if (!CollectionUtils.isEmpty(roleMenuList)) {
//                        List<RoleMenuVo.RoleMenuMapVo> roleMenuMapVoList = new ArrayList<>();
//                        for (VandesrRoleMenu roleMenu : roleMenuList) {
//                            RoleMenuVo.RoleMenuMapVo roleMenuMapVo = RoleMenuVo.RoleMenuMapVo.builder()
//                                    .menuCode(roleMenu.getMenuCode())
//                                    .menuName(roleMenu.getMenuName())
//                                    .menuId(roleMenu.getMenuId())
//                                    .build();
//                            roleMenuMapVoList.add(roleMenuMapVo);
//
//                        }
//                        vo.setMenuList(roleMenuMapVoList);
//                    }

                    list.add(vo);
                }
            }

            responseMsg = CommonInstance.SUCCESS_MSG;
            responseCode = CommonInstance.SUCCESS_CODE;
            isSuccess = CommonInstance.SUCCESS;

        } catch (Exception e) {
            e.printStackTrace();

        }


        return responseDto.success(isSuccess)
                .responseCode(responseCode)
                .responseMsg(responseMsg)
                .data(vandesrRoleIPage);
    }


}
