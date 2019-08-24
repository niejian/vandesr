package cn.com.vandesr.admin.service;

import cn.com.vandesr.admin.entity.VandesrRole;
import cn.com.vandesr.admin.entity.VandesrUser;
import cn.com.vandesr.admin.entity.VandesrUserRole;
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
public interface IVandesrUserRoleService extends IService<VandesrUserRole> {

    /**
     * 获取指定用户的所有角色信息
     * @param userId
     * @return
     */
    List<VandesrRole> getRoleList(String userId);

    /**
     * 更新用户角色信息
     * @param user
     * @param roleIdList
     * @throws Exception
     */
    boolean updateUserRole(VandesrUser user, List<Integer> roleIdList, String updateUser) throws Exception;

}
