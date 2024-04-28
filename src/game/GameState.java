	package game;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import main.Settings;
import spaceObjects.Asteroid;
import spaceObjects.Asteroid.AsteroidSize;
import spaceObjects.Missile;
import spaceObjects.SpaceObject;
import spaceObjects.Spaceship;
import spaceObjects.Ufo;

public class GameState {

	private long score;
	private int lives;
	private ArrayList<Asteroid> asteroids;
	private Spaceship spaceShip;
	private ArrayList<Missile> missiles;
	private ArrayList<Missile> missilesUfo;
	private Ufo ufo;

	//keybindings
	private boolean leftPressed;
	private boolean rightPressed;
	private boolean upPressed;
	private boolean spacePressed;
	private boolean spaceWasReleased;
	
	
	private long lastMissileShot;
	private boolean shipInvincible;
	private int shipAlpha;
	private int level;

	public GameState() {
		this.asteroids = new ArrayList<>();
		this.missiles = new ArrayList<>();
		this.missilesUfo = new ArrayList<>();
		this.spaceShip = new Spaceship();
		this.score = 0;
		this.lives = 3;
		this.level = 1;

		this.lastMissileShot = 0;
		shipAlpha = 255;
		this.spaceWasReleased = true;
	}

	public ArrayList<Asteroid> getAsteroids() {
		return asteroids;
	}

	public Spaceship getSpaceShip() {
		return spaceShip;
	}

	public Ufo getUfo() {
		return ufo;
	}

	public long getScore() {
		return this.score;
	}

	public int getShipAlphaValue() {
		return this.shipAlpha;
	}

	public int getNumberOfAsteroids() {
		return this.asteroids.size();
	}

	public int getLives() {
		return this.lives;
	}

	public boolean isAccelerating() {
		return this.upPressed;
	}

	public ArrayList<Missile> getMissiles() {
		return this.missiles;
	}

	public ArrayList<Missile> getMissilesUfo() {
		return this.missilesUfo;
	}

	public void leftPressed() {
		this.leftPressed = true;
	}

	public void rightPressed() {
		this.rightPressed = true;
	}

	public void upPressed() {
		this.upPressed = true;
	}

	public void leftReleased() {
		this.leftPressed = false;
	}

	public void rightReleased() {
		this.rightPressed = false;
	}

	public void upReleased() {
		this.upPressed = false;
	}

	// CAREFUL 2 different booleans
	// space needs to be released before firing another missile
	public void spacePressed() {
		this.spacePressed = true;
	}

	public void spaceReleased() {
		this.spacePressed = false;
		spaceWasReleased = true;

	}

	public int getLevel() {
		return this.level;
	}

	public void addScore(int score) {
		this.score += score;
	}

	public void increaseLevel() {
		this.level++;
	}

	public void addLife() {
		this.lives++;

	}

	public void loseLife() {
		this.lives--;
	}

	public boolean ufoActive() {
		return this.ufo != null;
	}

	public synchronized void setInvincible() {
		this.shipInvincible = true;

	}

	public synchronized void setVulnerable() {
		this.shipInvincible = false;

	}

	public void setShipAlphaValue(int alpha) {
		this.shipAlpha = alpha;
	}

	// ---Spawn objects---

	public synchronized void spawnUfo() {
		this.ufo = new Ufo();
	}

	public synchronized void spawnAsteroid() {
		this.asteroids.add(new Asteroid());

	}

	public synchronized void shootMissile() {
		if (System.currentTimeMillis() - this.lastMissileShot > Settings.MISSILE_RELOADSPEED && this.spaceWasReleased) {
			this.spaceWasReleased = false;
			this.missiles.add(new Missile(this.spaceShip.getShape().get(0).x, this.spaceShip.getShape().get(0).y,
					this.spaceShip.getRadiant()));
			this.lastMissileShot = System.currentTimeMillis();
		}

	}

	public synchronized void shootMissileUfo() {

		double p1 = this.ufo.getPositionY() - this.spaceShip.getPositionY();
		double p2 = this.ufo.getPositionX() - this.spaceShip.getPositionX();

		// correct angle to hit ship
		double angle = Math.atan2(p1, p2);

		// random cone from 0.8 to 1.2 
		angle += ThreadLocalRandom.current().nextDouble(0.8 * Math.PI, 1.2 * Math.PI);
		this.missilesUfo.add(new Missile(this.ufo.getPositionX(), this.ufo.getPositionY(), angle));
	}

	// ---Move objects---

	/* *** Aufgabe (2b) *** */

	//	TODO
	public  void moveObjects() {
	}
	
	// move ship 
	public void moveShip() {
	}
	

	// move asteroids
	private void moveAsteroids() {
	}
	
	// move missiles
	public void moveMissiles() {
	}
	
	
	
	private void moveUfo() {
		this.ufo.move();
	}

	/* *** Aufgabe (4b) *** */
	
	private void shipGotHit() {
		//		TODO
	}

	/// ---Collision detection and helper methods---

	public void checkCollisions() {
		synchronized (this) {

			// Collision between asteroids and missiles
			ArrayList<Asteroid> hitAsteroids = new ArrayList<>();
			ArrayList<Missile> hitMissiles = new ArrayList<>();
			ArrayList<Missile> allMissiles = new ArrayList<>();
			allMissiles.addAll(this.missiles);
			allMissiles.addAll(this.missilesUfo);
			for (Asteroid asteroid : this.asteroids) {
				for (Missile missile : allMissiles) {
					if (this.polygonCollision(asteroid, missile)) {
						hitAsteroids.add(asteroid);
						if (this.missiles.contains(missile)) {
							this.addScore(asteroid.getPoints());
						}
						hitMissiles.add(missile);
					}
				}
			}

			this.asteroids.removeAll(hitAsteroids);
			this.missiles.removeAll(hitMissiles);
			this.missilesUfo.removeAll(hitMissiles);
			for (Asteroid asteroid : hitAsteroids) {
				if (asteroid.getSize().value >= AsteroidSize.SMALL.value) {
					//this.asteroids.addAll(asteroid.splitAsteroid()); //uncomment TODO
				}
			}
			
			// Collision between asteroids and ufo
			if (this.ufoActive() && this.ufo.getPositionX() > 0 && this.ufo.getPositionX() < Settings.WIDTH) {
				
				for (Asteroid asteroid : this.asteroids) {
						if (this.polygonCollision(asteroid, this.ufo)) {
							this.asteroids.remove(asteroid);
							this.ufo = null;
							if (asteroid.getSize().value >= AsteroidSize.SMALL.value) {
							//this.asteroids.addAll(asteroid.splitAsteroid()); //uncomment TODO
							}
							return;
					}
				}

			}
			if (!GameController.getInstance().isActive()) {
				return;
			}

			// Collision between asteroids and ship
			for (Asteroid asteroid : this.asteroids) {
				if (this.polygonCollision(asteroid, this.spaceShip)) {
					if (!this.shipInvincible) {
						this.shipGotHit();
						if (this.lives > 0) {
							this.asteroids.remove(asteroid);
							if (asteroid.getSize().value >= AsteroidSize.SMALL.value) {
								//this.asteroids.addAll(asteroid.splitAsteroid()); //uncomment TODO
							}
						}

						break;
					}
				}
			}

			// Collision between ufoMissiles and ship

			for (Missile missile : this.missilesUfo) {
				if (this.polygonCollision(missile, this.spaceShip)) {
					if (!this.shipInvincible) {
						this.shipGotHit();
						if (this.lives > 1) {
							this.missilesUfo.remove(missile);
						}
						break;
					}
				}
			}

			if (!this.ufoActive()) {
				return;
			}
			
			// Collision between ufo and ship

			if (this.polygonCollision(ufo, spaceShip)) {
				if (!this.shipInvincible) {
					this.shipGotHit();
					if(this.lives>1) {
						this.ufo = null;
					}
					return;
				}
			}
			// Collision between missiles and ufo

			for (Missile missile : this.missiles) {
				if (this.polygonCollision(missile, this.ufo)) {
					this.addScore(ufo.getPoints());
					this.ufo = null;
					this.missiles.remove(missile);
					return;
				}
			}

		}
	}

	// Seperating Axis Theorem
	private boolean polygonCollision(SpaceObject o1, SpaceObject o2) {

		int numberOfEdges1 = o1.getShape().size();
		int numberOfEdges2 = o2.getShape().size();
		double[] edge;

		// Edge loop
		for (int i = 0; i < numberOfEdges1 + numberOfEdges2; i++) {
			if (i < numberOfEdges1) {
				edge = o1.getEdge(i);
			} else {
				edge = o2.getEdge(i - numberOfEdges1);
			}
			double[] axis = new double[2];
			axis[0] = -1 * edge[1];
			axis[1] = edge[0];
			// normalize
			axis = this.normalize(axis);
			// project current axis
			double[] minMaxA = projectPolygon(axis, o1);
			double[] minMaxB = projectPolygon(axis, o2);
			if (intervalDistance(minMaxA[0], minMaxB[0], minMaxA[1], minMaxB[1]) > 0) {
				return false;
			}
		}

		return true;

	}

	private double[] projectPolygon(double[] axis, SpaceObject o) {

		double[] result = new double[2];
		double dotProduct = this.dotProduct(axis, o.getPoint(0));
		double min = dotProduct;
		double max = dotProduct;
		for (int i = 0; i < o.getShape().size(); i++) {
			dotProduct = this.dotProduct(axis, o.getPoint(i));
			if (dotProduct < min) {
				min = dotProduct;
			} else {
				if (dotProduct > max) {
					max = dotProduct;
				}
			}
		}
		result[0] = min;
		result[1] = max;

		return result;
	}

	private double[] normalize(double[] vector) {
		double length = 0;
		double[] r = new double[vector.length];
		for (double element : vector) {
			length += element * element;
		}
		length = Math.sqrt(length);
		for (int i = 0; i < vector.length; i++) {
			r[i] = vector[i] / length;
		}
		return r;

	}

	private double dotProduct(double[] axis, double[] point) {
		if (axis.length != point.length) {
			return -1;
		}
		double result = 0;
		for (int i = 0; i < axis.length; i++) {
			result += axis[i] * point[i];
		}
		return result;
	}

	private double intervalDistance(double minA, double minB, double maxA, double maxB) {
		if (minA < minB) {
			return minB - maxA;
		} else {
			return minA - maxB;
		}
	}

}
