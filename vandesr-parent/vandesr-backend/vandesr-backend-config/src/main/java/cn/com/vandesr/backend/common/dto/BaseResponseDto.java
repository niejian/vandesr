package cn.com.vandesr.backend.common.dto;

import cn.com.vandesr.backend.common.instance.CommonInstance;
import lombok.*;

import java.io.Serializable;

/**
 * @author: nj
 * @date: 2019-07-19:10:49
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BaseResponseDto<T extends Object> implements Serializable {
    private Boolean success = CommonInstance.FAIL;
    private Integer responseCode = CommonInstance.FAIL_CODE;
    private String responseMsg = CommonInstance.FAIL_MSG;
    private T data;

    public BaseResponseDto success(Boolean success) {
        this.success = success;
        return this;
    }

    public BaseResponseDto responseCode(Integer responseCode) {
        this.responseCode = responseCode;
        return this;
    }

    public BaseResponseDto responseMsg(String responseMsg) {
        this.responseMsg = responseMsg;
        return this;
    }

    public BaseResponseDto data(T data) {
        this.data = data;
        return this;
    }



}
