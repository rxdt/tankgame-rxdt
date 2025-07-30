package tankgame.factories;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;

public class ImageFactory {
    private static final HashMap<String, BufferedImage> imageCache = new HashMap<>();

    public static BufferedImage getImage(String path) {
        if (imageCache.containsKey(path)) {
            return imageCache.get(path);
        }

        try {
            BufferedImage img = ImageIO.read(ImageFactory.class.getClassLoader().getResourceAsStream(path));
            imageCache.put(path, img);
            return img;
        } catch (IOException | NullPointerException e) {
            System.err.println("Failed to load image: " + path);
            return null;
        }
    }
}
