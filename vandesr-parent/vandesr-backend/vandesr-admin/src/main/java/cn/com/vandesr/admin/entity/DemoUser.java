package cn.com.vandesr.admin.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * @author: nj
 * @date: 2019/1/21:下午1:56
 */
@Data
@ToString
@TableName("demo_user")
public class DemoUser {
    /**
     *  `user_id` varchar(32) NOT NULL,
     *   `user_name` varchar(16) DEFAULT '',
     *   `user_code` varchar(32) DEFAULT '',
     *   `user_password` varchar(32) NOT NULL,
     *   `user_email` varchar(32) DEFAULT '',
     *   `nick_name` varchar(16) DEFAULT '',
     *   `user_img` varchar(64) DEFAULT '',
     *   `create_date` datetime(3) DEFAULT NULL,
     *   `update_date` datetime(3) DEFAULT
     */

    private String userId;
    private String userName;
    private String userCode;
    private String userPassword;
    private String userEmail;
    private String nickName;
    private String userImg;
    private Date createDate;
    private Date updateDate;
}
