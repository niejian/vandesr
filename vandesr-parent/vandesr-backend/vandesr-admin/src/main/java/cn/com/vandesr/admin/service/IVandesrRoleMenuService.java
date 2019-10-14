package cn.com.vandesr.admin.service;

import cn.com.vandesr.admin.entity.VandesrRoleMenu;
import com.baomidou.mybatisplus.extension.service.IService;
import net.sf.json.JSONArray;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author code4fun
 * @since 2019-01-25
 */
public interface IVandesrRoleMenuService extends IService<VandesrRoleMenu> {

    /**
     * 获取指定角色绑定的菜单信息
     * @param roleId
     * @return
     */
    List<VandesrRoleMenu> getMenuByRoleId(Integer roleId);

    /**
     * 保存角色菜单信息
     * @param roleId
     * @param loginAccount  登录账号
     * @param menus
     * @return
     */
    boolean saveRoleMenus(Integer roleId, String loginAccount, JSONArray menus);
}
