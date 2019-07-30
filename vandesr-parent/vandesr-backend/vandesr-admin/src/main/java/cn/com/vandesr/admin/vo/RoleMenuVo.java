package cn.com.vandesr.admin.vo;

import cn.com.vandesr.admin.entity.VandesrRole;
import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author: nj
 * @date: 2019-07-30:15:26
 */
@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoleMenuVo implements Serializable {

    private VandesrRole role;

    private List<RoleMenuMapVo> menuList;

    /**
     * 该角色赋予的菜单信息
     */
    @Data
    @ToString
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RoleMenuMapVo {
        private int menuId;
        private String menuCode;
        private String menuName;
    }
}
