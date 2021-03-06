package cn.com.vandesr.admin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import lombok.*;
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
@Builder
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class VandesrRoleMenu implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer roleId;

    private Integer menuId;

    private String menuCode;

    private String menuName;

    private Integer deleteFlag;

    private Date createDate;

    private String createUserCode;

    private String createUserName;

    private Date updateDate;

    private String updateUserCode;

    private String updateUserName;


}
