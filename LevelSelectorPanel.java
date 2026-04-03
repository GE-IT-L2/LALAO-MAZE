import javax.swing.*;
import java.awt.*;

public class LevelSelectorPanel extends JPanel {
    private JFrame mainFrame;
    private int maxUnlockedLevel = 1; // Le joueur commence au niveau 1
    private JPanel gridPanel;

    public LevelSelectorPanel(JFrame frame) {
        this.mainFrame = frame;
        setPreferredSize(new Dimension(600, 600));
        setBackground(new Color(15, 23, 42)); 
        setLayout(null);

        // Titre "LEVEL SELECTION"
        JLabel title = new JLabel("LEVEL SELECTION", SwingConstants.CENTER);
        title.setFont(new Font("Impact", Font.PLAIN, 32));
        title.setForeground(Color.WHITE);
        title.setBounds(150, 30, 300, 40);
        add(title);

        // Panneau pour la grille des boutons
        gridPanel = new JPanel();
        gridPanel.setBounds(100, 100, 400, 300);
        gridPanel.setOpaque(false);
        gridPanel.setLayout(new GridLayout(3, 4, 15, 15)); // 3 lignes, 4 colonnes
        add(gridPanel);

        // On génère les boutons une première fois
        updateLevelButtons();

        // Bouton BACK
        JButton backBtn = new JButton("BACK");
        backBtn.setBounds(225, 450, 150, 40);
        backBtn.setBackground(new Color(30, 41, 59));
        backBtn.setForeground(Color.WHITE);
        backBtn.addActionListener(e -> {
            CardLayout cl = (CardLayout) mainFrame.getContentPane().getLayout();
            cl.show(mainFrame.getContentPane(), "MENU");
        });
        add(backBtn);
    }

    // Fonction pour rafraîchir les cadenas quand on gagne un niveau
    public void updateLevelButtons() {
        gridPanel.removeAll(); // On vide la grille

        for (int i = 1; i <= 12; i++) {
            final int levelNum = i;
            JButton levelBtn = new JButton(String.valueOf(levelNum));
            levelBtn.setFont(new Font("Arial", Font.BOLD, 20));
            
            // VERIFICATION : Si le niveau dépasse le max débloqué, on met un cadenas
            if (levelNum > maxUnlockedLevel) {
                levelBtn.setText("🔒");
                levelBtn.setEnabled(false); // Impossible de cliquer
                levelBtn.setBackground(new Color(30, 41, 59));
            } else {
                levelBtn.setBackground(new Color(56, 189, 248)); // Bleu clair débloqué
                levelBtn.setForeground(Color.WHITE);
                levelBtn.setEnabled(true);
                
                // Clic sur un niveau débloqué
                levelBtn.addActionListener(e -> {
                    // On dit au jeu quel niveau charger
                    LalaoMaze app = (LalaoMaze) mainFrame;
                    app.gamePanel.loadLevel(levelNum);
                    
                    CardLayout cl = (CardLayout) mainFrame.getContentPane().getLayout();
                    cl.show(mainFrame.getContentPane(), "GAME");
                });
            }
            gridPanel.add(levelBtn);
        }
        gridPanel.revalidate();
        gridPanel.repaint();
    }

    // Permet de débloquer le niveau suivant
    public void unlockNextLevel() {
        if (maxUnlockedLevel < 12) {
            maxUnlockedLevel++;
            updateLevelButtons(); // On recalcule l'affichage pour enlever le cadenas
        }
    }
}