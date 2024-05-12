package spaceObjects;

import main.Settings;

import java.awt.geom.Point2D;
import java.util.ArrayList;

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
        setRadiant((getRadiant() - Settings.SHIP_ROTATIONSPEED));
    }

    public void rotateRight() {
        //	TODO
        setRadiant((getRadiant() + Settings.SHIP_ROTATIONSPEED));
    }

    public void accelerate() {
        //	TODO
        double radians = Math.toRadians(getRadiant());
        double accelX = Settings.SHIP_ACCELERATION * Math.cos(radians);
        double accelY = Settings.SHIP_ACCELERATION * Math.sin(radians);

        setVx(getVelocityX() + accelX);
        setVy(getVelocityY() + accelY);

        // Begrenze die Geschwindigkeit auf die maximale Geschwindigkeit
        double currentSpeed = Math.sqrt(getVelocityX() * getVelocityX() + getVelocityY() * getVelocityY());
        if (currentSpeed > Settings.SHIP_MAXSPEED) {
            double ratio = Settings.SHIP_MAXSPEED / currentSpeed;
            setVx(getVx() * ratio);
            setVy(getVy() * ratio);
        }
    }

    public void move() {
        double vectorLength = Math.sqrt(getVelocityX() * getVelocityX() + getVelocityY() * getVelocityY());

        if (vectorLength > Settings.SHIP_DECELERATION) {
            double ratio = (vectorLength - Settings.SHIP_DECELERATION) / vectorLength;
            setVx(getVelocityX() * ratio);
            setVy(getVelocityY() * ratio);
        }

        if (vectorLength > 0) {
            double deceleration = Settings.SHIP_DECELERATION;
            if (vectorLength < deceleration) {
                setVx(0);
                setVy(0);
            } else {
                double ratio = (vectorLength - deceleration) / vectorLength;
                setVx(getVelocityX() * ratio);
                setVy(getVelocityY() * ratio);
            }
        }

        // Begrenzung der Position innerhalb der Spielfläche
        checkBoundaries();

        // Verschieben der Position basierend auf der Geschwindigkeit
        setX((float) (getPositionX() + getVelocityX()));
        setY((float) (getPositionY() + getVelocityY()));
        setShape();
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
