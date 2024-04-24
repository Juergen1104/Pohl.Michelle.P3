package game;

import main.Settings;

public class GameController {

	private static GameController instance;
	private GameFrame gameFrame;
	private GameState gameState;
	private int currentDelay;
	private int currentMaxAsteroids;
	private boolean gamePaused;
	private boolean gameActive;


	public static GameController getInstance() {
		if (instance == null) {
			instance = new GameController();
		}
		return instance;
	}

	public GameFrame getGameFrame() {
		return this.gameFrame;
	}

	public GameState getGameState() {
		return this.gameState;
	}

	public boolean isActive() {
		return this.gameActive;
	}

	public boolean isPaused() {
		return this.gamePaused;
	}

	public void launch() {
		this.gameState = new GameState();
		this.gameFrame = new GameFrame();
		this.currentDelay = Settings.GAME_DELAY;
		this.currentMaxAsteroids = Settings.MAX_ASTEROIDS;
		this.gameActive = false;		
	}

	public void startNewGame() {
		this.gameState = new GameState();
		this.currentDelay = Settings.GAME_DELAY;
		this.currentMaxAsteroids = Settings.MAX_ASTEROIDS;
		this.gameActive = true;
		this.gamePaused = false;	
	}

	public void gameOver() {
		this.updateGui();

	}

	public void updateGui() {
		this.gameFrame.update();
	}

	/* *** Aufgabe (4d) *** */
	public void togglePause() {

		//	TODO

		// refresh gui for pause screen
		this.updateGui();

	}


	/* *** Aufgabe (1a) *** */

	private void startThreads() {
		
	}
	
	private void interruptThreads() {
		
	}
	
	
	/* *** Aufgabe (2b) *** */
	
	
	

	/* *** Aufgabe (3b) *** */
	
	
	
	
	/* *** Aufgabe (4a) *** */

	

	
	/* *** Aufgabe (4b) *** */

	
	
	
	/* *** Aufgabe (4c) *** */
	


}
