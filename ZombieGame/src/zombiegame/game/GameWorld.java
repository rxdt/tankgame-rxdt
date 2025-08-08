package zombiegame.game;

import zombiegame.GameConstants;
import zombiegame.Launcher;
import zombiegame.game.powerups.*;
import zombiegame.game.resources.ResourceManager;
import zombiegame.game.walls.BreakableWall;
import zombiegame.game.walls.Wall;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GameWorld extends JPanel implements Runnable {

    private BufferedImage world;
    private Zombie zombie1;
    private Zombie zombie2;
    private final Launcher launcher;
    private BufferedImage background;
    private List<Wall> walls;
    private List<PowerUp> powerUps = new ArrayList<>();
    private long lastPowerUpSpawnTime = 0;
    private BufferedImage healthImg, speedImg, shieldImg, laserImg;
    private Boolean gameOver = false;
    private String winnerText;
    private List<GameObject> gameObjects;

    public GameWorld(Launcher launcher) {
        this.launcher = launcher;
        InitializeGame();
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
                    updatePowerUps();
                    checkGameOver();
                }
                this.repaint();   // redraw game
                removeInactiveBullets();
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

    private void updatePowerUps() {
        synchronized (powerUps) {
            for (PowerUp powerUp : powerUps) {
                powerUp.update(); // grows/appears, shrinks/disappears
                powerUp.updateGlow(); // pulsing glow
            }
        }
    }

    private void checkGameOver() {
        int zombie1Lives = zombie1.getLives();
        int zombie2Lives = zombie2.getLives();
        if ((zombie1Lives <= 0 && zombie1.getHealth() <= 0) ||
                (zombie2Lives <= 0 && zombie2.getHealth() <= 0)) {
            this.gameOver = true;
            ResourceManager.getInstance().stopAllSounds();
            ResourceManager.getInstance().playLoopedSound("Plants vs. Zombies - Moongrains.wav");
            this.winnerText = zombie1Lives > zombie2Lives ? "Green zombie has won!" : "Red zombie has won!";
            launcher.getEndGamePanel().setWinnerText(this.winnerText);
            Timer timer = new Timer(3000, e -> launcher.setFrame("end"));
            timer.setRepeats(false);
            timer.start();
        }
    }

    private void removeExpiredPowerUps() {
        List<PowerUp> toRemove = new ArrayList<>();
        synchronized (powerUps) {
            for (PowerUp powerUp : powerUps) {
                long age = System.currentTimeMillis() - powerUp.getSpawnTime();
                if (age > GameConstants.POWERUP_DURATION && !powerUp.isDisappearing()) {
                    powerUp.startDisappearingAnimation();
                }
                if (powerUp.isFullyDisappeared()) {
                    toRemove.add(powerUp);
                }
            }
            powerUps.removeAll(toRemove);
        }
    }

    private void createRandomPowerUp() {
        if (System.currentTimeMillis() - this.lastPowerUpSpawnTime < GameConstants.POWERUP_SPAWN_COOLDOWN) return; // hasn't been long enough
        int x = (int)(Math.random() * (GameConstants.GAME_SCREEN_WIDTH - 32));
        int y = (int)(Math.random() * (GameConstants.GAME_SCREEN_HEIGHT - 32));
        // Prevent spawning inside walls
        Rectangle spawnArea = new Rectangle(x, y, GameConstants.GENERIC_SIZE, GameConstants.GENERIC_SIZE);
        for (Wall wall : this.walls) {
            if (spawnArea.intersects(wall.getBounds())) return;
        }
        // Prevent spawning on zombie
        if (spawnArea.intersects(zombie1.getBounds()) || spawnArea.intersects(zombie2.getBounds())) return;
        PowerUp powerUp;
        int type = (int)(Math.random() * 4); // get a random number between 0–3
        switch (type) {
            case 0 -> powerUp = new HealthBoost(x, y, healthImg);
            case 1 -> powerUp = new SpeedBoost(x, y, speedImg);
            case 2 -> powerUp = new ShieldBoost(x, y, shieldImg);
            case 3 -> powerUp = new LaserBoost(x, y, laserImg);
            default -> throw new IllegalStateException("Unexpected value: " + type);
        }
        powerUp.setSpawnTime(System.currentTimeMillis());
        this.powerUps.add(powerUp);
        this.lastPowerUpSpawnTime = System.currentTimeMillis();
        powerUp.startAppearingAnimation();
    }

    private void checkPowerUpPickup(Zombie zombie) {
        List<PowerUp> toRemove = new ArrayList<>();
        synchronized (powerUps) {
            for (PowerUp powerUp : powerUps) {
                if (powerUp.getBounds().intersects(new Rectangle((int)zombie.getX(), (int)zombie.getY(), zombie.getImage().getWidth(), zombie.getImage().getHeight()))) {
                    powerUp.applyTo(zombie);
                    toRemove.add(powerUp);
                }
            }
            powerUps.removeAll(toRemove);
        }
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
        this.walls = new ArrayList<>();
        /*
         * note class loaders read files from the out folder (build folder in Netbeans) and not the
         * current working directory. When running a jar, class loaders will read from within the jar.
         */
        BufferedImage z1img = ResourceManager.getInstance().getImage("vfx/zombie1.png", GameConstants.GENERIC_SIZE, GameConstants.GENERIC_SIZE);
        BufferedImage z2img = ResourceManager.getInstance().getImage("vfx/zombie2.png", GameConstants.GENERIC_SIZE, GameConstants.GENERIC_SIZE);
        BufferedImage bulletImg = ResourceManager.getInstance().getImage("vfx/bullet.png", GameConstants.GENERIC_SIZE/2, GameConstants.GENERIC_SIZE/2);
        BufferedImage laserImg = ResourceManager.getInstance().getImage("vfx/ammo.png", GameConstants.GENERIC_SIZE, GameConstants.GENERIC_SIZE);
        this.healthImg = ResourceManager.getInstance().getImage("vfx/health_brain_powerup.png", GameConstants.GENERIC_SIZE, GameConstants.GENERIC_SIZE);
        this.speedImg = ResourceManager.getInstance().getImage("vfx/speed_potion_powerup.png", GameConstants.GENERIC_SIZE, GameConstants.GENERIC_SIZE);
        this.shieldImg = ResourceManager.getInstance().getImage("vfx/shield_injection_powerup.png", GameConstants.GENERIC_SIZE, GameConstants.GENERIC_SIZE);
        this.laserImg = ResourceManager.getInstance().getImage("vfx/ammo.png", GameConstants.GENERIC_SIZE, GameConstants.GENERIC_SIZE);
        if (z1img == null || z2img == null || bulletImg == null || healthImg == null || speedImg == null || shieldImg == null || this.laserImg == null) {
            System.err.println("Error: could not load png");
            System.exit(-3);
        }
        zombie1 = new Zombie(300, 350, 0, 0, 0, z1img, GameConstants.KONAMI_CODE_WASD); // green zombie
        zombie2 = new Zombie(600, 350, 0, 0, 0, z2img, GameConstants.KONAMI_CODE_ARROW); // red zombie
        zombie2.setFacingOffset(180);
        zombie1.setBulletImage(bulletImg);
        zombie2.setBulletImage(bulletImg);
        this.addKeyListener( //  listen to key events on the panel, not the jframe
            new ZombieControl(zombie1, KeyEvent.VK_W, KeyEvent.VK_S, KeyEvent.VK_A, KeyEvent.VK_D, KeyEvent.VK_SPACE, this)
        );
        this.addKeyListener(
            new ZombieControl(zombie2, KeyEvent.VK_DOWN, KeyEvent.VK_UP, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT, KeyEvent.VK_ENTER, this)
        );
        try {
            loadMap();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.setFocusable(true);        // allow GameWorld to be focused
        this.requestFocusInWindow();    // ask Java to give it focus when this panel appears
    }

    public void loadMap() throws IOException {
        List<BufferedImage> breakable = new ArrayList<>();
        List<BufferedImage> nonBreakable = new ArrayList<>();
        // Non-breakable - flowers
        breakable.add(ResourceManager.getInstance().getImage("vfx/sunflower.png", GameConstants.GENERIC_SIZE, GameConstants.GENERIC_SIZE));
        breakable.add(ResourceManager.getInstance().getImage("vfx/daisies.png", GameConstants.GENERIC_SIZE, GameConstants.GENERIC_SIZE));
        breakable.add(ResourceManager.getInstance().getImage("vfx/blue_flowers.png", GameConstants.GENERIC_SIZE, GameConstants.GENERIC_SIZE));
        breakable.add(ResourceManager.getInstance().getImage("vfx/roses.png", GameConstants.GENERIC_SIZE, GameConstants.GENERIC_SIZE));
        // Breakable - non-flowers
        nonBreakable.add(ResourceManager.getInstance().getImage("vfx/bush.png", GameConstants.GENERIC_SIZE, GameConstants.GENERIC_SIZE));
        nonBreakable.add(ResourceManager.getInstance().getImage("vfx/log.png", GameConstants.GENERIC_SIZE*2, GameConstants.GENERIC_SIZE*2));
        nonBreakable.add(ResourceManager.getInstance().getImage("vfx/trees.png", GameConstants.GENERIC_SIZE, GameConstants.GENERIC_SIZE));

        int mapIndex = 1 + (int)(Math.random() * 3); // 1, 2, or 3
        String mapPath = "vfx/map" + mapIndex + ".png";
        this.background = ResourceManager.getInstance().getImage(mapPath, GameConstants.GAME_SCREEN_WIDTH, GameConstants.GAME_SCREEN_HEIGHT);

        InputStream input = getClass().getClassLoader().getResourceAsStream("maps/map" + mapIndex + ".csv");
        if (input == null) {
            throw new FileNotFoundException("Could not find: maps/map" + mapIndex + ".csv");
        }
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(input));
        String line;
        int row = 0;
        while ((line = bufferedReader.readLine()) != null) {
            String[] lineArray = line.split(",");
            for (int column = 0; column < lineArray.length; column++) {
                int code = Integer.parseInt(lineArray[column]);
                int x = column * GameConstants.GENERIC_SIZE;
                int y = row * GameConstants.GENERIC_SIZE;
                switch (code) {
                    case 1: // Non-breakable flowers
                        this.walls.add(new Wall(x, y, GameConstants.GENERIC_SIZE, GameConstants.GENERIC_SIZE,
                                nonBreakable.get((int)(Math.random() * nonBreakable.size()))));
                        break;
                    case 2: // Breakable
                        this.walls.add(new BreakableWall(x, y, GameConstants.GENERIC_SIZE, GameConstants.GENERIC_SIZE,
                                breakable.get((int)(Math.random() * breakable.size()))));
                        break;
                }
            }
            row++;
        }
        bufferedReader.close();
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
        buffer.drawImage(this.background, 0, 0, null);
        for (Wall wall : walls) {
            wall.draw(buffer);
        }
        zombie1.draw(buffer);
        zombie2.draw(buffer);
        for (Bullet b : zombie1.getBullets()) b.draw(buffer);
        for (Bullet b : zombie2.getBullets()) b.draw(buffer);
        synchronized (powerUps) {
            for (PowerUp p : powerUps) {
                p.draw(buffer);
            }
        }
        // Set viewport dimensions for each player
        int viewWidth = GameConstants.GAME_SCREEN_WIDTH / 2;
        int viewHeight = GameConstants.GAME_SCREEN_HEIGHT;
        // Get each zombie's view
        BufferedImage leftView = getViewport(zombie1.getX(), zombie1.getY(), viewWidth, viewHeight);
        BufferedImage rightView = getViewport(zombie2.getX(), zombie2.getY(), viewWidth, viewHeight);
        // Draw both views side by side
        g2.drawImage(leftView, 0, 0, null);
        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(3));
        g2.drawLine(viewWidth, 0, viewWidth, viewHeight);
        g2.drawImage(rightView, viewWidth, 0, null);
        drawMiniMap(g2);
        if (gameOver && winnerText != null) {
            g2.setColor(new Color(0, 0, 0, 150));
            g2.fillRect(0, 0, GameConstants.GAME_SCREEN_WIDTH, GameConstants.GAME_SCREEN_HEIGHT);
            g2.setColor(Color.GREEN);
            g2.setFont(new Font("Papyrus", Font.BOLD, 70));
            FontMetrics fm = g2.getFontMetrics();
            int textWidth = fm.stringWidth(winnerText);
            g2.drawString(winnerText, (GameConstants.GAME_SCREEN_WIDTH - textWidth) / 2, GameConstants.GAME_SCREEN_HEIGHT / 2);
        }
    }

    private void drawMiniMap(Graphics2D g2) {
        int miniWidth = GameConstants.GAME_SCREEN_WIDTH / 4;
        int miniHeight = GameConstants.GAME_SCREEN_HEIGHT / 4;
        int xOffset = (GameConstants.GAME_SCREEN_WIDTH - miniWidth) / 2;
        int yOffset = GameConstants.GAME_SCREEN_HEIGHT - miniHeight - 15;
        Composite originalComposite = g2.getComposite();
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
        g2.drawImage(world, xOffset, yOffset, miniWidth, miniHeight, null);
        g2.setComposite(originalComposite);
        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(2));
        g2.drawRect(xOffset - 1, yOffset - 1, miniWidth + 1, miniHeight + 1);
    }

    private BufferedImage getViewport(float centerX, float centerY, int width, int height) {
        float x = centerX - width / 2;
        float y = centerY - height / 2;
        // Clamp to map boundaries
        x = Math.max(0, Math.min(x, world.getWidth() - width));
        y = Math.max(0, Math.min(y, world.getHeight() - height));
        return world.getSubimage((int)x, (int)y, width, height);
    }

    @Override
    public void addNotify() {
        super.addNotify();
        this.requestFocusInWindow(); // ensure events are captured when panel appears
    }

    // handles bullet movement, deteects wall collisipn, removes bullets that hit walls
    private void updateBullets(Zombie shooter, Zombie target) {
        List<Bullet> bullets = shooter.getBullets();
        List<Bullet> bulletsCopy = new ArrayList<>(bullets);
        Rectangle targetBounds = new Rectangle(
                (int) target.getX(),
                (int) target.getY(),
                target.getImage().getWidth(),
                target.getImage().getHeight()
        );
        for (Bullet bullet : bulletsCopy) {
            bullet.update();
            Rectangle bulletBounds = bullet.getBounds();
            // 1. Check wall collisions
            for (Wall wall : walls) {
                if (bulletBounds.intersects(wall.getBounds())) {
                    bullet.setActive(false);
                    if (wall instanceof BreakableWall breakableWall) {
                        breakableWall.destroy();
                    }
                    break;
                }
            }
            // 2. Check if bullet hits the other zombie
            if (bullet.isActive() && !target.isExploding() && bulletBounds.intersects(targetBounds)) {
                bullet.setActive(false);
                if (!target.isShieldActive()) {
                    target.onHit(bullet.getDamage());
                    if (target.getHealth() <= 0 && target.getLives() >= 1) {
                        target.deductALife();
                    }
                }
            }
        }
    }

    public boolean gameIsOver() {
        return this.gameOver;
    }

    private void removeInactiveBullets() {
        synchronized (zombie1.getBullets()) {
            zombie1.getBullets().removeIf(b -> !b.isActive());
        }
        synchronized (zombie1.getBullets()) {
            zombie2.getBullets().removeIf(b -> !b.isActive());
        }
    }
}
