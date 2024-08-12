package Scrabble;

public class Tile {
	public final char letter;
	public final int score;
	public static final Tile[] defaultLetters = {
			new Tile('A', 1), new Tile('B', 3), new Tile('C', 3), new Tile('D', 2),
			new Tile('E', 1), new Tile('F', 4), new Tile('G', 2), new Tile('H', 4),
			new Tile('I', 1), new Tile('J', 8), new Tile('K', 5), new Tile('L', 1),
			new Tile('M', 3), new Tile('N', 1), new Tile('O', 1), new Tile('P', 3),
			new Tile('Q', 10), new Tile('R', 1), new Tile('S', 1), new Tile('T', 1),
			new Tile('U', 1), new Tile('V', 4), new Tile('W', 4), new Tile('X', 8),
			new Tile('Y', 4), new Tile('Z', 10)
	};

	private Tile(char letter, int score) {
		if((int) letter < 65 || (int) letter > 90)
			throw new IllegalArgumentException("Invalid value as a letter A-Z");
		this.letter = letter;
		this.score = score;
	}

	public boolean equals(Tile t) {
		if (this.letter != t.letter)
			return false;
		return true;
	}
	
	public Tile dup() {
		return new Tile(this.letter, this.score);
	}

	public static class Bag {
		private static Bag singleBag = null;
		public int[] tilesAmount;
		public Tile[] tilesScore;
		private int tilesTotal;

		private Bag(int[] tilesAmount, Tile[] tilesScore, int tileTotal) {
			this.tilesAmount = tilesAmount;
			this.tilesScore = tilesScore;
			this.tilesTotal = tileTotal;
		}

		public static Bag getBag() {
			if (singleBag == null) {
				singleBag = new Bag(new int[] { 9, 2, 2, 4, 12, 2, 3, 2, 9, 1, 1, 4, 2, 6, 8, 2, 1, 6, 4, 6, 4, 2, 2, 1, 2, 1 }, Tile.defaultLetters, 98);
			}
			return singleBag;
		}

		public Tile getRand() {
			if (this.tilesTotal > 0) {
				int randTile = (int) Math.floor(Math.random() * 26);
				int limit = 0;
				while (this.tilesAmount[randTile] == 0 && limit < 26) {
					randTile = (randTile + 1) % 26;
					limit++;
				}
				if (limit == 26) {
					this.tilesTotal = 0;
					return null;
				}
				this.tilesAmount[randTile]--;
				this.tilesTotal--;
				return this.tilesScore[randTile];
			} else {
				return null;
			}
		}

		public Tile getTile(char letter) {
			int tileIndex = (int) letter - 65;
			if (this.tilesTotal > 0 && (tileIndex >= 0 && tileIndex <= 25)) {
				if (this.tilesAmount[tileIndex] > 0) {
					this.tilesAmount[tileIndex]--;
					this.tilesTotal--;
					return this.tilesScore[tileIndex];
				} else {
					return null;
				}
			} else {
				return null;
			}
		}

		public void put(Tile t) {
			int[] maxPerTile = { 9, 2, 2, 4, 12, 2, 3, 2, 9, 1, 1, 4, 2, 6, 8, 2, 1, 6, 4, 6, 4, 2, 2, 1, 2, 1 };
			int tileIndex = (int) t.letter - 65;
			if (this.tilesTotal < 98
					&& tileIndex >= 0 && tileIndex <= 25
					&& maxPerTile[tileIndex] > this.tilesAmount[tileIndex]) {
				this.tilesAmount[tileIndex]++;
				this.tilesTotal++;
			}
		}

		public int size() {
			return this.tilesTotal;
		}

		public int[] getQuantities() {
			return this.tilesAmount.clone();
		}
	}
}
