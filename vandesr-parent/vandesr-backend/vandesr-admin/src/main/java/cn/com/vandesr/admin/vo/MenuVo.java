package cn.com.vandesr.admin.vo;

import cn.com.vandesr.admin.entity.VandesrMenu;
import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author: nj
 * @date: 2019/1/28:下午2:30
 */
@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MenuVo implements Serializable {

    /**
     * icon: 'el-icon-lx-home',
     * index: 'dashboard',
     * title: '系统首页'
     */

    private String parentId;
    private String icon;
    private String path;
    private String title;
    private String menuId;
    private String menuCode;
    private String name;
    private List<MenuVo> children = new ArrayList<>();
    private boolean hasChildren = false;



    /**
     * 添加子节点
     * @param menuVo
     */
    public void addChildrens(MenuVo menuVo) {
        this.children.add(menuVo);
    }

}
