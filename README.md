Profanity Filter. 

Usage Example:
	// initialize the profanity filter
	ProfanityFilter filter = new ProfanityFilter();
	
	// load dictionary, where bad words are located.
	filter.buildDictionaryTree("demo_eng.txt");
	
	String badWords = "fuck you";
	
	String result = filter.filterBadWords(badWords);
	// result becomes "**** you"
	System.out.println(result);


髒話過濾器

使用範例：
	// 初始化髒話過濾器
	ProfanityFilter filter = new ProfanityFilter();
	
	// 把裝有髒話的字典檔讀進來
	filter.buildDictionaryTree("demo_eng.txt");
	
	String badWords = "fuck you";
	
	String result = filter.filterBadWords(badWords);
	// 參數result會等於「**** you」
	System.out.println(result);
