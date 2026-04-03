import java.awt.*;

public class Monster {
    public int x, y;
    private int moveDelay = 0; // Temporisateur pour que le monstre ne soit pas trop rapide

    public Monster(int startX, int startY) {
        this.x = startX;
        this.y = startY;
    }

    // IA du monstre : tente de se rapprocher du joueur
    public void update(int playerX, int playerY, Map map) {
        moveDelay++;
        if (moveDelay < 15) return; // Le monstre bouge 1 fois toutes les 15 frames
        moveDelay = 0;

        int nextX = x;
        int nextY = y;

        // Calcul de la direction horizontale
        if (x < playerX) nextX++;
        else if (x > playerX) nextX--;
        
        // Calcul de la direction verticale
        if (y < playerY) nextY++;
        else if (y > playerY) nextY--;

        // Teste si le mouvement en X est libre
        if (map.isWalkable(nextX, y)) {
            x = nextX;
        } 
        // Sinon, teste si le mouvement en Y est libre
        else if (map.isWalkable(x, nextY)) {
            y = nextY;
        }
    }

    public void draw(Graphics g, int tileSize) {
        g.setColor(Color.ORANGE); // Couleur du monstre
        g.fillOval(x * tileSize + 5, y * tileSize + 5, tileSize - 10, tileSize - 10);
    }
}