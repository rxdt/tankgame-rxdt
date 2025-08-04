package zombiegame.menus;

import zombiegame.Launcher;
import zombiegame.game.ResourceManager;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class EndGamePanel extends JPanel {

    private String winnerText = "Game Over"; // default
    private final Launcher launcher;

    public EndGamePanel(Launcher launcher) {
        this.launcher = launcher;
        this.setBackground(Color.BLACK);
        this.setLayout(null);

        JButton start = new JButton("Restart Game");
        start.setFont(new Font("Courier New", Font.BOLD, 24));
        start.setBounds(150, 300, 250, 50);
        start.addActionListener(actionEvent -> {
            this.launcher.getGamePanel().resetGame(); // reset game world
            Thread gameThread = new Thread(this.launcher.getGamePanel());
            gameThread.start();
            ResourceManager.getInstance().stopAllSounds();
            this.launcher.setFrame("game"); // then switch to game view
            ResourceManager.getInstance().playLoopedSound("Plants vs. Zombies - Ultimate Battle.wav");
        });
        JButton exit = new JButton("Exit");
        exit.setFont(new Font("Courier New", Font.BOLD, 24));
        exit.setBounds(150, 400, 250, 50);
        exit.addActionListener((actionEvent -> this.launcher.closeGame()));

        this.add(start);
        this.add(exit);
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // clears the background
        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(null, 0, 0, this.getWidth(), this.getHeight(), null);
        g.setFont(new Font("Comic Sans MS", Font.BOLD, 60));
        g.setColor(Color.CYAN);
        FontMetrics fm = g.getFontMetrics();
        int textWidth = fm.stringWidth(this.winnerText);
        g.drawString(this.winnerText, (getWidth() - textWidth) / 2, 200);
    }
}
