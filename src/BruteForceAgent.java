import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import edu.princeton.cs.algs4.StdDraw;

public class BruteForceAgent {
	
	BruteForceState current = null;
	int states;
	
	int boardSize;
	BruteForceAgent(int theBoardSize) {
		boardSize = theBoardSize;
		init();
	}
	
	void init() {
		states = 0;
		current = new BruteForceState(new int[boardSize][boardSize], 0, 0);
		BruteForceState nextState = null;
		List<Integer> availMoves = new ArrayList<Integer>();
		Stack<BruteForceState> frontier = new Stack<BruteForceState>();
		frontier.add(current);
		while (true) {
			if (frontier.isEmpty()) {
				System.out.println("States: " + states);
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
				System.out.println("States: " + states);
				break;
			}
			states++;
			
		}
		draw(current);
	}
	
	BruteForceState getCurrent() {
		return current;
	}
	
	int[][] getBoard() {
		return current.getBoard();
	}
	
	void draw(BruteForceState current) {
    	// We implemented a draw method that shows how the agent operates. You need algs4. library for this to work and comment this out
    	StdDraw.pause(1000);
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
