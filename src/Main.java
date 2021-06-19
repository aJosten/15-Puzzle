import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

/**
 * 
 * @author Andrew Josten
 * TCSS 435 Assignment 1
 * 15 Puzzle
 *
 */
public class Main {
	public static void main(String args[]) {
		Scanner sc = new Scanner(System.in);
		String next = "";
		
		
		//Node a = new Node(("123456789ABCDEF ").toCharArray());
		//Node b = new Node(("624F91A38 57DECB").toCharArray());
		//Set<Node> visited = new HashSet<>();
		//visited.add(a);
		/*for (int x = 0; x < 16; x++) {
			System.out.println(Character.getNumericValue(a.getState()[x]));
        	
        }*/
		//System.out.println(a.geth2());
		//System.out.println(b.geth2());
		
		//Input loop
		while(!next.equals("q")) {
			//Prompt
			System.out.println("Type 'q' to quit");
			System.out.println("Input 15-puzzle as “[initialstate]” [searchmethod] [options]:");
			next = sc.nextLine();
			
			//Check if quit
			if(next.equals("q")) {
				break;
			}
			
			//Parse
			String state = "";
			String method = "";
			String opt = "";
			
			//Get instructions
			state = next.substring(1, 17);
			next = next.substring(19);
			String[] instructs = next.split("\\s+");
			
			if(state.length() != 16) {
				System.out.println("Bad Input (Initial state), try again.");
			}
			else if(instructs.length < 1 || instructs.length > 2) {
				System.out.println("Bad Input (Arguements), try again.");
			}
			else {
				method = instructs[0];
				if(method.equals("DLS") || method.equals("GBFS") || method.equals("AStar")) {
					opt = instructs[1];
				}
				solve(state, method, opt);
			}
		}
		System.out.println("Quitting...");		
		sc.close();
	}

	public static void solve(String state, String method, String opt) {
		System.out.println(state + ", " + method + ", " + opt);
		Board x = new Board(state);
		
		/*if(!x.isSolvable()) {//Not used because we are using either goal state
			System.out.println("Unsolvable: -1, 0, 0, 0");			
		}*/
		if(method.equals("BFS")) {
			try {
				x.BFS();
			}
			catch(OutOfMemoryError E){
				System.out.println("Unsolvable for BFS: -1, 0, 0, 0");	
			}
		}
		else if(method.equals("DFS")) {
			try {
				x.DFS();
			}
			catch(OutOfMemoryError E){
				System.out.println("Unsolvable for DFS: -1, 0, 0, 0");	
			}
		}
		else if(method.equals("DLS")) {
			x.DLS(Integer.parseInt(opt));	
		}
		else if(method.equals("ID")) {
			x.ID();
		}
		else if(method.equals("GBFS")) {
			x.GBFS(opt);
		}
		else if(method.equals("AStar")) {
			x.AStar(opt);
		}
		else {
			System.out.println("Bad Input (Search Type: " + method + "), try again.\n");
		}
		System.out.println();
	}
}