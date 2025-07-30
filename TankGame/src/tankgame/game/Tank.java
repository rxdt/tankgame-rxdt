package tankgame.game;

import tankgame.GameConstants;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.EnumSet;

public class Tank {
    private static final float TANK_DIST_FROM_EDGE_X = 85;
    private static final float TANK_DIST_FROM_EDGE_Y = 75;
    
    public enum Direction {UP, DOWN, LEFT, RIGHT, SHOOT}
    private final EnumSet<Direction> keysPressed = EnumSet.noneOf(Direction.class);

    private float x;
    private float y;
    private float vx; // useful for simple approach to collisions, velocity x distance traveled over time
    private float vy; // useful for simple approach to collisions, velocity y

    private float angle; // direction tank is facing - make sure all assets facing same way
    private float R = 5; // speed
    private float ROTATIONSPEED = 3.0f;

    private BufferedImage img; // don't want it static atm because need two tanks that look different


    Tank(float x, float y, float vx, float vy, float angle, BufferedImage img) {
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.img = img;
        this.angle = angle;
    }

    void setX(float x){ this.x = x; }

    void setY(float y) { this. y = y;}

   public void pressed(Direction dir) {
        keysPressed.add(dir);
    }

    public void released(Direction dir) {
        keysPressed.remove(dir);
    }

    void update() {
       if (this.keysPressed.contains(Direction.UP)) { // W
            this.moveForwards();
        }
        if (this.keysPressed.contains(Direction.DOWN)) { // S
            this.moveBackwards();
        }
        if (this.keysPressed.contains(Direction.LEFT)) { // A
            this.rotateLeft();
        }
        if (this.keysPressed.contains(Direction.RIGHT)) { // D
            this.rotateRight();
        }
    }

    private void rotateLeft() {
        this.angle -= this.ROTATIONSPEED;
    }

    private void rotateRight() {
        this.angle += this.ROTATIONSPEED;
    }

    private void moveBackwards() {
        vx =  Math.round(R * Math.cos(Math.toRadians(angle)));
        vy =  Math.round(R * Math.sin(Math.toRadians(angle)));
        x -= vx;
        y -= vy;
       checkBorder();
    }

    private void moveForwards() {
        vx = Math.round(R * Math.cos(Math.toRadians(angle)));
        vy = Math.round(R * Math.sin(Math.toRadians(angle)));
        x += vx;
        y += vy;
        checkBorder();
    }

    // should be GAME_WORLD_HEIGHT, don't go off the world especially during split screen
    private void checkBorder() {
        int tankWidth = this.img.getWidth();
        int tankHeight = this.img.getHeight();
        if (x < 0) {
            x = 0;
        }
        if (x > GameConstants.GAME_SCREEN_WIDTH - tankWidth) {
            x = GameConstants.GAME_SCREEN_WIDTH - tankWidth;
        }
        if (y < 0) {
            y = 0;
        }
        if (y > GameConstants.GAME_SCREEN_HEIGHT - tankHeight) {
            y = GameConstants.GAME_SCREEN_HEIGHT - tankHeight;
        }
    }

    public void drawImage(Graphics2D g) {
        AffineTransform at = new AffineTransform();
        at.translate(this.x, this.y);
        at.rotate(Math.toRadians(this.angle), this.img.getWidth() / 2.0, this.img.getHeight() / 2.0);
        g.drawImage(this.img, at, null);
    }

    public int getX() {
        return (int) this.x;
    }

    public int getY() {
        return (int) this.y;
    }
}
