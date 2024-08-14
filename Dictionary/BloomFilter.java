package Dictionary;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.BitSet;
import java.util.HashSet;
import java.math.BigInteger;
import java.util.BitSet;

public class BloomFilter {
	BitSet bits;
	int size;
	HashSet<MessageDigest> hashFuncs;

	public BloomFilter(int size, String... hashList) {
		this.size = size;
		this.bits = new BitSet(size);
		this.hashFuncs = new HashSet<MessageDigest>();
		for (String func : hashList) {
			try {
				MessageDigest newFunc = MessageDigest.getInstance(func);
				this.hashFuncs.add(newFunc);
			} catch (NoSuchAlgorithmException e) {
				System.out.println("Invalid given algorithm name");
			}
		}
	}

	public int getWordBit(String word, MessageDigest hashFunc) {
		byte[] bts = hashFunc.digest(word.getBytes());
		return Math.abs(new BigInteger(bts).intValue()) % this.size;
	}

	public void add(String word) {
		for (MessageDigest func : this.hashFuncs)
			this.bits.set(this.getWordBit(word, func));
	}

	public boolean contains(String word) {
		for (MessageDigest func : this.hashFuncs) {
			if (!this.bits.get(getWordBit(word, func)))
				return false;
		}
		return true;
	}

	public String toString() {
		char[] bitList = new char[this.bits.length()];
		for (int i = 0; i < this.bits.length(); i++) {
			if (this.bits.get(i))
				bitList[i] = '1';
			else
				bitList[i] = '0';
		}
		return new String(bitList);
	}
}
