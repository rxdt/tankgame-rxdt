package zombiegame.game.powerups;

import zombiegame.game.resources.ResourceManager;
import zombiegame.game.Zombie;

import java.awt.image.BufferedImage;

public class HealthBoost extends PowerUp {
    private int healAmount = 35;

    public HealthBoost(int x, int y, BufferedImage img) {
        super(x, y, img);
    }

    @Override
    public void applyTo(Zombie zombie) {
        System.out.println("35 point Health Boost");
        ResourceManager.getInstance().playSound("zombies-eating.wav");
        zombie.heal(healAmount);
    }
}
