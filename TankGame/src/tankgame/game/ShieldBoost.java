package tankgame.game;

import java.awt.image.BufferedImage;

public class ShieldBoost extends PowerUp {
    public ShieldBoost(int x, int y, BufferedImage img) {
        super(x, y, img);
    }

    @Override
    public void applyTo(Tank zombie) {
        zombie.setShield(true);
    }
}