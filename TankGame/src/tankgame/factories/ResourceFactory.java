package tankgame.factories;

import tankgame.GameConstants;
import tankgame.game.ResourceManager;

public class ResourceFactory {

    public static void preloadResources() {
        ResourceManager.getInstance().getImage("zombie1.png", GameConstants.GENERIC_SIZE, GameConstants.GENERIC_SIZE);
        ResourceManager.getInstance().getImage("zombie2.png", GameConstants.GENERIC_SIZE, GameConstants.GENERIC_SIZE);
        ResourceManager.getInstance().getImage("title.png", GameConstants.GAME_SCREEN_WIDTH, GameConstants.GAME_SCREEN_HEIGHT);
        ResourceManager.getInstance().getImage("map1.png", GameConstants.GAME_SCREEN_WIDTH, GameConstants.GAME_SCREEN_HEIGHT);
        ResourceManager.getInstance().getImage("map2.png", GameConstants.GAME_SCREEN_WIDTH, GameConstants.GAME_SCREEN_HEIGHT);
        ResourceManager.getInstance().getImage("map3.png", GameConstants.GAME_SCREEN_WIDTH, GameConstants.GAME_SCREEN_HEIGHT);
        ResourceManager.getInstance().getImage("brain_powerup.png", GameConstants.GENERIC_SIZE, GameConstants.GENERIC_SIZE);
        ResourceManager.getInstance().getImage("potion_powerup.png", GameConstants.GENERIC_SIZE, GameConstants.GENERIC_SIZE);
        ResourceManager.getInstance().getImage("injection_powerup.png", GameConstants.GENERIC_SIZE, GameConstants.GENERIC_SIZE);
    }
}
