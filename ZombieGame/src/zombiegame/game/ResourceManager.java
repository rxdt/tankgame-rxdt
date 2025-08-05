package zombiegame.game;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Singleton resource manager using Flyweight pattern.
 * Caches and reuses images, sounds, and animations.
 */
public class ResourceManager {

    private static final ResourceManager instance = new ResourceManager();
    private static final Map<String, BufferedImage> imageCache = new HashMap<>();
    private final Map<String, Clip> soundCache = new HashMap<>();
    private ResourceManager() {}
    public static ResourceManager getInstance() {
        return instance;
    }

    // Loads or retrieves a cached image, resized to given width and height.
    public BufferedImage getImage(String path, int width, int height) {
        String key = path + "_" + width + "x" + height;
        if (imageCache.containsKey(key)) {
            return imageCache.get(key);
        }
        try {
            BufferedImage original = ImageIO.read(getClass().getClassLoader().getResourceAsStream(path));
            if (original == null) {
                System.err.println("Image not found: " + path);
                return null;
            }
            BufferedImage scaled = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = scaled.createGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
            g2d.drawImage(original, 0, 0, width, height, null);
            g2d.dispose();
            imageCache.put(key, scaled);
            return scaled;
        } catch (IOException e) {
            System.err.println("Failed to load image: " + path);
            e.printStackTrace();
            return null;
        }
    }

    public Clip getSound(String fileName) {
        if (!soundCache.containsKey(fileName)) {
            try {
                URL soundURL = getClass().getResource("/sfx/" + fileName);
                if (soundURL == null) {
                    System.err.println("Sound not found: " + fileName);
                    return null;
                }
                AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundURL);
                Clip clip = AudioSystem.getClip();
                clip.open(audioIn);
                soundCache.put(fileName, clip);
            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                e.printStackTrace();
                return null;
            }
        }
        return soundCache.get(fileName);
    }

    public void playSound(String fileName) {
        Clip clip = getSound(fileName);
        if (clip != null) {
            if (clip.isRunning()) {
                clip.stop();
            }
            clip.setFramePosition(0);
            clip.start();
        }
    }

    public void playLoopedSound(String fileName) {
        Clip clip = getSound(fileName);
        if (clip != null) {
            if (clip.isRunning()) clip.stop();
            clip.setFramePosition(0);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    public void stopAllSounds() {
        for (Clip clip : soundCache.values()) {
            if (clip != null && clip.isRunning()) {
                clip.stop();
            }
        }
    }
}