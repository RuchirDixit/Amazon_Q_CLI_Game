import java.awt.*;

public class Player {
    private int x, y;
    private int width, height;
    private double velX, velY;
    private boolean isJumping;
    private final int JUMP_FORCE = -15;
    
    public Player(int x, int y) {
        this.x = x;
        this.y = y;
        this.width = 40;
        this.height = 40;
        this.velX = 0;
        this.velY = 0;
        this.isJumping = false;
    }
    
    public void update() {
        x += velX;
        y += velY;
        
        // Keep player within screen bounds
        if (x < 0) {
            x = 0;
        } else if (x > 800 - width) {
            x = 800 - width;
        }
    }
    
    public void draw(Graphics g) {
        g.setColor(Color.BLUE);
        g.fillRect(x, y, width, height);
    }
    
    public void jump() {
        if (!isJumping) {
            velY = JUMP_FORCE;
            isJumping = true;
        }
    }
    
    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
    
    // Getters and setters
    public int getX() {
        return x;
    }
    
    public void setX(int x) {
        this.x = x;
    }
    
    public int getY() {
        return y;
    }
    
    public void setY(int y) {
        this.y = y;
    }
    
    public int getWidth() {
        return width;
    }
    
    public int getHeight() {
        return height;
    }
    
    public double getVelX() {
        return velX;
    }
    
    public void setVelX(double velX) {
        this.velX = velX;
    }
    
    public double getVelY() {
        return velY;
    }
    
    public void setVelY(double velY) {
        this.velY = velY;
    }
    
    public boolean isJumping() {
        return isJumping;
    }
    
    public void setJumping(boolean jumping) {
        isJumping = jumping;
    }
}
