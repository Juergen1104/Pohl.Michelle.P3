package spaceObjects;

import main.Settings;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Asteroid extends SpaceObject {
    private AsteroidSize size;

    public enum AsteroidSize {

        TINY(20), SMALL(40), NORMAL(70), LARGE(90);

        public int value;

        private AsteroidSize(int value) {
            this.value = value;
        }
    }

    /* *** Aufgabe (3a) *** */
    public Asteroid() {
        this.size = AsteroidSize.values()[(int) (Math.random() * AsteroidSize.values().length)];

        double x, y;
        switch (ThreadLocalRandom.current().nextInt(4)) {
            case 0: // links außerhalb
                x = -50;
                y = ThreadLocalRandom.current().nextInt(50, Settings.HEIGHT - 50);
                break;
            case 1: // rechts außerhalb
                x = Settings.WIDTH + 50;
                y = ThreadLocalRandom.current().nextInt(50, Settings.HEIGHT - 50);
                break;
            case 2: // oben außerhalb
                x = ThreadLocalRandom.current().nextInt(50, Settings.WIDTH - 50);
                y = -50;
                break;
            case 3: // unten außerhalb
                x = ThreadLocalRandom.current().nextInt(50, Settings.WIDTH - 50);
                y = Settings.HEIGHT + 50;
                break;
            default:
                x = 0;
                y = 0;
                break;
        }
        this.x = (float) x;
        this.y = (float) y;

        // Zufälliger Radiant für Flugrichtung
        double radiant = Math.random() * Math.PI; // zufälliger Wert zwischen 0 und Math.PI

        // Zufällige Geschwindigkeit zwischen 2 und 4
        double speed = ThreadLocalRandom.current().nextDouble(2, 4);
        this.vx = speed * Math.cos(radiant);
        this.vy = speed * Math.sin(radiant);

        // Generiere die zufällige Form des Asteroiden
        generateRandomShape();

    }

    // only for splitting asteroids
    private Asteroid(float x, float y, double vx, double vy, AsteroidSize size) {
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.size = size;
        this.width = size.value;
        this.height = size.value;
        this.generateRandomShape();

    }

    public AsteroidSize getSize() {
        return this.size;
    }

    public int getPoints() {
        switch (this.size) {
            case LARGE:
                return 100;
            case NORMAL:
                return 300;
            case SMALL:
                return 500;
            case TINY:
                return 1000;
            default:
                return 100;

        }
    }

    /* *** Aufgabe (2b) *** */
    public void move() {
        this.x = (float) +this.vx;
        this.y = (float) +this.vy;

        //this.shape.setsetShape();
    }

    /* *** Aufgabe (3c) *** */

    public List<Asteroid> splitAsteroid() {

        List<Asteroid> newAsteroids = new ArrayList<>();
        if (size == AsteroidSize.TINY) {
            return newAsteroids;
        }
        if (size == AsteroidSize.SMALL) {
            newAsteroids.add(createNewAsteroid(AsteroidSize.TINY));
            newAsteroids.add(createNewAsteroid(AsteroidSize.TINY));
        } else if (size == AsteroidSize.NORMAL) {
            newAsteroids.add(createNewAsteroid(AsteroidSize.SMALL));
            newAsteroids.add(createNewAsteroid(AsteroidSize.TINY));
        } else if (size == AsteroidSize.LARGE) {
            newAsteroids.add(createNewAsteroid(AsteroidSize.NORMAL));
            newAsteroids.add(createNewAsteroid(AsteroidSize.SMALL));
            newAsteroids.add(createNewAsteroid(AsteroidSize.TINY));
        }

        return newAsteroids;
    }

    private Asteroid createNewAsteroid(AsteroidSize newSize) {
        // Calculate new velocity vector with adjusted direction
        double angle = Math.PI / 8; // 22.5 degrees in radians

        double newVxUp = vx * Math.cos(angle) - vy * Math.sin(angle);
        double newVyUp = vx * Math.sin(angle) + vy * Math.cos(angle);

        double newVxDown = vx * Math.cos(-angle) - vy * Math.sin(-angle);
        double newVyDown = vx * Math.sin(-angle) + vy * Math.cos(-angle);

        // Create new Asteroid with adjusted velocity and position
        if (newSize == AsteroidSize.TINY) {
            return new Asteroid(x + 10, y + 10, newVxUp, newVyUp, newSize);
        } else {
            return new Asteroid(x - 10, y - 10, newVxDown, newVyDown, newSize);
        }
    }

    // generates shape based on size
    private void generateRandomShape() {
        int numberOfPoints;
        switch (this.size) {
            case LARGE:
                numberOfPoints = 18;
                break;
            case NORMAL:
                numberOfPoints = 16;
                break;
            case SMALL:
                numberOfPoints = 15;
                break;
            case TINY:
                numberOfPoints = 12;
                break;
            default:
                numberOfPoints = 15;
                break;
        }
        this.shape = this.generateRandomConvexPolygon(numberOfPoints);
        for (int i = 0; i < numberOfPoints; i++) {
            this.shape.get(i).x += this.x;
            this.shape.get(i).y += this.y;
        }

    }

    // https://cglab.ca/~sander/misc/ConvexGeneration/convex.html
    // creates convex polygon with n edges
    private List<Point2D.Double> generateRandomConvexPolygon(int n) {
        List<Double> xValues = new ArrayList<>(n);
        List<Double> yValues = new ArrayList<>(n);

        for (int i = 0; i < n; i++) {
            xValues.add(ThreadLocalRandom.current().nextDouble(-this.size.value, this.size.value));
            yValues.add(ThreadLocalRandom.current().nextDouble(-this.size.value, this.size.value));
        }

        Collections.sort(xValues);
        Collections.sort(yValues);

        Double minX = xValues.get(0);
        Double minY = yValues.get(0);

        Double maxX = xValues.get(n - 1);
        Double maxY = yValues.get(n - 1);

        List<Double> xVec = new ArrayList<>(n);
        List<Double> yVec = new ArrayList<>(n);

        double lastTop = minX;
        double lastBot = minX;

        for (int i = 1; i < n - 1; i++) {
            double x = xValues.get(i);

            if (ThreadLocalRandom.current().nextBoolean()) {
                xVec.add(x - lastTop);
                lastTop = x;
            } else {
                xVec.add(lastBot - x);
                lastBot = x;
            }
        }

        xVec.add(maxX - lastTop);
        xVec.add(lastBot - maxX);

        double lastLeft = minY;
        double lastRight = minY;

        for (int i = 1; i < n - 1; i++) {
            double y = yValues.get(i);

            if (ThreadLocalRandom.current().nextBoolean()) {
                yVec.add(y - lastLeft);
                lastLeft = y;
            } else {
                yVec.add(lastRight - y);
                lastRight = y;
            }
        }

        yVec.add(maxY - lastLeft);
        yVec.add(lastRight - maxY);

        Collections.shuffle(yVec);

        List<Point2D.Double> vec = new ArrayList<>(n);

        for (int i = 0; i < n; i++) {
            vec.add(new Point2D.Double(xVec.get(i), yVec.get(i)));
        }

        Collections.sort(vec, Comparator.comparingDouble(v -> Math.atan2(v.getY(), v.getX())));

        double x = 0;
        double y = 0;

        double minPolygonX = 0;
        double minPolygonY = 0;

        List<Point2D.Double> points = new ArrayList<>(n);

        for (int i = 0; i < n; i++) {
            points.add(new Point2D.Double(x, y));

            x += vec.get(i).getX();
            y += vec.get(i).getY();

            minPolygonX = Math.min(minPolygonX, x);
            minPolygonY = Math.min(minPolygonY, y);
        }

        double xShift = minX - minPolygonX;
        double yShift = minY - minPolygonY;

        for (int i = 0; i < n; i++) {
            Point2D.Double p = points.get(i);
            points.set(i, new Point2D.Double(p.x + xShift, p.y + yShift));
        }

        return points;
    }

}
