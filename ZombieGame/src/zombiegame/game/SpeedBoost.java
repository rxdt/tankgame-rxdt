package zombiegame.game;

import java.awt.image.BufferedImage;

public class SpeedBoost extends PowerUp {
    public SpeedBoost(int x, int y, BufferedImage img) {
        super(x, y, img);
    }

    @Override
    public void applyTo(Zombie zombie) {
        zombie.setSpeedBoost(4); // triple speeed
        zombie.setBoostTimer(System.currentTimeMillis());
    }
}
