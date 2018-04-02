import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CounteringConstraintState {
	int board[][];
	int xCord;
	int yCord;
	int bSize;
	boolean changesMade;
	int[] lastAdvancedMove;
	ArrayList<ArrayList<ArrayList<Integer>>> theAvailMoves;
	ArrayList<int[]> alreadyFoundMoves;
	
	CounteringConstraintState(int theBoard[][], int x, int y, ArrayList<ArrayList<ArrayList<Integer>>> theMoves, ArrayList<int[]> theFoundMoves) {
		alreadyFoundMoves = theFoundMoves;
		board = theBoard;
		xCord = x;
		yCord = y;
		bSize = board[0].length;
		theAvailMoves = theMoves;
		allAvailMoves();
	}
	
	CounteringConstraintState nextState(int chosenNumber) {
		
		int[][] newBoard = new int[bSize][bSize];
		for (int i = 0; i < bSize; i++) {
			newBoard[i] = board[i].clone();
		}
		newBoard[xCord][yCord] = chosenNumber;
		int nextXCord = xCord;
		int nextYCord = yCord;
		if (xCord < (bSize - 1)) {
			nextXCord++;
		}
		else {
			nextXCord = 0;
			nextYCord++;
		}
		ArrayList<ArrayList<ArrayList<Integer>>> availCopy = new ArrayList<ArrayList<ArrayList<Integer>>>();
		for(int i = 0; i < theAvailMoves.size(); i++) {
			availCopy.add(new ArrayList<ArrayList<Integer>>());
			for(int j = 0; j < theAvailMoves.get(i).size(); j++) {
				availCopy.get(i).add(new ArrayList<Integer>());
				for(int k = 0; k < theAvailMoves.get(i).get(j).size(); k++) {
					availCopy.get(i).get(j).add(theAvailMoves.get(i).get(j).get(k));
				}
			}
		}
		ArrayList<int[]> foundCopy = new ArrayList<int[]>();
		for (int i = 0; i < alreadyFoundMoves.size(); i++) {
			foundCopy.add(alreadyFoundMoves.get(i).clone());
		}
		return new CounteringConstraintState(newBoard, nextXCord, nextYCord, availCopy, foundCopy);
	}
	
	List<Integer> availMoves() {
		if (isValid()) {
			Collections.shuffle(theAvailMoves.get(xCord).get(yCord));
			return (theAvailMoves.get(xCord).get(yCord));
		}
		return (new ArrayList<Integer>());
	}
	
	
	boolean isValid() {
		for (int i = 0; i < bSize; i++) {
			for (int j = 0; j < bSize; j++) {
				if (board[i][j] == 0) {
					if (theAvailMoves.get(i).get(j).isEmpty()) {
						return false;
					}
				}
			}
		}
		
		// unique numbers in a vertical/horizontal line or in a box
		Set<Integer> uniqueN;
		int numberOfEmptyCells;
		
		// see if there are fewer unique options than empty cells in each vertical line.
		for (int i = 0; i < bSize; i++) {
			numberOfEmptyCells = 0;
			uniqueN = new HashSet<>();
			for (int j = 0; j < bSize; j++) {
				if (board[i][j] == 0) {
					numberOfEmptyCells++;
					uniqueN.addAll(theAvailMoves.get(i).get(j));
				}
			}
			if (uniqueN.size() < numberOfEmptyCells) {
				return false;
			}
			if (uniqueN.size() > numberOfEmptyCells) {
				System.out.println("Bigger?");
			}
		}
		
		// see if there are fewer unique options than empty cells in each horizontal line.
		for (int i = 0; i < bSize; i++) {
			numberOfEmptyCells = 0;
			uniqueN = new HashSet<>();
			for (int j = 0; j < bSize; j++) {
				if (board[j][i] == 0) {
					numberOfEmptyCells++;
					uniqueN.addAll(theAvailMoves.get(j).get(i));
				}
			}
			if (uniqueN.size() < numberOfEmptyCells) {
				return false;
			}
			if (uniqueN.size() > numberOfEmptyCells) {
				System.out.println("Bigger?");
			}
		}
		
		// see if there are fewer unique options than empty cells in all the boxes.
		int boxSize = (int)Math.sqrt(bSize);
		
		for (int i = 0; i < boxSize; i++) {
			for (int j = 0; j < boxSize; j++) {
				numberOfEmptyCells = 0;
				uniqueN = new HashSet<>();
				for (int l = boxSize * i; l < boxSize * i + boxSize; l++) {
					for (int m = boxSize * j; m < boxSize * j + boxSize; m++) {
						if (board[l][m] == 0) {
							numberOfEmptyCells++;
							uniqueN.addAll(theAvailMoves.get(l).get(m));
						}
					}
				}
				if (uniqueN.size() < numberOfEmptyCells) {
					return false;
				}
				if (uniqueN.size() > numberOfEmptyCells) {
					System.out.println("Bigger?");
				}
			}
		}
		
		return true;
	}
	
	int[] getLastPos() {
		 int x = 0;
		 int y = 0;
		 if (xCord == 0) {
		  if (yCord > 0) {
		   y = yCord - 1;
		   x = bSize - 1;
		  }
		 }
		 else {
		  y = yCord;
		  x = xCord - 1;
		 }
		 return new int[] {x, y};
	}
	
	
	void allAvailMoves() {
		
		int nIndex;
		int[] lastMove = getLastPos();
		int i = lastMove[0];
		int j = lastMove[1];
		if (j > -1) {
			//vertical
			for (int k = 0; k < bSize; k++) {
				if (board[i][k] == 0) {
					nIndex = theAvailMoves.get(i).get(k).indexOf(board[i][j]);
					if (nIndex > -1) {
						theAvailMoves.get(i).get(k).remove(nIndex);
					}
				}
			}
			//horizontal
			for (int k = 0; k < bSize; k++) {
				if (board[k][j] == 0) {
					nIndex = theAvailMoves.get(k).get(j).indexOf(board[i][j]);
					if (nIndex > -1) {
						theAvailMoves.get(k).get(j).remove(nIndex);
					}
				}
			}
			
			//in box
			int boxSize = (int)Math.sqrt(bSize);
			int xBox = (i/boxSize);
			int yBox = (j/boxSize);
			for (int l = boxSize * xBox; l < boxSize * xBox + boxSize; l++) {
				for (int m = boxSize * yBox; m < boxSize * yBox + boxSize; m++) {
					if (board[l][m] == 0) {
						nIndex = theAvailMoves.get(l).get(m).indexOf(board[i][j]);
						if (nIndex > -1) {
							theAvailMoves.get(l).get(m).remove(nIndex);
						}
					}
				}
			}
			changesMade = true;
			lastAdvancedMove = lastMove;
			while(changesMade == true) {
				advancedAvailMoves();
			}
		}
	}
	
	void advancedAvailMoves() {
		changesMade = false;
		int counter = 0;
		for (int i = 0; i < bSize; i++) {
			for (int j = 0; j < bSize; j++) {
				if (board[i][j] == 0) {
					for (int ai = 0; ai < alreadyFoundMoves.size(); ai++) {
						if (alreadyFoundMoves.get(ai)[0] == i && alreadyFoundMoves.get(ai)[1] == j) {
							continue;
						}
					}
					for (int k = 0; k < theAvailMoves.get(i).get(j).size(); k++) {
						//vertical
						counter = 0;
						for (int l = 0; l < bSize; l++) {
							if (board[i][l] == 0) {
								if(theAvailMoves.get(i).get(l).contains(theAvailMoves.get(i).get(j).get(k))) {
									counter++;
								}
							}
						}
						if (counter == 1) {
							toRemove(i, j, theAvailMoves.get(i).get(j).get(k));
						}
						else {
							//horizontal
							counter = 0;
							for (int l = 0; l < bSize; l++) {
								if (board[l][j] == 0) {
									if(theAvailMoves.get(l).get(j).contains(theAvailMoves.get(i).get(j).get(k))) {
										counter++;
									}
								}
							}
							if (counter == 1) {
								toRemove(i, j, theAvailMoves.get(i).get(j).get(k));
							}
							else {
								// inisde the box
								counter = 0;
								int boxSize = (int)Math.sqrt(bSize);
								int xBox = (i/boxSize);
								int yBox = (j/boxSize);
								for (int l = boxSize * xBox; l < boxSize * xBox + boxSize; l++) {
									for (int m = boxSize * yBox; m < boxSize * yBox + boxSize; m++) {
										if (board[l][m] == 0) {
											if(theAvailMoves.get(l).get(m).contains(theAvailMoves.get(i).get(j).get(k))) {
												counter++;
											}
										}
									}
								}
								if (counter == 1) {
									toRemove(i, j, theAvailMoves.get(i).get(j).get(k));
								}
							}
						}
					}
				}
			}
		}
	}
	
	void toRemove(int x, int y, int num) {
		for (int i = 0; i < alreadyFoundMoves.size(); i++) {
			if (alreadyFoundMoves.get(i)[0] == x && alreadyFoundMoves.get(i)[1] == y) {
				return;
			}
		}
		alreadyFoundMoves.add(new int[] {x, y});
		changesMade = true;
		int nIndex;
		//vertical
		for (int k = 0; k < bSize; k++) {
			if (board[x][k] == 0) {
				nIndex = theAvailMoves.get(x).get(k).indexOf(num);
				if (nIndex > -1) {
					theAvailMoves.get(x).get(k).remove(nIndex);
				}
			}
		}
		//horizontal
		for (int k = 0; k < bSize; k++) {
			if (board[k][y] == 0) {
				nIndex = theAvailMoves.get(k).get(y).indexOf(num);
				if (nIndex > -1) {
					theAvailMoves.get(k).get(y).remove(nIndex);
				}
			}
		}
		//in box
		int boxSize = (int)Math.sqrt(bSize);
		int xBox = (x/boxSize);
		int yBox = (y/boxSize);
		for (int l = boxSize * xBox; l < boxSize * xBox + boxSize; l++) {
			for (int m = boxSize * yBox; m < boxSize * yBox + boxSize; m++) {
				if (board[l][m] == 0) {
					nIndex = theAvailMoves.get(l).get(m).indexOf(num);
					if (nIndex > -1) {
						theAvailMoves.get(l).get(m).remove(nIndex);
					}
				}
			}
		}
		theAvailMoves.get(x).get(y).clear();
		theAvailMoves.get(x).get(y).add(num);
	}
	
	int[][] getBoard() {
		return board;
	}
	
	List <Integer> getBox() {
		int boxSize = (int)Math.sqrt(bSize);
		int xBox = (xCord/boxSize);
		int yBox = (yCord/boxSize);
		List <Integer> boxValues = new ArrayList<Integer>();
		
		for (int i = boxSize * xBox; i < boxSize * xBox + boxSize; i++) {
			for (int j = boxSize * yBox; j < boxSize * yBox + boxSize; j++) {
				if (board[i][j] != 0) {
					boxValues.add(board[i][j]);
				}
			}
		}
		return boxValues;
	}
	
	boolean goalState() {
		if (yCord == bSize) {
			return true;
		}
		return false;
	}
}
