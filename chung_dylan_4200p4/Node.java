import java.util.ArrayList;

/**
 * CS 4200.01: Artificial Intelligence
 * 
 *
 * Programming Assignment #4
 *
 * < Description:   
 * The Node class which holds the board object and links it to it's children.>
 *
 * @author Dylan Chung 
 *   
 */

public class Node {
	int fitness;
	Board board;
	ArrayList<Node> children;
	Node parent;
	int utility; // "v" value
	int alpha;
	int beta;
	
	//Constructor for Root Node
	public Node(Board board){
		this.parent = null;
		this.board = board; 
		this.fitness = board.getFitnessVal();  // DO NOT CHANGE
		this.children = new ArrayList<Node>();
	}
	
	// Constructor for Non-Root Node
	public Node(Board board, Node parent){
		this.parent = parent;
		this.board = board;
		this.fitness = board.fitness;
		this.children = new ArrayList<Node>();
		parent.children.add(this);
	}
	
	
	// Given a board, generates the possible boards that can be created from it.
	// Save it as an arraylist of node object and attach it to the parent board.
	public ArrayList<Node> generateSuccessors(String character){
		
		Board curr = this.board;
		ArrayList <int []> possibleMoves = null;
		
		if(character.equals("COMPUTER")){
			possibleMoves = curr.getComputerPossibleMoveList();			
		}
		else if(character.equals("PLAYER")){
			possibleMoves = curr.getPlayerPossibleMoveList();
		}
		
	
		for(int i = 0; i< possibleMoves.size(); i++){
			Board childBoard = new Board(curr,possibleMoves.get(i)[0], possibleMoves.get(i)[1]+1, character);
			// Just by initializing this node, we have linked it to it's parent.
			Node childNode = new Node(childBoard, this);
		}
		
		return children;
	}
	
	

}
