import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList; // Essentiel pour gérer plusieurs monstres !

public class GamePanel extends JPanel {
    private Map map;
    
    // On remplace "Monster monster" par une liste de monstres
    private ArrayList<Monster> monsters = new ArrayList<>();
    
    // Coordonnées de départ du joueur
    private int playerX = 1;
    private int playerY = 1;

    // Variables du temps
    private int timeLeft = 45; 
    private Timer gameLoopTimer;
    private Timer countdownTimer;
    private boolean isGameOver = false;
    private int currentLevel = 1;

    // Référence vers la fenêtre principale
    private LalaoMaze mainFrame; 

    public GamePanel(LalaoMaze frame) {
        this.mainFrame = frame; // Mémorise la fenêtre principale
        
        map = new Map();

        // Dimensions : 15 cases * 40 pixels = 600x600 pixels
        setPreferredSize(new Dimension(map.gridWidth * map.tileSize, map.gridHeight * map.tileSize));
        setBackground(Color.WHITE);
        setFocusable(true);

        // Écouteur pour les déplacements du joueur
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (isGameOver) return;

                int key = e.getKeyCode();
                int nextX = playerX;
                int nextY = playerY;

                if (key == KeyEvent.VK_UP || key == KeyEvent.VK_Z) nextY--;
                if (key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S) nextY++;
                if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_Q) nextX--;
                if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) nextX++;

                if (map.isWalkable(nextX, nextY)) {
                    playerX = nextX;
                    playerY = nextY;
                }
                checkRules();
            }
        });

        // Boucle du jeu (Génère le rendu graphique et fait agir l'IA)
        gameLoopTimer = new Timer(1000 / 60, e -> {
            if (!isGameOver) {
                // On fait bouger TOUS les monstres de la liste
                for (Monster m : monsters) {
                    m.update(playerX, playerY, map);
                }
                checkRules();
                repaint();
            }
        });
        gameLoopTimer.start();

        // Minuteur du niveau
        countdownTimer = new Timer(1000, e -> {
            if (!isGameOver) {
                timeLeft--;
                if (timeLeft <= 0) {
                    endGame("Temps écoulé !", false);
                }
            }
        });
        countdownTimer.start();

        // Focus pour que le clavier fonctionne directement
        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentShown(java.awt.event.ComponentEvent e) {
                requestFocusInWindow();
            }
        });
    }

    // MÉTHODE LOADLEVEL (S'ADAPTE À LA DIFFICULTÉ)
    public void loadLevel(int levelNumber) {
        this.currentLevel = levelNumber;
        playerX = 1;
        playerY = 1;
        isGameOver = false;

        // On récupère la difficulté choisie sur l'écran d'accueil
        int diff = mainFrame.gameDifficulty;

        // ⏱️ 1. Gestion du minuteur
        if (diff == 0) {
            timeLeft = 999; // Mode Easy : On donne un temps géant (infini)
        } else {
            timeLeft = 45 - (levelNumber * 2); // Mode Medium et Hard
            if (timeLeft < 15) timeLeft = 15; // Minimum 15 secondes
        }

        // 👾 2. Gestion des monstres
        monsters.clear(); // On supprime les monstres du niveau précédent
        
        if (diff == 1) {
            // Mode Medium : Toujours 1 seul monstre
            monsters.add(new Monster(13, 1));
        } else if (diff == 2) {
            // Mode Hard : Le nombre de monstres augmente avec le niveau !
            for (int i = 0; i < levelNumber; i++) {
                // On décale leur spawn pour éviter qu'ils se superposent
                int spawnX = 13 - (i % 3);
                int spawnY = 1 + (i / 3);
                monsters.add(new Monster(spawnX, spawnY));
            }
        }
        // Note : Si diff == 0 (Easy), la liste reste vide ! Pas de monstres.

        gameLoopTimer.start();
        countdownTimer.start();
        repaint();
    }

    // VÉRIFICATION DES RÈGLES
    private void checkRules() {
        // Condition de défaite : On boucle sur tous les monstres
        for (Monster m : monsters) {
            if (playerX == m.x && playerY == m.y) {
                endGame("Le monstre vous a dévoré !", false);
                return; // On arrête la méthode immédiatement
            }
        }

        // Condition de victoire
        if (map.maze[playerY][playerX] == 2) {
            endGame("Victoire !", true);
        }
    }

    private void endGame(String message, boolean victory) {
        isGameOver = true;
        gameLoopTimer.stop();
        countdownTimer.stop();
        repaint();

        Color msgColor = victory ? Color.GREEN : Color.RED;

        // Met à jour les textes sur le panneau de fin
        mainFrame.endGamePanel.setMessage(message, msgColor, timeLeft);

        // Affiche l'écran de fin
        CardLayout cl = (CardLayout) mainFrame.getContentPane().getLayout();
        cl.show(mainFrame.getContentPane(), "END_GAME");
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    // 🔄 MÉTHODE DE RESTART (Celle qui manquait et provoquait ton bug !)
    public void restartLevel() {
        playerX = 1;
        playerY = 1;
        isGameOver = false;

        // On recharge la configuration du niveau actuel (temps et monstres)
        loadLevel(currentLevel);
        
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Dessine le labyrinthe
        map.draw(g);

        // Dessine TOUS les monstres présents dans la liste
        for (Monster m : monsters) {
            m.draw(g, map.tileSize);
        }

        // Dessine le joueur (Rond bleu)
        g.setColor(Color.BLUE);
        g.fillOval(playerX * map.tileSize + 5, playerY * map.tileSize + 5, map.tileSize - 10, map.tileSize - 10);

        // Affichage du minuteur en haut de l'écran
        int diff = mainFrame.gameDifficulty;
        if (diff != 0) { // On n'affiche pas le texte si on est en mode Easy
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.drawString("Temps restant : " + timeLeft + "s", 15, 30);
        }
    }
}