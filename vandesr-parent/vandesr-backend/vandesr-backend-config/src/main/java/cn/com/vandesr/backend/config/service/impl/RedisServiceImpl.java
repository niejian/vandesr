//package cn.com.vandesr.backend.config.service.impl;
//
//import cn.com.vandesr.backend.config.service.RedisService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.stereotype.Service;
//
//import java.util.concurrent.TimeUnit;
//
///**
// * @author: nj
// * @date: 2019/1/24:下午4:08
// */
//@Service(value = "redisService")
//public class RedisServiceImpl implements RedisService {
//    @Autowired
//    private RedisTemplate redisTemplate;
//    /**
//     * 设置缓存
//     *
//     * @param key     键
//     * @param value   值
//     * @param timeout 超时时间， 毫秒
//     * @throws Exception
//     */
//    @Override
//    public void setValue(String key, Object value, Long timeout) throws Exception {
//        if (null != timeout && timeout > 0) {
//            redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.MILLISECONDS);
//        } else {
//            redisTemplate.opsForValue().set(key, value, timeout);
//
//        }
//    }
//
//    @Override
//    public Object getValue(String key, Class returnClazz) throws Exception {
//        Object o = this.redisTemplate.opsForValue().get(key);
//        return o;
//    }
//}
