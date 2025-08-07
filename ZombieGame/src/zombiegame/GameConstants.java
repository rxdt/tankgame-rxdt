package zombiegame;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

// don't go bigger than 1080p
public class GameConstants {
    public static final int GENERIC_SIZE = 64;

    private static final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private static final int SCREEN_WIDTH = (int) screenSize.getWidth();
    private static final int SCREEN_HEIGHT = (int) screenSize.getHeight();

    public static final int FULLSCREEN_WIDTH = Math.min(SCREEN_WIDTH-10, 1920-10);
    public static final int FULLSCREEN_HEIGHT = Math.min(SCREEN_HEIGHT-25, 1080-25);

    public static final int GAME_SCREEN_WIDTH = FULLSCREEN_WIDTH;
    public static final int GAME_SCREEN_HEIGHT = FULLSCREEN_HEIGHT;

    public static final Map<Object,Object> RENDER_HINTS =  new HashMap<>(){{
        put(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    }};

    public static final int MAX_HEALTH = 100;
    public static final int POWERUP_DURATION = 9000; // 9 seconds
    public static final int POWERUP_SPAWN_COOLDOWN = 13000;
    public static final int LIVES = 3;
    public static final int HIT_FLASH_DURATION_MS = 200;
    public static final long ANIMATION_DURATION = 300;
    public static final double SPEED_BOOST = 3.0;
    public static final float BULLET_SPEED = 9f;
    public static final int IDLE_TIME_BEFORE_BREATHE = 30000;
    public static final int KONAMI_MESSAGE_DURATION = 2000;
    public static final String SECRET_MESSAGE = "Secret code unlock";
    public static final String SPEED_ON = "SPEED 3X";
    public static final String SHIELD_ON = "SHIELD ON";

    public static final String EXIT = "EXIT";
    public static final String START_GAME = "START GAME";
    public static final String PLAY_AGAIN = "PLAY AGAIN";

    public static final List<Integer> KONAMI_CODE_ARROW = List.of(
            KeyEvent.VK_UP, KeyEvent.VK_UP,
            KeyEvent.VK_DOWN, KeyEvent.VK_DOWN,
            KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT,
            KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT,
            KeyEvent.VK_B, KeyEvent.VK_A
    );

    public static final List<Integer> KONAMI_CODE_WASD = List.of(
            KeyEvent.VK_W, KeyEvent.VK_W,
            KeyEvent.VK_S, KeyEvent.VK_S,
            KeyEvent.VK_A, KeyEvent.VK_D,
            KeyEvent.VK_A, KeyEvent.VK_D,
            KeyEvent.VK_B, KeyEvent.VK_A
    );
}
