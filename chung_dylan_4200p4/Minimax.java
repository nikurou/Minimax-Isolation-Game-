import java.util.ArrayList;

/**
 * CS 4200.01: Artificial Intelligence
 * 
 *
 * Programming Assignment #4
 *
 * < Description:   
 * An implementation of the Minimax Algorithm with Alpha-Beta Pruning used to
 * decide the computer player's most optimal move to use on it's turn......
 * 
 * Makes use of Iterative Deepening.>
 *
 * @author Dylan Chung 
 *   
 */

public class Minimax {
	int initial_depth = 3;
	int current_depth;
	int timeLimit; //MILLISECONDS
	String currentPlayer;
	Node n1;
	private long startTime;
	private boolean timeOut;
	private Node utilityState;
	private Node bestCurrentBoard;
	
	
	// Constructor
	public Minimax(Node n1, String currentPlayer, int timeLimit){
		this.timeLimit = timeLimit * 1000; //CONVERT TO MILLISECONDS
		this.n1 = n1;
		this.currentPlayer = currentPlayer;
	}
	
	
	//No Iterative Deepening, only Minimax AB Pruning to fixed depth.
	public Board decideBestMove(){
		timeOut = false;
		startTime = System.currentTimeMillis();
	
		int v = Max_Value(n1, initial_depth, Integer.MIN_VALUE, Integer.MAX_VALUE);
		utilityState = utility(v);
		
		return utilityState.board;
	}
	
	// Iterative Deepening Depth First Search Minimax with AB Pruning
	public Board iterativeDepthFirstSearchBestMove(){
		timeOut = false;
		startTime = System.currentTimeMillis();
		int i = 0;
		
		
		while(System.currentTimeMillis() - startTime <= timeLimit){
			//System.out.println("TIME ELAPSED : " + (System.currentTimeMillis() - startTime)/1000 + " seconds");
			current_depth = initial_depth + i++;
			
			int v = Max_Value(n1, current_depth, Integer.MIN_VALUE, Integer.MAX_VALUE);
		
			if(timeOut==true || current_depth == 6){ 
				return bestCurrentBoard.board;
			}
			
			bestCurrentBoard = utility(v);
			//TODO: DELETE THIS PART, FOR DEBUG ONLY!
			//bestCurrentBoard.board.printBoard2();
			//System.out.println("At current depth: " + current_depth);
		}
		return bestCurrentBoard.board;
	}
	
	// Get the node with the V value of the best found from minimax
	private Node utility(int v) {
		for(int i = 0; i< n1.children.size();i++ ){		//If it's the same board, reject
			if(n1.children.get(i).utility == v && n1.board.equals(n1.children.get(i).board) == false){
				return n1.children.get(i);
			}
		}
		//Else if it can't find anything...return a board
		return n1.children.get(0);
	}
	
	public int Max_Value(Node node, int depth, int alpha, int beta){
		int v = Integer.MIN_VALUE;
		
		//No Time Left 
		//System.out.println("TIME ELAPSED : " + (System.currentTimeMillis() - startTime)/1000 + " seconds");
		if(System.currentTimeMillis() - startTime > timeLimit){
			timeOut = true;
			node.utility = alpha;
			return alpha; //Alpha is current max.
		}
		
		if(depth == 0 ) { 
			node.utility = node.fitness;
			return node.fitness;
		}
		
		// By default, set it to this. The children have been generated prior
		ArrayList<Node> possibleNode = node.children;
		// However if successors have not been previously generated...
		if(node.children.size() == 0){
			possibleNode = node.generateSuccessors("COMPUTER");
		}
		
		for(int i = 0; i< possibleNode.size(); i++){
			int minUtility = Min_Value(possibleNode.get(i), depth-1, alpha, beta);
			if(minUtility > v ){ 
				v = minUtility;
				node.utility = v;
			}
			if(minUtility >= beta){
				return v;
			}
			if(minUtility > alpha){
				alpha = minUtility;
				node.alpha = alpha;
			}
		}
		return v;
	}
	
	public int Min_Value(Node node, int depth, int alpha, int beta){
		int v = Integer.MAX_VALUE;

		node.alpha = alpha;
		node.beta = beta;
		
		if(System.currentTimeMillis() - startTime > timeLimit){
			timeOut = true;
			node.utility = beta;
			return beta; //Alpha is current max.
		}
		
		if(depth== 0){
			node.utility = node.fitness;
			return node.fitness;
		}
		
		// By default, set it to this.
		ArrayList<Node> possibleNode = node.children;
		// However if successors have not been previously generated...
		if(node.children.size() == 0){
			possibleNode = node.generateSuccessors("PLAYER");
		}
		
		
		for(int i = 0; i< possibleNode.size(); i++){

			int maxUtility = Max_Value(possibleNode.get(i), depth-1, alpha, beta);
			if(maxUtility < v){
				v = maxUtility;
				node.utility = v;
			}
			if(maxUtility <= alpha){
				return v;
			}
			if(maxUtility < beta){
				beta = maxUtility;
				node.beta = beta;
			}
		}
		return v;
	}
	
}
