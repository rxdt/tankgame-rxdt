package zombiegame.factories;

import zombiegame.GameConstants;
import zombiegame.game.ResourceManager;

import java.awt.image.BufferedImage;

public class ResourceFactory {
    public static BufferedImage[] explosionFrames;

    public static void preloadResources() {
        // images
        ResourceManager.getInstance().getImage("zombie1.png", GameConstants.GENERIC_SIZE, GameConstants.GENERIC_SIZE);
        ResourceManager.getInstance().getImage("zombie2.png", GameConstants.GENERIC_SIZE, GameConstants.GENERIC_SIZE);
        ResourceManager.getInstance().getImage("title.png", GameConstants.GAME_SCREEN_WIDTH, GameConstants.GAME_SCREEN_HEIGHT);
        ResourceManager.getInstance().getImage("map1.png", GameConstants.GAME_SCREEN_WIDTH, GameConstants.GAME_SCREEN_HEIGHT);
        ResourceManager.getInstance().getImage("map2.png", GameConstants.GAME_SCREEN_WIDTH, GameConstants.GAME_SCREEN_HEIGHT);
        ResourceManager.getInstance().getImage("map3.png", GameConstants.GAME_SCREEN_WIDTH, GameConstants.GAME_SCREEN_HEIGHT);
        ResourceManager.getInstance().getImage("health_brain_powerup.png", GameConstants.GENERIC_SIZE, GameConstants.GENERIC_SIZE);
        ResourceManager.getInstance().getImage("speed_potion_powerup.png", GameConstants.GENERIC_SIZE, GameConstants.GENERIC_SIZE);
        ResourceManager.getInstance().getImage("shield_injection_powerup.png", GameConstants.GENERIC_SIZE, GameConstants.GENERIC_SIZE);
        // sounds
        ResourceManager.getInstance().getSound("zombies-eating.wav");
        ResourceManager.getInstance().getSound("Plants vs. Zombies - Moongrains.wav");
        ResourceManager.getInstance().getSound("Plants vs. Zombies - Ultimate Battle.wav");
        ResourceManager.getInstance().getSound("plants-vs-zombies-halloween-spooky.wav");
        ResourceManager.getInstance().getSound("healing_pickup.wav");
        ResourceManager.getInstance().getSound("zombie_hit.wav");
        ResourceManager.getInstance().getSound("speed_powering_up.wav");
        ResourceManager.getInstance().getSound("shield_item_pick_up_ding.wav");
        ResourceManager.getInstance().getSound("bullet-shot.wav");
        // explosions
        explosionFrames = new BufferedImage[5];
        for (int i = 0; i < 5; i++) {
            String x = "explosion" + i + ".png";
            System.out.println(x);
            explosionFrames[i] = ResourceManager.getInstance().getImage(
                    x,
                    GameConstants.GENERIC_SIZE,
                    GameConstants.GENERIC_SIZE
            );
        }
    }
}
