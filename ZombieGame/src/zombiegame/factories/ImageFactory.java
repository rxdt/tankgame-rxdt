package zombiegame.factories;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;

public class ImageFactory {
    private static final HashMap<String, BufferedImage> imageCache = new HashMap<>();

    public static BufferedImage getImage(String path, int width, int height) {
        if (imageCache.containsKey(path)) {
            return imageCache.get(path);
        }
        try {
            BufferedImage original = ImageIO.read(ImageFactory.class.getClassLoader().getResourceAsStream(path));
            BufferedImage scaled = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = scaled.createGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
            g2d.drawImage(original, 0, 0, width, height, null);
            g2d.dispose();
            imageCache.put(path, scaled);
            return scaled;
        } catch (IOException | NullPointerException e) {
            System.err.println("Failed to load image: " + path);
            return null;
        }
    }
}
