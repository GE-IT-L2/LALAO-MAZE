import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class MazeInterface extends JFrame {
    private static final int TILE_SIZE = 30;
    private static final int MAZE_WIDTH = 20;
    private static final int MAZE_HEIGHT = 15;
    
    private char[][] maze = new char[MAZE_HEIGHT][MAZE_WIDTH];
    private int playerX = 1, playerY = 1;
    private int level = 1;
    private int timeLeft = 60;
    private boolean gameWon = false;
    private Timer timer;
    
    public MazeInterface() {
        initStaticMaze();
        initUI();
        startTimer();
    }
    
    private void initStaticMaze() {
        // Labyrinthe fixe simple - modifiable facilement
        char[][] staticMaze = {
            "####################".toCharArray(),
            "#P               E#".toCharArray(),
            "# # ### # # ### # #".toCharArray(),
            "#   #   #   #   #  #".toCharArray(),
            "### ### ### ### ###".toCharArray(),
            "#     #     #      #".toCharArray(),
            "# ### # ### ### ###".toCharArray(),
            "# #   # #   # #   #".toCharArray(),
            "### # ### ### # ###".toCharArray(),
            "#   #   #       #  #".toCharArray(),
            "# ### ### ### ### ##".toCharArray(),
            "#                 ##".toCharArray(),
            "## ### ### ### ### #".toCharArray(),
            "#                 P#".toCharArray(),
            "####################".toCharArray()
        };
        
        for (int y = 0; y < MAZE_HEIGHT; y++) {
            System.arraycopy(staticMaze[y], 0, maze[y], 0, MAZE_WIDTH);
        }
    }
    
    private void initUI() {
        setTitle("Maze Interface - Niveau " + level + " | " + timeLeft + "s");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                movePlayer(e.getKeyCode());
            }
        });
        
        setFocusable(true);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    private void movePlayer(int keyCode) {
        int newX = playerX, newY = playerY;
        
        switch (keyCode) {
            case KeyEvent.VK_UP: newY--; break;
            case KeyEvent.VK_DOWN: newY++; break;
            case KeyEvent.VK_LEFT: newX--; break;
            case KeyEvent.VK_RIGHT: newX++; break;
        }
        
        if (newX > 0 && newX < MAZE_WIDTH-1 && newY > 0 && newY < MAZE_HEIGHT-1 && maze[newY][newX] != '#') {
            maze[playerY][playerX] = ' ';
            playerX = newX;
            playerY = newY;
            maze[playerY][playerX] = 'P';
            
            checkWin();
            repaint();
        }
    }
    
    private void checkWin() {
        if (maze[playerY][playerX] == 'E') {
            gameWon = true;
            timer.stop();
        }
    }
    
    private void startTimer() {
        timer = new Timer(1000, e -> {
            timeLeft--;
            setTitle("Maze Interface - Niveau " + level + " | " + timeLeft + "s");
            repaint();
        });
        timer.start();
    }
    
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        
        // Fond
        g.setColor(Color.DARK_GRAY);
        g.fillRect(0, 0, getWidth(), getHeight());
        
        // Grille du labyrinthe
        for (int y = 0; y < MAZE_HEIGHT; y++) {
            for (int x = 0; x < MAZE_WIDTH; x++) {
                int px = x * TILE_SIZE;
                int py = y * TILE_SIZE + 50;
                
                if (maze[y][x] == '#') {
                    g.setColor(Color.GRAY);
                    g.fillRect(px, py, TILE_SIZE, TILE_SIZE);
                    g.setColor(Color.BLACK);
                    g.drawRect(px, py, TILE_SIZE, TILE_SIZE);
                } else if (maze[y][x] == 'P') {
                    g.setColor(Color.BLUE);
                    g.fillOval(px+5, py+5, TILE_SIZE-10, TILE_SIZE-10);
                } else if (maze[y][x] == 'E') {
                    g.setColor(Color.GREEN);
                    g.fillRect(px+8, py+8, TILE_SIZE-16, TILE_SIZE-16);
                } else {
                    g.setColor(Color.BLACK);
                    g.fillRect(px, py, TILE_SIZE, TILE_SIZE);
                }
            }
        }
        
        // Interface
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 16));
        g.drawString("NIVEAU: " + level + "  TEMPS: " + timeLeft + "s", 10, 25);
        g.drawString("← → ↑ ↓ pour jouer", 10, 40);
        
        if (gameWon) {
            g.setColor(Color.YELLOW);
            g.setFont(new Font("Arial", Font.BOLD, 24));
            g.drawString("GAGNÉ! 🎉", 150, 200);
        }
    }
    
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(MAZE_WIDTH * TILE_SIZE + 20, MAZE_HEIGHT * TILE_SIZE + 80);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(MazeInterface::new);
    }
}