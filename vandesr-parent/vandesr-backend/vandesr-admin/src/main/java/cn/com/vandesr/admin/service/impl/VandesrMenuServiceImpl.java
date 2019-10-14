package cn.com.vandesr.admin.service.impl;

import cn.com.vandesr.admin.entity.VandesrMenu;
import cn.com.vandesr.admin.mapper.VandesrMenuMapper;
import cn.com.vandesr.admin.service.IVandesrMenuService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
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
@Service
public class VandesrMenuServiceImpl extends ServiceImpl<VandesrMenuMapper, VandesrMenu> implements IVandesrMenuService {

    @Override
    public boolean addOrUpdateMenu(VandesrMenu menu) {

        //bianji
        if (null != menu.getId()) {
            return updateById(menu);
        } else {
            return save(menu);
        }

    }

    /**
     * 通过菜单名称或者菜单编码获取菜单信息；
     * 校验菜单名称和编码的唯一性
     *
     * @param menuName
     * @param menuCode
     * @return
     */
    @Override
    public VandesrMenu getMenuByMenuNameOrMenuCode(String menuName, String menuCode) {
        QueryWrapper<VandesrMenu> queryWrapper = new QueryWrapper<>();
        VandesrMenu menu = null;
        if (null != menuCode) {
            queryWrapper.eq("menu_code", menuCode);

        }

        if (null != menuName) {
            queryWrapper.eq("menu_name", menuName);
        }

        List<VandesrMenu> list1 = this.list(queryWrapper);
        if (!CollectionUtils.isEmpty(list1)) {
            menu = list1.get(0);
        }
        return menu;
    }

    /**
     * 删除菜单信息，将parentIds中含有此menuId的菜单也删除
     *
     * @param menuId
     * @return
     */
    @Override
    public boolean deleteMenu(String menuId, String updateUserCode) {
        QueryWrapper<VandesrMenu> queryWrapper = new QueryWrapper<>();

        VandesrMenu parentMenu = this.getById(menuId);
        if (null != parentMenu) {
            parentMenu.setDeleteFlag(1);
            Date date = new Date();
            parentMenu.setUpdateDate(date);
            parentMenu.setUpdateUserCode(updateUserCode);
            parentMenu.setUpdateUserName(updateUserCode);
            //查询所有子菜单信息
            queryWrapper.like("parent_ids", menuId + ",");
            List<VandesrMenu> list = this.list(queryWrapper);
            if (null == list) {
                list = new ArrayList<>();
            }
            for (int i = 0; i < list.size(); i++) {
                list.get(i).setDeleteFlag(1);
                list.get(i).setUpdateDate(date);
                list.get(i).setUpdateUserCode(updateUserCode);
                list.get(i).setUpdateUserName(updateUserCode);
            }

            list.add(parentMenu);

            updateBatchById(list);


        }

        return false;
    }
}
