import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import java.util.Arrays;
import java.util.List;
import javafx.geometry.*;
import javafx.scene.paint.*;

/**
 * This class represents the game Gomoku which is played on a grid of squares.
 * It is a two player game (Black & White) and the players alternate playing
 * pieces on the board. A player wins if they get a set amount of "stones" in a
 * row in any direction.
 * 
 * @author Akhila Vuppalapati
 */
public class Gomoku extends Application {
	// Adding GridPane
	private GridPane gridPane = new GridPane();

	// 2D array of Buttons with value of 19,19
	private Button[][] board;

	// how many stones you need to win
	private int toWin;

	// keeps track of whose turn it is
	private int turn = 1;

	// essentially the black circle
	private BackgroundFill fillBlack = new BackgroundFill(Color.BLACK, new CornerRadii(40), new Insets(1));

	// essentially the white circle
	private BackgroundFill fillWhite = new BackgroundFill(Color.WHITE, new CornerRadii(40), new Insets(1));

	// stores all the possible directions of the stone placed
	private int[][] directions = { { 0, 1 }, { 1, 1 }, { 1, 0 }, { 1, -1 }, { 0, -1 }, { -1, -1 }, { -1, 0 },
			{ -1, 1 } };

	// just stores whether someone has won or not. if 0, has not won. if 1, has won.
	private int won = 0;

	/**
	 * This method handles how to take in the parameters put into the main method.
	 * 
	 * @param inputs this is just what the user inputs when running the program
	 */
	public void inputs(List<String> inputs) {
		// when the user inputs " 'java Gomoku' + toWin"
		if (inputs.size() == 1) {
			board = new Button[19][19];
			toWin = Integer.parseInt(inputs.get(0));
		}
		// when the user inputs "java Gomoku"
		// the toWin is 5 when the user does not assign a value
		else if (inputs.size() == 0) {
			board = new Button[19][19];
			toWin = 5;
		}
		// when the user inputs " 'java Gomoku' + i + j "
		// i and j are ints that will be used in start when making the int[][] of
		// buttons
		// the toWin is 5 when the user does not assign a value
		else if (inputs.size() == 2) {
			board = new Button[Integer.parseInt(inputs.get(0))][Integer.parseInt(inputs.get(1))];
			toWin = 5;
		}
		// when the user inputs " 'java Gomoku' + toWin + i + j "
		// i and j are ints that will be used in start when making the int[][] of
		// buttons
		else if (inputs.size() == 3) {
			board = new Button[Integer.parseInt(inputs.get(1))][Integer.parseInt(inputs.get(2))];
			toWin = Integer.parseInt(inputs.get(0));
		} else {
			board = new Button[Integer.parseInt(inputs.get(1))][Integer.parseInt(inputs.get(2))];
			toWin = Integer.parseInt(inputs.get(0));
		}
	}

	/**
	 * This method builds the board and calls the methods that happen when the mouse
	 * clicks or hovers.
	 * 
	 * @param primaryStage Stage that the gridpane is on
	 */
	@Override
	public void start(Stage primaryStage) {
		// sets title of the window
		primaryStage.setTitle("Game of Gomoku. Best of Luck!");
		// tells what inputs() will have as its parameters
		inputs(getParameters().getRaw());
		// Two FOR loops used for creating 2D array of buttons with values i,j
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				// Initializing 2D buttons with values i,j
				board[i][j] = new Button("");
				// sets size of the buttons
				// this is also an extra credit component. it sets the size of the button.
				board[i][j].setPrefSize(40, 40);
				// create a background fill
				// this is also an extra credit component. it has the color be ~different~ than
				// the typical board.
				BackgroundFill button_fill = new BackgroundFill(Color.CADETBLUE, CornerRadii.EMPTY, Insets.EMPTY);
				// create Background
				Background background = new Background(button_fill);
				// set background
				board[i][j].setBackground(background);
				board[i][j].setOnAction(clickButton);
				gridPane.add(board[i][j], i, j);
				board[i][j].setOnMouseEntered(enterEvent);
				board[i][j].setOnMouseExited(exitEvent);
			}
		}
		// adds the gridlines because of the spacing
		gridPane.setHgap(3);
		gridPane.setVgap(3);
		// create a background fill
		BackgroundFill background_fill = new BackgroundFill(Color.BROWN, CornerRadii.EMPTY, Insets.EMPTY);
		// create Background
		Background background = new Background(background_fill);
		// set background
		gridPane.setBackground(background);
		// Adding GridPane to the scene
		Scene scene = new Scene(gridPane);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	/**
	 * Event handler for the button that has been pressed.
	 * 
	 * @param e is the event that triggers the event handler.
	 */
	EventHandler<ActionEvent> clickButton = new EventHandler<ActionEvent>() {
		public void handle(ActionEvent e) {
			Button button = (Button) e.getSource();
			if (won == 0) {
				if (allowed(button)) {
					setColor(button);
					if (hasWon(button)) {
						System.out.println("You won!");
						won = 1;
					}
					changeTurn();
				}
			}
		}
	};

	/**
	 * This is part of my extra credit. It adds a shadow effect when hovering over a
	 * button.
	 * 
	 * @param e is the event that triggers the event handler.
	 */
	EventHandler<MouseEvent> enterEvent = new EventHandler<MouseEvent>() {
		DropShadow shadow = new DropShadow();

		public void handle(MouseEvent e) {
			Button button = (Button) e.getSource();
			button.setEffect(shadow);
		}
	};

	/**
	 * This is part of my extra credit. It takes away the shadow effect when no
	 * longer hovering over a button.
	 * 
	 * @param e is the event that triggers the event handler.
	 */
	EventHandler<MouseEvent> exitEvent = new EventHandler<MouseEvent>() {
		public void handle(MouseEvent e) {
			Button button = (Button) e.getSource();
			button.setEffect(null);
		}
	};

	/**
	 * This method gets the color of the button. If there is a stone it tells the
	 * color of that. If there is not a stone there it will just return the color of
	 * the rest of the board.
	 * 
	 * @param button this is the button that was passed in
	 * @return returns a string of the hex code of the color of the stone or the
	 *         color of the board
	 */
	public String getColor(Button button) {
		// there is not a second BackgroundFill returns the background color
		if (button.getBackground().getFills().size() == 1) {
			// hex code for the background color
			return "0x5f9ea0ff";
		}
		// returns the color of the stone on the board
		return button.getBackground().getFills().get(1).getFill().toString();
	}

	/**
	 * This just adds a stone to the board.
	 * 
	 * @param button this is the button that was passed in
	 */
	public void setColor(Button button) {
		if (turn == 1) {
			button.setBackground(new Background(button.getBackground().getFills().get(0), fillBlack));
		} else {
			button.setBackground(new Background(button.getBackground().getFills().get(0), fillWhite));
		}
	}

	/**
	 * This changes whose turn it is.
	 */
	public void changeTurn() {
		/*
		 * the comment below is just me learning how to write shorter code. I left it as
		 * wanted to write it and be able to reference it in the future if I ever use
		 * this code.
		 */
		// turn = turn == 0 ? 1 : 0;
		if (turn == 0) {
			turn = 1;
		} else {
			turn = 0;
		}
	}

	/**
	 * This method just checks to see whether there is a stone in that position on
	 * the board.
	 * 
	 * @param button this is the button that was passed in
	 * @return returns true or false. true if there already is a stone in that
	 *         position on the board and false if there is not a stone at that
	 *         position on the board.
	 */
	public boolean taken(Button button) {
		return getColor(button).equals("0x000000ff") || getColor(button).equals("0xffffffff");
	}

	/**
	 * This method just sees if it is possible to put a stone at that position on
	 * the board.
	 * 
	 * @param button this is the button that was passed in
	 * @return returns true or false. true if a stone can be placed there and false
	 *         if the stone can't be placed there.
	 */
	public boolean allowed(Button button) {
		// allowed to put here if space is empty, does not violate 3by3 and 4by4
		if (!taken(board[getXOfButton(button)][getYOfButton(button)]) && !threeThree(button) && !fourFour(button)) {
			return true;
		}
		return false;
	}

	/**
	 * This is a rule that prevents a stone being placed if that move would cause
	 * more than two groups of two less than is needed to win in a row.
	 * 
	 * @param button this is the button that was passed in
	 * @return returns true or false depending on if the three-three rule is
	 *         violated. true if the stone would violate the three-three rule and
	 *         false if it would not.
	 */
	public boolean threeThree(Button button) {
		// returns true if the number of stones in the positive slope diagonal and
		// negative slope diagonal are the same AND they both equal two less than toWin
		if (checkAllDirections(button).get(0) == toWin - 2 && checkAllDirections(button).get(1) == toWin - 2) {
			return true;
		}
		// returns true if the number of stones in the positive slope diagonal and
		// horizontal direction are the same AND they both equal two less than toWin
		else if (checkAllDirections(button).get(0) == toWin - 2 && checkAllDirections(button).get(2) == toWin - 2) {
			return true;
		}
		// returns true if the number of stones in the positive slope diagonal and
		// vertical direction are the same AND they both equal two less than toWin
		else if (checkAllDirections(button).get(0) == toWin - 2 && checkAllDirections(button).get(3) == toWin - 2) {
			return true;
		}
		// returns true if the number of stones in the negative slope diagonal and
		// horizontal direction are the same AND they both equal two less than toWin
		else if (checkAllDirections(button).get(1) == toWin - 2 && checkAllDirections(button).get(2) == toWin - 2) {
			return true;
		}
		// returns true if the number of stones in the negative slope diagonal and
		// vertical direction are the same AND they both equal two less than toWin
		else if (checkAllDirections(button).get(1) == toWin - 2 && checkAllDirections(button).get(3) == toWin - 2) {
			return true;
		}
		// returns true if the number of stones in the horizontal direction and vertical
		// direction are the same AND they both equal two less than toWin
		else if (checkAllDirections(button).get(2) == toWin - 2 && checkAllDirections(button).get(3) == toWin - 2) {
			return true;
		}
		// returns false if the stone would not violate the three-three rule
		return false;
	}

	/**
	 * This is a rule that prevents a stone being placed if that move would cause
	 * more than two groups of one less than is needed to win in a row.
	 * 
	 * @param button this is the button that was passed in
	 * @return returns true or false depending on if the four-four rule is violated.
	 *         true if the stone would violate the four-four rule and false if it
	 *         would not.
	 */
	public boolean fourFour(Button button) {
		// returns true if the number of stones in the positive slope diagonal and
		// negative slope diagonal are the same AND they both equal one less than toWin
		if (checkAllDirections(button).get(0) == toWin - 1 && checkAllDirections(button).get(1) == toWin - 1) {
			return true;
		}
		// returns true if the number of stones in the positive slope diagonal and
		// horizontal direction are the same AND they both equal one less than toWin
		else if (checkAllDirections(button).get(0) == toWin - 1 && checkAllDirections(button).get(2) == toWin - 1) {
			return true;
		}
		// returns true if the number of stones in the positive slope diagonal and
		// vertical direction are the same AND they both equal one less than toWin
		else if (checkAllDirections(button).get(0) == toWin - 1 && checkAllDirections(button).get(3) == toWin - 1) {
			return true;
		}
		// returns true if the number of stones in the negative slope diagonal and
		// horizontal direction are the same AND they both equal one less than toWin
		else if (checkAllDirections(button).get(1) == toWin - 1 && checkAllDirections(button).get(2) == toWin - 1) {
			return true;
		}
		// returns true if the number of stones in the negative slope diagonal and
		// vertical direction are the same AND they both equal one less than toWin
		else if (checkAllDirections(button).get(1) == toWin - 1 && checkAllDirections(button).get(3) == toWin - 1) {
			return true;
		}
		// returns true if the number of stones in the horizontal direction and vertical
		// direction are the same AND they both equal one less than toWin
		else if (checkAllDirections(button).get(2) == toWin - 1 && checkAllDirections(button).get(3) == toWin - 1) {
			return true;
		}
		return false;
	}

	/**
	 * This method will check to see if the user has won the game (gotten toWin
	 * stones in a row)
	 * 
	 * @param button this is the button that was passed in
	 * @return returns true or false. true if the user has won the game (gotten
	 *         toWin stones in a row) and false if the user has not (and should
	 *         continue playing the game).
	 */
	public boolean hasWon(Button button) {
		// returns true if the number of stones in the positive slope diagonal is the
		// amount needed to win
		if (checkAllDirections(button).get(0) == toWin) {
			return true;
		}
		// returns true if the number of stones in the negative slope diagonal is the
		// amount needed to win
		else if (checkAllDirections(button).get(1) == toWin) {
			return true;
		}
		// returns true if the number of stones in the horizontal direction is the
		// amount needed to win
		else if (checkAllDirections(button).get(2) == toWin) {
			return true;
		}
		// returns true if the number of stones in the vertical direction is the amount
		// needed to win
		else if (checkAllDirections(button).get(3) == toWin) {
			return true;
		}
		return false;
	}

	/**
	 * This method adds up all same colored, consecutive stones in one direction for
	 * all of the directions.
	 * 
	 * @param button this is the button that was passed in
	 * @return returns a list with the number of stones in each direction
	 */
	public List<Integer> checkAllDirections(Button button) {
		return Arrays.asList(
				// the number of stones in the positive slope diagonal in total
				checkOneDirection(button, 1) + checkOneDirection(button, 5) + 1,
				// the number of stones in the negative slope diagonal in total
				checkOneDirection(button, 3) + checkOneDirection(button, 7) + 1,
				// the number of stones in the horizontal direction in total
				checkOneDirection(button, 2) + checkOneDirection(button, 6) + 1,
				// the number of stones in the vertical direction in total
				checkOneDirection(button, 0) + checkOneDirection(button, 4) + 1);
	}

	/**
	 * This method counts up same colored, consecutive stones in one direction.
	 * 
	 * @param button    this is the button that was passed in
	 * @param direction this is the location of an array in the directions array
	 * @return returns how many same colored, consecutive stones are in one
	 *         direction.
	 */
	public int checkOneDirection(Button button, int direction) {
		// hex code of white or black depending on whose turn it is
		String color = new String();
		if (turn == 0) {
			color = "0xffffffff";
		} else {
			color = "0x000000ff";
		}
		int x = directions[direction][0];
		int y = directions[direction][1];
		int xx = directions[direction][0];
		int yy = directions[direction][1];
		int numOfStonesInARow = 0;
		// checks to make sure that the stones are the same color before counting them
		while (isValid(getXOfButton(button) + x, getYOfButton(button) + y)
				&& color.equals(getColor(board[getXOfButton(button) + x][getYOfButton(button) + y]))) {
			numOfStonesInARow++;
			x = x + xx;
			y = y + yy;
		}
		return numOfStonesInARow;
	}

	/**
	 * This method ensures that it's possible to keep checking (there's a button
	 * there/there's more board left to check).
	 * 
	 * @param a this is the x-value of the button
	 * @param b this is the y-value of the button
	 * @return returns true or false. true if there is more board. false if there is
	 *         not.
	 */
	public boolean isValid(int a, int b) {
		return a >= 0 && a < board.length && b >= 0 && b < board[0].length;
	}

	/**
	 * This method gets the x-value of a button
	 * 
	 * @param button this is the button that was passed in
	 * @return returns the x-value of the button
	 */
	public static int getXOfButton(Button button) {
		return GridPane.getColumnIndex(button);
	}

	/**
	 * This method gets the y-value of a button
	 * 
	 * @param button this is the button that was passed in
	 * @return returns the y-value of the button
	 */
	public static int getYOfButton(Button button) {
		return GridPane.getRowIndex(button);
	}

	/**
	 * The main method here handles the content that is passed in by the user when
	 * running it.
	 * 
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		launch(args);
	}

	/*
	 * ---------------------------------------------------------------------- These
	 * are getter and setter methods I wrote to help during testing. Not actually
	 * part of the application code.
	 * ----------------------------------------------------------------------
	 */
	public GridPane getGridPane() {
		return gridPane;
	}

	public void setGridPane(GridPane gridPane) {
		this.gridPane = gridPane;
	}

	public Button[][] getBoard() {
		return board;
	}

	public void setBoard(Button[][] board) {
		this.board = board;
	}

	public int getToWin() {
		return toWin;
	}

	public void setToWin(int toWin) {
		this.toWin = toWin;
	}

	public int getTurn() {
		return turn;
	}

	public void setTurn(int turn) {
		this.turn = turn;
	}

	public BackgroundFill getFillBlack() {
		return fillBlack;
	}

	public void setFillBlack(BackgroundFill fillBlack) {
		this.fillBlack = fillBlack;
	}

	public BackgroundFill getFillWhite() {
		return fillWhite;
	}

	public void setFillWhite(BackgroundFill fillWhite) {
		this.fillWhite = fillWhite;
	}

	public int[][] getDirections() {
		return directions;
	}

	public void setDirections(int[][] directions) {
		this.directions = directions;
	}

	public EventHandler<ActionEvent> getClickButton() {
		return clickButton;
	}

	public void setClickButton(EventHandler<ActionEvent> clickButton) {
		this.clickButton = clickButton;
	}

	public EventHandler<MouseEvent> getEnterEvent() {
		return enterEvent;
	}

	public void setEnterEvent(EventHandler<MouseEvent> enterEvent) {
		this.enterEvent = enterEvent;
	}

	public EventHandler<MouseEvent> getExitEvent() {
		return exitEvent;
	}

	public void setExitEvent(EventHandler<MouseEvent> exitEvent) {
		this.exitEvent = exitEvent;
	}
}