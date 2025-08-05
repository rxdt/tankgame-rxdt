package zombiegame.menus;

import zombiegame.GameConstants;
import zombiegame.Launcher;
import zombiegame.game.GameWorld;
import zombiegame.game.ResourceManager;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class EndGamePanel extends JPanel {

    private String winnerText = "Game Over"; // default
    private final Launcher launcher;
    private BufferedImage green = ResourceManager.getInstance().getImage("vfx/zombie1.png", GameConstants.GAME_SCREEN_WIDTH, GameConstants.GAME_SCREEN_HEIGHT);
    private BufferedImage red = ResourceManager.getInstance().getImage("vfx/zombie2.png", GameConstants.GAME_SCREEN_WIDTH, GameConstants.GAME_SCREEN_HEIGHT);

    public EndGamePanel(Launcher launcher) {

        this.launcher = launcher;
        ResourceManager.getInstance().playLoopedSound("plants-vs-zombies-halloween-spooky.wav");
        this.setBackground(Color.BLACK);
        this.setLayout(new BorderLayout());
        Color neonGreen = new Color(57, 255, 20);

        JButton restartGame = new JButton("Restart Game");
        restartGame.setFont(new Font("Courier New", Font.BOLD, 24));
        restartGame.setForeground(neonGreen);
        restartGame.setFocusPainted(false);
        restartGame.setContentAreaFilled(false);
        restartGame.setOpaque(false);
        restartGame.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(neonGreen, 2),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)));
        restartGame.addActionListener(actionEvent -> {
            ResourceManager.getInstance().stopAllSounds();
            this.launcher.setFrame("game");
            ResourceManager.getInstance().playLoopedSound("Plants vs. Zombies - Ultimate Battle.wav");
        });

        JButton exit = new JButton("Exit");
        exit.setFont(new Font("Courier New", Font.BOLD, 24));
        exit.setForeground(neonGreen);
        exit.setFocusPainted(false);
        exit.setContentAreaFilled(false);
        exit.setOpaque(false);
        exit.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(neonGreen, 2),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)));

        exit.addActionListener((actionEvent -> this.launcher.closeGame()));
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false); // make transparent to show background
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 20)); // center, horizontal gap = 50
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 40, 0)); // bottom padding
        buttonPanel.add(restartGame);
        buttonPanel.add(exit);
        this.add(buttonPanel, BorderLayout.SOUTH);
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setFont(new Font("Comic Sans MS", Font.BOLD, 70));
        g.setColor(Color.GREEN);
        FontMetrics fm = g.getFontMetrics();
        int textWidth = fm.stringWidth(this.winnerText);
        g.drawString(this.winnerText, (getWidth() - textWidth) / 2, 150); // Adjust vertical position

        if (this.winnerText.contains("Green")) { // green won, red is loser/hit
            this.red = ResourceManager.getInstance().getImage("vfx/zombie2_hit.png", GameConstants.GAME_SCREEN_WIDTH, GameConstants.GAME_SCREEN_HEIGHT);
        } else if (this.winnerText.contains("Red")) { // red won, green is loser/hit
            this.green = ResourceManager.getInstance().getImage("vfx/zombie1_hit.png", GameConstants.GAME_SCREEN_WIDTH, GameConstants.GAME_SCREEN_HEIGHT);
        }
        if (green == null || red == null) {
            System.err.println("Error: cannot load menu end game images");
            System.exit(-3);
        }
        g.drawImage(this.green, GameConstants.GAME_SCREEN_WIDTH/4, GameConstants.GAME_SCREEN_HEIGHT/4, GameConstants.GENERIC_SIZE*4, GameConstants.GENERIC_SIZE*4, null);
        g.drawImage(this.red, GameConstants.GAME_SCREEN_WIDTH/2, GameConstants.GAME_SCREEN_HEIGHT/4, GameConstants.GENERIC_SIZE*4, GameConstants.GENERIC_SIZE*4, null);
    }

    public void setWinnerText(String text) {
        this.winnerText = text;
        repaint();
    }
}
