package cn.com.vandesr.backend.config.service;

/**
 * @author: nj
 * @date: 2019/1/24:下午4:08
 */
public interface RedisService {
    /**
     * 设置缓存
     * @param key 键
     * @param value 值
     * @param timeout 超时时间， 毫秒
     * @throws Exception
     */
    void setValue(String key, Object value, Long timeout) throws Exception;


    Object getValue(String key, Class returnClazz) throws Exception;
}
