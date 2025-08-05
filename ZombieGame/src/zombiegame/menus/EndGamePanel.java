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
        this.setLayout(new BorderLayout());
        Color neonGreen = new Color(57, 255, 20);

        JButton restartButton = new JButton("Restart Game");
        restartButton.setFont(new Font("Courier New", Font.BOLD, 28));
        restartButton.setForeground(neonGreen);
        restartButton.setFocusPainted(false);
        restartButton.setContentAreaFilled(false);
        restartButton.setOpaque(false);
        restartButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(neonGreen, 2),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        restartButton.setPreferredSize(new Dimension(240, 60));
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
        exitButton.setForeground(neonGreen);
        exitButton.setFocusPainted(false);
        exitButton.setContentAreaFilled(false);
        exitButton.setOpaque(false);
        exitButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(neonGreen, 2),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        exitButton.setPreferredSize(new Dimension(240, 60));
        exitButton.addActionListener(actionEvent -> this.launcher.closeGame());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.add(Box.createVerticalGlue());
        restartButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonPanel.add(restartButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        buttonPanel.add(exitButton);
        buttonPanel.add(Box.createVerticalGlue());

        this.add(buttonPanel, BorderLayout.CENTER);
    }


    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setFont(new Font("Comic Sans MS", Font.BOLD, 70));
        g.setColor(Color.GREEN);
        FontMetrics fm = g.getFontMetrics();
        int textWidth = fm.stringWidth(this.winnerText);
        g.drawString(this.winnerText, (getWidth() - textWidth) / 2, 150); // Adjust vertical position
    }

    public void setWinnerText(String text) {
        this.winnerText = text;
        repaint();
    }
}
