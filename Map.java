import java.awt.*;

public class Map {
    public final int tileSize = 40; // Chaque case fera 40x40 pixels
    public final int gridWidth = 15;
    public final int gridHeight = 15;

    // 0 = Chemin libre, 1 = Mur infranchissable, 2 = Sortie
    public final int[][] maze = {
        {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
        {1,0,0,0,1,0,0,0,0,0,1,0,0,0,1},
        {1,1,1,0,1,0,1,1,1,0,1,1,1,0,1},
        {1,0,0,0,0,0,0,0,1,0,0,0,1,0,1},
        {1,0,1,1,1,1,1,0,1,1,1,0,1,0,1},
        {1,0,1,0,0,0,1,0,0,0,1,0,1,0,1},
        {1,0,1,1,1,0,1,1,1,0,1,0,0,0,1},
        {1,0,0,0,1,0,0,0,1,0,1,1,1,1,1},
        {1,1,1,0,1,1,1,0,1,0,0,0,0,0,1},
        {1,0,0,0,0,0,1,0,1,1,1,1,1,0,1},
        {1,0,1,1,1,0,1,0,0,0,0,0,1,0,1},
        {1,0,1,0,0,0,1,1,1,1,1,0,1,0,1},
        {1,0,1,0,1,0,0,0,0,0,1,0,1,0,1},
        {1,0,0,0,1,1,1,1,1,0,0,0,1,2,1}, // Le '2' est la case d'arrivée
        {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1}
    };

    // Dessine la grille complète
    public void draw(Graphics g) {
        for (int row = 0; row < gridHeight; row++) {
            for (int col = 0; col < gridWidth; col++) {
                int x = col * tileSize;
                int y = row * tileSize;

                if (maze[row][col] == 1) {
                    g.setColor(Color.BLACK); // Mur
                    g.fillRect(x, y, tileSize, tileSize);
                } else if (maze[row][col] == 2) {
                    g.setColor(Color.RED); // Case de sortie
                    g.fillRect(x, y, tileSize, tileSize);
                } else {
                    g.setColor(Color.LIGHT_GRAY); // Quadrillage du sol
                    g.drawRect(x, y, tileSize, tileSize);
                }
            }
        }
    }

    // Méthode de collision : renvoie 'true' si l'entité peut marcher ici
    public boolean isWalkable(int x, int y) {
        if (x < 0 || x >= gridWidth || y < 0 || y >= gridHeight) return false;
        return maze[y][x] != 1;
    }
}