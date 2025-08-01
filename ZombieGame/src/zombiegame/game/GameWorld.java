package zombiegame.game;

import zombiegame.GameConstants;
import zombiegame.Launcher;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;


public class GameWorld extends JPanel implements Runnable {

    private BufferedImage world;
    private Zombie zombie1;
    private Zombie zombie2;
    private final Launcher launcher;
    private BufferedImage background;
    private List<Wall> walls;
    final int TILE_SIZE = 64;
    private List<PowerUp> powerUps = new ArrayList<>();
    private long lastPowerUpSpawnTime = 0;
    private final long powerUpSpawnCooldown = 7000; // 7 seconds
    private BufferedImage healthImg, speedImg, shieldImg;
    private Boolean gameOver = false;
    private String winnerText;
    private int mapIndex = 1; // starts with 1 as default

    public GameWorld(Launcher launcher) {
        this.launcher = launcher;
    }

    @Override
    public void run() {
        try {
            while (true) {
                if (!gameOver) {
                    this.zombie1.update(walls, zombie2); // update zombies
                    this.zombie2.update(walls, zombie1);
                    updateBullets(zombie1, zombie2);
                    updateBullets(zombie2, zombie1);
                    createRandomPowerUp(); // randomly create and place powerups of different types
                    checkPowerUpPickup(zombie1);
                    checkPowerUpPickup(zombie2);
                    removeExpiredPowerUps();
                    checkGameOver();
                }
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

    private void checkGameOver() {
        int zombie1Health = zombie1.getHealth();
        int zombie2Health = zombie2.getHealth();
        if (zombie1Health <= 0 || zombie2Health <= 0) {
            gameOver = true;
            this.winnerText = zombie1Health > zombie2Health ? "Green zombie has won!" : "Red zombie has won!";
        }
    }

    private void removeExpiredPowerUps() {
        long now = System.currentTimeMillis();
        powerUps.removeIf(powerUp -> now - powerUp.getSpawnTime() > 7000); // Remove after 7 sec
    }

    private void createRandomPowerUp() {
        if (System.currentTimeMillis() - lastPowerUpSpawnTime < powerUpSpawnCooldown) return; // hasn't been long enough
        int x = (int)(Math.random() * (GameConstants.GAME_SCREEN_WIDTH - 32));
        int y = (int)(Math.random() * (GameConstants.GAME_SCREEN_HEIGHT - 32));
        // Prevent spawning inside walls
        Rectangle spawnArea = new Rectangle(x, y, GameConstants.POWERUP_SIZE, GameConstants.POWERUP_SIZE);
        for (Wall wall : walls) {
            if (spawnArea.intersects(wall.getBounds())) return;
        }
        PowerUp powerUp;
        int type = (int)(Math.random() * 3); // get a random number between 0–2
        switch (type) {
            case 0 -> powerUp = new HealthBoost(x, y, healthImg);
            case 1 -> powerUp = new SpeedBoost(x, y, speedImg);
            case 2 -> powerUp = new ShieldBoost(x, y, shieldImg);
            default -> throw new IllegalStateException("Unexpected value: " + type);
        }
        powerUp.setSpawnTime(System.currentTimeMillis());
        this.powerUps.add(powerUp);
        this.lastPowerUpSpawnTime = System.currentTimeMillis();
    }

    private void checkPowerUpPickup(Zombie zombie) {
        List<PowerUp> toRemove = new ArrayList<>();
        for (PowerUp powerUp : powerUps) {
            if (powerUp.getBounds().intersects(new Rectangle(zombie.getX(), zombie.getY(), zombie.getImage().getWidth(), zombie.getImage().getHeight()))) {
                powerUp.applyTo(zombie);
                toRemove.add(powerUp);
            }
        }
        powerUps.removeAll(toRemove);
    }

    /**
    * Reset game to its initial state.
    */
    public void resetGame() {
        this.gameOver = false;
        InitializeGame(); // Resets the zombie and reinitializes the world
    }

    /**
     * Load all resources for Zombie Wars Game. Set all Game Objects to their
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
        this.mapIndex = 1 + (int)(Math.random() * 3); // 1, 2, or 3
        String mapPath = "map" + mapIndex + ".png";
        background = ResourceManager.getInstance().getImage(mapPath, GameConstants.GAME_SCREEN_WIDTH, GameConstants.GAME_SCREEN_HEIGHT);
        walls = new ArrayList<>();
        this.placeWalls();
        BufferedImage z1img = ResourceManager.getInstance().getImage("zombie1.png", GameConstants.GENERIC_SIZE, GameConstants.GENERIC_SIZE);
        BufferedImage z2img = ResourceManager.getInstance().getImage("zombie2.png", GameConstants.GENERIC_SIZE, GameConstants.GENERIC_SIZE);
        BufferedImage bulletImg = ResourceManager.getInstance().getImage("bullet.png", GameConstants.GENERIC_SIZE/2, GameConstants.GENERIC_SIZE/2);
        this.healthImg = ResourceManager.getInstance().getImage("health_brain_powerup.png", GameConstants.POWERUP_SIZE, GameConstants.POWERUP_SIZE);
        this.speedImg = ResourceManager.getInstance().getImage("speed_potion_powerup.png", GameConstants.POWERUP_SIZE, GameConstants.POWERUP_SIZE);
        this.shieldImg = ResourceManager.getInstance().getImage("shield_injection_powerup.png", GameConstants.POWERUP_SIZE, GameConstants.POWERUP_SIZE);
        if (z1img == null || z2img == null || background == null || bulletImg == null || healthImg == null || speedImg == null || shieldImg == null) {
            System.err.println("Error: could not load png");
            System.exit(-3);
        }
        zombie1 = new Zombie(300, 350, 0, 0, (short) 0, z1img);
        zombie2 = new Zombie(600, 350, 0, 0, (short) 0, z2img);
        zombie2.setFacingOffset(180);
        zombie1.setBulletImage(bulletImg);
        zombie2.setBulletImage(bulletImg);
        this.addKeyListener( //  listen to key events on the panel, not the jframe
            new ZombieControl(zombie1, KeyEvent.VK_W, KeyEvent.VK_S, KeyEvent.VK_A, KeyEvent.VK_D, KeyEvent.VK_SPACE)
        );
        this.addKeyListener(
            new ZombieControl(zombie2, KeyEvent.VK_DOWN, KeyEvent.VK_UP, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT, KeyEvent.VK_ENTER)
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

        for (PowerUp p : powerUps) {
            p.draw(buffer);
        }
        // Set viewport dimensions for each player
        int viewWidth = GameConstants.GAME_SCREEN_WIDTH / 2;
        int viewHeight = GameConstants.GAME_SCREEN_HEIGHT;
        // Get each zombie's view
        BufferedImage leftView = getViewport(zombie1.getX(), zombie1.getY(), viewWidth, viewHeight);
        BufferedImage rightView = getViewport(zombie2.getX(), zombie2.getY(), viewWidth, viewHeight);
        // Draw both views side by side
        g2.drawImage(leftView, 0, 0, null);
        g2.drawImage(rightView, viewWidth, 0, null);

        if (gameOver && winnerText != null) {
            g2.setColor(new Color(0, 0, 0, 150));
            g2.fillRect(0, 0, GameConstants.GAME_SCREEN_WIDTH, GameConstants.GAME_SCREEN_HEIGHT);

            g2.setColor(Color.CYAN);
            g2.setFont(new Font("Papyrus", Font.BOLD, 70));
            FontMetrics fm = g2.getFontMetrics();
            int textWidth = fm.stringWidth(winnerText);
            g2.drawString(winnerText, (GameConstants.GAME_SCREEN_WIDTH - textWidth) / 2, GameConstants.GAME_SCREEN_HEIGHT / 2);
            Timer timer = new Timer(3000, e -> launcher.setFrame("end"));
            timer.setRepeats(false);
            timer.start();
        }
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
        BufferedImage daisies = ResourceManager.getInstance().getImage("daisies.png", TILE_SIZE, TILE_SIZE);
        BufferedImage blueFlowers = ResourceManager.getInstance().getImage("blue_flowers.png", TILE_SIZE, TILE_SIZE);
        BufferedImage roses = ResourceManager.getInstance().getImage("roses.png", TILE_SIZE, TILE_SIZE);
        BufferedImage log = ResourceManager.getInstance().getImage("log.png", TILE_SIZE*2, TILE_SIZE*2);
        BufferedImage tree = ResourceManager.getInstance().getImage("trees.png", TILE_SIZE, TILE_SIZE);

        // Grid placement: addWallAt(col, row, image)
        addItemAtSpot(1, 2, daisies, TILE_SIZE, TILE_SIZE, true);
        addItemAtSpot(2, 2, sunflower, TILE_SIZE, TILE_SIZE, true);
        addItemAtSpot(3, 2, bush, TILE_SIZE, TILE_SIZE, false);
        addItemAtSpot(3, 3, blueFlowers, TILE_SIZE, TILE_SIZE, true);
        // Bottom-left
        addItemAtSpot(2, 6, blueFlowers, TILE_SIZE, TILE_SIZE, true);
        addItemAtSpot(1, 6, roses, TILE_SIZE, TILE_SIZE, true);
        // Diagonal stack
        addItemAtSpot(4, 7, daisies, TILE_SIZE, TILE_SIZE, true);
        addItemAtSpot(3, 8, blueFlowers, TILE_SIZE, TILE_SIZE, true);
//        addItemAtSpot(7, 7, log, TILE_SIZE*2,TILE_SIZE*2, false);
        // Vertical column
//        addItemAtSpot(8, 0, log, TILE_SIZE*2, TILE_SIZE*2, false);
        addItemAtSpot(8, 4, blueFlowers, TILE_SIZE, TILE_SIZE, true);
        addItemAtSpot(9, 4, daisies, TILE_SIZE, TILE_SIZE, true);
        // Top-right stack
        addItemAtSpot(13, 2, bush, TILE_SIZE, TILE_SIZE, false);
        addItemAtSpot(13, 3, sunflower, TILE_SIZE, TILE_SIZE, true);
        addItemAtSpot(13, 4, tree, TILE_SIZE, TILE_SIZE, false);
        addItemAtSpot(10, 0, daisies, TILE_SIZE, TILE_SIZE, true);
        // Bottom-right
        addItemAtSpot(11, 8, bush, TILE_SIZE, TILE_SIZE, false);
        addItemAtSpot(12, 8, daisies, TILE_SIZE, TILE_SIZE, true);
        addItemAtSpot(13, 8, roses, TILE_SIZE, TILE_SIZE, true);
        addItemAtSpot(14, 8, bush, TILE_SIZE, TILE_SIZE, false);
    }

    private void addItemAtSpot(int col, int row, BufferedImage img, int width, int height, Boolean isBreakable) {
        if (!isBreakable) {
            walls.add(new Wall(col * TILE_SIZE, row * TILE_SIZE, width, height, img));
        }
        walls.add(new BreakableWall(col * TILE_SIZE, row * TILE_SIZE, width, height, img));
    }

    // handles bullet movement, deteects wall collisipn, removes bullets that hit walls
    private void updateBullets(Zombie shooter, Zombie zombieTarget) {
        List<Bullet> bulletsToRemove = new ArrayList<>();
        List<Bullet> bulletsToRemoveCopy = new ArrayList<>(shooter.getBullets());
        for (Bullet bullet : bulletsToRemoveCopy) {
            bullet.update();
            Rectangle bulletBounds = bullet.getBounds();
            ResourceManager.getInstance().playSound("bullet-shot.wav");
            // 1. Check wall collisions
            for (Wall wall : walls) {
                if (bulletBounds.intersects(wall.getBounds())) {
                    bullet.setActive(false);
                    bulletsToRemove.add(bullet);
                    if (wall instanceof BreakableWall breakable) {
                         breakable.destroy();
                    }
                    break;
                }
            }
            // 2. Check if bullet hits the other zombie
            if (bullet.isActive()) {
                Rectangle targetBounds = new Rectangle(
                        zombieTarget.getX(), zombieTarget.getY(),
                        zombieTarget.getImage().getWidth(), zombieTarget.getImage().getHeight()
                );
                if (bulletBounds.intersects(targetBounds)) {
                    bullet.setActive(false);
                    bulletsToRemove.add(bullet);
                    if (!zombieTarget.isShieldActive()) {
                        zombieTarget.onHit();
                    }
                    if (zombieTarget.getHealth() <= 0) {
                        if (zombieTarget.getLives() > 1) {
                            zombieTarget.deductALife();
                        } else {
                            gameOver = true;
                            ResourceManager.getInstance().stopAllSounds();
                            ResourceManager.getInstance().playLoopedSound("Plants vs. Zombies - Moongrains.wav");
                        }
                    }
                    System.out.println("Bullet hit " + (zombieTarget == zombie1 ? "zombie1" : "zombie2"));
                }
            }
        }
        shooter.getBullets().removeAll(bulletsToRemove);
    }
}
