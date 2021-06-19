import java.util.Arrays;

/**
 * 
 * @author Andrew Josten
 *
 * Represents a search node/state of board and the immediate previous state.
 * Various utility functions included
 * 
 */
public class Node {
	//The current look of the board
	char[] state;
	//Previous board state
	Node parent;
	//Location of '0' (the space)
	private int SpaceIndex;
	//Hueristiscs
	private int manh;
	private int hamming;
	//Potential goal states
	private String goalState1 = "123456789ABCDEF ";
	private String goalState2 = "123456789ABCDFE ";
	//Num parents of this node
	private int numParents;
	
	/**
	 * Constructor for the root node
	 * @param inputState The initial board state as a 1D char array
	 */
	public Node(char[] inputState) {
		state = inputState;
		parent = null;
		numParents = 0;
		for(int i = 0; i < state.length; i++) {
			if(state[i] == ' ') {
				SpaceIndex = i;
				break;
			}
		}
		
		hamming = 0;
		for(int i = 0; i < state.length; i++) {
			if(state[i] != goalState1.charAt(i)) {
				hamming++;
			}
		}
		
		//|x1-x2| + |y1-y2|
		int index = 0;
		manh = 0;
		for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
            	int pt = Character.getNumericValue(state[index]);
            	if(pt == -1) {//space is -1
            		pt = 16;
            	}
            	pt--;
            	int actX = pt % 4;
            	int actY = pt / 4; 
            	manh += (Math.abs(actY - y) + Math.abs(actX - x));
            	index++;
            }
		}		
	}
	
	/**
	 * Constructor for the board states post-root state
	 * @param inputState The board state as a 1D array
	 * @param inputParent the previous board node
	 */
	public Node(char[] inputState, Node inputParent) {
		state = inputState;
		parent = inputParent;
		numParents = parent.getNumParents() + 1;
		for(int i = 0; i < state.length; i++) {
			if(state[i] == ' ') {
				SpaceIndex = i;
				break;
			}
		}
		
		hamming = 0;
		for(int i = 0; i < state.length; i++) {
			if(state[i] != goalState1.charAt(i)) {
				hamming++;
			}
		}
		
		//|x1-x2| + |y1-y2|
		int index = 0;
		manh = 0;
		for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
            	int pt = Character.getNumericValue(state[index]);
            	if(pt == -1) {//space is -1
            		pt = 16;
            	}
            	pt--;
            	int actX = pt % 4;
            	int actY = pt / 4; 
            	manh += (Math.abs(actY - y) + Math.abs(actX - x));
            	index++;
            }
		}	
	}
	
	//Behavior
	/**
	 * Checks if we are in the goal state
	 * @return true if puzzle is solved
	 */
	public boolean isGoal() {			
		if(Arrays.equals(state, goalState1.toCharArray())) {
			return true;
		}
		else {
			return false;
		}		
	}
	
	/**
	 * Checks if we are in the other goal state
	 * @return true if puzzle is solved
	 */
	public boolean isAltGoal() {
		if(Arrays.equals(state, goalState2.toCharArray())) {
			return true;
		}
		else {
			return false;
		}		
	}
	
	/**
	 * Gets the index in the state of the 'space'
	 * @return int index
	 */
	public int get0() {	return SpaceIndex;}
	
	/**
	 * Gets the state as a 1D char array
	 * @return char[] representing board state
	 */
	public char[] getState() {return state.clone();}
	
	/**
	 * Gets the parent of this particular node
	 * @return Node the parent of this state
	 */
	public Node getParent() {return parent;}
	
	/**
	 * return num of parents
	 * @return
	 */
	public int getNumParents() {return numParents;}
	
	/**
	 * Num misplaced tiles
	 * Not particularly optimal for goalstate2
	 * @return
	 */
	public int geth1() {return hamming;}
	
	/**
	 * M-distance
	 * @return
	 */
	public int geth2() {return manh;}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Node n = (Node) o;
        return Arrays.equals(state, n.getState());
	}
	
	@Override
	public int hashCode() {
		return Arrays.hashCode(state);
	}
}
