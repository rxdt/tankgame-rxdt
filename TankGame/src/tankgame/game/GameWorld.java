package tankgame.game;


import tankgame.GameConstants;
import tankgame.Launcher;
import tankgame.factories.ImageFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;


public class GameWorld extends JPanel implements Runnable {

    private BufferedImage world;
    private Tank zombie1;
    private Tank zombie2;
    private final Launcher launcher;

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
                this.zombie1.update(); // update tank
                this.zombie2.update();
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
        BufferedImage z1img = ImageFactory.getImage("zombie1.png", 64, 64);
        BufferedImage z2img = ImageFactory.getImage("zombie2.png", 64, 64);
        if (z1img == null || z2img == null) {
            System.err.println("Error: could not load zombie png");
            System.exit(-3);
        }
        zombie1 = new Tank(300, 300, 0, 0, (short) 0, z1img);
        zombie2 = new Tank(600, 300, 0, 0, (short) 0, z2img);
        this.addKeyListener( //  listen to key events on the panel, not the jframe
            new TankControl(zombie1, KeyEvent.VK_W, KeyEvent.VK_S, KeyEvent.VK_A, KeyEvent.VK_D)
        );
        this.addKeyListener(
            new TankControl(zombie2, KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT)
        );
        this.setFocusable(true);        // allow GameWorld to be focused
        this.requestFocusInWindow();    // ask Java to give it focus when this panel appears
    }

    // change as we like, allows us to draw without casting to ints
    // don't draw to g until entire frame is done
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g; // Graphics2D has more useful functions
        g2.setRenderingHints(GameConstants.RENDER_HINTS);
        Graphics2D buffer = world.createGraphics();
        buffer.setColor(Color.black);
        buffer.fillRect(0, 0, GameConstants.GAME_SCREEN_WIDTH, GameConstants.GAME_SCREEN_HEIGHT);
        this.zombie1.drawImage(buffer);
        this.zombie2.drawImage(buffer);
        g2.drawImage(world, 0, 0, null);
    }

    @Override
    public void addNotify() {
        super.addNotify();
        this.requestFocusInWindow(); // ensure events are captured when panel appears
    }
}
