package cn.com.vandesr.admin.service.impl;

import cn.com.vandesr.admin.entity.VandesrRole;
import cn.com.vandesr.admin.entity.VandesrUser;
import cn.com.vandesr.admin.mapper.VandesrRoleMapper;
import cn.com.vandesr.admin.service.IVandesrRoleService;
import cn.com.vandesr.admin.service.IVandesrUserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

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
public class VandesrRoleServiceImpl extends ServiceImpl<VandesrRoleMapper, VandesrRole> implements IVandesrRoleService {

    @Autowired
    private IVandesrUserService userService;
    /**
     * 分页获取角色信息
     *
     * @param roleCode
     * @param roleName
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public IPage<VandesrRole> getRoleList(String roleCode, String roleName,
                                          int pageNum, int pageSize) throws Exception{
        IPage<VandesrRole> page = new Page<>(pageNum, pageSize);
        QueryWrapper<VandesrRole> queryWrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(roleCode)) {
            queryWrapper.like("role_code", "%" + roleCode + "%");

        }

        if (!StringUtils.isEmpty(roleName)) {
            queryWrapper.like("role_name", "%" + roleName + "%");
        }
        queryWrapper.orderByAsc("id");
        IPage<VandesrRole> vandesrRoleIPage = this.page(page, queryWrapper);


        return vandesrRoleIPage;
    }

    /**
     * 更新或删除角色信息；当角色编码已存在
     *
     * @param loginAccount
     * @param type
     * @param roleJSON
     * @return
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateRole(String loginAccount, String type, JSONObject roleJSON) throws Exception {
        Integer roleId = roleJSON.getInt("id");
        if ("del".equals(type)) {
            removeById(roleId);
        } else {
            QueryWrapper<VandesrUser> userQueryWrapper = new QueryWrapper<>();
            userQueryWrapper.eq("login_name", loginAccount).eq("delete_flag", 0);
            List<VandesrUser> list = this.userService.list(userQueryWrapper);
            String loginName = "";
            if (!CollectionUtils.isEmpty(list)) {
                loginName = list.get(0).getUserName();
            }

            Date date = new Date();
            VandesrRole role = getById(roleId);
            if ("add".equals(type)) {
                role = new VandesrRole();
            }
            role.setUpdateDate(date);
            role.setUpdateUserCode(loginAccount);
            String roleCode = roleJSON.getString("roleCode");
            role.setUpdateUserName(loginName);
            if (roleCode.indexOf("ROLE_") != 0) {
                roleCode = "ROLE_" + roleCode;
            }
            role.setRoleCode(roleCode);
            role.setRoleName(roleJSON.getString("roleName"));
            if ("edit".equals(type)) {
                role.setId(roleId);
                updateById(role);
            } else if ("add".equals(type)) {
                role.setDeleteFlag(0);
                role.setCreateUserName(loginName);
                role.setCreateUserCode(loginAccount);
                role.setCreateDate(date);

                save(role);
            }


        }
        return true;
    }
}
