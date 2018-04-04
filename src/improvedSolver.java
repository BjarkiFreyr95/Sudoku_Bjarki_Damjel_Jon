import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class improvedSolver {
	int boardSize;
	int board[][];
	boolean[][][] availMoves;
	// boolean that becomes true if 1) a square has no availMoves 2) a box or a line vertically/horizontally has no availMove for one or more numbers
	boolean insufficientAmountOfUnique;
	int numbersLeft;
	int fewestNumbersLeft;
	boolean changesMade;
	int boxSize;
	Queue<int[]> moveQueue;
	boolean isFirstMove;
	List<Integer> numbersNotChecked;
	
	//, boolean[][][] theAvailMoves, int theNumbersLeft, int[] theMove
	improvedSolver(int[][] theBoard){
		//board = theBoard;
		boardSize = theBoard.length;
		board = new int[boardSize][boardSize];
		availMoves = getNewAvailMoves();
		insufficientAmountOfUnique = false;
		numbersLeft = boardSize * boardSize;
		changesMade = true;
		boxSize = (int)Math.sqrt(boardSize);
		moveQueue = new LinkedList<int[]>();
		//moveQueue.add(theMove);
		isFirstMove = true;
		doAllFirstMoves(theBoard);
		doMove();
	}
	
	// sees if it can find any numbers to put in (this means we missed them). It slows down the program a lot.
	void seeIfTrue() {
		for (int i = 0; i < boardSize; i++) {
			for (int j = 0; j < boardSize; j++) {
				int tempCounter = 0;
				for (int k = 0; k < boardSize; k++) {
					if (availMoves[i][j][k] == true) {
						tempCounter++;
					}
				}
				if (tempCounter == 1) {
					System.out.println("FOUND A SQUARE WITH ONLY ONE POSSIBLE OPTION");
				}
			}
		}
		//checking every vertical line if there is some number that has only one number available
		//for every number
		for (int i = 0; i < boardSize; i++) {
			//for every line
			for (int j = 0; j < boardSize; j++) {
				//for every square
				int tempCounter = 0;
				for (int k = 0; k < boardSize; k++) {
					if (availMoves[j][k][i] == true) {
						tempCounter++;
					}
				}
				if (tempCounter == 1) {
					System.out.println("found a vertical line that had an optional input the number " + (i + 1) + " at line: " + j);
				}
			}
		}
		//checking every vertical line if there is some number that has only one number available
		//for every number
		for (int i = 0; i < boardSize; i++) {
			//for every line
			for (int j = 0; j < boardSize; j++) {
				//for every square
				int tempCounter = 0;
				for (int k = 0; k < boardSize; k++) {
					if (availMoves[k][j][i] == true) {
						tempCounter++;
					}
				}
				if (tempCounter == 1) {
					System.out.println("found a horizontal line that had an optional input the number " + (i + 1) + " at line: " + j);
				}
			}
		}
		
	}
	
	void doAllFirstMoves(int[][] theBoard) {
		for(int i = 0; i < boardSize; i++) {
			for (int j = 0; j < boardSize; j++) {
				if (theBoard[i][j] > 0) {
					if (board[i][j] == 0) {
						isFirstMove = true;
						insertNumber(i, j, theBoard[i][j]);
					}
				}
			}
		}
	}
	
	// returns a new boolean array. This array holds around our available moves. If availMoves[0][0][1] == true that means we can place 1 at square 0,0 without violating the sudoku rules.
	boolean[][][] getNewAvailMoves() {
		boolean[][][] newAvailMoves = new boolean[boardSize][boardSize][boardSize];
		for (int i = 0; i < boardSize; i++) {
			for (int j = 0; j < boardSize; j++) {
				for (int k = 0; k < boardSize; k++) {
					newAvailMoves[i][j][k] = true;
				}
			}
		}
		return newAvailMoves;
	}
	
	// simply does the move
	void doMove() {
		// we saved the moves in a queue to avoid recursing.
		int[] move;
		while(!moveQueue.isEmpty()) {
			move = moveQueue.remove(); 
			if (board[move[0]][move[1]] == 0) {
				insertNumber(move[0], move[1], move[2]);
			}
		}
		goOverLines();
	}
	
	// sees if there is any vertical/horizontal line with only a number that is only available at one location.
	void goOverLines() {
		//checking every vertical line if there is some number that has only one number available
		//for every number
		for (int i = 0; i < boardSize; i++) {
			//for every line
			for (int j = 0; j < boardSize; j++) {
				//for every square
				int tempCounter = 0;
				int tempK = 0;
				for (int k = 0; k < boardSize; k++) {
					if (availMoves[j][k][i] == true) {
						tempCounter++;
						tempK = k;
					}
				}
				if (tempCounter == 1) {
					moveQueue.add(new int[] {j, tempK, (i + 1)});
				}
			}
		}
		
		//checking every vertical line if there is some number that has only one number available
		//for every number
		for (int i = 0; i < boardSize; i++) {
			//for every line
			for (int j = 0; j < boardSize; j++) {
				//for every square
				int tempCounter = 0;
				int tempK = 0;
				for (int k = 0; k < boardSize; k++) {
					if (availMoves[k][j][i] == true) {
						tempCounter++;
						tempK = k;
					}
				}
				if (tempCounter == 1) {
					moveQueue.add(new int[] {tempK, j, (i + 1)});
				}
			}
		}
		if (!moveQueue.isEmpty()) {
			doMove();
		}
	}
	
	// Creating new board array to send into the next state
	int[][] copyBoard(){
		int[][] copyB = new int[boardSize][boardSize];
		for (int i = 0; i < boardSize; i++) {
			copyB[i] = board[i].clone();
		}
		return copyB;
	}
	
	// if there are no squares left to fill the sudoku has been generated.
	boolean goalState() {
		return (numbersLeft == 0);
	}
	
	// updates the availMovesList vertically (if there is a number we remove that number (in availMoves) from the other squares in line )
	void updateAvailMovesInLine(int x, int y, int num) {
		for(int i = 0; i < boardSize; i++) {
			availMoves[x][i][num-1] = false;
			availMoves[i][y][num-1] = false;
		}
	}
	
	// updates the availMovesList inside the box (if there is a number we remove that number (in availMoves) from the other squares in the box )
	void updateAvailMovesInBox(int x, int y, int num) {
		// xBox and yBox are used to find which box we are inside (so we can loop through the box)
		int xBox = (x/boxSize);
		int yBox = (y/boxSize);
		for (int i = boxSize * xBox; i < boxSize * xBox + boxSize; i++) {
			for (int j = boxSize * yBox; j < boxSize * yBox + boxSize; j++) {
				availMoves[i][j][num-1] = false;
			}
		}
	}
	
	//inserts a number inside the sudoku map.
	void insertNumber(int x, int y, int num) {
		changesMade = true;
		numbersLeft--;
		board[x][y] = num;
		if (isFirstMove) {
			numbersNotChecked = new ArrayList<Integer>();
			for (int i = 0; i < boardSize; i++) {
				if (availMoves[x][y][i] == true) {
					if (i+1 != num) {
						numbersNotChecked.add(i+1);
					}
				}
			}
		}
		removeFromAvailMoves(x, y);
		updateAvailMovesInLine(x, y, num);
		updateAvailMovesInBox(x, y, num);
		if (isFirstMove) {
			isFirstMove = false;
			for (int i = 0; i < numbersNotChecked.size(); i++) {
				updateAvailMovesInBoxForFirstMove(x, y, numbersNotChecked.get(i));
			}
		}
		while(changesMade == true) {
			changesMade = false;
			updateAvailMovesInLineInBox(num);
		}
		findSoloAvailMoves(x, y);
		advancedAvailMovesBox(num);
		advancedAvailMovesLine(num);
	}
	
	// finds squares that have only one available move inside them (and inserts the number if they find it)
	void findSoloAvailMoves(int x, int y) {
		int counter;
		int toInsert = 0;
		for(int i = 0; i < boardSize; i++) {
			for (int j = 0; j < boardSize; j++) {
				counter = 0;
				for (int k = 0; k < boardSize; k++) {
					if (availMoves[i][j][k] == true) {
						counter++;
						toInsert = k + 1;
						if (counter > 1) {
							break;
						}
					}
				}
				if(counter == 1) {
					insertNumber(i, j, toInsert);
				}
			}
		}
	}
	
	//clears the boolean list at position x, y
	void removeFromAvailMoves(int x, int y) {
		for (int i = 0; i < boardSize; i++) {
			availMoves[x][y][i] = false;
		}
	}
	
	// sees if the inserted number appears only once in some box
	// this loops through all the boxes and counts how many times num appears
	void advancedAvailMovesBox(int num) {
		
		int counter;
		// a and b are which box we are in
		for (int a = 0; a < boxSize; a++) {
			for (int b = 0; b < boxSize; b++) {
				counter = 0;
				int xPos = 0;
				int yPos = 0;
				//boxSize * a or b gets us inside the box.
				for (int i = boxSize * a; i < boxSize * a + boxSize; i++) {
					for (int j = boxSize * b; j < boxSize * b + boxSize; j++) {
						if (availMoves[i][j][num-1] == true) {
							counter++;
							xPos = i;
							yPos = j;
						}
						if (counter > 1) {
							break;
						}
					}
					if (counter > 1) {
						break;
					}
				}
				if (counter == 1) {
					moveQueue.add(new int[] {xPos, yPos, num});
				}
			}
		}
		
		
	}
	
	// sees if the num appears only once in availMoves in any line vertically or horizontally
	void advancedAvailMovesLine(int num) {
		
		 //vertically
		int counter;
		for (int i = 0; i < boardSize; i++) {
			counter = 0;
			int yPos = 0;
			for (int j = 0; j < boardSize; j++) {
				if (availMoves[i][j][num-1] == true) {
					counter++;
					yPos = j;
					if (counter > 1) {
						break;
					}
				}
			}
			if (counter == 1) {
				moveQueue.add(new int[] {i, yPos, num});
			}			
		}
		//horizontally
		for (int i = 0; i < boardSize; i++) {
			counter = 0;
			int xPos = 0;
			for(int j = 0; j < boardSize; j++) {
				if (availMoves[j][i][num-1] == true) {
					counter++;
					xPos = j;
					if (counter > 1) {
						break;
					}
				}
			}
			if (counter == 1) {
				moveQueue.add(new int[] {xPos, i, num});
			}
		}
		
	}
	
	//sees if there is a case if a avail number appears only vertically or horizontally inside a box. That means it cannot appear anywhere else in that line.
	void updateAvailMovesInLineInBox(int num) {
		int counter;
		//for loop through each box
		
		for (int i = 0; i < boxSize; i++) {
			for (int j = 0; j < boxSize; j++) {
				// here we count the amount/number of the inserted num inside the box
				counter = 0;
				for (int k = boxSize * i; k < boxSize * i + boxSize; k++) {
					for (int l = boxSize * j; l < boxSize * j + boxSize; l++) {
						if (availMoves[k][l][num-1] == true) {
							counter++;
						}
					}
				}
				// next we see if there is a horizontal/vertical line that contains all of them, if it does, we can remove them from the line in the other boxes.
				if (counter > 1) {
					int lineCounter;
					// see if all the the availMoves that contain num are vertical
					for (int k = boxSize * i; k < boxSize * i + boxSize; k++) {
						lineCounter = 0;
						for (int l = boxSize * j; l < boxSize * j + boxSize; l++) {
							if (availMoves[k][l][num-1] == true) {
								lineCounter++;
							}
						}
						if (lineCounter == counter) {
							updateAvailMovesInLineInBoxVertical(num, k, j);
							break;
						}
						else if(lineCounter > 0) {
							break;
						}
					}
					// see if all the the availMoves that contain num are horizontal
					for (int l = boxSize * j; l < boxSize * j + boxSize; l++) {
						lineCounter = 0;
						for (int k = boxSize * i; k < boxSize * i + boxSize; k++) {
							if (availMoves[k][l][num-1] == true) {
								lineCounter++;
							}
						}
						if (lineCounter == counter) {
							updateAvailMovesInLineInBoxHorizontal(num, l, i);
							break;
						}
						else if(lineCounter > 0) {
							break;
						}
					}
					
				}
			}
		}
		
	}
	
	// helper function for updateAvailMovesInLineInBox. This removes all the "num" in line vertically that are not in the box
	void updateAvailMovesInLineInBoxVertical(int num, int line, int box) {
		for (int i = 0; i < boardSize; i++) {
			if (availMoves[line][i][num-1] == true) {
				// we don't want to remove the elements from the box we are inside so we do this check.
				if (i/boxSize != box) {
					//System.out.println("removing " + num + " vertically, found another at (" + line + "," + i + ")");
					changesMade = true;
					availMoves[line][i][num-1] = false;
				}
			}
		}
	}
	
	// helper function for updateAvailMovesInLineInBox. This removes all the "num" in line horizontally that are not in the box
	void updateAvailMovesInLineInBoxHorizontal(int num, int line, int box) {
		for (int i = 0; i < boardSize; i++) {
			if (availMoves[i][line][num-1] == true) {
				// we don't want to remove the elements from the box we are inside so we do this check.
				if (i/boxSize != box) {
					//System.out.println("removing " + num + " horizontal, found another at (" + i + "," + line + ")");
					changesMade = true;
					availMoves[i][line][num-1] = false;
				}
			}
		}
	}

	//same as the function above except it only checks that box. This is only checked for the first inserted number each turn (controlled with boolean isFirstMove) because
	//only that number can contain more than one availNumber.
	void updateAvailMovesInBoxForFirstMove(int x, int y, int num) {
		int counter = 0;
		int i = x/boxSize;
		int j = y/boxSize;
		for (int k = boxSize * i; k < boxSize * i + boxSize; k++) {
			for (int l = boxSize * j; l < boxSize * j + boxSize; l++) {
				if (availMoves[k][l][num-1] == true) {
					counter++;
				}
			}
		}
		// next we see if there is a horizontal/vertical line that contains all of them, if it does, we can remove them from the line in the other boxes.
		if (counter > 1) {
			int lineCounter;
			// see if all the the availMoves that contain num are vertical
			for (int k = boxSize * i; k < boxSize * i + boxSize; k++) {
				lineCounter = 0;
				for (int l = boxSize * j; l < boxSize * j + boxSize; l++) {
					if (availMoves[k][l][num-1] == true) {
						lineCounter++;
					}
				}
				if (lineCounter == counter) {
					updateAvailMovesInLineInBoxVertical(num, k, j);
					break;
				}
				else if(lineCounter > 0) {
					break;
				}
			}
			// see if all the the availMoves that contain num are horizontal
			for (int l = boxSize * j; l < boxSize * j + boxSize; l++) {
				lineCounter = 0;
				for (int k = boxSize * i; k < boxSize * i + boxSize; k++) {
					if (availMoves[k][l][num-1] == true) {
						lineCounter++;
					}
				}
				if (lineCounter == counter) {
					updateAvailMovesInLineInBoxHorizontal(num, l, i);
					break;
				}
				else if(lineCounter > 0) {
					break;
				}
			}
		}
		
		
	}
	
	// Sees if there is a square with no availMoves or if there are fewer unique numbers in line/box than empty squares (if that happens there is a number that cannot be placed anywhere)
	boolean isValid() {
		// see if there is a empty square with no available move
		int counter;
		for (int i = 0; i < boardSize; i++) {
			for(int j = 0; j < boardSize; j++) {
				counter = 0;
				if (board[i][j] == 0) {
					for (int k = 0; k < boardSize; k++) {
						if (availMoves[i][j][k] == true) {
							counter++;
							break;
						}
					}
					if (counter == 0) {
						return false;
					}
				}
			}
		}
		
		// see if there is a number missing in availMoves + board in each line/box 
		boolean[][] verticalNumbers = new boolean[boardSize][boardSize];
		boolean[][] horizontalNumbers = new boolean[boardSize][boardSize];
		boolean[][] boxNumbers = new boolean[boardSize][boardSize];
		int box = 0;
		for(int i = 0; i < boardSize; i++) {
			for (int j = 0; j < boardSize; j++) {
				box = i/boxSize + ((j/boxSize) * boxSize); 
				if (board[i][j] > 0) {
					verticalNumbers[i][board[i][j] - 1] = true;
					horizontalNumbers[j][board[i][j] - 1] = true;
					boxNumbers[box][board[i][j] - 1] = true;
					
				}
				else {
					for (int k = 0; k < boardSize; k++) {
						if (availMoves[i][j][k] == true) {
							verticalNumbers[i][k] = true;
							horizontalNumbers[j][k] = true;
							boxNumbers[box][k] = true;
						}
					}
				}
			}
		}
		
		// if there is any boolean still false it means that number is missing from that box/line
		for (int i = 0; i < boardSize; i++) {
			for (int j = 0; j < boardSize; j++) {
				if (verticalNumbers[i][j] == false) {
					return false;
				}
				if (horizontalNumbers[i][j] == false) {
					return false;
				}
				if (boxNumbers[i][j] == false) {
					return false;
				}
			}
		}
		
		return true;
	}
	
	void printMissingMoves(){
		for (int i = 0; i < boardSize; i++) {
			for (int j = 0; j < boardSize; j++) {
				for (int k = 0; k < boardSize; k++) {
					if (availMoves[i][j][k] == false) {
						System.out.println("(" + i + "," + j + "): " + (k+1));
					}
				}
			}
		}
	}
	
}
