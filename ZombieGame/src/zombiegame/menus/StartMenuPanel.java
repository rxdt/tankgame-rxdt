package zombiegame.menus;

import zombiegame.GameConstants;
import zombiegame.Launcher;
import zombiegame.game.resources.ResourceManager;

import java.awt.*;
import java.awt.image.BufferedImage;

public class StartMenuPanel extends MenuPanel {

    private BufferedImage menuBackground;
    private Launcher launcher;

    public StartMenuPanel(Launcher launcher) {
        super(launcher, "Plants vs. Zombies - Moongrains.wav", GameConstants.START_GAME);
        this.launcher = launcher;
        this.menuBackground = ResourceManager.getInstance().getImage("vfx/title.png",
                GameConstants.GAME_SCREEN_WIDTH, GameConstants.GAME_SCREEN_HEIGHT);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(this.menuBackground, 0, 0, getWidth(), getHeight(), null);
    }

    @Override
    protected void onStartGamePressed() {
        ResourceManager.getInstance().stopAllSounds();
        launcher.setFrame("game");
        ResourceManager.getInstance().playLoopedSound("Plants vs. Zombies - Ultimate Battle.wav");
    }
}
