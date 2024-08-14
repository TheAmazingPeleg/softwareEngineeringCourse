package Dictionary;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.util.Scanner;

public class Dictionary {
	CacheReplacementPolicy LRU;
	CacheReplacementPolicy LFU;
	CacheManager exists;
	CacheManager notExists;
	BloomFilter bf;
	String[] files;

	public Dictionary(String... files) {
		this.LRU = new LRU();
		this.LFU = new LFU();
		this.exists = new CacheManager(400, new LRU());
		this.notExists = new CacheManager(100, new LFU());
		this.bf = new BloomFilter(256, "MD5", "SHA1");
		this.files = files.clone();
		for (String file : files) {
			try {
				Scanner buffer = new Scanner(new File(Paths.get(file).toUri()));
				while (buffer.hasNext())
					bf.add(buffer.next());
			} catch (FileNotFoundException e) {
				System.out.println("Invalid file name");
			}
		}
	}

	public boolean query(String word) {
		if (this.notExists.query(word)) {
			return false;
		} else {
			if (this.exists.query(word)) {
				return true;
			} else if (this.bf.contains(word)) {
				this.exists.add(word);
				return true;
			} else {
				this.notExists.add(word);
				return false;
			}
		}
	}

	public boolean challenge(String word) {
		if (IOSearcher.search(word, this.files)) {
			this.exists.add(word);
			return true;
		} else {
			this.notExists.add(word);
			return false;
		}
	}
}