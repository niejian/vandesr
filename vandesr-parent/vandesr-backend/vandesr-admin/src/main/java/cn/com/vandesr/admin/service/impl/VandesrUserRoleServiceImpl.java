package cn.com.vandesr.admin.service.impl;

import cn.com.vandesr.admin.entity.VandesrRole;
import cn.com.vandesr.admin.entity.VandesrUserRole;
import cn.com.vandesr.admin.mapper.VandesrUserRoleMapper;
import cn.com.vandesr.admin.service.IVandesrRoleService;
import cn.com.vandesr.admin.service.IVandesrUserRoleService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
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
}
