package com.shekhar.dictionary.config;

import com.shekhar.dictionary.model.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;

@ContextConfiguration(classes = LocalRedisConfig.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class LocalRedisConfigTest {

	@Inject
	private JedisConnectionFactory jedisConnectionFactory;

	@Inject
	private StringRedisTemplate redisTemplate;

	@Test
	public void testJedisConnectionFactory() {
		Assert.assertNotNull(jedisConnectionFactory);
	}

	@Test
	public void testRedisTemplate() {
		Assert.assertNotNull(redisTemplate);
	}


	@Test
	public void testGetAndSet() {
		System.out.println(redisTemplate.opsForValue().get("hello"));
		// 通过ValueOperation设置值
		redisTemplate.opsForValue().set("hello", "world");
		System.out.println(redisTemplate.opsForValue().get("hello"));
		// 通过RedisConnection设置值
		String result = redisTemplate.execute(new RedisCallback<String>() {
			@Override
			public String doInRedis(RedisConnection redisConnection) throws DataAccessException {
				redisConnection.set("hello".getBytes(), "haha".getBytes());
				return new String(redisConnection.get("hello".getBytes()));
			}
		});
		System.out.println(result);
	}

	@Test
	public void testHash() {
		User user1 = new User("user1ID", "User 1");
		User user2 = new User("user2ID", "User 2");

		// userdao.add(user)
		// 
		redisTemplate.opsForHash().put(user1.getObjectKey(), user1.getKey(), user1);
		redisTemplate.opsForHash().put(user2.getObjectKey(), user2.getKey(), user2);
		// hkey field value
		// "User" "user1Id" user1.toString
		// "User" "user2Id" user2.toString

		// 从redis中读取对象 userdao.get()
//		User user3 = (User) redisTemplate.opsForHash().get(user1.getObjectKey(), user1.getKey());
//		System.out.println(user3);

//		redisTemplate.opsForHash().delete(user1.getObjectKey(), user1.getKey());
//		user3 = (User) redisTemplate.opsForHash().get(user1.getObjectKey(), user1.getKey());
//		System.out.println(user3);
	}



}
