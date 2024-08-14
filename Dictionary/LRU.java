package Dictionary;

import java.util.LinkedList;

public class LRU implements CacheReplacementPolicy {
	private LinkedList<String> wordList;

	public LRU() {
		this.wordList = new LinkedList<String>();
	}

	public void add(String word) {
		this.wordList.remove(word);
		this.wordList.add(word);
	}

	public String remove() {
		return this.wordList.getFirst();
	}
}
