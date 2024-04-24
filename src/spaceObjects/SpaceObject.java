package spaceObjects;

import java.awt.geom.Point2D;
import java.util.List;

/**
 * Super class for all Objects
 */
public abstract class SpaceObject {


	//coordinates
	protected float x;
	protected float y;

	//speed vector
	protected double vx;
	protected double vy;

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

	//checkBounds TODO


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
