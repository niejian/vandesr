package cn.com.vandesr.admin.service.impl;

import cn.com.vandesr.admin.entity.VandesrRole;
import cn.com.vandesr.admin.mapper.VandesrRoleMapper;
import cn.com.vandesr.admin.service.IVandesrRoleService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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
public class VandesrRoleServiceImpl extends ServiceImpl<VandesrRoleMapper, VandesrRole> implements IVandesrRoleService {

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
}
