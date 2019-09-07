package cn.com.vandesr.admin.service;

import cn.com.vandesr.admin.entity.VandesrMenu;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author code4fun
 * @since 2019-01-25
 */
public interface IVandesrMenuService extends IService<VandesrMenu> {
    boolean addMenu(VandesrMenu menu);

    /**
     * 通过菜单名称或者菜单编码获取菜单信息；
     * 校验菜单名称和编码的唯一性
     * @param menuName
     * @param menuCode
     * @return
     */
    VandesrMenu getMenuByMenuNameOrMenuCode(String menuName, String menuCode);
}
