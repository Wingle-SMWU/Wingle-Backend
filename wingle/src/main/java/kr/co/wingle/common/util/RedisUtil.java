package kr.co.wingle.common.util;

import java.time.Duration;
import java.util.Collections;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class RedisUtil {

	private final RedisTemplate<String, String> redisTemplate;

	public static final String PREFIX_REFRESH_TOKEN = "REFRESH_TOKEN:";
	public static final String PREFIX_LOGOUT = "LOGOUT:";
	public static final String PREFIX_EMAIL = "EMAIL:";

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

	public boolean existsData(String key) {
		return redisTemplate.hasKey(key);
	}

	public void updateData(String key, String value) {
		System.out.println("In updateData");
		try {
			RedisScript script = RedisScript.of("return redis.call('SET', KEYS[1], ARGV[1], 'KEEPTTL')");
			redisTemplate.execute(script, Collections.singletonList(key), value);
		} catch (Exception e) {
			System.out.println(e);
		}

		System.out.println("Out updateData");
	}
}
