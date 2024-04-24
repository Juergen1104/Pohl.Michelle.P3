package spaceObjects;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import main.Settings;

public class Spaceship extends SpaceObject {

	public Spaceship() {

		// middle of screen
		this.x = Settings.WIDTH / 2;
		this.y = Settings.HEIGHT / 2;
		this.height = 25;
		this.width = this.height;
		this.radiant = 1.5 * Math.PI;

		// shape
		this.setShape();

	}

	/* *** Aufgabe (2a) *** */
	
	public void rotateLeft() {
		//	TODO
	}
	
	public void rotateRight() {
		//	TODO
	}
	
	public void accelerate() {
		//	TODO
	}

	public void move() {
		//	TODO
	}
	
	
	//points to draw spaceship
	private void setShape() {
		this.shape = new ArrayList<Point2D.Double>();
		shape.add(new Point2D.Double(this.getPositionX() + Math.cos(this.getRadiant()) * this.height,
				this.getPositionY() + Math.sin(this.getRadiant()) * this.height));
		shape.add(new Point2D.Double(this.getPositionX() + Math.cos(this.getRadiant() - Math.PI * 0.8) * this.height,
				this.getPositionY() + Math.sin(this.getRadiant() - Math.PI * 0.8) * this.height));
		shape.add(new Point2D.Double(this.getPositionX() + Math.cos(this.getRadiant() + Math.PI * 0.8) * this.height,
				this.getPositionY() + Math.sin(this.getRadiant() + Math.PI * 0.8) * this.height));
	}

	//resets spaceship to the middle of the screen
	public void resetPosition() {
		this.x = Settings.WIDTH / 2f;
		this.y = Settings.HEIGHT / 2f;
		this.vx = 0;
		this.vy = 0;
		this.radiant = 1.5 * Math.PI;

	}

}
