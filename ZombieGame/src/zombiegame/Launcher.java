package zombiegame;

import zombiegame.factories.ResourceFactory;
import zombiegame.game.GameWorld;
import zombiegame.menus.EndGamePanel;
import zombiegame.menus.StartMenuPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;

public class Launcher {
//    ConcurrentMap<Integer,GameWorld> map = new ConcurrentHashMap<Integer, GameWorld>();
    /*
     * Main panel in JFrame, the layout of this panel
     * will be card layout, this will allow us to switch
     * to sub-panels depending on game state.
     */
    private JPanel mainPanel;
    /*
     * game panel is used to show our game to the screen. inside this panel
     * also contains the game loop. This is where out objects are updated and
     * redrawn. This panel will execute its game loop on a separate thread.
     * This is to ensure responsiveness of the GUI. It is also a bad practice to
     * run long-running loops(or tasks) on Java Swing's main thread. This thread is
     * called the event dispatch thread.
     */
    private GameWorld gamePanel;
    /*
     * JFrame used to store our main panel. We will also attach all event
     * listeners to this JFrame.
     */
    private EndGamePanel endPanel;
    private final JFrame jf;
    /*
     * CardLayout is used to manage our sub-panels. This is a layout manager
     * used for our game. It will be attached to the main panel.
     */
    private CardLayout cl;

    public Launcher() {
        this.jf = new JFrame(); // creating a new JFrame object
        this.jf.setTitle("Zombie Wars Game"); // setting the title of the JFrame window.
        // when the GUI is closed, this will also shut down the VM
        this.jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void initUIComponents() {
        ResourceFactory.preloadResources();
        this.mainPanel = new JPanel(); // create a new main panel
        /*
         * start panel will be used to view the start menu. It will contain
         * two buttons start and exit.
         */
        JPanel startPanel = new StartMenuPanel(this); // create a new start panel, hands launcher to subpanel so it can call setFrame
        this.gamePanel = new GameWorld(this); // create a new game panel
        this.gamePanel.InitializeGame(); // initialize game, but DO NOT start game
        this.endPanel = new EndGamePanel(this);
        /*
         * end panel is used to show the end game panel. it will contain
         * two buttons restart and exit.
         */
        cl = new CardLayout(); // creating a new CardLayout Panel, allows us to put panels on top of each other
        this.mainPanel.setLayout(cl); // set the layout of the main panel to our card layout

        startPanel.setPreferredSize(new Dimension(GameConstants.GAME_SCREEN_WIDTH, GameConstants.GAME_SCREEN_HEIGHT));
        this.gamePanel.setPreferredSize(new Dimension(GameConstants.GAME_SCREEN_WIDTH, GameConstants.GAME_SCREEN_HEIGHT));
        this.endPanel.setPreferredSize(new Dimension(GameConstants.GAME_SCREEN_WIDTH, GameConstants.GAME_SCREEN_HEIGHT));

        this.mainPanel.add(startPanel, "start"); // add the start panel to the main panel
        this.mainPanel.add(gamePanel, "game"); // add the game panel to the main panel
        this.mainPanel.add(endPanel, "end"); // add the end game panel to the main panel

        this.jf.add(mainPanel); // add the main panel to the JFrame
        this.jf.setResizable(false); // make the JFrame not resizable
        this.jf.pack();
        this.setFrame("start"); // set the current panel to start panel
    }

    /**
     * Changes current frame in card layout
     * @param type which frame to switch the card layout to
     */
    public void setFrame(String type) {
        this.jf.setVisible(false); // hide the JFrame
        // these width/height resolutions will change to account for minimap
        switch (type) {
            case "start" -> {
                this.jf.setSize(GameConstants.GAME_SCREEN_WIDTH, GameConstants.GAME_SCREEN_HEIGHT);
                this.cl.show(mainPanel, type); // change current panel shown on main panel tp the panel denoted by type.
                this.jf.setVisible(true); // show the JFrame
            }
            case "game" -> {
                this.jf.setSize(GameConstants.GAME_SCREEN_WIDTH, GameConstants.GAME_SCREEN_HEIGHT);
                this.cl.show(mainPanel, type);         // show GameWorld panel
                this.jf.setVisible(true);              // reveal the window again
                this.gamePanel.requestFocusInWindow(); // give GameWorld panel keyboard focus
                (new Thread(this.gamePanel)).start();  // start game loop
            }
            case "end" -> {
                this.jf.setSize(GameConstants.GAME_SCREEN_WIDTH, GameConstants.GAME_SCREEN_HEIGHT);
                this.cl.show(mainPanel, type);
                this.jf.setVisible(true);
            }
        }
        this.jf.setLocationRelativeTo(null); // center on screen
    }

    public GameWorld getGamePanel() {
        return this.gamePanel;
    }

    /**
     * close game and send signal to kill JVM as well.
     */
    public void closeGame() {
        this.jf.dispatchEvent(new WindowEvent(this.jf, WindowEvent.WINDOW_CLOSING));
    }

    public static void main(String[] args) {
        (new Launcher()).initUIComponents();
    }
}