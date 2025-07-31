package tankgame.game;

import java.awt.image.BufferedImage;

public class HealthBoost extends PowerUp {
    private int healAmount = 30;

    public HealthBoost(int x, int y, BufferedImage img) {
        super(x, y, img);
    }

    @Override
    public void applyTo(Tank tank) {
        tank.heal(healAmount);
    }
}
