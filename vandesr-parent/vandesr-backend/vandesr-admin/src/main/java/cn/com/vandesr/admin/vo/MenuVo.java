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
    /**vue 相对于 compenents的路径*/
    private String path;
    private String title;
    private String menuId;
    private String menuCode;
    private String name;
    private List<MenuVo> children = new ArrayList<>();
    private boolean hasChildren = false;
    /**路由路径*/
    private String routerPath;
    private String parentIds;


    /**
     * 添加子节点
     * @param menuVo
     */
    public void addChildrens(MenuVo menuVo) {
        this.children.add(menuVo);
    }

    public void setRouterPath() {
        int index = this.getPath().lastIndexOf("/");
        if (index > 0) {
            this.routerPath = this.getPath().substring(index);
        } else {
            this.routerPath = this.getPath();
        }
    }



}
