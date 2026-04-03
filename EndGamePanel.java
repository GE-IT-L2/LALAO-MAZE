import javax.swing.*;
import java.awt.*;
import java.io.File;
import javax.imageio.ImageIO;

public class EndGamePanel extends JPanel {
    private JFrame mainFrame;
    private JLabel messageLabel;
    private JLabel scoreLabel;
    private Image retryImg, replayImg, continueImg, homeImg;

    public EndGamePanel(JFrame frame) {
        this.mainFrame = frame;
        setPreferredSize(new Dimension(600, 600)); // Même taille que le jeu
        setBackground(new Color(0, 0, 0, 200)); // Fond semi-transparent noir
        setLayout(null); // Placement absolu

        // 1. Chargement des images des boutons (assure-toi que les fichiers existent !)
        try {
            retryImg = ImageIO.read(new File("retry_btn.png"));
            replayImg = ImageIO.read(new File("replay_btn.png"));
            continueImg = ImageIO.read(new File("continue_btn.png"));
            // homeImg = ImageIO.read(new File("home_btn.png")); // Décommente quand tu as l'image
        } catch (Exception e) {
            System.out.println("⚠️ Une ou plusieurs images de boutons sont introuvables. Des boutons texte seront utilisés.");
        }

        // 2. Labels pour le message et le score
        messageLabel = new JLabel("VICTOIRE !", SwingConstants.CENTER);
        messageLabel.setFont(new Font("Arial", Font.BOLD, 40));
        messageLabel.setForeground(Color.GREEN);
        messageLabel.setBounds(100, 100, 400, 50);
        add(messageLabel);

        scoreLabel = new JLabel("Temps restant : --s", SwingConstants.CENTER);
        scoreLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        scoreLabel.setForeground(Color.WHITE);
        scoreLabel.setBounds(100, 160, 400, 30);
        add(scoreLabel);

        // 3. Création des boutons graphiques
        int btnWidth = 200; // Ajuste selon la taille réelle de tes images
        int btnHeight = 60;  // Ajuste selon la taille réelle de tes images
        int centerX = (600 - btnWidth) / 2;

        // // Bouton CONTINUE
        // JButton continueBtn = createGraphicButton(continueImg, "CONTINUE");
        // continueBtn.setBounds(centerX, 250, btnWidth, btnHeight);
        // continueBtn.addActionListener(e -> {
        //     // Logique pour passer au niveau suivant (à implémenter plus tard)
        //     JOptionPane.showMessageDialog(this, "Niveau suivant bientôt disponible !");
        // });
        // add(continueBtn);
        // Bouton CONTINUE
        JButton continueBtn = createGraphicButton(continueImg, "CONTINUE");
        continueBtn.setBounds(centerX, 250, btnWidth, btnHeight);
        continueBtn.addActionListener(e -> {
            LalaoMaze app = (LalaoMaze) mainFrame;
            
            // 1. On débloque le niveau suivant dans le sélecteur
            app.selectorPanel.unlockNextLevel();
            
            // 2. On récupère le numéro du niveau actuel et on fait +1
            int nextLevel = app.gamePanel.getCurrentLevel() + 1;
            
            if (nextLevel <= 12) {
                // 3. On charge le niveau suivant
                app.gamePanel.loadLevel(nextLevel);
                
                // 4. On rebascule sur l'écran de jeu
                CardLayout cl = (CardLayout) mainFrame.getContentPane().getLayout();
                cl.show(mainFrame.getContentPane(), "GAME");
            } else {
                JOptionPane.showMessageDialog(this, "Félicitations ! Vous avez fini tous les niveaux !");
                CardLayout cl = (CardLayout) mainFrame.getContentPane().getLayout();
                cl.show(mainFrame.getContentPane(), "MENU");
            }
        });
        add(continueBtn);
        
        // Bouton REPLAY (ou RETRY, selon si c'est une victoire ou défaite)
        JButton replayBtn = createGraphicButton(replayImg, "REPLAY");
        replayBtn.setBounds(centerX, 330, btnWidth, btnHeight);
        replayBtn.addActionListener(e -> {
            // Logique pour recommencer le niveau actuel (à implémenter dans GamePanel)
            ((GamePanel)mainFrame.getContentPane().getComponent(2)).restartLevel(); // Hack moche pour l'instant
            CardLayout cl = (CardLayout) mainFrame.getContentPane().getLayout();
            cl.show(mainFrame.getContentPane(), "GAME");
        });
        add(replayBtn);

        // Bouton HOME vers le menu principal
        JButton homeBtn = createGraphicButton(homeImg, "HOME"); // Utilise une image si tu en as une, sinon texte
        if (homeImg == null) {
             homeBtn.setBackground(new Color(70, 70, 70));
             homeBtn.setForeground(Color.WHITE);
        }
        homeBtn.setBounds(centerX, 410, btnWidth, btnHeight);
        homeBtn.addActionListener(e -> {
            CardLayout cl = (CardLayout) mainFrame.getContentPane().getLayout();
            cl.show(mainFrame.getContentPane(), "MENU");
        });
        add(homeBtn);
    }

    // Méthode pour mettre à jour le message et le score avant d'afficher le panneau
    public void setMessage(String message, Color color, int score) {
        messageLabel.setText(message);
        messageLabel.setForeground(color);
        scoreLabel.setText("Temps restant : " + score + "s");
    }

    // Méthode utilitaire pour créer un JButton avec une image ou du texte par défaut
    private JButton createGraphicButton(Image img, String fallbackText) {
        JButton btn;
        if (img != null) {
            btn = new JButton(new ImageIcon(img));
            btn.setBorderPainted(false);
            btn.setContentAreaFilled(false);
            btn.setFocusPainted(false);
        } else {
            btn = new JButton(fallbackText);
            btn.setFont(new Font("Arial", Font.BOLD, 20));
        }
        return btn;
    }
}