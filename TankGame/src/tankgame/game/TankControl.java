package tankgame.game;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class TankControl implements KeyListener {
    private final Tank tank;
    private int up;
    private int down;
    private int right;
    private int left;
    private final int fireKey;

    public TankControl(Tank tank, int up, int down, int left, int right, int fireKey) {
        this.tank = tank;
        this.up = up;
        this.down = down;
        this.left = left;
        this.right = right;
        this.fireKey = fireKey;
    }

    @Override
    public void keyTyped(KeyEvent ke) {

    }

    @Override
    public void keyPressed(KeyEvent ke) {
        int keyPressed = ke.getKeyCode();
        if (keyPressed == up)
            this.tank.pressed(Tank.Direction.UP);
        if (keyPressed == down)
            this.tank.pressed(Tank.Direction.DOWN);
        if (keyPressed == left)
            this.tank.pressed(Tank.Direction.LEFT);
        if (keyPressed == right)
            this.tank.pressed(Tank.Direction.RIGHT);
        else if (keyPressed == fireKey)
            this.tank.fire();
    }

    @Override
    public void keyReleased(KeyEvent ke) {
        int keyReleased = ke.getKeyCode();
        if (keyReleased == up)
            this.tank.released(Tank.Direction.UP);
        if (keyReleased == down)
            this.tank.released(Tank.Direction.DOWN);
        if (keyReleased == left)
            this.tank.released(Tank.Direction.LEFT);
        if (keyReleased == right)
            this.tank.released(Tank.Direction.RIGHT);
    }
}
