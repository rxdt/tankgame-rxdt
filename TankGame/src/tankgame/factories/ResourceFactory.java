package tankgame.factories;

import tankgame.GameConstants;
import tankgame.game.ResourceManager;

public class ResourceFactory {
    public static void preloadResources() {
        ResourceManager.getInstance().getImage("zombie1.png", 64, 64);
        ResourceManager.getInstance().getImage("zombie2.png", 64, 64);
        ResourceManager.getInstance().getImage("title.png", 1536, 1024);
        ResourceManager.getInstance().getImage("map1.png", GameConstants.GAME_SCREEN_WIDTH, GameConstants.GAME_SCREEN_HEIGHT);
        ResourceManager.getInstance().getImage("map2.png", GameConstants.GAME_SCREEN_WIDTH, GameConstants.GAME_SCREEN_HEIGHT);
        ResourceManager.getInstance().getImage("map3.png", GameConstants.GAME_SCREEN_WIDTH, GameConstants.GAME_SCREEN_HEIGHT);
    }
}
