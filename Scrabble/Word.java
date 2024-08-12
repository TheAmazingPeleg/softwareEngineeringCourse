package Scrabble;

public class Word {
    private Tile[] tiles;
    private int row, col;
    private boolean vertical;    
    

    public Word(Tile[] tiles, int row, int col, boolean vertical) {
    	this.tiles = tiles.clone();
        this.row = row;
        this.col = col;
        this.vertical = vertical;
    }

    public Tile[] getTiles() {
        return this.tiles;
    }

    public int getRow() {
        return this.row;
    }

    public int getCol() {
        return this.col;
    }

    public boolean getVertical() {
        return this.vertical;
    }
    
    @Override
    public boolean equals(Object obj) {
    	Word word = (Word) obj;
        if (this.tiles.length != word.tiles.length)
            return false;
        for (int i = 0; i < word.tiles.length; i++) {
            if (this.tiles[i].letter != word.tiles[i].letter)
                return false;
        }
        return true;
    }
}
