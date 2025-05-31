import java.awt.*;

public class Coin {
    private int x, y;
    private int size;
    
    public Coin(int x, int y) {
        this.x = x;
        this.y = y;
        this.size = 20;
    }
    
    public void draw(Graphics g) {
        g.setColor(Color.YELLOW);
        g.fillOval(x, y, size, size);
    }
    
    public Rectangle getBounds() {
        return new Rectangle(x, y, size, size);
    }
}
