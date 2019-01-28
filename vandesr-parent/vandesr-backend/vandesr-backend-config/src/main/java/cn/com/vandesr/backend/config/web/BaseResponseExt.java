package cn.com.vandesr.backend.config.web;

import lombok.Data;
import lombok.ToString;

/**
 * @author: nj
 * @date: 2019/1/25:上午11:40
 */
@Data
@ToString
public class BaseResponseExt<T> extends BaseResponse {
    private T data;
}
