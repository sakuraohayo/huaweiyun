package javaweb.remember.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

public interface RedisService {
    /**
     * 存储键值对
     * @param key 键
     * @param value 值
     */
    void set(String key, String value);

    /**
     * 根据key来获取value
     * @param key 键
     * @return 值
     */
    String get(String key);

    /**
     * 给键值对设置过期时间
     * @param key 要设置的键值对的键
     * @param expire 过期时间（单位为秒）
     * @return 是否设置成功
     */
    boolean expire(String key, long expire);

    /**
     * 删除键值对
     * @param key 要删除的键值对的键
     */
    void delete(String key);


}
