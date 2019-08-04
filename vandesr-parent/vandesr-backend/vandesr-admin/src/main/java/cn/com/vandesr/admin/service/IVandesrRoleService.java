package cn.com.vandesr.admin.service;

import cn.com.vandesr.admin.entity.VandesrRole;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import net.sf.json.JSONObject;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author code4fun
 * @since 2019-01-25
 */
public interface IVandesrRoleService extends IService<VandesrRole> {

    /**
     * 分页获取角色信息
     *
     * @param roleCode
     * @param roleName
     * @param pageNum
     * @param pageSize
     * @return
     */
    IPage<VandesrRole> getRoleList(String roleCode, String roleName, int pageNum, int pageSize) throws Exception;

    /**
     * 更新或删除角色信息；当角色编码已存在
     * @param loginAccount
     * @param type
     * @param roleJSON
     * @return
     * @throws Exception
     */
    boolean updateRole(String loginAccount, String type, JSONObject roleJSON) throws Exception;
}
