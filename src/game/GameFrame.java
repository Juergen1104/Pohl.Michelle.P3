package game;

import main.Settings;
import spaceObjects.Asteroid;
import spaceObjects.Missile;
import spaceObjects.Spaceship;
import spaceObjects.Ufo;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.util.ArrayList;

public class GameFrame extends JFrame {

    private GamePanel gamePanel;
    private static final long serialVersionUID = 12323L;
    private boolean disableBar = false;

    public GameFrame() {
        this.gamePanel = new GamePanel();
        this.setTitle("Asteroids");
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // removes top bar
        if (this.disableBar) {
            this.setExtendedState(Frame.MAXIMIZED_BOTH);
            this.setUndecorated(true);
        }

        this.add(gamePanel);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.setResizable(false);

        // set icon
        try {
            ImageIcon img = new ImageIcon(getClass().getResource(""));
            this.setIconImage((img.getImage()));
        } catch (Exception e) {
            System.out.println("Icon not found");
        }

        // sets focus inside frame
        this.requestFocus();

        addKeyListener(new CustomKeyListener());

    }

    public void update() {
        this.repaint();
    }

    private class GamePanel extends JPanel {

        private static final long serialVersionUID = 111111113L;
        private int enginePower;
        private int alphaStart;
        private int change;
        private String fontString = "Arial";

        GamePanel() {
            this.setPreferredSize(new Dimension(Settings.WIDTH, Settings.HEIGHT));
            this.setMinimumSize(new Dimension(Settings.WIDTH, Settings.HEIGHT));
            this.enginePower = 50;
            this.alphaStart = 255;
            this.change = -3;

        }

        @Override
        public void paint(Graphics g) {
            GameState gameState = GameController.getInstance().getGameState();
            synchronized (gameState) {

                Graphics2D g2d = (Graphics2D) g;
                // better rendering
                g2d.setStroke(new BasicStroke(1.6f));
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // get objects
                ArrayList<Missile> missiles = gameState.getMissiles();
                ArrayList<Missile> missilesUfo = gameState.getMissilesUfo();

                // Background
                g2d.setColor(Color.BLACK);
                g2d.fillRect(0, 0, Settings.WIDTH, Settings.HEIGHT);

                // paint Game Objects

                // Asteroids

                this.drawAsteroids(g2d);

                // Ship

                this.drawShip(g2d);

                // Missile
                for (Missile missile : missiles) {
                    g2d.fill(new Ellipse2D.Double(missile.getPositionX(), missile.getPositionY(), missile.getRadius(),
                            missile.getRadius()));
                }

                // Ufo

                this.drawUfo(g2d);

                // Ufo missiles

                g2d.setColor(Color.WHITE);
                for (Missile missile : missilesUfo) {
                    g2d.fill(new Ellipse2D.Double(missile.getPositionX(), missile.getPositionY(), missile.getRadius(),
                            missile.getRadius()));
                }

                // Ui Labels

                this.drawUi(g2d);

            }
        }

        private void drawUi(Graphics2D g2d) {
            GameState gameState = GameController.getInstance().getGameState();
            String name = "Asteroids";
            String score = Long.toString(gameState.getScore());
            String lives = Integer.toString(gameState.getLives());
            String level = "Level: " + gameState.getLevel();
            String start = "Press space";
            String move = "Move  ";
            String arrows = String.valueOf("\u2347") + String.valueOf("\u2350") + String.valueOf("\u2348");
            String shoot1 = "Shoot [space]";
            String pause = "Pause [esc]";
            String reset = "Restart [r]";
            g2d.setColor(Color.WHITE);

            g2d.setFont(new Font(fontString, Font.PLAIN, 28));

            // Score points
            g2d.drawString(score, 20, 30);
            g2d.drawString(level, 20, 60);

            // Lives
            g2d.drawString("Lives: " + lives, Settings.WIDTH - 100, 30);

            // Start Screen
            Font font1 = new Font(fontString, Font.ROMAN_BASELINE, 100);
            FontMetrics metricsStart1 = g2d.getFontMetrics(font1);

            if (!GameController.getInstance().isActive() || GameController.getInstance().isPaused()) {
                Font bindingsFont = new Font(fontString, Font.PLAIN, 28);
                FontMetrics bindingsMetric = g2d.getFontMetrics(bindingsFont);

                g2d.drawString(reset, 20, Settings.HEIGHT - 20);
                g2d.drawString(pause, 20, Settings.HEIGHT - 20 - bindingsMetric.getAscent());
                g2d.drawString(shoot1, 20, Settings.HEIGHT - 20 - 2 * bindingsMetric.getAscent());
                g2d.drawString(move, 20, Settings.HEIGHT - 20 - 3 * bindingsMetric.getAscent());

                g2d.setFont(new Font("TimesRoman", Font.ROMAN_BASELINE, 28));

                g2d.drawString(arrows, 20 + bindingsMetric.stringWidth(move),
                        Settings.HEIGHT - 20 - 3 * bindingsMetric.getAscent());
                g2d.setFont(font1);
                if (GameController.getInstance().isPaused()) {
                    g2d.drawString("Pause", (Settings.WIDTH - metricsStart1.stringWidth("Pause")) / 2,
                            Settings.HEIGHT / 3);
                } else {
                    g2d.setColor(Color.WHITE);

                    g2d.setFont(font1);
                    g2d.drawString(name, (Settings.WIDTH - metricsStart1.stringWidth(name)) / 2, Settings.HEIGHT / 4);

                    alphaStart -= change;
                    if (alphaStart <= 20) {
                        alphaStart = 20;
                        change *= -1;
                    }
                    if (alphaStart >= 255) {
                        alphaStart = 255;
                        change *= -1;
                    }

                    g2d.setColor(new Color(255, 255, 255, alphaStart));
                    Font font2 = new Font(fontString, Font.ROMAN_BASELINE, 65);
                    FontMetrics metricsStart2 = g2d.getFontMetrics(font2);
                    g2d.setFont(font2);
                    g2d.drawString(start, (Settings.WIDTH - metricsStart2.stringWidth(start)) / 2,
                            (int) (Settings.HEIGHT * 0.6));
                }

            }

            // Game Over
            if (Integer.parseInt(lives) == 0) {
                g2d.setColor(Color.WHITE);
                Font f1 = new Font(fontString, Font.PLAIN, 70);
                Font f2 = new Font(fontString, Font.PLAIN, 50);
                FontMetrics metrics1 = g2d.getFontMetrics(f1);
                FontMetrics metrics2 = g2d.getFontMetrics(f2);
                String s1 = "Game Over";
                String s2 = "Score: " + score;

                g2d.setFont(f1);
                g2d.drawString(s1, (Settings.WIDTH - metrics1.stringWidth(s1)) / 2, Settings.HEIGHT / 2);
                g2d.setFont(f2);
                g2d.drawString(s2, (Settings.WIDTH - metrics2.stringWidth(s2)) / 2,
                        Settings.HEIGHT / 2 + metrics1.getAscent());
                Font bindingsFont = new Font(fontString, Font.PLAIN, 28);
                FontMetrics bindingsMetric = g2d.getFontMetrics(bindingsFont);
                g2d.setFont(bindingsFont);
                g2d.drawString(reset, 20, Settings.HEIGHT - 20 - bindingsMetric.getAscent());
            }

        }

        private void drawShip(Graphics2D g2d) {
            GameState gameState = GameController.getInstance().getGameState();
            Spaceship ship = GameController.getInstance().getGameState().getSpaceShip();
            // inner line
            if (GameController.getInstance().isActive()) {
                double distance = 0.8;

                // point on the line between x0, y0 and x1, y1
                double px1 = (1 - distance) * ship.getShape().get(0).x + distance * ship.getShape().get(1).x;
                double py1 = (1 - distance) * ship.getShape().get(0).y + distance * ship.getShape().get(1).y;

                // point on the line between x0, y0 and x2, y2
                double px2 = (1 - distance) * ship.getShape().get(0).x + distance * ship.getShape().get(2).x;
                double py2 = (1 - distance) * ship.getShape().get(0).y + distance * ship.getShape().get(2).y;
                g2d.setColor(new Color(255, 255, 255, gameState.getShipAlphaValue()));
                g2d.draw(new Line2D.Double(ship.getShape().get(0).x, ship.getShape().get(0).y, ship.getShape().get(1).x,
                        ship.getShape().get(1).y));
                g2d.draw(new Line2D.Double(ship.getShape().get(0).x, ship.getShape().get(0).y, ship.getShape().get(2).x,
                        ship.getShape().get(2).y));
                g2d.draw(new Line2D.Double(px1, py1, px2, py2));

                // triangle when accelerating

                if (gameState.isAccelerating()) {
                    if (enginePower >= 0) {
                        distance = 0.25;
                        double px3 = (1 - distance) * px1 + distance * px2;
                        double py3 = (1 - distance) * py1 + distance * py2;

                        distance = 1 - distance;
                        double px4 = (1 - distance) * px1 + distance * px2;
                        double py4 = (1 - distance) * py1 + distance * py2;

                        double px5 = ship.getPositionX() - Math.cos(ship.getRadiant()) * ship.getHeight() * 0.9;
                        double py5 = ship.getPositionY() - Math.sin(ship.getRadiant()) * ship.getHeight() * 0.9;

                        g2d.draw(new Line2D.Double(px3, py3, px5, py5));
                        g2d.draw(new Line2D.Double(px4, py4, px5, py5));

                    }
                    // uncomment to make triangle flicker
                    // enginePower--;
                    if (enginePower == -7) {
                        enginePower = 3;
                    }

                }
            }

        }

        private void drawAsteroids(Graphics2D g2d) {
            ArrayList<Asteroid> asteroids = GameController.getInstance().getGameState().getAsteroids();
            Path2D base;
            for (Asteroid asteroid : asteroids) {
                base = new Path2D.Double();
                base.append(new Line2D.Double(asteroid.getShape().get(asteroid.getShape().size() - 1).x,
                        asteroid.getShape().get(asteroid.getShape().size() - 1).y, asteroid.getShape().get(0).x,
                        asteroid.getShape().get(0).y), true);
                for (int i = 0; i < asteroid.getShape().size() - 1; i++) {
                    base.append(new Line2D.Double(asteroid.getShape().get(i).x, asteroid.getShape().get(i).y,
                            asteroid.getShape().get(i + 1).x, asteroid.getShape().get(i + 1).y), true);
                }
                g2d.setColor(Color.BLACK);
                g2d.fill(base);
                g2d.setColor(Color.WHITE);
                g2d.draw(base);

            }

        }

        private void drawUfo(Graphics2D g2d) {
            GameState gameState = GameController.getInstance().getGameState();
            g2d.setColor(Color.WHITE);
            if (gameState.ufoActive()) {
                Ufo ufo = gameState.getUfo();

                g2d.draw(new Arc2D.Double(ufo.getPositionX() - ufo.getWidth() / 2,
                        ufo.getPositionY() - ufo.getHeight() / 2, ufo.getWidth(), ufo.getHeight(), 0, 360,
                        Arc2D.CHORD));
                g2d.draw(new Arc2D.Double(ufo.getPositionX() - ufo.getWidth() / 2 + 12,
                        ufo.getPositionY() - ufo.getHeight() / 2 - 10, ufo.getWidth() / 2f, ufo.getHeight() + 10, 0,
                        180, Arc2D.OPEN));
            }
        }
    }

    /* *** Aufgabe (1b) *** */

    private class CustomKeyListener implements KeyListener {
        @Override
        public void keyTyped(KeyEvent e) {
            // Nicht relevant für die Spielsteuerung
        }

        @Override
        public void keyPressed(KeyEvent e) {
            int keyCode = e.getKeyCode();
            GameState gameState = GameController.getInstance().getGameState();

            // Beenden des Spiels mit Q
            if (keyCode == KeyEvent.VK_Q) {
                // Spiel beenden (falls gewünscht)
                System.exit(0);
            }

            if (!GameController.getInstance().isActive()) {
                // Startet ein neues Spiel, wenn das Spiel nicht aktiv ist und die Leertaste gedrückt wird
                if (keyCode == KeyEvent.VK_SPACE) {
                    GameController.getInstance().startNewGame();
                }
            } else {
                // Spiel ist aktiv
                if (gameState.getLives() > 0) {
                    // Pause umschalten bei "ESC" (nur wenn Leben vorhanden)
                    if (keyCode == KeyEvent.VK_ESCAPE) {
                        GameController.getInstance().togglePause();
                    }
                }

                // Neues Spiel starten mit "R"
                if (keyCode == KeyEvent.VK_R) {
                    GameController.getInstance().startNewGame();
                }

                // Raumschiff lenken, wenn das Spiel aktiv und nicht pausiert ist
                if (!GameController.getInstance().isPaused()) {
                    if (keyCode == KeyEvent.VK_LEFT) {
                        gameState.leftPressed();
                    } else if (keyCode == KeyEvent.VK_RIGHT) {
                        gameState.rightPressed();
                    } else if (keyCode == KeyEvent.VK_UP) {
                        gameState.upPressed();
                    } else if (keyCode == KeyEvent.VK_SPACE) {
                        gameState.spacePressed();
                    }
                }
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            int keyCode = e.getKeyCode();
            GameState gameState = GameController.getInstance().getGameState();

            // Zurücksetzen der booleschen Werte für die Raumschiffsteuerung
            if (keyCode == KeyEvent.VK_LEFT) {
                gameState.leftReleased();
            } else if (keyCode == KeyEvent.VK_RIGHT) {
                gameState.rightReleased();
            } else if (keyCode == KeyEvent.VK_UP) {
                gameState.upReleased();
            } else if (keyCode == KeyEvent.VK_SPACE) {
                gameState.spaceReleased();
            }
        }
    }
}
