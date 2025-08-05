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
        this.setLayout(null);

        JButton start = new JButton("Start");
        start.setFont(new Font("Courier New", Font.BOLD, 24));
        start.setBounds(150, 300, 150, 50);
        start.addActionListener(actionEvent -> {
            ResourceManager.getInstance().stopAllSounds();
            this.launcher.setFrame("game");
            ResourceManager.getInstance().playLoopedSound("Plants vs. Zombies - Ultimate Battle.wav");
        });

        JButton exit = new JButton("Exit");
        exit.setSize(new Dimension(200, 100));
        exit.setFont(new Font("Courier New", Font.BOLD, 24));
        exit.setBounds(150, 400, 150, 50);
        exit.addActionListener((actionEvent -> this.launcher.closeGame()));

        this.add(start);
        this.add(exit);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(this.menuBackground, 0, 0, getWidth(), getHeight(), null);
    }
}
