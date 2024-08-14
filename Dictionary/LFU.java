package Dictionary;

import java.util.LinkedHashMap;
import java.util.Map;

public class LFU implements CacheReplacementPolicy {
	private LinkedHashMap<String, Integer> wordList;

	public LFU() {
		wordList = new LinkedHashMap<String, Integer>();
	}

	public void add(String word) {
		int amount = this.wordList.getOrDefault(word, 0) + 1;
		this.wordList.put(word, amount);
	}

	public String remove() {
		int min = Integer.MAX_VALUE;
		String word = null;
		for (Map.Entry<String, Integer> element : wordList.entrySet()) {
			if (element.getValue() < min) {
				min = element.getValue();
				word = element.getKey();
			}
		}
		return word;
	}
}
