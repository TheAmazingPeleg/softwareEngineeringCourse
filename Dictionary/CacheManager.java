package Dictionary;

import java.util.HashSet;

public class CacheManager {
	private CacheReplacementPolicy crp;
	// Holding unique list
	private HashSet<String> wordList;
	private int size;
	private int maxSize;

	public CacheManager(int maxSize, CacheReplacementPolicy crp) {
		this.crp = crp;
		this.maxSize = maxSize;
		this.wordList = new HashSet<String>();
		this.size = 0;
	}

	public void add(String word) {
		this.crp.add(word);
		if (this.maxSize == this.size) {
			String wordToRemove = crp.remove();
			wordList.remove(wordToRemove);
		} else {
			this.size++;
		}
		wordList.add(word);
	}

	public boolean query(String word) {
		return this.wordList.contains(word);
	}
}
