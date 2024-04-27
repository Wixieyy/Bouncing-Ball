import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

class Display extends JFrame {
    Panel panel;

    public void start() {
        panel = new Panel();

        this.setTitle("Bouncing Ball");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.add(panel);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setVisible(true);
        panel.repaint();
    }
}

class Panel extends JPanel implements ActionListener, KeyListener {
    final int PANEL_WIDTH = 500;
    final int PANEL_HEIGHT = 500;
    Image circle;
    Timer timer;
    int velocityX = 0;
    int velocityY = 0;
    int coordinateX = 268;
    int coordinateY = 100;
    int gravity = 1;
    int frames = 0;
    double collisionDamping = 1;
    long startTime = System.currentTimeMillis();
    JLabel fpsLabel, collisionDampingLabel;

    Panel() {
        collisionDampingLabel = new JLabel("collisionDamping");
        fpsLabel = new JLabel("fps");
        this.setPreferredSize(new Dimension(600, 600));
        this.setBackground(new Color(0xD3D3D3));
        this.addKeyListener(this);
        this.setFocusable(true);
        circle = new ImageIcon("circle.png").getImage();
        timer = new Timer(0, this);
        timer.start();
        this.add(collisionDampingLabel);
        this.add(fpsLabel);
    }

    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2D = (Graphics2D) g;
        g2D.setStroke(new BasicStroke(3));
        g2D.drawRect(50, 50, 500, 484);
        g2D.fillRect(50, 50, 500, 484);
        g2D.drawImage(circle, coordinateX, coordinateY, null);
    }

    private double fpsCounter() {
        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - startTime;
        double fps;
        fps = (double) frames / (elapsedTime / 1000.0);
        fps = Math.round(fps * 10.0) / 10.0;
        frames = 0;
        startTime = currentTime;
        return fps;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int imageHeight = circle.getHeight(null) / 2;
        int imageWidth = circle.getWidth(null) / 2;

        if (coordinateY + imageHeight >= PANEL_HEIGHT) {
            gravity = (int) (Math.abs(gravity + velocityY) * -1 * collisionDamping);
        } else {
            gravity++;
        }
        coordinateY = coordinateY + gravity;

        if (coordinateX <= 45 || coordinateX + imageWidth >= PANEL_WIDTH + 25) {
            velocityX = velocityX * -1;
        }
        coordinateX = coordinateX + velocityX;

        frames++;
        if (frames % 10 == 0) {
            fpsLabel.setText("FPS: " + fpsCounter());
            collisionDampingLabel.setText("Damping force: " + String.format("%.2f", collisionDamping));
        }
        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        switch (key) {
            case KeyEvent.VK_D:
                collisionDamping = Math.min(collisionDamping + 0.01, 1);
                break;
            case KeyEvent.VK_A:
                collisionDamping = Math.max(collisionDamping - 0.01, 0);
                break;
            case KeyEvent.VK_W:
                if (velocityX < 0) {
                    velocityX = velocityX - 1;
                } else {
                    velocityX = velocityX + 1;
                }
                break;
            case KeyEvent.VK_S:
                if (velocityX < 0) {
                    velocityX = velocityX + 1;
                } else {
                    velocityX = velocityX - 1;
                }
                break;
            case KeyEvent.VK_R :
                coordinateX = 268;
                coordinateY = 100;
                velocityX = 0;
                velocityY = 0;
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}