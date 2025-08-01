package zombiegame.game;

import java.awt.*;

public abstract class GameObject {
    protected int x, y, width, height;

    public int getX() { return x; }
    public int getY() { return y; }
    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
}
