package zombiegame;

import java.awt.RenderingHints;
import java.util.HashMap;
import java.util.Map;
// don't go bigger than 1080p
public class GameConstants {
    public static final int GENERIC_SIZE = 64;
    public static final int POWERUP_SIZE = 48;
    public static final int GAME_SCREEN_WIDTH = 960;
    public static final int GAME_SCREEN_HEIGHT = 600;

    public static final int START_MENU_SCREEN_WIDTH = 500;
    public static final int START_MENU_SCREEN_HEIGHT = 550;

    public static final int END_MENU_SCREEN_WIDTH = 500;
    public static final int END_MENU_SCREEN_HEIGHT = 500;
    
    public static final Map<Object,Object> RENDER_HINTS =  new HashMap<>(){{
        put(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    }};

    public static final int MAX_HEALTH = 100;
    public static final int BOOST_DURATION = 10000; // 10 seconds
    public static final int LIVES = 3;
    public static final int HIT_FLASH_DURATION_MS = 200;
}
