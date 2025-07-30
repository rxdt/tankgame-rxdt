package tankgame.factories;

public class ResourceFactory {
    public static void preloadResources() {
        ImageFactory.getImage("zombie1.png", 64, 64);
        ImageFactory.getImage("title.png", 1536, 1024);
    }
}
