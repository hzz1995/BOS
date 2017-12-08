package cn.itcast.fore.test;

import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:applicationContext.xml")
public class TestRedis {

	@Autowired
	RedisTemplate<String, String> redisTemplate;
	@Test
	public void test() {
		ValueOperations<String, String> opsForValue = redisTemplate.opsForValue();
		opsForValue.set("123", "实现理想");
		
		System.out.println(redisTemplate.opsForValue().get("123"));
		//redisTemplate.delete("胡正再");
		System.out.println(redisTemplate.opsForValue().get("123"));
		//redisTemplate.opsForValue().set("hzz", "ni", 30, TimeUnit.MINUTES);
		
	}

}
