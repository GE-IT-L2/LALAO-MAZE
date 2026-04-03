import javax.swing.*;
import java.awt.*;

public class LalaoMaze extends JFrame {
    public MenuPanel menuPanel;
    public LevelSelectorPanel selectorPanel;
    public GamePanel gamePanel;
    public EndGamePanel endGamePanel;

    // 🌟 NOUVEAU : Variable pour stocker la difficulté (0: Easy, 1: Medium, 2: Hard)
    public int gameDifficulty = 0; 

    public LalaoMaze() {
        setTitle("LALAO-MAZE - Projet L2");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        CardLayout cardLayout = new CardLayout();
        setLayout(cardLayout);

        // Instanciation des écrans
        menuPanel = new MenuPanel(this);
        selectorPanel = new LevelSelectorPanel(this);
        gamePanel = new GamePanel(this);
        endGamePanel = new EndGamePanel(this);

        add(menuPanel, "MENU");
        add(selectorPanel, "SELECTOR");
        add(gamePanel, "GAME");
        add(endGamePanel, "END_GAME");

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LalaoMaze());
    }
}