
public class Main {

	public static void main(String[] args) {
		
		// In sudoku the board needs to be square number (2, 4, 9, 16) where the root is whole number.
		int boardSize;
		//with size 9
		boardSize = 9;
		//BruteForceAgent bfa = new BruteForceAgent(boardSize);
		CounteringConstraintsAgent cca = new CounteringConstraintsAgent(boardSize);

	}

}
