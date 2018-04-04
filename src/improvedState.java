import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

public class improvedState {
	
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
	boolean[][] needToCheck;
	
	// simple constructor
	improvedState(int[][] theBoard, boolean[][][] theAvailMoves, int theNumbersLeft, int[] theMove){
		board = theBoard;
		availMoves = theAvailMoves;
		boardSize = board.length;
		insufficientAmountOfUnique = false;
		numbersLeft = theNumbersLeft;
		changesMade = true;
		boxSize = (int)Math.sqrt(boardSize);
		moveQueue = new LinkedList<int[]>();
		moveQueue.add(theMove);
		isFirstMove = true;
	}
	
	// this functions starts the state and therefore does all the work. We did this function because we only want to do all the work if we enter and continue with this state.
	void doMove() {
		int[] move;
		while(!moveQueue.isEmpty()) {
			move = moveQueue.remove(); 
			if (board[move[0]][move[1]] == 0) {
				insertNumber(move[0], move[1], move[2]);
			}
		}
		// uncomment to see if we missed any moves (takes much more time)
		//seeIfTrue();
	}

	
	// this function goes through the board and tries to find any move that we could have done (if it finds it we missed it).
	void seeIfTrue() {
		// seeing if any cell has only one available move.
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
				int tempPos = 0;
				for (int k = 0; k < boardSize; k++) {
					if (availMoves[j][k][i] == true) {
						tempCounter++;
						tempPos = k;
					}
				}
				if (tempCounter == 1) {
					System.out.println("found a vertical line that had an optional input the number " + (i + 1) + " at: " + j + "," + tempPos);
					System.out.println("since: ");
					for (int k = 0; k < boardSize; k++) {
						System.out.println(j + "," + k + ": " + availMoves[j][k][i]);
					}
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
				int tempPos = 0;
				for (int k = 0; k < boardSize; k++) {
					if (availMoves[k][j][i] == true) {
						tempCounter++;
						tempPos = k;
					}
				}
				if (tempCounter == 1) {
					System.out.println("found a horizontal line that had an optional input the number " + (i + 1) + " at: " + tempPos + "," + j);
					System.out.println("since: ");
					for (int k = 0; k < boardSize; k++) {
						System.out.println(k + "," + j + ": " + availMoves[k][j][i]);
					}
				}
			}
		}
		
	}
	
	//next state is the changes made to the board and availMoves (copied version)
	improvedState nextState(int [] theMove) {
		return new improvedState(copyBoard(), copyMoves(), numbersLeft, theMove);
	}
	
	// picks randomly one of the squares that have the fewest available moves (most constricted).
	int[] getPositionForMove() {
		fewestNumbersLeft = boardSize + 1;
		List<int[]> fewestMovePositionList = new ArrayList<int[]>();
		if(isValid()) {
			int counter = 0;
			for (int i = 0; i < boardSize; i++) {
				for (int j = 0; j < boardSize; j++) {
					counter = 0;
					if (board[i][j] == 0) {
						for (int k = 0; k < boardSize; k++) {
							if (availMoves[i][j][k] == true) {
								counter++;
								if (counter > fewestNumbersLeft) {
									break;
								}
							}
						}
						if (counter < fewestNumbersLeft) {
							fewestMovePositionList.clear();
							fewestNumbersLeft = counter;
						}
						if (counter == fewestNumbersLeft) {
							fewestMovePositionList.add(new int[] {i, j});
						}
					}
				}
			}
			if (fewestMovePositionList.size() > 0) {
				Random rnd = new Random();
				return (fewestMovePositionList.get(rnd.nextInt(fewestMovePositionList.size())));
			}
		}
		return (new int[0]);
	}
	
	// returns list of moves that are possible for the square located at (x, y)
	List<Integer> getMoveFromPosition(int[] pos) {
		int x = pos[0];
		int y = pos[1];
		List<Integer> toRet = new ArrayList<Integer>();
		for (int i = 0; i < boardSize; i++) {
			if (availMoves[x][y][i] == true) {
				toRet.add(i+1);
			}
		}
		Collections.shuffle(toRet);
		
		return toRet;
	}
	
	// Creating new board array to send into the next state
	int[][] copyBoard(){
		int[][] copyB = new int[boardSize][boardSize];
		for (int i = 0; i < boardSize; i++) {
			copyB[i] = board[i].clone();
		}
		return copyB;
	}
	
	// Creating new availMoves array to send into the next state
	boolean[][][] copyMoves(){
		boolean[][][] copyM = new boolean[boardSize][boardSize][boardSize];
		for (int i = 0; i < boardSize; i++) {
			for(int j = 0; j < boardSize; j++) {
				copyM[i][j] = availMoves[i][j].clone();
			}
		}
		return copyM;
	}
	
	// if there are no squares left to fill the sudoku has been generated.
	boolean goalState() {
		return (numbersLeft == 0);
	}
	
	// updates the availMovesList vertically (if there is a number we remove that number (in availMoves) from the other squares in line )
	void updateAvailMovesInLine(int x, int y, int num) {
		for(int i = 0; i < boardSize; i++) {
			if (availMoves[x][i][num-1] == true) {
				needToCheck[x/boxSize][i/boxSize] = true;
			}
			availMoves[x][i][num-1] = false;
			if (availMoves[i][y][num-1] == true) {
				needToCheck[i/boxSize][y/boxSize] = true;
			}
			availMoves[i][y][num-1] = false;
			//
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
		//System.out.println("inserting: " + num + " at " + x + "," + y);
		needToCheck = new boolean[boxSize][boxSize];
		needToCheck[x/boxSize][y/boxSize] = true;
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
		for (int i = 0; i < boxSize; i++) {
			for (int j = 0; j < boxSize; j++) {
				if (needToCheck[i][j] == true) {
					updateBoxAfterFirstMove(i, j);
				}
			}
		}
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
					
					if (availMoves[line][i][num-1] == true) {
						needToCheck[line/boxSize][i/boxSize] = true;
					}
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
					if (availMoves[i][line][num-1] == true) {
						needToCheck[i/boxSize][line/boxSize] = true;
					}
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
	
	void updateBoxAfterFirstMove(int xBox, int yBox) {
		//for each value
		for(int k = 0; k < boardSize; k++) {
			// for each vertical line in the box
			for (int i = xBox * boxSize; i < xBox*boxSize + boxSize; i++) {
				int tempCounter = 0;
				int tempPos = 0;
				// for each square in the line
				for (int j = 0; j < boardSize; j++) {
					if (availMoves[i][j][k] == true) {
						tempCounter++;
						tempPos = j;
					}
				}
				if (tempCounter == 1) {
					if (board[i][tempPos] == 0) {
						moveQueue.add(new int[] {i, tempPos, (k + 1)});
					}
					else {
						System.out.println("this pos is occupied");
					}
				}
			}
			// horizontal
			for (int i = yBox * boxSize; i < yBox * boxSize + boxSize; i++) {
				int tempCounter = 0;
				int tempPos = 0;
				for (int j = 0; j < boardSize; j++) {
					if (availMoves[j][i][k] == true) {
						tempCounter++;
						tempPos = j;
					}
				}
				if (tempCounter == 1) {
					if(board[tempPos][i] == 0) {
						moveQueue.add(new int[] {tempPos, i, (k + 1)});
					}
					else {
						System.out.println("this pos is occupied");
					}
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

/*
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
*/