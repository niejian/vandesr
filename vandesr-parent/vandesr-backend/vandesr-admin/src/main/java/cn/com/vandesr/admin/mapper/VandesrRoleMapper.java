package cn.com.vandesr.admin.mapper;

import cn.com.vandesr.admin.entity.VandesrRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author code4fun
 * @since 2019-01-25
 */
public interface VandesrRoleMapper extends BaseMapper<VandesrRole> {

    int isMutiRoleCode(@Param("id") String id,
                       @Param("roleCode") String roleCode) throws Exception;

    int isMutiRoleName(@Param("id") String id,
                       @Param("roleCode") String roleCode) throws Exception;

}
