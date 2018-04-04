import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.lang.Integer;

import edu.princeton.cs.algs4.StdDraw;

// this solver is not the better solver. You can use it be changing the solver inside the SudokuGenerator class. This is an old version which was too slow to generate larger maps.


public class Solver {
	boolean changesMade;
	boolean nothingFound;
	int board[][];
	int boardSize;
	ArrayList<ArrayList<ArrayList<Integer>>> theAvailMoves;
	int numbersLeft;
	boolean[][][] availMoves;
	List<Integer> numbersNotChecked;
	int boxSize;
	Queue<int[]> moveQueue;
	
	Solver(int theBoard[][]){
		boardSize = theBoard.length;
		board = new int[boardSize][boardSize];
		nothingFound = false;
		boxSize = (int)Math.sqrt(boardSize);
		numbersLeft = boardSize * boardSize;
		initializeAvailMoves();
		initializeMoveQueue(theBoard);
	}
	
	
	void doMove() {
		int[] move;
		int counter = 0;
		if (!moveQueue.isEmpty()) {
			counter = moveQueue.size();
		}
		else {
			System.out.println("moveQueue empty??");
		}
		//while inserting the numbers that were already inside the given board;
		while(!moveQueue.isEmpty() && counter > 0) {
			counter--;
			move = moveQueue.remove(); 
			if (board[move[0]][move[1]] == 0) {
				insertNumber(move[0], move[1], move[2], true);
			}
		}
		// this is the same as above except the last value in the insertNumber is set to false because we know only numbers that have only one option will be in the queue from this point on.
		while(!moveQueue.isEmpty()) {
			move = moveQueue.remove(); 
			if (board[move[0]][move[1]] == 0) {
				insertNumber(move[0], move[1], move[2], false);
			}
		}
		if (!goalState()) {
			seeIfTrue();
		}
		
	}
	
	
	
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
				if (tempCounter == 0 && board[i][j] == 0) {
					System.out.println("found an empty square with no possible moves");
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
	
	// returns a copy of the current board.
	int[][] copyBoard(){
		int[][] copyB = new int[boardSize][boardSize];
		for (int i = 0; i < boardSize; i++) {
			copyB[i] = board[i].clone();
		}
		return copyB;
	}
	
	
	/*
	//???
	void getNextMove(){
		toPlace = new LinkedList<int[]>();
		for (int i = 0; i < bSize; i++) {
			for (int j = 0; j < bSize; j++) {
				if (board[i][j] == 0) {
					if (theAvailMoves.get(i).get(j).size() == 1) {
						toPlace.add(new int[] {i, j, theAvailMoves.get(i).get(j).get(0)});
					}
				}
			}
		}
	}
	
	//??
	void nextMove() {
		allAvailMoves();
		getNextMove(); 
		int[] temp;
		if (toPlace.isEmpty()) {
			nothingFound = true;
		}
		while(!toPlace.isEmpty()) {
			temp = toPlace.remove();
			board[temp[0]][temp[1]] = temp[2];
		}
		//draw();
	}
	
	*/
	
	//This functions adds all the numbers found inside the given board inside a Queue. Later we will add the numbers.
	void initializeMoveQueue(int[][] givenBoard) {
		moveQueue = new LinkedList<int[]>();
		for (int i = 0; i < boardSize; i++) {
			for (int j = 0; j < boardSize; j++) {
				if (givenBoard[i][j] > 0) {
					moveQueue.add(new int[] {i, j, givenBoard[i][j]});
				}
			}
		}
	}
	
	//sets all the values inside the boolean[][][] availMoves to true
	void initializeAvailMoves() {
		availMoves = new boolean[boardSize][boardSize][boardSize];
		for (int i = 0; i < boardSize; i++) {
			for (int j = 0; j < boardSize; j++) {
				for (int k = 0; k < boardSize; k++) {
					availMoves[i][j][k] = true;
				}
			}
		}
	}
	
	
	//We know the goal has been reached if numbersLeft are 0
	boolean goalState() {
		return (numbersLeft == 0);
	}
	
	// returns the board. This is used for the solver.
	int[][] getBoard(){
		return board;
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
	void insertNumber(int x, int y, int num, boolean isFirst) {
		changesMade = true;
		numbersLeft--;
		board[x][y] = num;
		if (isFirst) {
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
		if (isFirst) {
			for (int i = 0; i < numbersNotChecked.size(); i++) {
				updateAvailMovesInBoxForFirstMove(x, y, numbersNotChecked.get(i));
			}
		}
		while(changesMade == true) {
			changesMade = false;
			updateAvailMovesInLineInBox(num);
		}
		findSoloAvailMoves();
		advancedAvailMovesBox(num);
		advancedAvailMovesLine(num);
	}
	
	// finds squares that have only one available move inside them (and inserts the number if they find it)
	void findSoloAvailMoves() {
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
					moveQueue.add(new int[] {i, j, toInsert});
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
		
	// checks if the current board is valid: If there are possible moves for all numbers in all boxes/lines and if each square has at least one possibility to put in
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
	
	// draw a picture of what the board looks like
	void draw() {
    	// We implemented a draw method that shows how the agent operates. You need algs4. library for this to work and comment this out
    	StdDraw.pause(500);
		int mapSize[] = new int[2];
		mapSize[0] = boardSize;
		mapSize[1] = boardSize;
		StdDraw.clear();
        StdDraw.setPenColor(255, 255, 255);
        StdDraw.setXscale(-0.05*mapSize[0], 1.05*mapSize[0]);
        StdDraw.setYscale(-0.05*mapSize[1], 1.05*mapSize[1]);   // leave a border to write text
        StdDraw.filledRectangle(mapSize[0]/2.0, mapSize[1]/2.0, mapSize[0]/2.0, mapSize[1]/2.0);
        
        for (int row = 0; row < mapSize[0]; row++) {
			for (int col = 0; col < mapSize[1]; col++) {
				StdDraw.setPenColor(0, 0, 0);
				StdDraw.square(row + 0.5, col + 0.5, 0.5);
			}
		}
        for (int i = 0; i <= (int)Math.sqrt(boardSize); i++) {
        	StdDraw.setPenRadius(0.006);
        	StdDraw.line(i*Math.sqrt(boardSize), 0, i*Math.sqrt(boardSize), boardSize);
        	StdDraw.line(0, i*Math.sqrt(boardSize), boardSize, i*Math.sqrt(boardSize));
        }
        StdDraw.setPenRadius();
        
        StdDraw.setPenColor(0, 0, 0);
        for(int i = 0; i < board.length; i++) {
        	for (int j = 0; j < board[0].length; j++) {
        		if (board[i][j] > 0) {
        			StdDraw.text(i + 0.5, j + 0.5, Integer.toString(board[i][j]));
        		}
        	}
        }
    }
}
