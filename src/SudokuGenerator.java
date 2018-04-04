import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.lang.Integer;

import edu.princeton.cs.algs4.StdDraw;

public class SudokuGenerator {
	Random rn = new Random();
	int nLeft;
	int[][] board;
	int bSize;
	
	
	SudokuGenerator(int theBoard[][]){
		bSize = theBoard.length;
		nLeft = bSize*bSize;
		board = theBoard;
		generateMap();
	}
	
	void generateMap() {
		ArrayList<Integer> tempList = new ArrayList<Integer>();
		for (int i = 0; i < bSize * bSize; i++) {
			tempList.add(i);
		}
		Collections.shuffle(tempList);
		Queue<Integer> canTry = new LinkedList<Integer>(tempList);
		
		while(!canTry.isEmpty()) {
			int x = 0;
			int y = 0;
			int nextNum = canTry.remove();
			x = nextNum%bSize;
			y = nextNum/bSize;
			int[][] boardCopy = new int[bSize][bSize];
			for (int i = 0; i < bSize; i++) {
				boardCopy[i] = board[i].clone();
			}
			boardCopy[x][y] = 0;
			improvedSolver sol = new improvedSolver(boardCopy);
			if (sol.goalState()) {
				nLeft--;
				board[x][y] = 0;
			}
			
			// the if statement below is to get some feedBack (good for larger maps like 36x36 which take longer time).
			if (nLeft%10 == 0) {
				System.out.println("nLeft: " + nLeft);
			}
			//uncomment below to see the map developing
			//draw();
			
		}
		System.out.println("Stopped with " + nLeft + " numbers left");
		//draw();
	}
	
	int[][] getBoard() {
		return board;
	}
	
	void draw() {
		int boardSize = bSize;
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
