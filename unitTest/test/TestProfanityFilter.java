package test;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import bad_words_filter.ProfanityFilter;

/**
 * @author Lyen
 *
 */
public class TestProfanityFilter {
	private String[] badWords_en;
	private String[] badWords_mix;
	private String[] dictionaryNames;
	private ProfanityFilter badWordsFilter;

	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();

	@Before
	public void generateBadWords() {
		System.setOut(new PrintStream(outContent));
		System.setErr(new PrintStream(errContent));
		badWords_mix = new String[] { "幹你娘卡好", "你帳密多少啊？",
				"來自英雄聯盟的髒話檔案：淫媚、無碼H漫", "贛林涼",
				"幹這是什麼東西，來試試看字串長一點會不會發生什麼問題，啦啦啦，肏??a插b。一般玩家能輸入的字串上限通常設多少呢？",
				"幹幹幹幹幹幹幹幹幹幹嘛", "asdfCum", "suck your dick, asshole!",
				"cum Cum", "cumasdf", "[ GM]", "不要抓我，我沒有說髒話！！！",
				"suck my dick suck my mother fucking dick",
				"suck my Dick suck my mother Fucking Dick", "大家來學ㄅㄆㄇ",
				"123asshole", "asshole123", "asshole", "123asshole123",
				"ㄅㄆㄇㄈㄉㄊㄋㄌㄍㄎㄏㄐㄑㄒㄓㄔㄕㄖㄗㄘㄅㄅㄅㄅㄙㄚㄛㄅㄜㄝㄞㄟㄠㄅㄡㄢㄣㄤㄥㄅㄦㄧㄨㄩㄅ" };
		dictionaryNames = new String[] { "badwords_zh_TW.txt",
				"badwords_en_US.txt" };
		badWordsFilter = new ProfanityFilter();
		// import two files for profanity filter
		badWordsFilter.buildDictionaryTree(dictionaryNames[0]);
		badWordsFilter.buildDictionaryTree(dictionaryNames[1]);
	}

	@After
	public void cleanUpStreams() {
		// setup
		System.setOut(null);
		System.setErr(null);
	}

	@Test
	public void testDuplicate() {
		// setup
		ProfanityFilter filter = new ProfanityFilter();
		filter.buildDictionaryTree("demo_eng.txt");

		String badWords = "abcd";
		String expected = "****";
		String actual = filter.filterBadWords(badWords);
		assertEquals(expected, actual);
	}

	// Chinese 
	@Test
	public void testDuplicate2() {
		ProfanityFilter filter = new ProfanityFilter();
		filter.buildDictionaryTree("demo_zh.txt");

		String badWords = "幹你娘卡好";
		String expected = "*****";
		String actual = filter.filterBadWords(badWords);
		assertEquals(expected, actual);
	}

	// Chinese 
	@Test
	public void testPerformance() {
		String expected = "***** *****啊？ 來自英雄聯盟的髒話檔案：**、**** *林涼 "
				+ "*這是什麼東西，來試試看字串長一點會不會發生什麼問題，啦啦啦，肏??a**。一般玩家能輸入的字串上限通常設多少呢？ "
				+ "**********嘛 asdf*** suck your dick, *******! "
				+ "cum *** cumasdf ***** "
				+ "不要抓我，我沒有說髒話！！！ suck my dick suck my mother ****ing dick "
				+ "suck my **** suck my mother ******* **** "
				+ "大家來學*** 123******* *******123 ******* 123*******123 ********************************************* ";
		String filteredWords;
		String[] appliedBadWords = badWords_mix;
		for (int i = 0; i < appliedBadWords.length; i++) {
			filteredWords = badWordsFilter.filterBadWords(appliedBadWords[i]);
			System.out.print(filteredWords + " ");
		}
		assertEquals(expected, outContent.toString());
	}
}
