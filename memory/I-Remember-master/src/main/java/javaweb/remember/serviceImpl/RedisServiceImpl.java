package javaweb.remember.serviceImpl;

import javaweb.remember.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisServiceImpl implements RedisService {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    @Override
    public void set(String key, String value){
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        ops.set(key, value);
    }

    @Override
    public String get(String key){
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        return ops.get(key);
    }

    @Override
    public boolean expire(String key, long expire) {
        return stringRedisTemplate.expire(key, expire, TimeUnit.SECONDS);
    }

    @Override
    public void delete(String key) {
        stringRedisTemplate.delete(key);
    }
}
