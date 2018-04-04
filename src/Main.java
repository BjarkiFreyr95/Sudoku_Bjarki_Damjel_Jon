import edu.princeton.cs.algs4.StdDraw;
import java.lang.Integer;

public class Main {

	public static void main(String[] args) {
		
		//When you go over this project we recommend looking mostly at the improvedAgent, improvedState and improvedSolver since they are our final and most advanced (also well commented).
		//We included the other agents and their state if you want to see how much they differentiated from the other agents.
		//We created a simple instructions below with examples of how to use program. We recommend you go over them all before testing or trying out the program. 
		
		// In sudoku the boardSize needs to be square number (4, 9, 16, 25, 36) where the root is whole number.
		int boardSize;
		//with size 9
		// boardSize is the size of the board, for example 9 would create a 9x9 sudoku board.
		boardSize = 9;
		// The lines here below are the different agents we implemented. This will (try) to create a new board with size (boardSize)x(boardSize).
		// We recommend using improvedAgent since that agent is our final agent (the best one) and can create the largest map (36x36). The worst agents can still go as high as 16x16.
		/*
		BruteForceAgent bfa = new BruteForceAgent(boardSize);
		HeuristicConstraintsAgent hca = new HeuristicConstraintsAgent(boardSize);
		CounteringConstraintsAgent cca = new CounteringConstraintsAgent(boardSize);
		improvedAgent imp = new improvedAgent(boardSize);
		*/
		
		
		// If you want to see the drawing of the map, you need to get the board and place it inside the draw function (a draw function is below). 
		//You will need to set up edu.princeton.cs.algs4.StdDraw library for this to work.
		// an example is below (  draw(agentName.getBoard())  )
		// you should only draw one at a time since the drawing updates (it does not create new drawings).
		/*
		// this shows a drawing of the completed maps.
		draw(bfa.getBoard());
		draw(hca.getBoard());
		draw(cca.getBoard());
		draw(imp.getBoard());
		*/
		
		
		// when you send a map inside the generator, only improvedAgent sends a copy of his board. Therefore you must draw before generating a puzzle if you want to see the completed map
		// with agents other than improvedAgent.
		// to create a map to solve (after we removed some of the numbers) send in the finished board from one of the agents above using .getBoard(), for an example
		/*
		SudokuGenerator gen;
		gen = new SudokuGenerator(bfa.getBoard());
		gen = new SudokuGenerator(hca.getBoard());
		gen = new SudokuGenerator(cca.getBoard());
		gen = new SudokuGenerator(imp.getBoard());
		*/
		
		//If you want to draw the solvable map you can do something like
		//draw(gen.getBoard());
		
		
		// If you want to get the number of states, you can do following.
		//System.out.println(imp.getStates());
		
		
		// here below is an example of how to try out one of the agent, see the amount of states and see the map itself.
		/*
		boardSize = 16;
		improvedAgent imp = new improvedAgent(boardSize);
		System.out.println("States: " + imp.getStates());
		draw(imp.getBoard());
		*/
		
		
		// Here below we created a method to see some results of how good one of the agent is
		// By doing this you can see the time in milliseconds and the average states needed to create the map.
		// boardSize = 25 took an average 170 milliseconds for my computer (with improvedAgent). 
		// boardSize = 36 took an average 2880 milliseconds for my computer (with improvedAgent).
		
		/*
		long startTime = System.currentTimeMillis();
		int totalStates = 0;
		// sizes of the board can be (4, 9, 16, 25, 36)
		boardSize = 25;
		improvedAgent imp;
		// numberOfBoards is how many times do you want to get a new board. 
		int numberOfBoards = 10;
		for (int i = 0; i < numberOfBoards; i++) {
			imp = new improvedAgent(boardSize);
			totalStates += imp.getStates();
		}
		System.out.println("Average time for " + boardSize + "x" + boardSize + " using IMP : " + ((long)(System.currentTimeMillis() - startTime)/numberOfBoards));
		System.out.println("Average states for" + boardSize + "x" + boardSize + " using IMP : " + totalStates/numberOfBoards);
		*/
	}
	
	static void draw(int[][] board) {
    	// We implemented a draw method that shows how the agent operates. You need algs4. library for this to work and comment this out
    	StdDraw.pause(500);
    	int boardSize = board.length;
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
