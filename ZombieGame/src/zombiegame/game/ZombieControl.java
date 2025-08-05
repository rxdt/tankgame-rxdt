package zombiegame.game;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class ZombieControl implements KeyListener {
    private final Zombie zombie;
    private int up;
    private int down;
    private int right;
    private int left;
    private final int fireKey;
    private final GameWorld gameWorld;

    public ZombieControl(Zombie zombie, int up, int down, int left, int right, int fireKey, GameWorld gameWorld) {
        this.zombie = zombie;
        this.up = up;
        this.down = down;
        this.left = left;
        this.right = right;
        this.fireKey = fireKey;
        this.gameWorld = gameWorld;
    }

    @Override
    public void keyTyped(KeyEvent ke) {}

    @Override
    public void keyPressed(KeyEvent ke) {
        if (this.gameWorld.gameIsOver()) return;
        int keyPressed = ke.getKeyCode();
        if (keyPressed == up)
            this.zombie.pressed(Zombie.Direction.UP);
        if (keyPressed == down)
            this.zombie.pressed(Zombie.Direction.DOWN);
        if (keyPressed == left)
            this.zombie.pressed(Zombie.Direction.LEFT);
        if (keyPressed == right)
            this.zombie.pressed(Zombie.Direction.RIGHT);
        else if (keyPressed == fireKey)
            this.zombie.fire();
    }

    @Override
    public void keyReleased(KeyEvent ke) {
        if (this.gameWorld.gameIsOver()) return;
        int keyReleased = ke.getKeyCode();
        if (keyReleased == up)
            this.zombie.released(Zombie.Direction.UP);
        if (keyReleased == down)
            this.zombie.released(Zombie.Direction.DOWN);
        if (keyReleased == left)
            this.zombie.released(Zombie.Direction.LEFT);
        if (keyReleased == right)
            this.zombie.released(Zombie.Direction.RIGHT);
    }
}
