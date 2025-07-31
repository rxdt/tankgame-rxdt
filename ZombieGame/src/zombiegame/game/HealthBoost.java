package zombiegame.game;

import java.awt.image.BufferedImage;

public class HealthBoost extends PowerUp {
    private int healAmount = 20;

    public HealthBoost(int x, int y, BufferedImage img) {
        super(x, y, img);
    }

    @Override
    public void applyTo(Zombie zombie) {
        ResourceManager.getInstance().playLoopedSound("zombies-eating.wav");
        zombie.heal(healAmount);
    }
}
