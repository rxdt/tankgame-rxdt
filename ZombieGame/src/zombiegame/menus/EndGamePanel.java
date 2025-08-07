package zombiegame.menus;

import zombiegame.GameConstants;
import zombiegame.Launcher;
import zombiegame.game.resources.ResourceManager;

import java.awt.*;
import java.awt.image.BufferedImage;

public class EndGamePanel extends MenuPanel {

    private String winnerText = "Game Over"; // default
    private BufferedImage green = ResourceManager.getInstance().getImage("vfx/zombie1.png", GameConstants.GAME_SCREEN_WIDTH, GameConstants.GAME_SCREEN_HEIGHT);
    private BufferedImage red = ResourceManager.getInstance().getImage("vfx/zombie2.png", GameConstants.GAME_SCREEN_WIDTH, GameConstants.GAME_SCREEN_HEIGHT);
    private Launcher launcher;

    public EndGamePanel(Launcher launcher) {
        super(launcher, "plants-vs-zombies-halloween-spooky.wav", GameConstants.PLAY_AGAIN);
        this.launcher = launcher;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setFont(new Font("Comic Sans MS", Font.BOLD, 70));
        g.setColor(Color.GREEN);
        FontMetrics fm = g.getFontMetrics();
        int textWidth = fm.stringWidth(this.winnerText);
        g.drawString(this.winnerText, (getWidth() - textWidth) / 2, 150); // Adjust vertical position
        g.drawImage(this.green, GameConstants.GAME_SCREEN_WIDTH/4, GameConstants.GAME_SCREEN_HEIGHT/4, GameConstants.GENERIC_SIZE*4, GameConstants.GENERIC_SIZE*4, null);
        g.drawImage(this.red, GameConstants.GAME_SCREEN_WIDTH/2, GameConstants.GAME_SCREEN_HEIGHT/4, GameConstants.GENERIC_SIZE*4, GameConstants.GENERIC_SIZE*4, null);
    }

    public void setWinnerText(String text) {
        this.winnerText = text;
        if (this.winnerText.contains("Green")) { // green won, red is loser/hit
            this.red = ResourceManager.getInstance().getImage("vfx/zombie2_hit.png", GameConstants.GAME_SCREEN_WIDTH, GameConstants.GAME_SCREEN_HEIGHT);
            this.green = ResourceManager.getInstance().getImage("vfx/zombie1.png", GameConstants.GAME_SCREEN_WIDTH, GameConstants.GAME_SCREEN_HEIGHT);
        } else if (this.winnerText.contains("Red")) { // red won, green is loser/hit
            this.red = ResourceManager.getInstance().getImage("vfx/zombie2.png", GameConstants.GAME_SCREEN_WIDTH, GameConstants.GAME_SCREEN_HEIGHT);
            this.green = ResourceManager.getInstance().getImage("vfx/zombie1_hit.png", GameConstants.GAME_SCREEN_WIDTH, GameConstants.GAME_SCREEN_HEIGHT);
        }
        if (green == null || red == null) {
            System.err.println("Error: cannot load menu end game images");
            System.exit(-3);
        }
        repaint();
    }

    @Override
    protected void onStartGamePressed() {
        launcher.getGamePanel().resetGame();
        new Thread(launcher.getGamePanel()).start();
        ResourceManager.getInstance().stopAllSounds();
        launcher.setFrame("game");
        ResourceManager.getInstance().playLoopedSound("Plants vs. Zombies - Ultimate Battle.wav");
    }
}
