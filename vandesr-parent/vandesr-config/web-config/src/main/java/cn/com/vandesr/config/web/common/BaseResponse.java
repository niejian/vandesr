package cn.com.vandesr.config.web.common;

import lombok.Data;
import lombok.ToString;

/**
 * @author: nj
 * @date: 2019/1/24:下午1:33
 */
@Data
@ToString
public class BaseResponse {
    private String responseMsg;
    private Integer responseCode;
    private Boolean success;

    public BaseResponse() {
        this.responseMsg = "请求成功";
        this.responseCode = 0;
        this.success = true;
    }
}
