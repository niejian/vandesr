package cn.com.vandesr.admin.service.impl;

import cn.com.vandesr.admin.entity.VandesrMenu;
import cn.com.vandesr.admin.entity.VandesrRoleMenu;
import cn.com.vandesr.admin.mapper.VandesrRoleMenuMapper;
import cn.com.vandesr.admin.service.IVandesrRoleMenuService;
import cn.com.vandesr.admin.vo.MenuVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author code4fun
 * @since 2019-01-25
 */
@Slf4j
@Service
public class VandesrRoleMenuServiceImpl extends ServiceImpl<VandesrRoleMenuMapper, VandesrRoleMenu> implements IVandesrRoleMenuService {

    /**
     * 获取指定角色绑定的菜单信息
     *
     * @param roleId
     * @return
     */
    @Override
    public List<VandesrRoleMenu> getMenuByRoleId(Integer roleId) {
        QueryWrapper<VandesrRoleMenu> queryWrapper = new QueryWrapper();
        queryWrapper.eq("delete_flag", 0)
                .eq("role_id", roleId);
        List<VandesrRoleMenu> list = this.list(queryWrapper);
        if (null == list) {
            list = new ArrayList<>();
        }
        return list;
    }

    /**
     * 保存角色菜单信息
     *
     * @param roleId
     * @param loginAccount 登录账号
     * @param menus
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveRoleMenus(Integer roleId, String loginAccount, JSONArray menus) {
        List<VandesrRoleMenu> roleMenus = new ArrayList<>();
        for (int i = 0; i < menus.size(); i++) {
            JSONObject json = menus.getJSONObject(i);
            VandesrRoleMenu roleMenu = this.convertRoleMenu(roleId, loginAccount, json);
            if (null != roleMenu) {
                roleMenus.add(roleMenu);
            }
        }
        //查询操作前额所有role_menu信息
        QueryWrapper<VandesrRoleMenu> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role_id", roleId)
                .eq("delete_flag", 0);
        remove(queryWrapper);

        if (!CollectionUtils.isEmpty(roleMenus)) {
            saveBatch(roleMenus);
        }


        return true;
    }

    private VandesrRoleMenu convertRoleMenu(Integer roleId, String loginAccount, JSONObject menuJSON) {
        MenuVo menu = (MenuVo)JSONObject.toBean(menuJSON, MenuVo.class);
        Date date = new Date();
        VandesrRoleMenu roleMenu = VandesrRoleMenu.builder()
                .menuCode(menu.getMenuCode())
                .menuId(Integer.parseInt(menu.getMenuId()))
                .menuName(menu.getName())
                .roleId(roleId)
                .createDate(date)
                .createUserCode(loginAccount)
                .createUserName(loginAccount)
                .deleteFlag(0)
                .updateDate(date)
                .updateUserCode(loginAccount)
                .updateUserName(loginAccount)
                .build();
        return roleMenu;

    }
}
