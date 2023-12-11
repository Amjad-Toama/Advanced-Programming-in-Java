import javax.swing.JOptionPane;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

/*  Take turns dropping chips into the columns in order to get four in a row.
 *  To drop a chip, click on a column on the board or click and drag your chip into a column.
 *  Connect 4 pieces horizontally, vertically or diagonally to win the match. 
 *  You can play against the computer, a friend, or challenge players online.*/
public class FourInARowController {

	// Constants
	private static final int NUM_OF_COL = 7;	// Number of columns
	private static final int NUM_OF_ROW = 6;	// Number of rows
	private static final int FULL_COL = -1;		// Indicate to full column
	private static final int SERIAL_CHIPS = 4;	// The number of Chips in 

	
	@FXML private HBox buttonHBox;		// Hbox contains the buttons.
	@FXML private Button clearButton;	// Clear button.
	@FXML private Pane gameAreaPane;	// Pane 

	
	private Button buttons[];		// The buttons to specify the column.
	private char turnFlag = 'r';	// The player turn - always red first.
	private static double radius;	// The radius (The minimum) of the circle to be draw in the cell.
	private static double width, height;	// The half size of both cells height and width.
	private static int countTurns = 0;		// Count the number of rounds to terminate the game.
	// 2-Dimentional array to manage the game.
	private char gameBoard[][] = new char[NUM_OF_ROW][NUM_OF_COL];
	
	/* Create and initialize buttons as much as the column number */
	@FXML
	public void initialize() {
		buttons = new Button[NUM_OF_COL];	// Button array to initialize the buttons.
		radius = getRadius();	// Calculate the minimum radius, in order to avoid overlapping.
		drawBoardLines();		// Draw the game borders.

		//	Create and initialize the buttons for each column.
		for (int i = 0; i < NUM_OF_COL; i++) {
			buttons[i] = new Button(""+(i+1));	// Set proper text on the button.
			// Specify the size of each buttons - All have same size.
			buttons[i].setPrefSize(buttonHBox.getHeight(), buttonHBox.getHeight());
			// Set on action to each button to treat events from buttons.
			buttons[i].setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent e) {
					handleButton(e);
				}
			});
			// Display the button on the pane.
			buttonHBox.getChildren().add(buttons[i]);
		}

	}

	/* Handle events from button, detect the value of button in order to 
	 * insert Chip into the correct place, in case the column is available,
	 * else ask the user to insert again.
	 * After each turn check if winning detected.
	 */
	private void handleButton(ActionEvent e) {
		// Detect the button, and parse the text to check its value.
		Button btn = (Button) e.getSource();
		// row indicate the specify row - if available.
		// col indicate the specify columns as specify from the pressed button.
		int row, col = Integer.parseInt(btn.getText()) - 1;
		
		/* Check if the column has vacant place to insert the chip */
		// If no place available - infer the user in dialog box.
		if((row = getLastIndex(col)) == FULL_COL)	
			JOptionPane.showMessageDialog(null, turnFlag + " Player: Column number " + (col + 1)  + " is already full.");
		// If the place is vacant - display the chip on screen, check winning.
		// This case treat both players.
		else {
			// insert the color into the 2-Dimensional array.
			gameBoard[row][col] = turnFlag;
			// save color.
			Color color = (turnFlag == 'r')? Color.RED : Color.BLUE;
			// Display the chip on the screen.
			gameAreaPane.getChildren().add(
					/* Put the center point of the circle into the center of the cell */
					new Circle(width + 2*width*col, height + 2*height*(NUM_OF_ROW - row - 1),radius,color));
			// Check winning
			if(checkWinning(row, col, turnFlag) == true) {
				// If winning detected infer the user, give them the option either to play again or to stop.
				JOptionPane.showMessageDialog(null,"Congrats!! " + ((turnFlag == 'r')? "Red" : "Blue") + " Player Win");
				int choice = JOptionPane.showConfirmDialog(null, "Play Again?");
				// In case user want to play again reset the game.
				if(choice == JOptionPane.YES_OPTION) {
					reset();
					return;
				}else{	// otherwise deactivate the buttons.
					changeButtonsState(true);
					return;
				}
			}
			// Check if the game board filled, with no winner detected.
			if(++countTurns == NUM_OF_ROW*NUM_OF_COL) {
				JOptionPane.showMessageDialog(null,"Game Over!! No Winner Announced.");
				changeButtonsState(true); //deactivate the buttons.
				return;
			}
			// Change the turn.
			turnFlag = (turnFlag == 'r')? 'b':'r';
		}
	}
	
	/* Return the top index if available in column otherwise return FULL_COL */
	private int getLastIndex(int col) {
		// depend on the default value of char arrays - array contain 0, r or b.
		for (int i = 0; i < NUM_OF_ROW; i++) {
			if(gameBoard[i][col] == 0)
				return i;
		}
		return FULL_COL;
	}

	/* Calculate the radiuses of and return the minimum */
	private double getRadius() {
		width = gameAreaPane.getPrefWidth()/(double)(NUM_OF_COL*2);		// half Width of the cell.
		height = gameAreaPane.getPrefHeight()/(double)(NUM_OF_ROW*2);	// half Height of the cell.
		return (width < height)? width: height;
	}

	/* Draw the borders of the game board and split each column. */
	private void drawBoardLines() {	
		// width*2*(NUM_OF_COL + 1) - The total width of the board.
		// height*2*NUM_OF_ROW - The total height of the board.
		
		// Vertical lines 
		for (double i = 0; i < width*2*(NUM_OF_COL + 1); i = i + 2*width) 
			gameAreaPane.getChildren().add(new Line(i, 0, i, height*2*NUM_OF_ROW));
		// Horizontal  lines
		for (double i = 0; i < height*2*NUM_OF_ROW; i = i + 2*height) 
			gameAreaPane.getChildren().add(new Line(0, i, width*2*NUM_OF_COL, i));

	}
	
	// Check if the index are in the range of the 2-Dimentional array.
	private static boolean isLegal(int row, int col) {
		return (0 <= row && row < NUM_OF_ROW && 0 <= col && col < NUM_OF_COL);
	}
	
	// Reset the values of the game in order to start new game.
	private void reset() {
		// Clear the pane - remove the chip.
		gameAreaPane.getChildren().clear();
		// Set the red player to start always.
		turnFlag = 'r';
		// Activate the buttons.
		changeButtonsState(false);
		// Clear the 2-Dimentional array.
		gameBoard = new char[NUM_OF_ROW][NUM_OF_COL];
		// Assign to zero the number of the rounds.
		countTurns=0;
		// Draw border lines.
		drawBoardLines();
	}
	
	// Handle clear button
	@FXML
	void clearButtonPressed(ActionEvent event) {
		reset();
	}
	
	private void changeButtonsState(boolean state) {
		for (int i = 0; i < NUM_OF_COL; i++)
			buttons[i].setDisable(state);
	}

	/* Check if chips are diagonal, row or column in serial. 
	 * Overlook the 8th state to check above because no true always.
	 */
	private boolean checkWinning(int row, int col, char player) {
		int i;
		// Direction 01.
		for(i = 1; i < SERIAL_CHIPS; i++) {
			if(!(isLegal(row+i, col+i) && player == gameBoard[row+i][col+i]))
				break;
		}
		if(i == SERIAL_CHIPS) return true;
		// Direction 02.
		for(i = 1; i < SERIAL_CHIPS; i++) {
			if(!(isLegal(row, col+i) && player == gameBoard[row][col+i]))
				break;
		}
		if(i == SERIAL_CHIPS) return true;

		// Direction 03.
		for(i = 1; i < SERIAL_CHIPS; i++) {
			if(!(isLegal(row-i, col+i) && player == gameBoard[row-i][col+i]))
				break;
		}
		if(i == SERIAL_CHIPS) return true;

		// Direction 04.
		for(i = 1; i < SERIAL_CHIPS; i++) {
			if(!(isLegal(row-i, col) && player == gameBoard[row-i][col]))
				break;
		}
		if(i == SERIAL_CHIPS) return true;

		// Direction 05.
		for(i = 1; i < SERIAL_CHIPS; i++) {
			if(!(isLegal(row-i, col-i) && player == gameBoard[row-i][col-i]))
				break;
		}
		if(i == SERIAL_CHIPS) return true;

		// // Direction 06.
		for(i = 1; i < SERIAL_CHIPS; i++) {
			if(!(isLegal(row, col-i) && player == gameBoard[row][col-i]))
				break;
		}
		if(i == SERIAL_CHIPS) return true;
		// Direction 07.
		for(i = 1; i < SERIAL_CHIPS; i++) {
			if(!(isLegal(row+i, col-i) && player == gameBoard[row+i][col-i]))
				break;
		}
		if(i == SERIAL_CHIPS) return true;
		// No serials detected.
		return false;
	}
}

