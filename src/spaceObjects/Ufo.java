package spaceObjects;

import main.Settings;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Ufo extends SpaceObject {
	private int points;

	public Ufo() {
		this.height = 10;
		this.width = 50;
		this.points = 5000;
		this.setRandomPosition();
		this.radiant = Math.PI;
		this.vx = Math.cos(radiant) * 2;
		this.vy = 0;
		this.shape = getBoundingBox();
	}
	
	public int getPoints() {
		return this.points;
	}
	
	public List<Point2D.Double> getBoundingBox() {
		ArrayList<Point2D.Double> boundingBox = new ArrayList<Point2D.Double>();
		boundingBox
				.add(new Point2D.Double(this.getPositionX() - this.width / 2, this.getPositionY() + 5 - this.height));
		boundingBox.add(
				new Point2D.Double(this.getPositionX() - this.width / 2 + 15, this.getPositionY() - 5 - this.height));
		boundingBox.add(
				new Point2D.Double(this.getPositionX() + this.width / 2 - 15, this.getPositionY() - 5 - this.height));
		boundingBox
				.add(new Point2D.Double(this.getPositionX() + this.width / 2, this.getPositionY() + 5 - this.height));
		boundingBox
				.add(new Point2D.Double(this.getPositionX() + this.width / 2, this.getPositionY() - 5 + this.height));
		boundingBox
				.add(new Point2D.Double(this.getPositionX() - this.width / 2, this.getPositionY() - 5 + this.height));
		return boundingBox;
	}

	private void setRandomPosition() {
		switch (ThreadLocalRandom.current().nextInt(4)) {
		case 0:
			this.x = Settings.WIDTH + 50;
			this.y = Settings.HEIGHT / 3.0f;
			break;
		case 1:
			this.x = 0 - 50;
			this.y = Settings.HEIGHT / 3.0f;
			break;
		case 2:
			this.x = Settings.WIDTH + 50;
			this.y = Settings.HEIGHT * 2 / 3.0f;
			break;
		case 3:
			this.x = 0 - 50;
			this.y = Settings.HEIGHT * 2 / 3.0f;
			break;
		default:
			this.x = 0;
			this.y = 0;
		}
	}

	public synchronized void move() {
		this.x += this.vx;
		this.y += this.vy;

		if (this.x < 0 - 70 || this.x > Settings.WIDTH + 70) {
			this.vx *= -1;
		}
		if (this.y < 0 || this.y > Settings.HEIGHT) {
			this.setRandomPosition();
		}
		this.shape = this.getBoundingBox();
	}

	/* *** Aufgabe (4a) *** */

	public void changeVelocity() {
		// Mit 50% Wahrscheinlichkeit entweder vy auf 2 oder -2 setzen
		if (ThreadLocalRandom.current().nextBoolean()) {
			vy = 2;
		} else {
			vy = -2;
		}
	}
}
