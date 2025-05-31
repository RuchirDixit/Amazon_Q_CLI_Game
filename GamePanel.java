import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener, KeyListener {
    private static final int PANEL_WIDTH = 800;
    private static final int PANEL_HEIGHT = 600;
    private static final int GROUND_LEVEL = 500;
    private static final int DELAY = 16; // ~60 FPS
    
    private Timer timer;
    private Player player;
    private List<Enemy> enemies;
    private List<Coin> coins;
    private int score;
    private int lives;
    private boolean gameOver;
    private boolean gameWon;
    private Random random;
    
    public GamePanel() {
        setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);
        
        random = new Random();
        initGame();
    }
    
    private void initGame() {
        player = new Player(100, GROUND_LEVEL - 50);
        enemies = new ArrayList<>();
        coins = new ArrayList<>();
        score = 0;
        lives = 3;
        gameOver = false;
        gameWon = false;
        
        // Create enemies
        for (int i = 0; i < 5; i++) {
            int x = random.nextInt(PANEL_WIDTH - 100) + 50;
            int y = GROUND_LEVEL - 30;
            enemies.add(new Enemy(x, y));
        }
        
        // Create coins
        for (int i = 0; i < 10; i++) {
            int x = random.nextInt(PANEL_WIDTH - 100) + 50;
            int y = random.nextInt(GROUND_LEVEL - 100) + 50;
            coins.add(new Coin(x, y));
        }
    }
    
    public void startGame() {
        timer = new Timer(DELAY, this);
        timer.start();
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameOver && !gameWon) {
            updateGame();
        }
        repaint();
    }
    
    private void updateGame() {
        player.update();
        
        // Apply gravity to player
        if (player.getY() + player.getHeight() < GROUND_LEVEL) {
            player.setVelY(player.getVelY() + 0.5); // Gravity
        } else {
            player.setY(GROUND_LEVEL - player.getHeight());
            player.setVelY(0);
            player.setJumping(false);
        }
        
        // Update enemies
        for (Enemy enemy : enemies) {
            enemy.update();
            
            // Keep enemies within bounds
            if (enemy.getX() <= 0 || enemy.getX() + enemy.getWidth() >= PANEL_WIDTH) {
                enemy.reverseDirection();
            }
            
            // Check collision with player
            if (player.getBounds().intersects(enemy.getBounds())) {
                lives--;
                player.setX(100); // Reset player position
                
                if (lives <= 0) {
                    gameOver = true;
                }
                break;
            }
        }
        
        // Check coin collisions
        for (int i = coins.size() - 1; i >= 0; i--) {
            Coin coin = coins.get(i);
            if (player.getBounds().intersects(coin.getBounds())) {
                coins.remove(i);
                score += 10;
            }
        }
        
        // Check if all coins are collected
        if (coins.isEmpty()) {
            gameWon = true;
        }
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // Draw ground
        g.setColor(Color.GREEN);
        g.fillRect(0, GROUND_LEVEL, PANEL_WIDTH, PANEL_HEIGHT - GROUND_LEVEL);
        
        // Draw player
        player.draw(g);
        
        // Draw enemies
        for (Enemy enemy : enemies) {
            enemy.draw(g);
        }
        
        // Draw coins
        for (Coin coin : coins) {
            coin.draw(g);
        }
        
        // Draw score and lives
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Score: " + score, 20, 30);
        g.drawString("Lives: " + lives, PANEL_WIDTH - 100, 30);
        
        // Draw game over or win message
        if (gameOver) {
            drawCenteredMessage(g, "Game Over! Press R to restart", Color.RED);
        } else if (gameWon) {
            drawCenteredMessage(g, "You Win! Press R to restart", Color.YELLOW);
        }
    }
    
    private void drawCenteredMessage(Graphics g, String message, Color color) {
        g.setColor(color);
        g.setFont(new Font("Arial", Font.BOLD, 30));
        FontMetrics metrics = g.getFontMetrics();
        int x = (PANEL_WIDTH - metrics.stringWidth(message)) / 2;
        int y = PANEL_HEIGHT / 2;
        g.drawString(message, x, y);
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        
        if (gameOver || gameWon) {
            if (key == KeyEvent.VK_R) {
                initGame();
            }
            return;
        }
        
        switch (key) {
            case KeyEvent.VK_LEFT:
                player.setVelX(-5);
                break;
            case KeyEvent.VK_RIGHT:
                player.setVelX(5);
                break;
            case KeyEvent.VK_SPACE:
                if (!player.isJumping()) {
                    player.jump();
                }
                break;
        }
    }
    
    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        
        if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_RIGHT) {
            player.setVelX(0);
        }
    }
    
    @Override
    public void keyTyped(KeyEvent e) {
        // Not used
    }
}
