package tankgame.game;

import tankgame.factories.ImageFactory;

import javax.sound.sampled.Clip;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Singleton resource manager using Flyweight pattern.
 * Caches and reuses images, sounds, and animations.
 */
public class ResourceManager {

    private static final ResourceManager instance = new ResourceManager();
    private final Map<String, BufferedImage> imageCache = new HashMap<>();
    private final Map<String, Clip> soundCache = new HashMap<>();
    private final Map<String, List<BufferedImage>> animationCache = new HashMap<>();
    private ResourceManager() {}
    public static ResourceManager getInstance() {
        return instance;
    }

    // Loads or retrieves a cached image, resized to given width and height.
    public BufferedImage getImage(String path, int width, int height) {
        String key = path + "_" + width + "x" + height;
        if (!imageCache.containsKey(key)) {
            BufferedImage img = ImageFactory.getImage(path, width, height);
            if (img != null) {
                imageCache.put(key, img);
            } else {
                System.err.println("Failed to load image: " + path);
            }
        }
        return imageCache.get(key);
    }
}