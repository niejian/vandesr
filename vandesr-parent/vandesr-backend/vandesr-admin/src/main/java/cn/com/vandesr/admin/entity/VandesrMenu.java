package cn.com.vandesr.admin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author code4fun
 * @since 2019-01-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class VandesrMenu implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String menuCode;

    private String menuName;

    private String menuIcon;

    private String menuUrl;

    private Integer parentId;

    private String parentIds;

    private String permissions;

    private Integer deleteFlag;

    private Date createDate;

    private String createUserCode;

    private String createUserName;

    private Date updateDate;

    private String updateUserCode;

    private String updateUserName;

    private boolean leaf;

//    private List<VandesrMenu> subs;
//
//    public void addSub(VandesrMenu menu) {
//        this.subs.add(menu);
//    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VandesrMenu menu = (VandesrMenu) o;
        return Objects.equals(id, menu.id) &&
                Objects.equals(menuCode, menu.menuCode) &&
                Objects.equals(menuName, menu.menuName) &&
                Objects.equals(menuIcon, menu.menuIcon) &&
                Objects.equals(menuUrl, menu.menuUrl) &&
                Objects.equals(parentId, menu.parentId) &&
                Objects.equals(parentIds, menu.parentIds) &&
                Objects.equals(permissions, menu.permissions) &&
                Objects.equals(deleteFlag, menu.deleteFlag) &&
                Objects.equals(createDate, menu.createDate) &&
                Objects.equals(createUserCode, menu.createUserCode) &&
                Objects.equals(createUserName, menu.createUserName) &&
                Objects.equals(updateDate, menu.updateDate) &&
                Objects.equals(updateUserCode, menu.updateUserCode) &&
                Objects.equals(updateUserName, menu.updateUserName);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, menuCode, menuName, menuIcon, menuUrl, parentId, parentIds,
                permissions, deleteFlag, createDate, createUserCode, createUserName, updateDate, updateUserCode, updateUserName);
    }
}
