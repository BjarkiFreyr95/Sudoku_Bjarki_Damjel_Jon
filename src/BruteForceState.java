import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class BruteForceState {
	
	int board[][];
	int xCord;
	int yCord;
	int bSize;
	
	BruteForceState(int theBoard[][], int x, int y) {
		board = theBoard;
		xCord = x;
		yCord = y;
		bSize = board[0].length;
	}
	
	BruteForceState nextState(int chosenNumber) {
		
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
		return new BruteForceState(newBoard, nextXCord, nextYCord);
	}
	
	
	List<Integer> availMoves() {
		List<Integer> notAvailMoves = getBox();
		// add all numbers in horizontal line
		for (int i = 0; i < xCord; i++) {
			if (board[i][yCord] != 0) {
				if (!notAvailMoves.contains(board[i][yCord])) {
					notAvailMoves.add(board[i][yCord]);
				}
			}
		}
		// add all numbers in vertical line
		for (int i = 0; i < yCord; i++) {
			if (board[xCord][i] != 0) {
				if (!notAvailMoves.contains(board[xCord][i])) {
					notAvailMoves.add(board[xCord][i]);
				}
			}
		}
		List<Integer> theAvailMoves = new ArrayList<Integer>();
		for (int i = 1; i < bSize + 1; i++) {
			if (!notAvailMoves.contains(i)) {
				theAvailMoves.add(i);
			}
		}
		// to randomise the chosen number so we get different sudoku maps.
		Collections.shuffle(theAvailMoves);
		
		
		return theAvailMoves;
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
	
	
	
	/*
	ArrayList<Integer>[][] availMoves;
	
	
	int[][] availMoves(){
		return null;
	}
	
	BruteForceState(int theBoard[][]) {
		board = theBoard;
		availMoves = new ArrayList[board[0].length][board[0].length];
	}
	*/
	
	/*
	public static void main(String[] args) {
		ArrayList<Integer>[][] sumMoves = new ArrayList[9][9];
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				sumMoves[i][j] = new ArrayList<Integer>();
			}
		}
		sumMoves[0][0].add(5);
		sumMoves[0][0].add(6);
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				System.out.println(sumMoves[i][j]);
			}
		}
		
	}
	*/
	
	
}
