import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;


public class HeuristicConstraintsState {
	int totalOptions;
	List<Integer>[][] moves;
	int board[][];
	int xCord;
	int yCord;
	int bSize;
	int movePrice;
	int cost;
	ArrayList<ArrayList<ArrayList<Integer>>> theAvailMoves;
	
	HeuristicConstraintsState(int theBoard[][], int x, int y, int oldTotalOptions) {
		board = theBoard;
		xCord = x;
		yCord = y;
		bSize = board[0].length;
		allAvailMoves();
		setTotalOptions();
		cost = oldTotalOptions - totalOptions;
	}
	
	HeuristicConstraintsState nextState(int chosenNumber) {
		
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
		return new HeuristicConstraintsState(newBoard, nextXCord, nextYCord, totalOptions);
	}
	
	int getCost() {
		return cost;
	}
	
	PriorityQueue<HeuristicConstraintsState> availMoves() {
		setTotalOptions();
		Comparator<HeuristicConstraintsState> comparator = new HeuristicComparator();
		PriorityQueue<HeuristicConstraintsState> toRet = new PriorityQueue<HeuristicConstraintsState>(10, comparator);
		if (isValid()) {
			Collections.shuffle(theAvailMoves.get(xCord).get(yCord));
			for (int i = 0; i < theAvailMoves.get(xCord).get(yCord).size(); i++) {
				toRet.add(nextState(theAvailMoves.get(xCord).get(yCord).get(i)));
			}
			
		}
		/*
		System.out.println("list after: ");
		PriorityQueue<HeuristicConstraintsState> toRet2 = new PriorityQueue<HeuristicConstraintsState>(10, comparator);
		while (toRet.size() > 0) {
			HeuristicConstraintsState temp = toRet.remove();
			//System.out.println("cost: " + temp.getCost());
			toRet2.add(temp);
		}
		*/
		return toRet;
	}
	
	public class HeuristicComparator implements Comparator<HeuristicConstraintsState>
    {
        @Override
        public int compare(HeuristicConstraintsState x, HeuristicConstraintsState y)
        {
            if (x.getCost() < y.getCost())
            {
                return 1;
            }
            if (x.getCost() > y.getCost())
            {
                return -1;
            }
            return 0;
        }
    }
	
	/*
	void prioritizeTheQueue() {
		for (int i = 0; i < theAvailMoves.get(xCord).get(yCord).size() - 1; i++) {
			for (int j = 0; j < theAvailMoves.get(xCord).get(yCord).size(); j++) {
				if ()
			}
		}
	}
	*/
	
	void setTotalOptions () {
		totalOptions = 0;
		for(int i = 0; i < theAvailMoves.size(); i++){
			for (int j = 0; j < theAvailMoves.get(i).size(); j++) {
				for (int k = 0; k < theAvailMoves.get(i).get(j).size(); k++) {
					totalOptions++;
				}
			}
		}
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
	
	
	void allAvailMoves() {
		// This is pretty much double array with a list
		theAvailMoves = new ArrayList<ArrayList<ArrayList<Integer>>>();
		
		for (int i = 0; i < bSize; i++) {
			theAvailMoves.add(new ArrayList<ArrayList<Integer>>());
			for (int j = 0; j < bSize; j++) {
				theAvailMoves.get(i).add(new ArrayList<Integer>());
				if (board[i][j] == 0) {
					for (int k = 1; k <= bSize; k++) {
						theAvailMoves.get(i).get(j).add(k);
					}
				}
			}
		}
		// add all numbers in vertical line
		int nIndex;
		for (int i = 0; i < bSize; i++) {
			for (int j = 0; j < bSize; j++) {
				if (board[i][j] > 0) {
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
				}
			}
		}
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
