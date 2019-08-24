package cn.com.vandesr.admin.service.impl;

import cn.com.vandesr.admin.entity.VandesrRole;
import cn.com.vandesr.admin.entity.VandesrUser;
import cn.com.vandesr.admin.entity.VandesrUserRole;
import cn.com.vandesr.admin.mapper.VandesrUserRoleMapper;
import cn.com.vandesr.admin.service.IVandesrRoleService;
import cn.com.vandesr.admin.service.IVandesrUserRoleService;
import cn.com.vandesr.admin.service.IVandesrUserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
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
@Service
public class VandesrUserRoleServiceImpl extends ServiceImpl<VandesrUserRoleMapper, VandesrUserRole> implements IVandesrUserRoleService {

    @Autowired
    private IVandesrRoleService roleService;

    /**
     * 获取指定用户的所有角色信息
     *
     * @param userId
     * @return
     */
    @Override
    public List<VandesrRole> getRoleList(String userId) {
        QueryWrapper<VandesrUserRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId).eq("delete_flag", 0);
        List<VandesrUserRole> userRoles = this.list(queryWrapper);
        List<VandesrRole> roleList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(userRoles)) {
            List<Integer> roleIdList = new ArrayList<>();
            for (VandesrUserRole userRole: userRoles) {
                roleIdList.add(userRole.getRoleId());
            }
            QueryWrapper<VandesrRole> roleQueryWrapper = new QueryWrapper<>();
            roleQueryWrapper.in("id", roleIdList);
            roleList = this.roleService.list(roleQueryWrapper);
        }
        queryWrapper = null;

        return roleList;
    }

    /**
     * 更新用户角色信息
     *
     * @param user
     * @param roleIdList
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateUserRole(VandesrUser user, List<Integer> roleIdList, String updateUser) throws Exception {

        Integer userId = user.getId();
        //remove user roles
        QueryWrapper<VandesrUserRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        this.remove(queryWrapper);

        // add new roles
        List<VandesrUserRole> userRoleList = new ArrayList<>();
        roleIdList.forEach( roleId -> {
            QueryWrapper<VandesrRole> roleQueryWrapper = new QueryWrapper<>();
            roleQueryWrapper.eq("id", roleId)
                    .eq("delete_flag", 0);
            List<VandesrRole> roles = this.roleService.list(roleQueryWrapper);
            if (!CollectionUtils.isEmpty(roles)) {
                VandesrRole ro = roles.get(0);
                VandesrUserRole userRole = new VandesrUserRole();
                userRole.setCreateDate(new Date());
                userRole.setUpdateUserName(updateUser);
                userRole.setUpdateUserCode(updateUser);
                userRole.setUpdateDate(new Date());
                userRole.setRoleName(ro.getRoleName());
                userRole.setRoleId(roleId);
                userRole.setRoleCode(ro.getRoleCode());
                userRole.setDeleteFlag(0);
                userRole.setCreateUserName(updateUser);
                userRole.setCreateUserCode(updateUser);
                userRole.setUserId(userId);
                userRoleList.add(userRole);
            }
        });

        if (!CollectionUtils.isEmpty(userRoleList)) {
            this.saveBatch(userRoleList);
        }
        return false;
    }
}
