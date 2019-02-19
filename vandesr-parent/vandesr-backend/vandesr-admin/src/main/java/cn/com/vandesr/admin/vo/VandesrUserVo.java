package cn.com.vandesr.admin.vo;

import cn.com.vandesr.admin.entity.VandesrUser;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * @author: nj
 * @date: 2019/1/28:上午10:43
 */
@Data
@ToString
public class VandesrUserVo extends VandesrUser implements Serializable {
    /**用户角色信息*/
    private List<String> roles;
    /**用户菜单信息*/
    private List<MenuVo> userMenus;
}
