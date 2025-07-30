package tankgame.factories;

import tankgame.game.ResourceManager;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.List;

public class AnimationFactory {
    private static final HashMap<String, List<BufferedImage>> animationCache = new HashMap<>();

    public static List<BufferedImage> getAnimation(String key, List<String> framePaths) {
        if (animationCache.containsKey(key)) {
            return animationCache.get(key);
        }

        List<BufferedImage> frames = framePaths.stream()
                .map(path -> ResourceManager.getInstance().getImage(path, 64, 64))
                .toList();

        animationCache.put(key, frames);
        return frames;
    }
}

