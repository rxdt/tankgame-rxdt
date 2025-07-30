package tankgame.factories;

import tankgame.GameConstants;

public class ResourceFactory {
    public static void preloadResources() {
        ImageFactory.getImage("zombie1.png", 64, 64);
        ImageFactory.getImage("zombie2.png", 64, 64);
        ImageFactory.getImage("title.png", 1536, 1024);
        ImageFactory.getImage("map1.png", GameConstants.GAME_SCREEN_WIDTH, GameConstants.GAME_SCREEN_HEIGHT);
        ImageFactory.getImage("map2.png", GameConstants.GAME_SCREEN_WIDTH, GameConstants.GAME_SCREEN_HEIGHT);
        ImageFactory.getImage("map3.png", GameConstants.GAME_SCREEN_WIDTH, GameConstants.GAME_SCREEN_HEIGHT);
    }
}
