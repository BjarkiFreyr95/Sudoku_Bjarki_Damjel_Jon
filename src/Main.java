import edu.princeton.cs.algs4.StdDraw;

public class Main {

	public static void main(String[] args) {
		
		// In sudoku the board needs to be square number (4, 9, 16, 25, 36) where the root is whole number.
		int boardSize;
		//with size 9
		boardSize = 9;
		//BruteForceAgent bfa = new BruteForceAgent(boardSize);
		//System.out.println("cca: ");
		//CounteringConstraintsAgent cca = new CounteringConstraintsAgent(boardSize);
		//int[][] board = cca.getBoard();
		//SudokuGenerator gen = new SudokuGenerator(board);
		//gen.generateMap();
		//System.out.println("hca: ");
		//HeuristicConstraintsAgent hca = new HeuristicConstraintsAgent(boardSize);
		//improvedAgent imp = new improvedAgent(boardSize);
		//SudokuGenerator gen = new SudokuGenerator(imp.getBoard());
		//gen.generateMap();
		//int[][] theBoard, boolean[][][] theAvailMoves, int theNumbersLeft, int[] theMove
		
		/*
		boolean[][][] tempBoolean = new boolean[4][4][4];
		tempBoolean[0][0][0] = true;
		tempBoolean[1][0][0] = true;
		tempBoolean[2][0][0] = true;
		tempBoolean[3][0][0] = true;
		tempBoolean[3][1][0] = true;
		tempBoolean[2][1][0] = true;
		tempBoolean[3][2][0] = true;
		tempBoolean[3][3][0] = true;
		
		
		int[][] board = new int[4][4];
		improvedState imp = new improvedState(board, tempBoolean, 16, new int[] {0, 1, 3});
		imp.updateAvailMovesInBoxForFirstMove(0, 0, 1);
		imp.updateAvailMovesInBoxForFirstMove(2, 2, 1);
		/*
		draw(imp);
		imp.printMissingMoves();
		
		
		board[0][1] = 7;
		board[4][1] = 8;
		board[6][1] = 5;
		board[1][2] = 9;
		board[4][2] = 7;
		board[7][2] = 4;
		board[0][3] = 2;
		board[3][3] = 6;
		board[8][3] = 8;
		board[2][4] = 5;
		board[6][4] = 4;
		board[0][5] = 1;
		board[5][5] = 3;
		board[8][5] = 9;
		board[1][6] = 4;
		board[4][6] = 2;
		board[7][6] = 3;
		board[2][7] = 3;
		board[4][7] = 1;
		board[8][7] = 6;
		board[2][8] = 2;
		*/
		//Solver sol = new Solver(board);
		//sol.solve(false);
		
		
		
		
	}
	static boolean[][][] getNewAvailMoves(int boardSize) {
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
	
	static void draw(improvedState current) {
    	// We implemented a draw method that shows how the agent operates. You need algs4. library for this to work and comment this out
    	StdDraw.pause(500);
    	int boardSize = current.boardSize;
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
        
        int[][] board = current.copyBoard();
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
