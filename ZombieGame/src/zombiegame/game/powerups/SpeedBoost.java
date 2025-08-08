package zombiegame.game.powerups;

import zombiegame.GameConstants;
import zombiegame.game.Zombie;

import java.awt.image.BufferedImage;

public class SpeedBoost extends PowerUp {
    public SpeedBoost(int x, int y, BufferedImage img) {
        super(x, y, img);
    }

    @Override
    public void applyTo(Zombie zombie) {
        zombie.setSpeedBoost(GameConstants.SPEED_BOOST); // triple speed
        zombie.setBoostTimer();
    }
}
