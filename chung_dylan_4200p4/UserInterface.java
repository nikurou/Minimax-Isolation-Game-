import java.util.Scanner;

/**
 * CS 4200.01: Artificial Intelligence
 * 
 *
 * Programming Assignment #4
 *
 * < Description:   
 * 	 The UserInterface of the game. Handles player input and prompts user for input. 
 * 	 Also keeps the game running until no remaining moves are left for one player or another.
 * >
 *
 * @author Dylan Chung 
 *   
 */

public class UserInterface {
	Scanner kb;
	Board b1;
	String currentPlayer;
	int timeLimit;
	
	public UserInterface(){
		this.b1 = new Board();	
		this.kb = new Scanner(System.in);
		this.currentPlayer = promptFirst();
		this.timeLimit = promptTimeLimit();		
	}
	
	private int promptTimeLimit() {
		int timeLimit = 20;
		while(true){
		try{
			
			System.out.print("What is the time limit per move?: ");
			
			timeLimit = Math.abs(kb.nextInt());
			kb.nextLine();
			
			// Prevents the user from entering an unfeasible time limit. 
			if(timeLimit < 5){
				System.out.println("You can't set a value below 5 seconds, defaulting to 5 second time limit");
				timeLimit = 5;
			}
			
			return timeLimit;
		}
		catch(Exception InputMismatchException){
			System.out.println("\nInvalid input! Input must be an integer. Please try again...\n");
			kb.next();
		}
		}
	}
	

	private String promptFirst() {
		while(true){
			System.out.print("Who goes first? C for computer, O for opponent: "); 
			String first = kb.nextLine().toUpperCase();
			if(first.equals("C") || first.equals("O")){
				b1.setFirst(first);
				return first;
			}
			else
				System.out.println("\nInvalid input! Please try again...\n");
		}
	
	}

	public void startGameLoop(){
		//Print the initial board
		b1.printBoard2();
		if(b1.getFirstPlayer().equals("C")){
			System.out.println("Please allow some time for the computer to calculate it's first move. "
					+ "\nNote that max depth has been set to 6 and Iterative Deepening will not search past depth 6.");
			
		}
		while(b1.numHumanMoves != 0 && b1.numCompMoves != 0){
		
			
			//If computer's turn
			if(currentPlayer.equals("C")) {
				
				Node n1 = new Node(b1);
				Minimax m1 = new Minimax(n1, "X", timeLimit);
				//b1 = m1.decideBestMove();
				b1 = m1.iterativeDepthFirstSearchBestMove();
				b1.printBoard2(); 
	
				currentPlayer = "O"; //switch to player turn
				System.out.print("Computer's (X) move is: " + b1.compPositionX + "\n");
			}
			//else if player's turn
			else if(currentPlayer.equals("O")){
				System.out.print("Enter Players's (O) move: ");
				String playerMove = promptMove();
				executeMove(playerMove);
				b1.printBoard2(); 
				currentPlayer = "C"; //switch to computer's turn 
				System.out.print("\nPlayer's (O) move is: " + b1.playerPositionO + "\n");
				System.out.print("Computer's (X) move is: " + "......Calculating...." +"\n");
			}
		}
		
		//Print the winner!
		if(b1.numHumanMoves == 0){
			System.out.println("\nThe player has lost! Congratulations to the AI.\n");
		}
		else{
			System.out.println("\nThe player has won! Congratulations to the player.\n");
		}
	}

	
	// Moves the current player and sets their old spot to "#" to indicate used.
	private void executeMove(String move) {
		
		//If it's the player's turn during the move execution
		if(currentPlayer.equals("O")){
			
			b1.moveCurrentPlayer(b1.playerPositionO, move, "O");
		}
		
		//else it was the Computer's turn 
		else{
			b1.moveCurrentPlayer(b1.compPositionX, move, "C");
		}
		
	}

	// Gets the user's input on where they want to move.
	// Only returns the input IFF the input is valid syntatically and within the rules of the game
	private String promptMove() {
		
		String move = kb.nextLine();
		
		// Automatically uppercase the row index
		move = move.substring(0,1).toUpperCase() + move.substring(1);
		
		
		
		if(move.length() != 2 ){
			System.out.println("\nInvalid Input. Input cannot possibly be longer or shorter than 2 characters. Please try again.\n");
			return promptMove();
		}
		// ASCII for A = 65 , H = 72
		// Conditions for invalid input, such as the first index now being a row index between A-H and the column index not being between 1-8.
		else if((int)move.charAt(0) < 65 || (int)move.charAt(0) > 72 || Integer.parseInt(move.substring(1,2)) < 1 || Integer.parseInt(move.substring(1,2)) > 8){ 
			System.out.println("\nInvalid Input. Please input with the format of a row (A-H) followed by a column (1-8) with no spaces \n");
			return promptMove();
		}
		
		// If the move syntax is valid, BUT the spot is in use or has been used, reject.
		else if(b1.isOccupied(move) == true){
			System.out.println("Error: Cannot move to a position that has been used or is currently occupied. Please try again.\n");
			return promptMove();
		}
		// If move is not possible by the definition of the game rules.
		// i.e. Piece can only move like a Queen Chess piece and can't hop over any obstructions.
		else {
			if(b1.moveIsLegal(move, currentPlayer) == true){
				return move;
			}
			else {
				System.out.println("The move is not legal because something is in the way. Please try again...");
				return promptMove();
			}
		}
		
	}
	
	
	
}
