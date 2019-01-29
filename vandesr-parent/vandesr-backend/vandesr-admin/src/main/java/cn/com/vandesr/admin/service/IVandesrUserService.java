package cn.com.vandesr.admin.service;

import cn.com.vandesr.admin.entity.VandesrRoleMenu;
import cn.com.vandesr.admin.entity.VandesrUser;
import cn.com.vandesr.admin.entity.VandesrUserRole;
import cn.com.vandesr.admin.vo.MenuVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author code4fun
 * @since 2019-01-25
 */
public interface IVandesrUserService extends IService<VandesrUser> {

    /**
     * 注册
     * @param email
     * @param userName
     * @param password
     * @throws Exception
     */
    void addUser(String userName, String email, String password) throws Exception;

    /**
     * 判断邮箱是否唯一
     * @param email
     * @return
     * @throws Exception
     */
    VandesrUser getUserByEmail(String email) throws Exception;

    /**
     * 通过userId获取用户角色信息
     * @param userId
     * @return
     * @throws Exception
     */
    List<VandesrUserRole> getUserRolesByUserId(Integer userId) throws Exception;

    /**
     * 通过角色信息获取菜单信息
     * @param roleIdList
     * @return
     * @throws Exception
     */
    List<VandesrRoleMenu> getRoleMenuByRoleIds(List<Integer> roleIdList) throws Exception;


    /**
     * 获取用户菜单信息
     * @param userId
     * @return
     */
    List<MenuVo> getUserMenuByUserId(Integer userId) throws Exception;

}
