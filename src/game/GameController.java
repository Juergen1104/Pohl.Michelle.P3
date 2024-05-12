package game;

import main.Settings;
import spaceObjects.Spaceship;
import spaceObjects.Ufo;
import java.util.concurrent.ThreadLocalRandom;
import static main.Settings.UFO_SPAWNRATE;

public class GameController {

    private static GameController instance;
    private GameFrame gameFrame;
    private GameState gameState;
    private int currentDelay;
    private int currentMaxAsteroids;
    private boolean gamePaused;
    private boolean gameActive;
    private GuiThread guiThread;
    private GameThread gameThread;
    private SpawnThread spawnThread;
    private UfoThread ufoThread;
    private InvincibilityThread invincibilityThread;
    private IncreaseDifficultyThread increaseDifficultyThread;

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

        if (gamePaused) {
            // continue all
            guiThread.continueThread();
            gameThread.continueThread();
            spawnThread.continueThread();
            ufoThread.continueThread();
            invincibilityThread.continueThread();
            increaseDifficultyThread.continueThread();
        } else {
            // pause all
            guiThread.pause();
            gameThread.pause();
            spawnThread.pause();
            ufoThread.pause();
            invincibilityThread.pause();
            increaseDifficultyThread.pause();
        }
        // switch
        gamePaused = !gamePaused;

        // refresh gui for pause screen
        this.updateGui();
    }

    /* *** Aufgabe (1a) *** */

    private class GuiThread extends PausableThread {
        @Override
        public void run() {
            Thread.currentThread().setName("GUI Thread");

            while (!Thread.interrupted()) {
                // Aktualisiere die GUI
                updateGui();

                try {
                    sleep(Settings.GUI_DELAY);
                } catch (InterruptedException e) {
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
        if (gameThread == null || !gameThread.isAlive()) {
            gameThread = new GameThread(this);
            gameThread.start();
        }
        this.spawnThread = new SpawnThread();
        spawnThread.start();
        if (ufoThread == null || !ufoThread.isAlive()) {
            ufoThread = new UfoThread();
            ufoThread.start();
        }
        invincibilityThread = new InvincibilityThread();
        invincibilityThread.start();
        increaseDifficultyThread = new IncreaseDifficultyThread();
        increaseDifficultyThread.start();
    }

    private void interruptThreads() {
        if (guiThread != null) {
            guiThread.interrupt();
            guiThread = null;
        }
        spawnThread.interrupt();

        if (ufoThread != null) {
            ufoThread.interrupt();
            ufoThread = null;
        }

        if (invincibilityThread != null) {
            invincibilityThread.interrupt();
            invincibilityThread = null;
        }
        if (increaseDifficultyThread != null) {
            increaseDifficultyThread.interrupt();
            increaseDifficultyThread = null;
        }
    }

    public void startInvincibleThread() {
        invincibilityThread = new InvincibilityThread();
        invincibilityThread.start();
    }


    /* *** Aufgabe (2b) *** */

    private class GameThread extends PausableThread {

        public GameThread(GameController gameController) {
            //super("Game Thread");
        }

        private boolean running = true;

        @Override
        public void run() {
            GameController gameControllerInstance = GameController.getInstance();
            while (running) {
                gameState.checkCollisions();
                gameState.moveObjects();
                gameState.addScore(1);

                // Bewegung des Raumschiffs
                Spaceship playerShip = gameState.getSpaceShip();
                if (playerShip != null && gameControllerInstance.isActive() && !gameControllerInstance.isPaused()) {
                    playerShip.move();
                }
                try {
                    Thread.sleep(Settings.GAME_DELAY);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        public void stopLoop() {
            running = false;
        }
    }


    /* *** Aufgabe (3b) *** */

    private class SpawnThread extends PausableThread {
        @Override
        public void run() {
            Thread.currentThread().setName("Spawn Thread");
            int i=0;
            while (!Thread.interrupted()) {
                System.out.println("SpawnThread" + i);
                i++;
                if (gameState.getAsteroids().size() < Settings.MAX_ASTEROIDS) {
                    gameState.spawnAsteroid();
                }
                try {
                    Thread.sleep(Settings.ASTEROIDS_SPAWNRATE);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    /* *** Aufgabe (4a) *** */
    private class UfoThread extends PausableThread {
        @Override
        public void run() {
            while (!Thread.interrupted()) {
                if (isActive()) {
                    try {
                        Thread.sleep(UFO_SPAWNRATE);
                        GameController gameControllerInstance = GameController.getInstance();

                        if (!gameState.ufoActive()) {
                            gameState.spawnUfo();
                            Thread.sleep(1000);
                        }
                        Ufo ufo = gameState.getUfo();
                        while (gameControllerInstance.isActive()) {
                            Thread.sleep(400);
                            ufo.changeVelocity();
                            Thread.sleep(200);
                            // Mit 20% Wahrscheinlichkeit ein Projektil abfeuern
                            gameState.shootMissileUfo();

                            if (ThreadLocalRandom.current().nextInt(1, 101) <= 20) {
                                gameState.shootMissileUfo();
                            }
                        }
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }
    }

    /* *** Aufgabe (4b) *** */

    private class InvincibilityThread extends PausableThread {
        private long remainingTime;

        public InvincibilityThread() {
            remainingTime = Settings.SHIP_INVINCIBLE_TIME;
        }

        @Override
        public void run() {
            while (remainingTime > 0 && !Thread.interrupted()) {
                gameState.setInvincible();
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                remainingTime -= 10;
            }
            gameState.setVulnerable();
        }
    }

    /* *** Aufgabe (4c) *** */
    private class IncreaseDifficultyThread extends PausableThread {
        @Override
        public void run() {
            while (!Thread.interrupted()) {
                try {
                    Thread.sleep(Settings.INCREASE_DIFFICULTY_DELAY); // Wartezeit für Schwierigkeitserhöhung
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

                GameState gameState = GameController.getInstance().getGameState();
                gameState.increaseLevel();

                if (currentDelay > 10) {
                    currentDelay--;
                }
                if (currentMaxAsteroids < 40) {
                    currentMaxAsteroids++;
                }

                if (gameState.getLevel() % 2 == 0) {
                    gameState.setLives(gameState.getLives() + 1);
                }
            }
        }
    }
}

