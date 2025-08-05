package zombiegame.menus;


import zombiegame.GameConstants;
import zombiegame.Launcher;
import zombiegame.game.ResourceManager;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class StartMenuPanel extends JPanel {

    private BufferedImage menuBackground;
    private final Launcher launcher;

    public StartMenuPanel(Launcher launcher) {
        this.launcher = launcher;
        ResourceManager.getInstance().playLoopedSound("plants-vs-zombies-halloween-spooky.wav");
        menuBackground = ResourceManager.getInstance().getImage("vfx/title.png", GameConstants.GAME_SCREEN_WIDTH, GameConstants.GAME_SCREEN_HEIGHT);
        if (menuBackground == null) {
            System.err.println("Error: cannot load menu background image (title.png)");
            System.exit(-3);
        }
        this.setBackground(Color.BLACK);
        this.setLayout(new BorderLayout());
        Color neonGreen = new Color(57, 255, 20);

        JButton start = new JButton("Start");
        start.setFont(new Font("Courier New", Font.BOLD, 24));
        start.setForeground(neonGreen);
        start.setFocusPainted(false);
        start.setContentAreaFilled(false);
        start.setOpaque(false);
        start.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(neonGreen, 2),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)));
        start.addActionListener(actionEvent -> {
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
        buttonPanel.add(start);
        buttonPanel.add(exit);
        this.add(buttonPanel, BorderLayout.SOUTH);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(this.menuBackground, 0, 0, getWidth(), getHeight(), null);
    }
}
