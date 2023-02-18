package kr.co.wingle.common.util;

import java.time.Duration;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class RedisUtil {

	private final RedisTemplate<String, String> redisTemplate;

	public static final String PREFIX_REFRESH_TOKEN = "REFRESH_TOKEN:";
	public static final String PREFIX_LOGOUT = "LOGOUT:";

	public String getData(String key) {
		ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
		return valueOperations.get(key);
	}

	public void setData(String key, String value) {
		ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
		valueOperations.set(key, value);
	}

	public void setDataExpire(String key, String value, long duration) {
		ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
		valueOperations.set(key, value, Duration.ofMillis(duration));
	}

	public void deleteData(String key) {
		redisTemplate.delete(key);
	}
}
