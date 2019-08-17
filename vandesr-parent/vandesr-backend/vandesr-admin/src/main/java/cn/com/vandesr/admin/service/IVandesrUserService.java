package cn.com.vandesr.admin.service;

import cn.com.vandesr.admin.entity.VandesrRoleMenu;
import cn.com.vandesr.admin.entity.VandesrUser;
import cn.com.vandesr.admin.entity.VandesrUserRole;
import cn.com.vandesr.admin.vo.MenuRouterVo;
import cn.com.vandesr.admin.vo.MenuVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
     * 获取用户菜单树 getUserMenuByUserId 的优化版本
     * @param userId
     * @return
     * @throws Exception
     */
    List<MenuVo> getMenuTreeByUserId(Integer userId) throws Exception;

    /**
     * 获取用户菜单信息
     * @param userId
     * @return
     */
    List<MenuVo> getUserMenuByUserId(Integer userId) throws Exception;

    /**
     * 获取该用户的所有叶子信息（构建菜单路由）
     * @param userId
     * @return
     * @throws Exception
     */
    List<MenuRouterVo> getLeafMenuByUserId(Integer userId) throws Exception;

    /**
     * 分页获取用户信息
     * @param userName
     * @param loginName
     * @param email
     * @param page
     * @return
     * @throws Exception
     */
    IPage<VandesrUser> getUsers(String userName, String loginName, String email, Page<VandesrUser> page) throws Exception;


    /**
     * 删除用户
     * @param userId
     * @return
     */
    boolean removeUser(String userId, String deleteFlag);

}
