import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

/**
 * Represents a board with an input state.
 * This class also contains all of the search methods
 * @author Andrew Josten
 *
 */
public class Board {
	private char inputState[];
	private Node root;
	
	private int depth;
	private int numcreated;
	private int numexpanded;
	private int maxfringe;
	
	public Board(String input) {		
		inputState = input.toCharArray();
		root = new Node(inputState);
		
		depth = 0;
		numcreated= 1;
		numexpanded= 1;
		maxfringe= 0;
	}
	
	//Algorithms
	/**
	 * BFS
	 */
	public void BFS() {
		//Queue
		Queue<Node> fringe = new LinkedList<>();		
		//Visited list
		Set<Node> visited = new HashSet<>();
		//Enqueue first node
		fringe.add(root);
		maxfringe++;
		
		//Loop
		while(!fringe.isEmpty()) {
			//Pop head of queue
			Node state = fringe.poll();
			visited.add(state);
			
			//Check if it's in either goal state
			if(state.isGoal()) {
				depth = state.getNumParents();
				System.out.println("Complete: [depth], [numCreated], [numExpanded], [maxFringe]");
				System.out.println(depth + ", "+ numcreated + ", " +  numexpanded +", " + maxfringe);
				break;
			}
			else if (state.isAltGoal()) {
				depth = state.getNumParents();
				System.out.println("Complete(V2): [depth], [numCreated], [numExpanded], [maxFringe]");
				System.out.println(depth + ", "+ numcreated + ", " +  numexpanded +", " + maxfringe);
			}
			else {//Not a goal state
				//Expand this node
				numexpanded++;
				
				//add viable successor states
				ArrayList<Node> kids = new ArrayList<>();
				kids.add(moveRight(state));
				kids.add(moveDown(state));
				kids.add(moveLeft(state));
				kids.add(moveUp(state));
				
				for (Node n : kids) {
					if(n != null && !visited.contains(n) && !fringe.contains(n)) {//it will be null if not a viable successor
						visited.add(n);//label as discovered
						fringe.add(n);//enqueue
						numcreated++;
					}					
				}
			}			
			maxfringe = Math.max(maxfringe, fringe.size());				
		}
	}
	
	/**
	 * DFS
	 * 
	 */
	public void DFS() {
		//Stack
		Stack<Node> fringe = new Stack<>();		
		//Visited set
		Set<Node> visited = new HashSet<>();
		//push first node
		fringe.push(root);
		maxfringe++;
						
		//Loop
		while(!fringe.isEmpty()) {
			Node state = fringe.pop();
			
			//Check if goal
			if(state.isGoal()) {
				depth = state.getNumParents();		
				System.out.println("Complete: [depth], [numCreated], [numExpanded], [maxFringe]");
				System.out.println(depth + ", "+ numcreated + ", " +  numexpanded +", " + maxfringe);
				break;
			}
			else if (state.isAltGoal()) {
				depth = state.getNumParents();
				System.out.println("Complete(V2): [depth], [numCreated], [numExpanded], [maxFringe]");
				System.out.println(depth + ", "+ numcreated + ", " +  numexpanded +", " + maxfringe);
				break;
			}
			else {//Not the goal state
				if(!visited.contains(state)) {//not yet visited. If is visited, do nothing
					numexpanded++;
					visited.add(state);
					
					ArrayList<Node> kids = new ArrayList<>();
					kids.add(moveUp(state));
					kids.add(moveLeft(state));
					kids.add(moveDown(state));
					kids.add(moveRight(state));
					
					for (Node n : kids) {
						if(n != null && !visited.contains(n)) {
							//visited.add(n);//label as discovered	
							fringe.push(n);//push
							numcreated++;
						}					
					}
				}
				maxfringe = Math.max(maxfringe, fringe.size());
			}			
		}
	}
	
	/**
	 * DLS
	 * @param limit
	 */
	public void DLS(int limit) {
		//Stack
		Stack<Node> fringe = new Stack<>();		
		//Visited set
		Set<Node> visited = new HashSet<>();
		//Depth tracker
		int depthTrack = 0;
		//push root
		fringe.push(root);
		maxfringe++;
						
		//Loop
		while(!fringe.isEmpty()) {
			depthTrack = fringe.peek().getNumParents();

			if(depthTrack > limit) {//if we're beyond the depth, prune
				fringe.pop();
			}
			else if(depthTrack <= limit) {//else, keep going
				Node state = fringe.pop();
				
				if(state.isGoal()) {
					depth = state.getNumParents();			
					System.out.println("Complete: [depth], [numCreated], [numExpanded], [maxFringe]");
					System.out.println(depth + ", "+ numcreated + ", " +  numexpanded +", " + maxfringe);
					break;
				}
				else if (state.isAltGoal()) {
					depth = state.getNumParents();
					System.out.println("Complete(V2): [depth], [numCreated], [numExpanded], [maxFringe]");
					System.out.println(depth + ", "+ numcreated + ", " +  numexpanded +", " + maxfringe);
					break;
				}
				else {//Not the goal state
					if(!visited.contains(state)) {//not yet visited
						numexpanded++;
						visited.add(state);
						
						ArrayList<Node> kids = new ArrayList<>();
						kids.add(moveUp(state));
						kids.add(moveLeft(state));
						kids.add(moveDown(state));
						kids.add(moveRight(state));
						
						for (Node n : kids) {
							if(n != null && !visited.contains(n)) {//it will be null if not a viable successor
								//visited.add(n);//label as discovered
								fringe.push(n);//enstack
								numcreated++;
							}					
						}
					}
				}
				maxfringe = Math.max(maxfringe, fringe.size());
			}
		}
		if(fringe.isEmpty()) {
			System.out.println("Unsolvable at limit " + limit + ": -1, 0, 0, 0");	
		}
	}
	
	
	/**
	 * ID
	 * 
	 */
	public void ID() {
		System.out.println("NOT IMPLEMENTED: chose to go with DLS instead");
	}
	
	
	/**
	 * GBFS
	 * @param htype
	 */
	public void GBFS(String htype) {
		//Priority queue sorted by chosen hueristic
		PriorityQueue<Node> fringe;
		if(htype.equals("h1")) {
			fringe = new PriorityQueue<>(50, new HamComparator());
		}
		else {
			fringe = new PriorityQueue<>(50, new ManComparator());
		}
		//Visited set
		Set<Node> visited = new HashSet<>();
		//Enqueue root
		fringe.add(root);
		maxfringe++;
						
		//Loop
		while(!fringe.isEmpty()) {
			Node state = fringe.poll();
			visited.add(state);
			
			if(state.isGoal()) {
				depth = state.getNumParents();			
				System.out.println("Complete: [depth], [numCreated], [numExpanded], [maxFringe]");
				System.out.println(depth + ", "+ numcreated + ", " +  numexpanded +", " + maxfringe);
				break;
			}
			else if (state.isAltGoal()) {
				depth = state.getNumParents();
				System.out.println("Complete(V2): [depth], [numCreated], [numExpanded], [maxFringe]");
				System.out.println(depth + ", "+ numcreated + ", " +  numexpanded +", " + maxfringe);
				break;
			}
			else {//Not the goal state
				//Expand a new node
				numexpanded++;
				
				//add viable successor states
				ArrayList<Node> kids = new ArrayList<>();
				kids.add(moveUp(state));
				kids.add(moveLeft(state));
				kids.add(moveDown(state));
				kids.add(moveRight(state));
				
				for (Node n : kids) {
					if(n != null) {//it will be null if not a viable successor
						//If this node is not visited, 
						if(!visited.contains(n)) {
							visited.add(n);//label as discovered
							fringe.add(n);//enqueue
							numcreated++;
						}
					}					
				}
				maxfringe = Math.max(maxfringe, fringe.size());
			}			
		}
	}
	
	public void AStar(String htype) {
		//priority queue based on chosen hueristic
		PriorityQueue<Node> fringe;
		if(htype.equals("h1")) {
			fringe = new PriorityQueue<>(50, new HamComparatorStar());
		}
		else {
			fringe = new PriorityQueue<>(50, new ManComparatorStar());
		}		
		//Visited set
		Set<Node> visited = new HashSet<>();
		//Enqueue root
		fringe.add(root);
		maxfringe++;
		
		//Loop
		while(!fringe.isEmpty()) {
			Node state = fringe.poll();
			visited.add(state);
			
			if(state.isGoal()) {
				depth = state.getNumParents();			
				System.out.println("Complete: [depth], [numCreated], [numExpanded], [maxFringe]");
				System.out.println(depth + ", "+ numcreated + ", " +  numexpanded +", " + maxfringe);
				break;
			}
			else if (state.isAltGoal()) {
				depth = state.getNumParents();
				System.out.println("Complete(V2): [depth], [numCreated], [numExpanded], [maxFringe]");
				System.out.println(depth + ", "+ numcreated + ", " +  numexpanded +", " + maxfringe);
				break;
			}
			else {//Not the goal state
				//Expand a new node
				numexpanded++;				
				
				//add viable successor states
				ArrayList<Node> kids = new ArrayList<>();
				kids.add(moveUp(state));
				kids.add(moveLeft(state));
				kids.add(moveDown(state));
				kids.add(moveRight(state));
								
				for (Node n : kids) {
					if(n != null) {//it will be null if not a viable successor
						//If this node is not visited, 
						if(!visited.contains(n)) {
							visited.add(n);//label as discovered
							fringe.add(n);//enqueue
							numcreated++;
						}
					}					
				}
				maxfringe = Math.max(maxfringe, fringe.size());
			}			
		}
	}
	
	/**
	 * Checks solvabilty via number of inversions
	 * https://www.geeksforgeeks.org/check-instance-15-puzzle-solvable/
	 * 
	 * Not used because we are evaluating to either goal state
	 */
	public boolean isSolvable() {
		Node node = root;
		int inversions = 0;
		char[] state = node.getState();
		int spacerow = node.get0() / 4;
		
		int[] numstate = new int[16];
		for(int k = 0; k< state.length; k++) {
			numstate[k] = Character.getNumericValue(state[k]);
			if(numstate[k] == -1) {//fix the space
				numstate[k] = 16;
			}
		}
		
		for(int i = 0; i< numstate.length; i++) {
			for(int j = i+1; j < state.length; j++) {
				if(numstate[i] > numstate[j]) {
					inversions++;
				}
			}
		}
		
		if(inversions % 2 == 0 && spacerow % 2 != 0) {
			return true;
		}
		else if(inversions % 2 != 0 && spacerow % 2 == 0) {
			return true;
		}
		else {
			return false;
		}
	}
	
	//Mover methods
	//These return null if an illegal move
	private Node moveRight(Node node) {
		//illegal moves indicies: 3, 7, 11, 15
		int i = node.get0();
		if(i == 3 ||  i == 7 || i == 11 || i == 15) {
			return null;
		}
		else {
			//move space to the right: space ++
			char[] childstate = node.getState();
			char temp = childstate[i+1];
			childstate[i+1] = childstate[i];
			childstate[i] = temp;
			
			return new Node(childstate, node);
		}		
	}
	private Node moveDown(Node node) {
		//illegal: 12, 13, 14, 15
		int i = node.get0();
		if(i == 12 ||  i == 13 || i == 14 || i == 15) {
			return null;
		}
		else {
			//move space down: space +4
			char[] childstate = node.getState();
			char temp = childstate[i+4];
			childstate[i+4] = childstate[i];
			childstate[i] = temp;
			
			return new Node(childstate, node);
		}	
	}
	private Node moveLeft(Node node) {
		//illegal: 0, 4, 8, 12
		int i = node.get0();
		if(i == 0 ||  i == 4 || i == 8 || i == 12) {
			return null;
		}
		else {
			//move space to the left: space --
			char[] childstate = node.getState();
			char temp = childstate[i-1];
			childstate[i-1] = childstate[i];
			childstate[i] = temp;
			
			return new Node(childstate, node);
		}	
	}
	private Node moveUp(Node node) {
		//illegal: 0, 1, 2, 3
		int i = node.get0();
		if(i == 0 ||  i == 1 || i == 2 || i == 3) {
			return null;
		}
		else {
			//move space up: space - 4
			char[] childstate = node.getState();
			char temp = childstate[i-4];
			childstate[i-4] = childstate[i];
			childstate[i] = temp;
			
			return new Node(childstate, node);
		}	
	}
}


/**
 * A comparator used for h1
 * @author Andrew Josten
 *
 */
class HamComparator implements Comparator<Node> {
	@Override
	public int compare(Node arg0, Node arg1) {
		int a = arg0.geth1();
		int b = arg1.geth1();
		if (a > b) {
            return 1;
		}
        else if (a < b) {
            return -1;            
        }
		return 0;
	}
}

/**
 * A comparator used for h2
 * @author Andrew Josten
 *
 */
class ManComparator implements Comparator<Node> {
	@Override
	public int compare(Node arg0, Node arg1) {
		int a = arg0.geth2();
		int b = arg1.geth2();
		if (a > b) {
            return 1;
		}
        else if (a < b) {
            return -1;            
        }
		return 0;
	}
}


/**
 * A comparator used for h1 in star
 * @author Andrew Josten
 *
 */
class HamComparatorStar implements Comparator<Node> {
	@Override
	public int compare(Node arg0, Node arg1) {
		int a = arg0.geth1() + arg0.getNumParents();
		int b = arg1.geth1() + arg1.getNumParents();
		if (a > b) {
            return 1;
		}
        else if (a < b) {
            return -1;            
        }
		return 0;
	}
}

/**
 * A comparator used for h2 in A star
 * @author Andrew Josten
 *
 */
class ManComparatorStar implements Comparator<Node> {
	@Override
	public int compare(Node arg0, Node arg1) {
		int a = arg0.geth2() + arg0.getNumParents();
		int b = arg1.geth2() + arg1.getNumParents();
		
		if (a > b) {
            return 1;
		}
        else if (a < b) {
            return -1;            
        }
		return 0;
	}
}
