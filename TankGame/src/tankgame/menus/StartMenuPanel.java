package tankgame.menus;


import tankgame.Launcher;
import tankgame.factories.ImageFactory;

import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StartMenuPanel extends JPanel {

    private BufferedImage menuBackground;
    private final Launcher launcher;

    // Flyweight pattern : move these resource factories to Resource Manager Singleton TODO
    private static final Map<String, BufferedImage> images = new HashMap<>();
    private static final Map<String, Clip> sounds = new HashMap<>(); // each clip is a thread, sounds and animations are for collisions
    private static final Map<String, List<BufferedImage>> animations = new HashMap<>();
    private static final Map<String, ResourceFactory<?>> factories = new HashMap<>();
    public static void loadSprites(){}
    public static void loadSounds(){}
    public static void loadAnimations(){}
    interface ResourceFactory<T> {
        void load(String path);
    }



    public StartMenuPanel(Launcher launcher) {
        this.launcher = launcher;
        menuBackground = ImageFactory.getImage("title.png");
        if (menuBackground == null) {
            System.err.println("Error: cannot load menu background image (title.png)");
            System.exit(-3);
        }
        this.setBackground(Color.BLACK);
        this.setLayout(null);

        JButton start = new JButton("Start");
        start.setFont(new Font("Courier New", Font.BOLD, 24));
        start.setBounds(150, 300, 150, 50);
        start.addActionListener(actionEvent -> this.launcher.setFrame("game"));

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
        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(this.menuBackground, 0, 0, null);
    }
}
