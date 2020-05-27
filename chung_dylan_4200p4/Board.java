import java.util.ArrayList;


/**
 * CS 4200.01: Artificial Intelligence
 * 
 *
 * Programming Assignment #4
 *
 * < Description:   
 * The Board Class. Holds the board array, player positions, and handles printing.
 * Game logic, movement, and legal checking is all done here. 
 * >
 *
 * @author Dylan Chung 
 *   
 */



public class Board {
	String [][] board;
	String playerPositionO;
	String compPositionX;
	ArrayList<int[]> computerPossibleMoves; 
	int fitness; 
	
	private ArrayList<String> game_Log; 
	private String firstPlayer;
	int numCompMoves; // COMPUTER 
	int numHumanMoves; // Player
	private ArrayList<int[]> playerPossibleMoves;
	

	
	public Board(){
		this.board = initializeBoard(8);
		this.game_Log = new ArrayList<String>(); //Set initialsize to 10
		this.playerPositionO = "H8";
		this.compPositionX = "A1";
		this.computerPossibleMoves = new ArrayList<int[]>();
		this.playerPossibleMoves = new ArrayList<int[]>();
		this.fitness = getFitnessVal(); 

		//Set the player positions to their default positions.
		updatePosition("A", 1, "X");
		updatePosition("H", 8, "O");	
	}
	
	
	// Constructor used only while creating a list of possible boards
	// for the computer move 
	@SuppressWarnings("unchecked")
	public Board(Board b1, int tRow, int tCol, String character){
		this.board = new String[8][8];
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				this.board[i][j] = b1.board[i][j];
			}
		}
		this.playerPositionO = b1.getPlayerPositionO();
		this.compPositionX = b1.getCompPositionX();
		this.computerPossibleMoves = new ArrayList<int[]>();
		this.playerPossibleMoves = new ArrayList<int[]>();
		this.game_Log = (ArrayList<String>) b1.game_Log.clone();
		this.firstPlayer = b1.firstPlayer;
		String moveToIndex = convertToLetter(tRow) + ""+ tCol;
	
		if(character.equals("COMPUTER")){
			// Move the "C" computer player to specified R/C		
			moveCurrentPlayer(compPositionX, moveToIndex, "C");			
		}
		else if (character.equals("PLAYER")){
			// Move the "O" computer player to specified R/C
			moveCurrentPlayer(playerPositionO, moveToIndex, "O");
		}
		this.fitness = getFitnessVal(); //MUST GO AFTER MOVING C
	}
	
	private String getPlayerPositionO(){
		return playerPositionO;
	}
	private String getCompPositionX(){
		return compPositionX;
	}


	// Populate the board with empty values
	private String[][] initializeBoard(int size) {
		String [][] board = new String[8][8];
		for(int r = 0; r < 8; r++){
			for(int c = 0; c < 8; c++){
				board[r][c] = "_";
			}
		}
		return board;
	}

	public void printBoard2(){
		
		// Iterations changes dynamically based on how many logs to print
		int iterations = getIterations();																															
	
		// Print the Correct VS order based on who went first....
		if(firstPlayer.equals("C")){
			System.out.println("\n\t1 2 3 4 5 6 7 8\t\t Computer vs. Opponent\n" );			
		}
		else {
			System.out.println("\n\t1 2 3 4 5 6 7 8\t\t Opponents vs. Computer\n" );
		}
		
		// Print each row ex: "A X _ _ _ _ _ _ " + gameLog 
		for(int i = 0; i < iterations; i++){
			// PRINTED DURING THE TABLE IS PRINTING
			if(i < 8){
				try {
					
					if(game_Log.size() % 2 == 0 || i < game_Log.size()/2 ) {
						System.out.println("     "+ convertToLetter(i) + printRow(i, board) + "\t  " + (i+1)+"."+ game_Log.get(i*2) + "\t"+ game_Log.get(i*2+1));
					}
					else if(game_Log.size() % 2 == 1){
						System.out.println("     "+ convertToLetter(i) + printRow(i, board) + "\t  " + (i+1) +"."+ game_Log.get(i*2) );
					}
				}
				catch(Exception IndexOutOfBoundsException){
					System.out.println("     "+ convertToLetter(i) + printRow(i, board));
				}
			}
			// PRINTED AFTER THE TABLE IS DONE BUT LOG ENTRIES STILL EXIST
			else{
				try {
					if(game_Log.size() % 2 == 0 || i < game_Log.size()/2 ) {
						System.out.println("\t\t\t\t  "+ (i+1)+"."+ game_Log.get(i*2) + "\t"+ game_Log.get(i*2+1)) ;
					}
					else if(game_Log.size() % 2 == 1){
						System.out.println("\t\t\t\t  " + (i+1) +"."+ game_Log.get(i*2));
					}
				}
				catch(Exception IndexOutOfBoundsException){
					//Do nothing, we're just going to ignore any out of bounds at this point.
				}
				
				
			}
		}
		System.out.println(""); //This final println for formatting.
	}
	
	
	// Returns amount of iterations needed for printing in printBoard()
	// By default, we need to print at least 8 times to show the full 8x8 gameboard
	// But as the game_Log grows in size, we need more iterations to print it all.
	private int getIterations() {
		int iterations;
		if(game_Log.size() <= 8)
			iterations = 8;
		else
			iterations = game_Log.size();
		return iterations;
	}


	// Print the elements of the board in a specified row.
	private String printRow(int row, String[][] board) {
		String toReturn = "  ";
		for(int column = 0; column < board[row].length; ++column){ //Always going to be 8x8
		
			String current = board[row][column];
			
			//Print the value in the index if it exists, otherwise, print a _ to indicate it's blank.
			if(current == null){
				toReturn+=("_");
			}
			else toReturn+= current;
		
			//Print the space between the board, 

			toReturn += (" ");
	
		}
		return toReturn;
	}
	
	/*
	 * Given a row, column index and a input, update the board at the specified row/col 
	 * to house the input. 
	 */
	public void updatePosition(String row, int column, String input){
		
		int rowNumber = convertToNumber(row);
		
		//Column-1 since board is represented 1-8 instead of 0-7
		board[rowNumber][column-1] = input;
	}
	
	 /*  Rows in the program are represented as A,B,C,D,E,F,G,H
	  *  This method converts each number into the corresponding integer row value 
	  *  and returns it. 
	  */
	private int convertToNumber(String row) {
		switch(row.toUpperCase()){
			case "A":
				return 0;
			case "B":
				return 1;
			case "C":
				return 2;	
			case "D":
				return 3;
			case "E":
				return 4;
			case "F":
				return 5;
			case "G":
				return 6;
			case "H":
				return 7;
			//If input is not any of these cases.
			default:
				return -1;
				
		}
	}
	
	private String convertToLetter(int row) {
		switch(row){
			case 0:
				return "A";
			case 1:
				return "B";
			case 2:
				return "C";	
			case 3:
				return "D";
			case 4:
				return "E";
			case 5:
				return "F";
			case 6:
				return "G";
			case 7:
				return "H";
			//If input is not any of these cases.
			default:
				return null;
				
		}
	}

	// Given a string in the format A-H1-8 ex: A5
	// Check it with the graph, and see if it has been or is occupied.
	public boolean isOccupied(String move) {
		int row = convertToNumber(move.substring(0,1));
		int column = Integer.parseInt(move.substring(1)) -1;
		
		// If position is "_" then it hasn't been used before
		if(board[row][column].equals("_")){
			
			return false;
		}
		// Any other input means it's been occupied.
		else return true;
	}
	
	// Given the row and column 
	// Check it with the graph, and see if it has been or is occupied.
	public boolean isOccupied(int row, int column) {
		// If position is "_" then it hasn't been used before
		if(board[row][column].equals("_")){
			return false;
		}
		// Any other input means it's been occupied.
		else return true;
	}

	// Updates position index to a # to signify it was used
	public void setToUsed(String positionIndex) {
		int column = Integer.parseInt(positionIndex.substring(1));
		
		updatePosition(positionIndex.substring(0,1), column, "#");
		
		
	}

	// Moves a player and updates their location, and calls setToUsed() to update their old location
	// Also updates the Log and numTurns
	public void moveCurrentPlayer(String positionIndex, String moveToIndex, String playerSymbol) {
		int column = Integer.parseInt(moveToIndex.substring(1));
		String row = moveToIndex.substring(0,1);
		setToUsed(positionIndex);
		
		
		
		//COMPUTER
		if(playerSymbol.equals("C")){ 
			compPositionX = moveToIndex;
			game_Log.add(moveToIndex);
			updatePosition(row, column, "X");
			getFitnessVal(); //Update current board object fitness vals after move.
		}
		//PLAYER
		else if(playerSymbol.equals("O")){ 
			playerPositionO = moveToIndex;
			game_Log.add(moveToIndex);
			updatePosition(row, column, "O");
			getFitnessVal(); //Update current board object fitness vals after move.
			
		}
		
	}
	
	// Returns true IFF the specified movement of the current player is legal
	// within the parameters of the rules of the game....
	// i.e. Diagonal movement and Verti/Hori as far as possible, as long as there is no obstruction.
	public boolean moveIsLegal(String moveToIndex, String currentPlayer) {
		
		// This is accurate to the REAL 0-7 index representation, not to the board!
		String currentPosition = getPositionOfCurrentPlayer(currentPlayer); 
		int playerRow = convertToNumber(currentPosition.substring(0,1));
		int playerCol = Integer.parseInt(currentPosition.substring(1)) - 1;
		int moveRow = convertToNumber(moveToIndex.substring(0,1));
		int moveCol = Integer.parseInt(moveToIndex.substring(1)) - 1;
		
		boolean legality = checkIfLegal(playerRow, playerCol, moveRow, moveCol);
		
		return legality;
	}

	// Checks if the intended path between the player and where it wants to go is hindered or not
	// If it's hindered, it's illegal, and we return false, else it's legal and we return true
	private boolean checkIfLegal(int playerRow, int playerCol, int moveRow, int moveCol) {
	
		boolean isHindered = false;
		
		int rise = Math.abs(playerCol - moveCol);
		int run = Math.abs(playerRow - moveRow);
		double slope;
														
		try{
			slope = rise/run;
		}
		catch (Exception ArithmeticException) {
			slope = -1;
		}
			
		
		// If it's not a diagonal, this isn't legal move && shouldn't be exact same place
		if(slope != 1 && playerRow != moveRow && playerCol != moveCol){
			return false;
		}
		// If same row (and not same column) (same as rise = 0)
		// and nothing in that row impedes movement, return true
		else if(playerRow == moveRow && playerCol != moveCol){
			isHindered = isStraightMovementHindered(playerCol, moveCol, playerRow, false);
		}
		// If same column (and not the same row) (same as run = 0)
		else if(playerCol == moveCol && playerRow != moveRow){
			isHindered = isStraightMovementHindered(playerRow, moveRow, playerCol, true);
		}
		// If movement is diagonal
		else if(slope == 1){
			isHindered  = isDiagonalMovementHindered(playerRow, playerCol, moveRow, moveCol);
		}
		
		//If it was hindered, then it was not legal. Return the opposite.
		return !isHindered;
		
	}

	// Checks for any blockages in the path for any straight line movements (up, down, left, right)
	// Takes playerRC and moveRC, or the Row/Column of the player and target move row/column.
	// staticPlayerRC represents the row or column that both player and move target is on. 
	// Return true if there are any blockages, else false.
	private boolean isStraightMovementHindered(int playerRC, int moveRC, int staticPlayerRC, boolean vertical) {
		
		// Format the input so that it always searches left to right for rows, and downwards for columns.
		int lowRC = playerRC, hiRC = moveRC;
		if(moveRC < playerRC){
			lowRC = moveRC;
			hiRC = playerRC;
		}
		
		// Only evaluate the spaces in between our targets and check if each 
		// index between is occupied or not. If any of them are, we return true.
		for( int i = lowRC + 1; i <= hiRC-1 ; i++){
			if(vertical == false){ // Then it's horizontal movement and we check changing  column index
				if(isOccupied(staticPlayerRC, i) == true){
					return true;
				}
			}
			else if(vertical == true){ // Then it's vertical movement, and we check by changing row index
				if(isOccupied(i, staticPlayerRC) == true){
					return true;
				}
			}
			
		}
		// Found no blockages, return false.
		return false;
	}

	private boolean isDiagonalMovementHindered(int playerRow, int playerCol, int moveRow, int moveCol) {
		
		// Smaller point goes first, something is smaller if it's closer to 0,0 
		// This allows us to check in the forward direction only. 
		// By Default:
		int smallRow = playerRow, smallCol = playerCol, bigRow = moveRow,  bigCol = moveCol;
		
		// If player is not closer to 0,0 than Move.... (Simplified Distance Formula with 0,0)
		// d^2 = x^2 + y^2
		if( (playerRow*playerRow + playerCol*playerCol) > (moveRow*moveRow + moveCol*moveCol)) {
			smallRow = moveRow; smallCol = moveCol;
			bigRow = playerRow; bigCol = playerCol;
		}
		
		//Evaluating only the indexes between 
		if(smallRow < bigRow){
			for(int r = smallRow+1; r<= bigRow-1; r++){
				for(int c = smallCol+1; c<=bigCol-1; c++){
						// If the index is occupied, aside from the original one we're starting from
						// Return True;
						if(isOccupied(r + 1,c + 1) == true && (r+1 != bigRow && c+1 != bigCol)) {
							//System.out.println(board[r+1][c+1]);
							return true;
						}
				}
			}
		}
		// No hinderances, return false
		return false;
	}


	private String getPositionOfCurrentPlayer(String currentPlayer) {
		// If currentPlayer is computer
		if( currentPlayer.equals("C")){
			return compPositionX;
		}
		else // currentPlayer is the player
		{
			return playerPositionO;
		}
	}
	
	// Sets the field firstPlayer to String First
	public void setFirst(String first) {
		this.firstPlayer = first;
		
	}
	
	// Return the number of available moves for the indicated player
	public int getAvailableMoves(String player) {
		// By default; evaluate human Player
		String playerToEval = playerPositionO;
		
		// If player is "C", evaluate the computer position
		if (player.equals("C")) {
			playerToEval = compPositionX;
		}
		
		int row = convertToNumber(playerToEval.substring(0,1));
		int column = Integer.parseInt(playerToEval.substring(1)) -1;
		
		return calculateAvailableMoves(row,column, player);
	}

	
	// Given the row and column index of a player, calculates the available moves
	// left for that player.
	private int calculateAvailableMoves(int row, int column, String player) {
		
		
		int availableMoveCounter = 0;
		int tRow = row, tCol = column;
		
		// UPWARD MOVMENT
		try{
			tRow = row -1; tCol = column;
			while(board[tRow][tCol].equals("_")) {
				//System.out.println("UP"+ tRow + ","+ tCol);
				if(player.equals("C")){
					computerPossibleMoves.add(new int[]{tRow,tCol});
				}
				else if(player.equals("O")){
					playerPossibleMoves.add(new int[]{tRow,tCol});
				}
				tRow = tRow - 1;
				availableMoveCounter++; 
			}
		}
		catch( Exception ArrayIndexOutOfBoundsException){};
			
		// DOWNWARD MOVMENT
		try {
			tRow = row+1; tCol = column;
			while(board[tRow][tCol] == "_") {
				//ystem.out.println("DOWN "+ tRow + ","+ tCol);
				if(player.equals("C")){
					computerPossibleMoves.add(new int[]{tRow,tCol});
				}
				else if(player.equals("O")){
					playerPossibleMoves.add(new int[]{tRow,tCol});
				}
				tRow = tRow + 1;
				availableMoveCounter++;
			}
		} catch(Exception ArrayIndexOutOfBoundsException){};
		
		//LEFT MOVEMENT
		try{
			tRow = row; tCol = column-1;
			while(board[tRow][tCol] == "_") {
				//System.out.println("LEFT"+ tRow + ","+ tCol);
				if(player.equals("C")){
					computerPossibleMoves.add(new int[]{tRow,tCol});
				}
				else if(player.equals("O")){
					playerPossibleMoves.add(new int[]{tRow,tCol});
				}
				tCol = tCol - 1;
				availableMoveCounter++;
			}			
		}catch( Exception ArrayIndexOutOfBoundsException){};
		
		// RIGHT MOVEMENT
		try{
			tRow = row; tCol = column + 1;
			while(board[tRow][tCol] == "_") {
				//System.out.println("RIGHT"+ tRow + ","+ tCol);
				if(player.equals("C")){
					computerPossibleMoves.add(new int[]{tRow,tCol});
				}
				else if(player.equals("O")){
					playerPossibleMoves.add(new int[]{tRow,tCol});
				}
				tCol++;
				availableMoveCounter++;
			}			
		}catch( Exception ArrayIndexOutOfBoundsException){};
		
		// UP AND TO THE RIGHT DIAGONAL
		try{			
			tRow = row-1; tCol = column+1;
			while(board[tRow][tCol] == "_") {
				//System.out.println("UP AND TO THE RIGHT DIAGONAL"+ tRow + ","+ tCol);
				if(player.equals("C")){
					computerPossibleMoves.add(new int[]{tRow,tCol});
				}
				else if(player.equals("O")){
					playerPossibleMoves.add(new int[]{tRow,tCol});
				}
				tRow--; tCol++;
				availableMoveCounter++;
			}
		}catch( Exception ArrayIndexOutOfBoundsException){};
		
		// DOWNWARD TO THE RIGHT DIAGONAL
		try {
			tRow = row+1; tCol = column+1;
			while(board[tRow][tCol] == "_") {
				//System.out.println("DOWNWARD TO THE RIGHT DIAGONAL "+ tRow + ","+ tCol);
				if(player.equals("C")){
					computerPossibleMoves.add(new int[]{tRow,tCol});
				}
				else if(player.equals("O")){
					playerPossibleMoves.add(new int[]{tRow,tCol});
				}
				tRow++; tCol++;
				availableMoveCounter++;
			}
			
		}catch( Exception ArrayIndexOutOfBoundsException){};
		
		// UPWARDS AND TO THE LEFT DIAGONAL
		try{			
			tRow = row-1; tCol = column-1;
			while(board[tRow][tCol] == "_") {
				//System.out.println("UPWARDS AND TO THE LEFT DIAGONAL"+ tRow + ","+ tCol);
				if(player.equals("C")){
					computerPossibleMoves.add(new int[]{tRow,tCol});
				}
				else if(player.equals("O")){
					playerPossibleMoves.add(new int[]{tRow,tCol});
				}
				tRow--; tCol--;
				availableMoveCounter++;
			}
		}catch( Exception ArrayIndexOutOfBoundsException){};
		
		// DOWNWARDS AND TO THE LEFT DIAGONAL
		try{			
			tRow = row+1; tCol = column-1;
			while(board[tRow][tCol] == "_") {
				//System.out.println("DOWNWARDS AND TO THE LEFT DIAGONAL" + tRow + ","+ tCol);
				if(player.equals("C")){
					computerPossibleMoves.add(new int[]{tRow,tCol});
				}
				else if(player.equals("O")){
					playerPossibleMoves.add(new int[]{tRow,tCol});
				}
				tRow++; tCol--;
				availableMoveCounter++;
			}
		}catch( Exception ArrayIndexOutOfBoundsException){};
		
		
		return availableMoveCounter;
	}
	
	// Returns an arrayList of all the possible moves
	@SuppressWarnings("unchecked")
	public ArrayList<int[]> getComputerPossibleMoveList(){
		ArrayList<int[]>temp = (ArrayList<int[]>) computerPossibleMoves.clone();
		// Clear the list of moves for next iteration
		computerPossibleMoves = new ArrayList<int[]>();
		return temp;
	}
	
	// Returns an arrayList of all the possible moves
	@SuppressWarnings("unchecked")
	public ArrayList<int[]> getPlayerPossibleMoveList(){
		ArrayList<int[]>temp = (ArrayList<int[]>)playerPossibleMoves.clone();
		// Clear the list of moves for next iteration
		playerPossibleMoves = new ArrayList<int[]>();
		return temp;
	}
	
	// Heuristic Defensive
	// Number of available moves - opponent moves
	public int getFitnessVal() {
		computerPossibleMoves = new ArrayList<int[]>();
		playerPossibleMoves = new ArrayList<int[]>();
		numCompMoves = getAvailableMoves("C"); // COMPUTER
		numHumanMoves = getAvailableMoves("O"); // HUMAN
		
		double weight = (numCompMoves + numHumanMoves)/64;
		
		// Go on offensive during second half
		if(weight <= 0.5)
			fitness = numCompMoves - (numHumanMoves*2);
		// Then play defenseively second half
		if(weight > 0.5) 
			fitness = (numCompMoves*2) - numHumanMoves;
			
		return fitness;
	}


	public String getFirstPlayer() {
		return firstPlayer;
	}
	
	// Checks if a board is the same as the current board object. 
	// True if equal, otherwise false.
	public boolean equals(Board o){
		// We can assume if the board has just the player and computer in the same position in the same place
		// Then it's the same board, because it should be impossible to revisit the same spot. 
		if(o.playerPositionO == this.playerPositionO && o.compPositionX == this.compPositionX){
			return true;
		}
		else 
			return false;
	}

}
