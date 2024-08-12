package Scrabble;

import java.util.ArrayList;
import java.util.Stack;


public class Board {
	public static Board singleBoard = null;
	private Tile[][] board = new Tile[15][15];
	private static ArrayList<int[]> doubleWordPos = new ArrayList<>();
	private static ArrayList<int[]> tripleWordPos = new ArrayList<>();
	
	private void initDupWords(){
		this.doubleWordPos.add(new int[] {1, 1});
		this.doubleWordPos.add(new int[] {2, 2});
		this.doubleWordPos.add(new int[] {3, 3});
		this.doubleWordPos.add(new int[] {4, 4});
		this.doubleWordPos.add(new int[] {7, 7});
		this.doubleWordPos.add(new int[] {10, 10});
		this.doubleWordPos.add(new int[] {11, 11});
		this.doubleWordPos.add(new int[] {12, 12});
		this.doubleWordPos.add(new int[] {13, 13});
		this.doubleWordPos.add(new int[] {1, 13});
		this.doubleWordPos.add(new int[] {2, 12});
		this.doubleWordPos.add(new int[] {3, 11});
		this.doubleWordPos.add(new int[] {4, 10});
		this.doubleWordPos.add(new int[] {10, 4});
		this.doubleWordPos.add(new int[] {11, 3});
		this.doubleWordPos.add(new int[] {12, 2});
		this.doubleWordPos.add(new int[] {13, 1});

		this.tripleWordPos.add(new int[] {0, 0});
		this.tripleWordPos.add(new int[] {0, 7});
		this.tripleWordPos.add(new int[] {0, 14});
		this.tripleWordPos.add(new int[] {7, 0});
		this.tripleWordPos.add(new int[] {7, 14});
		this.tripleWordPos.add(new int[] {14, 0});
		this.tripleWordPos.add(new int[] {14, 7});
		this.tripleWordPos.add(new int[] {14, 14});
	}
	
	//Array thats hold object - WordScore (word + it's score)
	private ArrayList<WordScore> wordsList;
	private class WordScore {
		public Word word;
		public int score;
		public WordScore(Word word, int score) {
			this.word = word;
			this.score = score;
		}
	}
	
	private Board() {
		this.board = new Tile[15][15];
		this.wordsList = new ArrayList<>();
		this.initDupWords();
	}
	
	//Calculate for a given word it's multiplier score
	private int getWordMultiplier(Word word) {
    	int dup = 1;
    	for (int i = 0; i < word.getTiles().length; i++) {
    		//Dup or Triple only if the user placed the letter itself
    		if(word.getTiles()[i] == null)
    			continue;
            int currentRow = word.getVertical() ? word.getRow() + i : word.getRow();
            int currentCol = word.getVertical() ? word.getCol() : word.getCol() + i;
            // Check if the current position matches any "Double Word Score" positions
            int j;
            for (j = 0; j < doubleWordPos.size(); j ++) {
                if (doubleWordPos.get(j)[0] == currentRow && doubleWordPos.get(j)[1] == currentCol) {
                    dup *= 2;
                    doubleWordPos.remove(j);
                    break;
                }
            }

            // Check if the current position matches any "Triple Word Score" positions
            for (j = 0; j < tripleWordPos.size(); j ++) {
                if (tripleWordPos.get(j)[0] == currentRow && tripleWordPos.get(j)[1] == currentCol) {
                    dup *= 3;
                    tripleWordPos.remove(j);
                    break;
                }
            }
        }
    	return dup;
    }
    
	//Calculate for a given letter it's multiplier score
    private int getLetterMultiplier(int[] letterPos) {
    	int dup = 1;
    	int[][] doubleLetterPos = {
    			{0, 3}, {0, 11}, {2, 6}, {2, 8}, {3, 0}, {3, 7}, {3, 14}, 
    			{6, 2}, {6, 6}, {6, 8}, {6, 12}, {7, 3}, {7, 11}, {8, 2}, 
    			{8, 6}, {8, 8}, {8, 12}, {11, 0}, {11, 7}, {11, 14}, 
    			{12, 6}, {12, 8}, {14, 3}, {14, 11}
    		}, tripleLetterPos = {
    			{1, 5}, {1, 9}, {5, 1}, {5, 5}, {5, 9}, {5, 13}, 
    	        {9, 1}, {9, 5}, {9, 9}, {9, 13}, {13, 5}, {13, 9}
    	    };
    	for(int[] pos : doubleLetterPos) {
    		if(pos[0] == letterPos[0] && pos[1] == letterPos[1])
    			dup = 2;
    	}
    	for(int[] pos : tripleLetterPos) {
    		if(pos[0] == letterPos[0] && pos[1] == letterPos[1])
    			dup = 3;
    	}
    	return dup;
    }
    
    //Calculate for a given word it's score    
    public int getScore(Word word) {
    	int sum, score = 0;
    	for(int i = 0; i < word.getTiles().length; i ++) {
        		if(word.getVertical()) {
                	//the letter's score
        			sum = this.board[word.getRow() + i][word.getCol()].score;
        			//multiple by its position on board
        			sum *= this.getLetterMultiplier(new int[] {word.getRow() + i, word.getCol()});
        		}else {
                	//the letter's score
        			sum = this.board[word.getRow()][word.getCol() + i].score;
        			//multiple by its position on board
        			sum *= this.getLetterMultiplier(new int[] {word.getRow(), word.getCol() + i});
        		}
        		//Add the letter score to the total sum
        		score += sum;
    	}
    	
    	//Multiple the word itself by it's position
    	score *= this.getWordMultiplier(word);
    	
    	return score;
    }
    
    //Create WordScore object with it's scores and inject into the words array
    public void addWord(Word word) {
    	wordsList.add(new WordScore(word, this.getScore(word)));
    }
	
	public Tile[][] getTiles() {
		Tile[][] currentBoard = new Tile[15][15];
		for (int i = 0; i < 15; i++)
			currentBoard[i] = board[i].clone();
		return currentBoard;
	}
	
	
	public static Board getBoard() {
		if(singleBoard == null)
			singleBoard = new Board();
		return singleBoard;
	}
	
	public boolean dictionaryLegal(Word word) {
		return true;
	}
	
	private boolean overFlow(Word word) {
		//Validate if the word position inside the table
		if(word.getCol() < 0 || word.getCol() > 14 || word.getRow() < 0 || word.getRow() > 14)
			return false;
		//Validate overflow
		if(word.getVertical()) {
			if((word.getRow() + word.getTiles().length) > 15)
				return false;	
		} else {
			if((word.getCol() + word.getTiles().length) > 15)
				return false;
		}
		return true;
	}
	
	private boolean firstWordPlace(Word word) {
		//Case word is vertical
		if(word.getVertical()) {
			//Checks if starts before the middle and ends after
			if(word.getCol() != 7 || word.getRow() > 7 || (word.getRow() + word.getTiles().length) < 7 )		
				return false;
		} else {
			//Checks if starts before the middle and ends after
			if(word.getRow() != 7 || word.getCol() > 7 || (word.getCol() + word.getTiles().length) < 7 )
				return false;
		}
		return true;
	}
	
	public boolean letterAttached(int row, int col) {
		boolean isAttached = false;
		if((row > 0 && this.board[row - 1][col] != null)
				|| (row < 14 && this.board[row + 1][col] != null)
				|| (col > 0 && this.board[row][col - 1] != null)
				|| (col < 14 && this.board[row][col + 1] != null))
			return true;
		return false;
			
	}
	
	public boolean boardLegal(Word word) {
		if(word == null || !this.dictionaryLegal(word))
			return false;
		//Check if the whole word is inside the table
		if(!this.overFlow(word))
			return false;
		//First placed word validation
		if(board[7][7] == null) {
			if(!this.firstWordPlace(word))
				return false;
		}else{
			int iter;
			boolean attachedTo = false;
			if(word.getVertical()) {
				iter = word.getRow();
				
			}
			else
				iter = word.getCol();
			for(int i = 0; i < word.getTiles().length; iter++, i++) {
				if(word.getVertical()) {
					if(this.board[iter][word.getCol()] != null && word.getTiles()[i] != null)
						return false;
					if(this.letterAttached(iter, word.getCol()))
						attachedTo = true;
				}else {
					if(this.board[word.getRow()][iter] != null && word.getTiles()[i] != null)
						return false;
					if(this.letterAttached(word.getRow(), iter))
						attachedTo = true;
				}
			}
			if(!attachedTo)
				return false;
		}
		return true;
	}
	
	public Word fetchNewWord(Tile[] tiles, int row, int col, boolean vertical){
		int iterStart, iterEnd;
		ArrayList <Tile> newTiles = new ArrayList<>();
		Stack <Tile> frontPart = new Stack<>();
		
		if(vertical) {
			iterStart = row;
			iterEnd = row + tiles.length - 1;
		}else {
			iterStart = col;
			iterEnd = col + tiles.length - 1;
		}
		
		//Iterate to the first letter and push each one to stack
		for(; iterStart > 0; iterStart --){
			if(vertical) {
				if(this.board[iterStart - 1][col] == null)
					break;
				frontPart.push(this.board[iterStart - 1][col]);
			}else {
				if(this.board[row][iterStart - 1] == null)
					break;
				frontPart.push(this.board[row][iterStart - 1]);
			}
		}
		
		//Adding the first part of the word to the list
		while(!frontPart.isEmpty()) {
			newTiles.add(frontPart.pop());
		}
		
		//Adding the users word
		for(Tile letter : tiles) {
			newTiles.add(letter);
		}
		
		//Adding the last part of the word to the list
		for(; iterEnd < 14; iterEnd ++){
			if(vertical) {
				if(this.board[iterEnd + 1][col] == null)
					break;
				newTiles.add(this.board[iterEnd + 1][col]);
			}else {
				if(this.board[row][iterEnd + 1] == null)
					break;
				newTiles.add(this.board[row][iterEnd + 1]);
			}
		}
		
		//Fixing the new full word position
		if(vertical)
			row = iterStart;
		else
			col = iterStart;
		if(newTiles.size() > 1)
			return new Word(newTiles.toArray(new Tile[newTiles.size()]), row, col, vertical);
		else
			return null;
	}
	
	public ArrayList<Word> getWords(Word word){
		ArrayList<Word> newWords = new ArrayList<>();
		int row = word.getRow(), col = word.getCol();
		newWords.add(word);
		
		//Same direction validation
		Word newWord = fetchNewWord(word.getTiles(), word.getRow(), word.getCol(), word.getVertical());
		if(word.getTiles().length != newWord.getTiles().length)
			newWords.add(newWord);

		//Each letter validation
		for(int i = 0; i < word.getTiles().length; i ++) {
			//Fetch new words only on letter that originally placed
			if(word.getTiles()[i] == null) {
				if(word.getVertical())
					row ++;
				else
					col ++;				
				continue;
			}
			newWord = fetchNewWord(new Tile[] {word.getTiles()[i]}, row, col, !word.getVertical());
			if(newWord != null)
				newWords.add(newWord);
			if(word.getVertical())
				row ++;
			else
				col ++;
		}
		return newWords;
	}
	
	private void placeWord(Word word) {
    	for(int i = 0; i < word.getTiles().length; i ++) {
    		if(word.getTiles()[i] != null)
	    		if(word.getVertical())
	    			this.board[word.getRow() + i][word.getCol()] = word.getTiles()[i];
	    		else
	    			this.board[word.getRow()][word.getCol() + i] = word.getTiles()[i];
    	}
	}

	public int tryPlaceWord(Word word) {
		int sum = 0;
		if(!this.boardLegal(word))
			return 0;
		this.placeWord(word);
		ArrayList<Word> newWords = this.getWords(word);
		for(Word w : newWords)
			if(!this.dictionaryLegal(w))
				return 0;
		for(Word w : newWords) {
			sum += this.getScore(w);
			this.addWord(w);
		}
		return sum;
	}
}
