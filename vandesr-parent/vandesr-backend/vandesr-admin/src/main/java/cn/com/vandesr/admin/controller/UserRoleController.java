package cn.com.vandesr.admin.controller;/**
 * Created by niejian on 2019/8/17.
 */

import cn.com.vandesr.admin.entity.VandesrRole;
import cn.com.vandesr.admin.service.IVandesrUserRoleService;
import cn.com.vandesr.backend.common.dto.BaseResponseDto;
import cn.com.vandesr.backend.common.instance.CommonInstance;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author niejian
 * @date 2019/8/17
 */
@CrossOrigin
@Slf4j
@RestController
@RequestMapping(value = "/userRole")
public class UserRoleController {

    @Autowired
    private IVandesrUserRoleService userRoleService;

    @PostMapping(value = "/list")
    public BaseResponseDto<List<VandesrRole>> getUserRoleList(@RequestBody JSONObject jsonObject) {
        BaseResponseDto<Boolean> baseResponseDto = new BaseResponseDto<>();
        Boolean isSuccess = CommonInstance.FAIL;
        String responseMsg = CommonInstance.FAIL_MSG;
        Integer responseCode = CommonInstance.FAIL_CODE;
        Boolean isContinue = true;
        List<VandesrRole> roleList = new ArrayList<>();

        try {
            String userId = jsonObject.optString("userId", null);
            if (StringUtils.isEmpty(userId)) {
                isContinue = false;
                responseMsg = "请选择用户信息";
            }

            if (isContinue) {
                roleList = this.userRoleService.getRoleList(userId);
                responseMsg = CommonInstance.SUCCESS_MSG;
                responseCode = CommonInstance.SUCCESS_CODE;
                isSuccess = CommonInstance.SUCCESS;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return baseResponseDto.success(isSuccess)
                .responseMsg(responseMsg)
                .responseCode(responseCode)
                .data(roleList);
    }

}
