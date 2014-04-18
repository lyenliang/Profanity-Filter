package bad_words_filter;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author Lyen
 * 
 */
public class ProfanityFilter {
	private TreeNode root;
	private int badWordStart;
	private int badWordEnd;
	private boolean isSuspicionFound;
	private boolean[] asteriskMark;

	public static void main(String[] args) {
		ProfanityFilter badWordsFilterHash = new ProfanityFilter();
		badWordsFilterHash.start(args);
	}

	public ProfanityFilter() {
		root = new TreeNode();
	}

	/**
	 * Ask user to input something, then display the filtered text
	 * 
	 * @param args
	 */
	public void start(String[] args) {
		buildDictionaryTree(args[0]); // bad words list file name
		String userInput = readUserInput();
		// show filtered bad words
		String filteredBadWords = filterBadWords(userInput);
		System.out.println(filteredBadWords);
	}

	/**
	 * Setup a tree for profanity filter
	 * 
	 * @param fileName
	 */
	public void buildDictionaryTree(String fileName) {
		String line;
		BufferedReader in = null;
		try {
			in = new BufferedReader(new FileReader(fileName));
			while ((line = in.readLine()) != null) {
				// for each bad word
				addToTree(line, 0, root);
			}

		} catch (FileNotFoundException e) { // FileReader
			e.printStackTrace();
		} catch (IOException e) { // readLine
			e.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @param badWordLine
	 * @param characterIndex
	 *            : index of each letter in a bad word
	 * @param node
	 *            that iterates through the tree
	 */
	private void addToTree(String badWordLine, int characterIndex, TreeNode node) {
		if (characterIndex < badWordLine.length()) {
			Character c = badWordLine.charAt(characterIndex);
			if (!node.containsChild(c)) {
				node.addChild(c);
			}
			node = node.getChildByLetter(c);
			// check if this is the last letter
			if (characterIndex == (badWordLine.length() - 1)) {
				// mark this letter as the end of a bad word
				node.setEnd(true);
			} else {
				// add next letter
				addToTree(badWordLine, characterIndex + 1, node);
			}
		}
	}

	/**
	 * Ask the user to input something
	 * 
	 * @return string that user inputs
	 */
	private String readUserInput() {
		String userInput = null;
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Say something: ");
		try {
			userInput = br.readLine();
		} catch (IOException ioe) {
			System.out.println("readLine error!!!");
			System.exit(1);
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return userInput;
	}

	/**
	 * @param userInput
	 * @return string with bad words filtered
	 */
	public String filterBadWords(String userInput) {
		init(userInput.length());
		// for each character in a bad word
		for (int i = 0; i < userInput.length(); i++) {
			searchAlongTree(userInput, i, root);
		}
		return applyAsteriskMark(userInput);
	}

	private void init(int length) {
		asteriskMark = new boolean[length];
		for (int i = 0; i < length; i++) {
			asteriskMark[i] = false;
		}
		badWordStart = -1;
		badWordEnd = -1;
		isSuspicionFound = false;
	}

	private void searchAlongTree(String pUserInput, int characterIndex,
			TreeNode node) {
		if (characterIndex < pUserInput.length()) {
			// get the corresponding letter
			Character letter = pUserInput.charAt(characterIndex);
			if (node.containsChild(letter)) {
				// find a word whose first letter is equal to one of the bad
				// words' first letter
				if (isSuspicionFound == false) {
					isSuspicionFound = true;
					badWordStart = characterIndex;
				}
				// if this is the final letter of a bad word
				if (node.getChildByLetter(letter).isEnd()) {
					badWordEnd = characterIndex;
					markAsterisk(badWordStart, badWordEnd);
				}
				node = node.getChildByLetter(letter);
				searchAlongTree(pUserInput, characterIndex + 1, node);
			} else {
				// initialize some parameters
				isSuspicionFound = false;
				badWordStart = -1;
				badWordEnd = -1;
			}
		}
	}

	/**
	 * Replace some of the letters in userInput as * according to asteriskMark
	 * 
	 * @param userInput
	 * @return string with bad words filtered
	 */
	private String applyAsteriskMark(String userInput) {
		StringBuilder filteredBadWords = new StringBuilder(userInput);
		for (int i = 0; i < asteriskMark.length; i++) {
			if (asteriskMark[i] == true) {
				filteredBadWords.setCharAt(i, '*');
			}
		}
		return filteredBadWords.toString();
	}

	/**
	 * Identify the letters of userInput that should be marked as "*"
	 * 
	 * @param badWordStart
	 * @param badWordEnd
	 */
	private void markAsterisk(int badWordStart, int badWordEnd) {
		for (int i = badWordStart; i <= badWordEnd; i++) {
			asteriskMark[i] = true;
		}
	}
}
