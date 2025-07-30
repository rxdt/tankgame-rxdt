package tankgame.menus;

import tankgame.GameConstants;
import tankgame.Launcher;
import tankgame.factories.ImageFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class EndGamePanel extends JPanel {

    private BufferedImage menuBackground;
    private final Launcher launcher;

    public EndGamePanel(Launcher launcher) {
        this.launcher = launcher;
        menuBackground = ImageFactory.getImage("title.png", GameConstants.END_MENU_SCREEN_WIDTH, GameConstants.END_MENU_SCREEN_HEIGHT);
        if (menuBackground == null) {
            System.err.println("Error: cannot load menu background image (title.png)");
            System.exit(-3);
        }
        this.setBackground(Color.BLACK);
        this.setLayout(null);

        JButton start = new JButton("Restart Game");
        start.setFont(new Font("Courier New", Font.BOLD, 24));
        start.setBounds(150, 300, 250, 50);
        start.addActionListener(actionEvent -> {
            this.launcher.getGamePanel().resetGame(); // reset game world
            this.launcher.setFrame("game");           // then switch to game view
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
        g2.drawImage(this.menuBackground, 0, 0, this.getWidth(), this.getHeight(), null);
    }
}
