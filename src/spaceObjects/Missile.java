package spaceObjects;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Missile extends SpaceObject {

	private double radius;

	// Ship Missile
	public Missile(double posX, double posY, double radiant) {
		this.radius = 3;
		this.x = (float) posX;
		this.y = (float) posY;
		this.vx = Math.cos(radiant) * 15;
		this.vy = Math.sin(radiant) * 15;
		this.setBoundingBox();

	}

	public double getRadius() {
		return this.radius;
	}
	
	/* *** Aufgabe (2b) *** */
	public void move() {
	//	TODO
	}

	//box around missile for sat unit collision detection
	public void setBoundingBox() {
		ArrayList<Point2D.Double> boundingBox = new ArrayList<Point2D.Double>();
		boundingBox.add(new Point2D.Double(this.getPositionX() - this.radius, this.getPositionY() - this.radius));
		boundingBox.add(new Point2D.Double(this.getPositionX() + this.radius, this.getPositionY() - this.radius));
		boundingBox.add(new Point2D.Double(this.getPositionX() + this.radius, this.getPositionY() + this.radius));
		boundingBox.add(new Point2D.Double(this.getPositionX() - this.radius, this.getPositionY() + this.radius));
		this.shape = boundingBox;
	}

}
