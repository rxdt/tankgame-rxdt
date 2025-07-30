package tankgame.factories;

public class ResourceFactory {
    public static void preloadResources() {
        ImageFactory.getImage("tank1.png");
        ImageFactory.getImage("title.png");
    }
}
