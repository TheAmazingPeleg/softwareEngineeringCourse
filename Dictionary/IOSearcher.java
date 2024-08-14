package Dictionary;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class IOSearcher {
	public static boolean search(String word, String... files) {
		for (String file : files) {
			try {
				Stream<String> stream;
				stream = Files.lines(Paths.get(file));
				if (stream.anyMatch(line -> line.contains(word))) {
					stream.close();
					return true;
				} else {
					stream.close();
				}
			} catch (IOException e) {
				System.out.println("Error reading file: " + file);
			}
		}
		return false;
	}
}
