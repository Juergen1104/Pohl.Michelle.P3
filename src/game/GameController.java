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
    private GuiThread guiThread;

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
        startThreads();

    }

    public void startNewGame() {
        interruptThreads();
        this.gameState = new GameState();
        this.currentDelay = Settings.GAME_DELAY;
        this.currentMaxAsteroids = Settings.MAX_ASTEROIDS;
        this.gameActive = true;
        this.gamePaused = false;
        startThreads();
    }

    public void gameOver() {
        interruptThreads();
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

    private class GuiThread extends Thread {
        @Override
        public void run() {
            // Ändere den Namen des Threads
            Thread.currentThread().setName("GUI Thread");

            while (!Thread.interrupted()) {
                // Aktualisiere die GUI
                updateGui();

                try {
                    // Pausiere den Thread für den angegebenen Zeitraum
                    sleep(Settings.GUI_DELAY);
                } catch (InterruptedException e) {
                    // Behandlung der Unterbrechung
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    private void startThreads() {
        // Initialisiere den GuiThread
        guiThread = new GuiThread();
        // Starte den GuiThread
        guiThread.start();

    }

    private void interruptThreads() {
        if (guiThread != null) {
            guiThread.interrupt();
            guiThread = null;
        }
    }


    /* *** Aufgabe (2b) *** */




    /* *** Aufgabe (3b) *** */




    /* *** Aufgabe (4a) *** */




    /* *** Aufgabe (4b) *** */




    /* *** Aufgabe (4c) *** */


}
