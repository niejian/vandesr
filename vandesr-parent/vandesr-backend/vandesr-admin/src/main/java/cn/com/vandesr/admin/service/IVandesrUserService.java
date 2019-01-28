package cn.com.vandesr.admin.service;

import cn.com.vandesr.admin.entity.VandesrUser;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author code4fun
 * @since 2019-01-25
 */
public interface IVandesrUserService extends IService<VandesrUser> {

    /**
     * 注册
     * @param email
     * @param userName
     * @param password
     * @throws Exception
     */
    void addUser(String userName, String email, String password) throws Exception;

    /**
     * 判断邮箱是否唯一
     * @param email
     * @return
     * @throws Exception
     */
    VandesrUser getUserByEmail(String email) throws Exception;

}
