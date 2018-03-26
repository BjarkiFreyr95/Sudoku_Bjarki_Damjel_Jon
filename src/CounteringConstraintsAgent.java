import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import edu.princeton.cs.algs4.StdDraw;

public class CounteringConstraintsAgent {
	CounteringConstraintState current = null;
	
	int boardSize;
	CounteringConstraintsAgent(int theBoardSize) {
		boardSize = theBoardSize;
		init();
	}
	
	void init() {
		int states = 0;
		boolean hasReachedOne = false;
		boolean hasReachedTwo = false;
		boolean hasReachedThree = false;
		boolean hasReachedFour = false;
		boolean hasReachedFive = false;
		boolean hasReachedSix = false;
		current = new CounteringConstraintState(new int[boardSize][boardSize], 0, 0);
		CounteringConstraintState nextState = null;
		List<Integer> availMoves = new ArrayList<Integer>();
		Stack<CounteringConstraintState> frontier = new Stack<CounteringConstraintState>();
		frontier.add(current);
		int maxStates = (int)Math.pow(boardSize, 3);
		System.out.println("power is: " + maxStates);
		while (true) {
			states++;
			if (frontier.isEmpty()) {
				break;
			}
			current = frontier.pop();
			if (!current.goalState()) {
				availMoves = current.availMoves();
				for (int i = 0; i < availMoves.size(); i++) {
					nextState = current.nextState(availMoves.get(i));
					frontier.add(nextState);
				}
			}
			else {
				break;
			}
			if (hasReachedOne == false) {
				if (current.yCord * boardSize > 100 ) {
					System.out.println("reached 100!");
					hasReachedOne = true;
				}
			}
			else if (hasReachedTwo == false) {
				if (current.yCord * boardSize > 200 ) {
					System.out.println("reached 200!");
					hasReachedTwo = true;
				}
			}
			else if (hasReachedThree == false) {
				if (current.yCord * boardSize > 300 ) {
					System.out.println("reached 300!");
					hasReachedThree = true;
				}
			}
			else if (hasReachedFour == false) {
				if (current.yCord * boardSize > 400 ) {
					System.out.println("reached 400!");
					hasReachedFour = true;
				}
			}
			else if (hasReachedFive == false) {
				if (current.yCord * boardSize > 500 ) {
					System.out.println("reached 500!");
					hasReachedFive = true;
				}
			}
			else if (hasReachedSix == false) {
				if (current.yCord * boardSize > 600 ) {
					System.out.println("reached 600!");
					hasReachedSix = true;
				}
			}
			if (states > maxStates) {
				System.out.println("finally reached: " + current.yCord * boardSize + " at " + states);
				draw(current);
				init();
			}
			
		}
		draw(current);
		System.out.println("States: " + states);
		
	}
	
	CounteringConstraintState getCurrent() {
		return current;
	}
	
	int[][] getBoard() {
		return current.getBoard();
	}
	
	void draw(CounteringConstraintState current) {
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
        
        int[][] board = current.getBoard();
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
