package com.shekhar.dictionary.dao;

import com.shekhar.dictionary.config.LocalRedisConfig;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

@ContextConfiguration(classes = {LocalRedisConfig.class})
@RunWith(SpringJUnit4ClassRunner.class)
public class DictionaryDaoTest {

	@Inject
	private DictionaryDao dictionaryDao;

	@Inject
	private StringRedisTemplate redisTemplate;

	@Test
	public void testAddWordWithItsMeaningToDictionary() {
		String meaning = "To move forward with a bounding, drooping motion.";
		Long index = dictionaryDao.addWordWithItsMeaningToDictionary("lollop",
				meaning);
		System.out.println(index);
		assertThat(index, is(notNullValue()));
		assertThat(index, is(equalTo(1L)));
	}

	@Test
	public void shouldAddMeaningToAWordIfItExists() {
		Long index = dictionaryDao.addWordWithItsMeaningToDictionary("lollop",
				"To move forward with a bounding, drooping motion.");
		assertThat(index, is(notNullValue()));
		assertThat(index, is(equalTo(1L)));
		index = dictionaryDao.addWordWithItsMeaningToDictionary("lollop",
				"To hang loosely; droop; dangle.");
		assertThat(index, is(equalTo(2L)));
	}

	@Test
	public void shouldGetAllTheMeaningForAWord() {
		setupOneWord();
		List<String> allMeanings = dictionaryDao
				.getAllTheMeaningsForAWord("lollop");
		assertThat(allMeanings.size(), is(equalTo(2)));
		assertThat(
				allMeanings,
				hasItems("To move forward with a bounding, drooping motion.",
						"To hang loosely; droop; dangle."));
	}

	private Matcher<List<String>> hasItems(final String... str) {
		return new Matcher<List<String>>() {
			@Override
			public boolean matches(Object o) {
				boolean hasFlag = true;
				List list = (List)o;
				for (String s : str) {
					if(!list.contains(s)) hasFlag = false;
				}
				return hasFlag;
			}

			@Override
			public void _dont_implement_Matcher___instead_extend_BaseMatcher_() { }

			@Override
			public void describeTo(Description description) { }
		};
	}

	private void setupOneWord() {
		dictionaryDao.removeWord("lollop");
		dictionaryDao.addWordWithItsMeaningToDictionary("lollop",
				"To move forward with a bounding, drooping motion.");
		dictionaryDao.addWordWithItsMeaningToDictionary("lollop",
				"To hang loosely; droop; dangle.");
	}

	@Test
	public void shouldDeleteAWordFromDictionary() throws Exception {
		setupOneWord();
		dictionaryDao.removeWord("lollop");
		List<String> allMeanings = dictionaryDao
				.getAllTheMeaningsForAWord("lollop");
		assertThat(allMeanings.size(), is(equalTo(0)));
	}

	@Test
	public void shouldDeleteMultipleWordsFromDictionary() {
		setupTwoWords();
		dictionaryDao.removeWords("fain", "lollop");
		List<String> allMeaningsForLollop = dictionaryDao
				.getAllTheMeaningsForAWord("lollop");
		List<String> allMeaningsForFain = dictionaryDao
				.getAllTheMeaningsForAWord("fain");
		assertThat(allMeaningsForLollop.size(), is(equalTo(0)));
		assertThat(allMeaningsForFain.size(), is(equalTo(0)));
	}

	private void setupTwoWords() {
		dictionaryDao.removeWord("lollop");
		dictionaryDao.removeWord("fain");
		dictionaryDao.addWordWithItsMeaningToDictionary("lollop", "aa");
		dictionaryDao.addWordWithItsMeaningToDictionary("fain", "aa");
	}
}
