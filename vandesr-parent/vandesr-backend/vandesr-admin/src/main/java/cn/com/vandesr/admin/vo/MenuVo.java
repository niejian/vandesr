package cn.com.vandesr.admin.vo;

import cn.com.vandesr.admin.entity.VandesrMenu;
import lombok.Data;
import lombok.ToString;

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
public class MenuVo implements Serializable {

    /**
     * icon: 'el-icon-lx-home',
     * index: 'dashboard',
     * title: '系统首页'
     */

    private String parentId;
    private String icon;
    private String index;
    private String title;
    private String menuId;
    private String menuCode;
    private List<MenuVo> childrens = new ArrayList<>();



    /**
     * 添加子节点
     * @param menuVo
     */
    public void addChildrens(MenuVo menuVo) {
        this.childrens.add(menuVo);
    }

}
