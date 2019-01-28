package cn.com.vandesr.backend.config.web;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author: nj
 * @date: 2019/1/25:上午11:36
 */
@Data
@ToString
public class BaseResponse implements Serializable {
    private String responseMsg;
    private Integer responseCode;
    private Boolean success;

    public BaseResponse() {
        this.responseMsg = "请求成功";
        this.responseCode = 0;
        this.success = true;
    }
}
