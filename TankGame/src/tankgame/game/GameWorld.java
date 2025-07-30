package tankgame.game;

import tankgame.GameConstants;
import tankgame.Launcher;
import tankgame.factories.ImageFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;


public class GameWorld extends JPanel implements Runnable {

    private BufferedImage world;
    private Tank zombie1;
    private Tank zombie2;
    private final Launcher launcher;
    private BufferedImage background;
    private List<Wall> walls;
    final int TILE_SIZE = 64;

    /**
     *
     */
    public GameWorld(Launcher launcher) {
        this.launcher = launcher;
    }

    @Override
    public void run() {
        try {
            while (true) {
                this.zombie1.update(walls); // update zombie
                this.zombie2.update(walls);
                zombie1.getBullets().forEach(Bullet::update);
                zombie2.getBullets().forEach(Bullet::update);
                this.repaint();   // redraw game
                /*
                 * Sleep for 1000/144 ms (~6.9ms). This is done to have our 
                 * loop run at a fixed rate per/sec. 
                */
                Thread.sleep(1000 / 144);
            }
        } catch (InterruptedException ignored) {
            System.out.println(ignored);
        }
    }

    /**
     * Reset game to its initial state.
     */
    public void resetGame() {
        InitializeGame(); // Resets the tank and reinitializes the world
    }

    /**
     * Load all resources for Tank Wars Game. Set all Game Objects to their
     * initial state as well. // thse width/height resolutions will change to account for minimap
     * WILL HAVE TO REWRITE THIS
     */
    public void InitializeGame() {
        this.world = new BufferedImage(GameConstants.GAME_SCREEN_WIDTH,
                GameConstants.GAME_SCREEN_HEIGHT,
                BufferedImage.TYPE_INT_RGB);
        /*
         * note class loaders read files from the out folder (build folder in Netbeans) and not the
         * current working directory. When running a jar, class loaders will read from within the jar.
         */
        background = ResourceManager.getInstance().getImage("map1.png", GameConstants.GAME_SCREEN_WIDTH, GameConstants.GAME_SCREEN_HEIGHT);
        walls = new ArrayList<>();
        this.placeWalls();
        BufferedImage z1img = ResourceManager.getInstance().getImage("zombie1.png", 64, 64);
        BufferedImage z2img = ResourceManager.getInstance().getImage("zombie2.png", 64, 64);
        BufferedImage bulletImg = ResourceManager.getInstance().getImage("bullet.png", 32, 32);
        if (z1img == null || z2img == null || background == null || bulletImg == null) {
            System.err.println("Error: could not load png");
            System.exit(-3);
        }
        zombie1 = new Tank(300, 350, 0, 0, (short) 0, z1img);
        zombie2 = new Tank(600, 350, 0, 0, (short) 0, z2img);
        zombie2.setFacingOffset(180);
        zombie1.setBulletImage(bulletImg);
        zombie2.setBulletImage(bulletImg);
        this.addKeyListener( //  listen to key events on the panel, not the jframe
            new TankControl(zombie1, KeyEvent.VK_W, KeyEvent.VK_S, KeyEvent.VK_A, KeyEvent.VK_D, KeyEvent.VK_SPACE)
        );
        this.addKeyListener(
            new TankControl(zombie2, KeyEvent.VK_DOWN, KeyEvent.VK_UP, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT, KeyEvent.VK_ENTER)
        );
        this.setFocusable(true);        // allow GameWorld to be focused
        this.requestFocusInWindow();    // ask Java to give it focus when this panel appears
    }

    // change as we like, allows us to draw without casting to ints
    // don't draw to g until entire frame is done
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHints(GameConstants.RENDER_HINTS);
        // Draw full world to offscreen buffer
        Graphics2D buffer = world.createGraphics();
        buffer.setRenderingHints(GameConstants.RENDER_HINTS);
        buffer.drawImage(background, 0, 0, null);
        for (Wall wall : walls) {
            wall.draw(buffer);
        }
        zombie1.drawImage(buffer);
        zombie2.drawImage(buffer);
        for (Bullet b : zombie1.getBullets()) b.draw(buffer);
        for (Bullet b : zombie2.getBullets()) b.draw(buffer);
        // Set viewport dimensions for each player
        int viewWidth = GameConstants.GAME_SCREEN_WIDTH / 2;
        int viewHeight = GameConstants.GAME_SCREEN_HEIGHT;
        // Get each zombie's view
        BufferedImage leftView = getViewport(zombie1.getX(), zombie1.getY(), viewWidth, viewHeight);
        BufferedImage rightView = getViewport(zombie2.getX(), zombie2.getY(), viewWidth, viewHeight);
        // Draw both views side by side
        g2.drawImage(leftView, 0, 0, null);
        g2.drawImage(rightView, viewWidth, 0, null);
    }

    private BufferedImage getViewport(int centerX, int centerY, int width, int height) {
        int x = centerX - width / 2;
        int y = centerY - height / 2;
        // Clamp to map boundaries
        x = Math.max(0, Math.min(x, world.getWidth() - width));
        y = Math.max(0, Math.min(y, world.getHeight() - height));
        return world.getSubimage(x, y, width, height);
    }

    @Override
    public void addNotify() {
        super.addNotify();
        this.requestFocusInWindow(); // ensure events are captured when panel appears
    }

    private void placeWalls() {
        walls = new ArrayList<>();
        BufferedImage sunflower = ResourceManager.getInstance().getImage("sunflower.png", TILE_SIZE, TILE_SIZE);
        BufferedImage bush = ResourceManager.getInstance().getImage("bush.png", TILE_SIZE, TILE_SIZE);
        BufferedImage weeds = ResourceManager.getInstance().getImage("weeds.png", TILE_SIZE, TILE_SIZE);
        BufferedImage blueFlowers = ResourceManager.getInstance().getImage("blue_flowers.png", TILE_SIZE, TILE_SIZE);
        BufferedImage roses = ResourceManager.getInstance().getImage("roses.png", TILE_SIZE, TILE_SIZE);
        BufferedImage log = ResourceManager.getInstance().getImage("log.png", TILE_SIZE*2, TILE_SIZE*2);
        BufferedImage tree = ResourceManager.getInstance().getImage("trees.png", TILE_SIZE, TILE_SIZE);

        // Grid placement: addWallAt(col, row, image)
        addWallAt(1, 2, weeds, TILE_SIZE, TILE_SIZE);
        addWallAt(2, 2, bush, TILE_SIZE, TILE_SIZE);
        addWallAt(3, 2, bush, TILE_SIZE, TILE_SIZE);
        addWallAt(3, 3, blueFlowers, TILE_SIZE, TILE_SIZE);
        // Bottom-left
        addWallAt(2, 6, blueFlowers, TILE_SIZE, TILE_SIZE);
        addWallAt(1, 6, roses, TILE_SIZE, TILE_SIZE);
        // Diagonal stack
        addWallAt(4, 7, weeds, TILE_SIZE, TILE_SIZE);
        addWallAt(3, 8, blueFlowers, TILE_SIZE, TILE_SIZE);
        addWallAt(4, 1, log, TILE_SIZE*2,TILE_SIZE*2);
        // Vertical column
        addWallAt(8, 3, log, TILE_SIZE*2, TILE_SIZE*2);
        addWallAt(8, 5, blueFlowers, TILE_SIZE, TILE_SIZE);
        addWallAt(8, 6, weeds, TILE_SIZE, TILE_SIZE);
        // Top-right stack
        addWallAt(13, 2, bush, TILE_SIZE, TILE_SIZE);
        addWallAt(13, 3, sunflower, TILE_SIZE, TILE_SIZE);
        addWallAt(13, 4, tree, TILE_SIZE, TILE_SIZE);
        addWallAt(10, 0, weeds, TILE_SIZE, TILE_SIZE);
        // Bottom-right
        addWallAt(11, 8, bush, TILE_SIZE, TILE_SIZE);
        addWallAt(12, 8, weeds, TILE_SIZE, TILE_SIZE);
        addWallAt(13, 8, roses, TILE_SIZE, TILE_SIZE);
        addWallAt(14, 8, bush, TILE_SIZE, TILE_SIZE);
    }

    private void addWallAt(int col, int row, BufferedImage img, int width, int height) {
        walls.add(new Wall(col * TILE_SIZE, row * TILE_SIZE, width, height, img));
    }
}
