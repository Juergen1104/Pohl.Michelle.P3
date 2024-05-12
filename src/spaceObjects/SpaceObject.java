package spaceObjects;

import main.Settings;

import java.awt.geom.Point2D;
import java.util.List;

/**
 * Super class for all Objects
 */
public abstract class SpaceObject {


	//coordinates
	protected float x;

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public double getVx() {
		return vx;
	}

	public double getVy() {
		return vy;
	}

	protected float y;

	public void setX(float x) {
		this.x = x;
	}

	public void setY(float y) {
		this.y = y;
	}

	public void setVx(double vx) {
		this.vx = vx;
	}

	public void setVy(double vy) {
		this.vy = vy;
	}

	//speed vector
	protected double vx;
	protected double vy;

	public void setRadiant(double radiant) {
		this.radiant = radiant;
	}

	//angle
	protected double radiant;

	//size
	protected int width;
	protected int height;

	protected List<Point2D.Double> shape;

	public float getPositionX() {
		return x;
	}

	public float getPositionY() {
		return y;
	}

	public double getVelocityX() {
		return vx;
	}

	public double getVelocityY() {
		return vy;
	}

	public double getRadiant() {
		return radiant;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public List<Point2D.Double> getShape() {
		return this.shape;
	}
	
	// moves the object in direction of (vx, vy)
	public abstract void move();

	
	/* *** Aufgabe (2a) *** */
	public void checkBoundaries() {
		if (getPositionX() < 0) {
			setX(Settings.WIDTH); // Bewegen Sie das Raumschiff zur rechten Seite
		} else if (getPositionX() > Settings.WIDTH) {
			setX(0); // Bewegen Sie das Raumschiff zur linken Seite
		}

		if (getPositionY() < 0) {
			setY(Settings.HEIGHT); // Bewegen Sie das Raumschiff nach unten
		} else if (getPositionY() > Settings.HEIGHT) {
			setY(0); // Bewegen Sie das Raumschiff nach oben
		}
	}


	//for SAT calculations
	public double[] getPoint(int index) {
		return new double[]{this.shape.get(index).x, this.shape.get(index).y};
	}

	//for SAT calculations
	public double[] getEdge(int edgeNumber) {
		if (this.shape.size() <= edgeNumber) {
			System.out.println("ERROR");
			return null;
		}
		double[] edge = new double[2];
		if (this.shape.size() - 1 == edgeNumber) {

			edge[0] = this.shape.get(edgeNumber - 1).x - this.shape.get(0).x;
			edge[1] = this.shape.get(edgeNumber - 1).x - this.shape.get(0).y;
		} else {
			edge[0] = this.shape.get(edgeNumber).x - this.shape.get(edgeNumber + 1).x;
			edge[1] = this.shape.get(edgeNumber).y - this.shape.get(edgeNumber + 1).y;
		}
		return edge;
	}
}
