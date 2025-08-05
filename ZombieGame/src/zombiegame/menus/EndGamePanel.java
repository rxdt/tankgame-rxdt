package zombiegame.menus;

import zombiegame.Launcher;
import zombiegame.game.ResourceManager;

import javax.swing.*;
import java.awt.*;

public class EndGamePanel extends JPanel {

    private String winnerText = "Game Over"; // default
    private final Launcher launcher;

    public EndGamePanel(Launcher launcher) {
        this.launcher = launcher;
        this.setBackground(Color.BLACK);
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JButton restartButton = new JButton("Restart Game");
        restartButton.setFont(new Font("Courier New", Font.BOLD, 28));
        restartButton.setPreferredSize(new Dimension(300, 60));
        restartButton.setMaximumSize(new Dimension(300, 60)); // restrict growth
        restartButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        restartButton.addActionListener(actionEvent -> {
            this.launcher.getGamePanel().resetGame(); // reset game world
            Thread gameThread = new Thread(this.launcher.getGamePanel());
            gameThread.start();
            ResourceManager.getInstance().stopAllSounds();
            this.launcher.setFrame("game"); // switch to game view
            ResourceManager.getInstance().playLoopedSound("Plants vs. Zombies - Ultimate Battle.wav");
        });

        JButton exitButton = new JButton("Exit");
        exitButton.setFont(new Font("Courier New", Font.BOLD, 28));
        exitButton.setPreferredSize(new Dimension(300, 60));
        exitButton.setMaximumSize(new Dimension(300, 60));
        exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        exitButton.addActionListener(actionEvent -> this.launcher.closeGame());

        this.add(Box.createVerticalGlue());
        this.add(restartButton);
        this.add(Box.createRigidArea(new Dimension(0, 30)));
        this.add(exitButton);
        this.add(Box.createVerticalGlue());
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setFont(new Font("Comic Sans MS", Font.BOLD, 60));
        g.setColor(Color.CYAN);
        FontMetrics fm = g.getFontMetrics();
        int textWidth = fm.stringWidth(this.winnerText);
        g.drawString(this.winnerText, (getWidth() - textWidth) / 2, 150); // Adjust vertical position
    }

    public void setWinnerText(String text) {
        this.winnerText = text;
        repaint();
    }
}
