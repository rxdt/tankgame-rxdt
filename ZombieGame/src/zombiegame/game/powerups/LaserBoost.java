package zombiegame.game.powerups;

import zombiegame.game.Zombie;

import java.awt.image.BufferedImage;

public class LaserBoost extends PowerUp {
    public LaserBoost(int x, int y, BufferedImage img) { super(x, y, img); }

    @Override
    public void applyTo(Zombie zombie) {
        zombie.enableLaser();
    }
}
