package zombiegame.menus;

import zombiegame.GameConstants;
import zombiegame.Launcher;
import zombiegame.game.resources.ResourceManager;

import javax.swing.*;
import java.awt.*;

public abstract class MenuPanel extends JPanel {
    private final Launcher launcher;
    private String endGameText = GameConstants.EXIT;
    Color neonGreen = new Color(57, 255, 20);

    public MenuPanel(Launcher launcher, String songPath, String startGameText) {
        this.launcher = launcher;
        ResourceManager.getInstance().stopAllSounds();
        ResourceManager.getInstance().playLoopedSound(songPath);
        this.setBackground(Color.BLACK);
        this.setLayout(new BorderLayout());

        JButton startGame = new JButton(startGameText);
        styleButton(startGame);
        startGame.addActionListener(e -> onStartGamePressed());

        JButton exit = new JButton(endGameText);
        styleButton(exit);
        exit.addActionListener((actionEvent -> this.launcher.closeGame()));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false); // make transparent to show background
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 20)); // center, horizontal gap = 50
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 40, 0)); // bottom padding
        buttonPanel.add(startGame);
        buttonPanel.add(exit);
        this.add(buttonPanel, BorderLayout.SOUTH);
    }

    private void styleButton(JButton button) {
        button.setFont(new Font("Courier New", Font.BOLD, 24));
        button.setForeground(neonGreen);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(neonGreen, 2),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)));
    }

    protected abstract void onStartGamePressed();
}
