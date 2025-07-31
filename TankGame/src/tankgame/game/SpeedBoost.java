package tankgame.game;

import java.awt.image.BufferedImage;

public class SpeedBoost extends PowerUp {
    public SpeedBoost(int x, int y, BufferedImage img) {
        super(x, y, img);
    }

    @Override
    public void applyTo(Tank zombie) {
        zombie.setSpeedBoost(2); // double speeed
        zombie.setBoostTimer(System.currentTimeMillis());
    }
}
