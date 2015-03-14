package com.shekhar.dictionary.dao;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;

@Repository
public class DictionaryDao {

	private static final String ALL_UNIQUE_WORDS = "all-unique-words";

	private StringRedisTemplate redisTemplate;

	@Inject
	public DictionaryDao(StringRedisTemplate redisTemplate){
		this.redisTemplate = redisTemplate;
	}

	public Long addWordWithItsMeaningToDictionary(String word, String meaning) {
		Long index = redisTemplate.opsForList().rightPush(word, meaning);
		redisTemplate.getConnectionFactory().getConnection();
		return index;
	}

	public List<String> getAllTheMeaningsForAWord(String word) {
		// opsForList返回ListOperations操作List，还可以返回opsForSet,opsForHash,opsForValue等
		List<String> meanings = redisTemplate.opsForList().range(word, 0, -1);
		return meanings;
	}

	public void removeWord(String word) {
		redisTemplate.delete(Arrays.asList(word));
	}

	public void removeWords(String... words) {
		redisTemplate.delete(Arrays.asList(words));
	}
}
