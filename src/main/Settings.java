package main;

public class Settings {

    public static final int WIDTH = 800;
    public static final int HEIGHT = 800;
    public static final int GUI_DELAY = 15; // in ms

    // initial value, gets changed in GameController
    public static final int GAME_DELAY = 25; // in ms
    public static final int MAX_ASTEROIDS = 10;

    // ship
    public static final double SHIP_ACCELERATION = 0.5;
    public static final int SHIP_MAXSPEED = 12;
    public static final double SHIP_DECELERATION = 0.15;
    public static final double SHIP_ROTATIONSPEED = 0.1; // radiant
    public static final long SHIP_INVINCIBLE_TIME = 3000; // in ms

    public static final int MISSILE_RELOADSPEED = 300;    // in ms
    public static final int UFO_SPAWNRATE = 15000; // in ms
    public static final int ASTEROIDS_SPAWNRATE = 600; // in ms

    public static final long INCREASE_DIFFICULTY_DELAY = 30000; // in ms


}
