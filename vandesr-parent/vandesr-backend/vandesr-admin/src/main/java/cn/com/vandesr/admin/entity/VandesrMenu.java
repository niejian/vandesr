package cn.com.vandesr.admin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
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


}
