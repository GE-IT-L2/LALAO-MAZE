import javax.swing.*;
import java.awt.*;
import java.io.File;
import javax.imageio.ImageIO;

public class MenuPanel extends JPanel {
    private Image brainImage;
    private JButton playButton;
    private JFrame mainFrame; // Référence pour changer d'écran

    public MenuPanel(JFrame frame) {
        this.mainFrame = frame;
        
        // Configuration du panneau
        setPreferredSize(new Dimension(600, 600)); // Même taille que le labyrinthe
        setBackground(Color.WHITE);
        setLayout(null); // Placement libre des composants (absolute layout)

        // 1. Chargement de ton image de cerveau
        try {
            brainImage = ImageIO.read(new File("cerveau.png"));
        } catch (Exception e) {
            System.out.println("⚠️ Image 'cerveau.png' introuvable. Un carré rose sera dessiné à la place.");
        }

        // 2. Création du bouton Play (en bas)
        playButton = new JButton("▶");
        playButton.setFont(new Font("Arial", Font.BOLD, 40));
        playButton.setBounds(250, 480, 100, 60); // Centré en bas
        playButton.setFocusPainted(false);
        playButton.setBackground(Color.WHITE);
        
        // Événement : Quand on clique, on va vers le sélecteur de niveaux
        playButton.addActionListener(e -> {
            CardLayout cl = (CardLayout) mainFrame.getContentPane().getLayout();
            cl.show(mainFrame.getContentPane(), "SELECTOR"); // 🔄 Changé ici !
        });

        // Événement : Quand on clique, on bascule sur l'écran de jeu
        playButton.addActionListener(e -> {
            CardLayout cl = (CardLayout) mainFrame.getContentPane().getLayout();
            cl.show(mainFrame.getContentPane(), "GAME");
        });

        add(playButton);

        // 🌟 NOUVEAU : Vrais boutons pour la difficulté
        JButton easyBtn = new JButton("EASY");
        easyBtn.setBounds(180, 430, 75, 25);
        easyBtn.setBackground(new Color(100, 100, 100)); // Foncé par défaut (actif)
        easyBtn.setForeground(Color.WHITE);

        JButton mediumBtn = new JButton("MEDIUM");
        mediumBtn.setBounds(260, 430, 85, 25);
        mediumBtn.setBackground(new Color(230, 220, 255));
        mediumBtn.setForeground(new Color(75, 0, 130));

        JButton hardBtn = new JButton("HARD");
        hardBtn.setBounds(350, 430, 75, 25);
        hardBtn.setBackground(new Color(230, 220, 255));
        hardBtn.setForeground(new Color(75, 0, 130));

        // Événements de clic pour changer la couleur et la variable
        LalaoMaze app = (LalaoMaze) mainFrame;

        easyBtn.addActionListener(e -> {
            app.gameDifficulty = 0;
            easyBtn.setBackground(new Color(100, 100, 100)); easyBtn.setForeground(Color.WHITE);
            mediumBtn.setBackground(new Color(230, 220, 255)); mediumBtn.setForeground(new Color(75, 0, 130));
            hardBtn.setBackground(new Color(230, 220, 255)); hardBtn.setForeground(new Color(75, 0, 130));
        });

        mediumBtn.addActionListener(e -> {
            app.gameDifficulty = 1;
            easyBtn.setBackground(new Color(230, 220, 255)); easyBtn.setForeground(new Color(75, 0, 130));
            mediumBtn.setBackground(new Color(100, 100, 100)); mediumBtn.setForeground(Color.WHITE);
            hardBtn.setBackground(new Color(230, 220, 255)); hardBtn.setForeground(new Color(75, 0, 130));
        });

        hardBtn.addActionListener(e -> {
            app.gameDifficulty = 2;
            easyBtn.setBackground(new Color(230, 220, 255)); easyBtn.setForeground(new Color(75, 0, 130));
            mediumBtn.setBackground(new Color(230, 220, 255)); mediumBtn.setForeground(new Color(75, 0, 130));
            hardBtn.setBackground(new Color(100, 100, 100)); hardBtn.setForeground(Color.WHITE);
        });

        add(easyBtn);
        add(mediumBtn);
        add(hardBtn);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Dessin du quadrillage en pointillés du fond (comme sur ta maquette)
        g2d.setColor(new Color(220, 220, 220));
        for (int x = 0; x < getWidth(); x += 20) {
            for (int y = 0; y < getHeight(); y += 20) {
                g2d.fillOval(x, y, 3, 3);
            }
        }

        // Dessin de la bannière du titre "MAZE IQ GAME"
        g2d.setColor(new Color(118, 255, 3)); // Vert flashy
        g2d.fillRoundRect(150, 50, 300, 60, 15, 15);
        g2d.setColor(new Color(0, 150, 0));
        g2d.drawRoundRect(150, 50, 300, 60, 15, 15);

        g2d.setColor(new Color(75, 0, 130)); // Texte Indigo/Violet
        g2d.setFont(new Font("Arial", Font.BOLD, 22));
        g2d.drawString("MAZE IQ", 250, 80);
        g2d.drawString("GAME", 265, 100);

        // Dessin de l'image du cerveau
        if (brainImage != null) {
            // On adapte l'image au centre
            g2d.drawImage(brainImage, 150, 140, 300, 300, null);
        } else {
            // Carré de secours si l'image n'est pas trouvée
            g2d.setColor(Color.PINK);
            g2d.fillRect(150, 140, 300, 300);
            g2d.setColor(Color.BLACK);
            g2d.drawString("Mettre 'cerveau.png' ici", 180, 280);
        }

        // Petits boutons de difficulté décoratifs (non cliquables pour l'instant)
        // drawDifficultyPill(g2d, "EASY", 180, 430, true);
        // drawDifficultyPill(g2d, "MEDIUM", 260, 430, false);
        // drawDifficultyPill(g2d, "HARD", 350, 430, false);
    }

    // Méthode utilitaire pour dessiner les pilules de difficulté
    // private void drawDifficultyPill(Graphics2D g, String text, int x, int y, boolean active) {
    //     g.setFont(new Font("Arial", Font.BOLD, 12));
    //     if (active) {
    //         g.setColor(new Color(100, 100, 100)); // Foncé pour l'actif
    //         g.fillRoundRect(x, y, 70, 25, 10, 10);
    //         g.setColor(Color.WHITE);
    //     } else {
    //         g.setColor(new Color(230, 220, 255)); // Violet clair
    //         g.fillRoundRect(x, y, 70, 25, 10, 10);
    //         g.setColor(new Color(75, 0, 130));
    //     }
    //     g.drawString(text, x + 15, y + 17);
    // }
}
