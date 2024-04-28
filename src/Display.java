import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Arrays;

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
    ArrayList<Integer> velocityX = new ArrayList<>(Arrays.asList(0));
    int velocityY = 0;
    ArrayList<Integer> coordinateX = new ArrayList<>(Arrays.asList(268));
    ArrayList<Integer> coordinateY = new ArrayList<>(Arrays.asList(100));
    ArrayList<Integer> gravity = new ArrayList<>(Arrays.asList(1));
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
        for (int i = 0; i < coordinateX.size(); i++) {
            g2D.drawImage(circle, coordinateX.get(i), coordinateY.get(i), null);
        }
    }

    public void addCircle() {
        coordinateX.add(268);
        coordinateY.add(100);
        gravity.add(1);
        velocityX.add(0);
    }

    private void checkCollision() {
        int radius = circle.getWidth(null) / 2;
        for (int i = 0; i < coordinateX.size(); i++) {
            for (int j = i + 1; j < coordinateX.size(); j++) {
                int dx = coordinateX.get(i) - coordinateX.get(j);
                int dy = coordinateY.get(i) - coordinateY.get(j);
                int distance = (int) Math.sqrt(dx * dx + dy * dy);

                if (distance < radius * 2) {
//                    velocityX.set(i, velocityX.get(i) * -1);
//                    velocityX.set(j, velocityX.get(j) * -1);
                    System.out.println("Collision");
                }
            }
        }
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

        for (int i = 0; i < coordinateX.size(); i++) {
            if (coordinateY.get(i) + imageHeight >= PANEL_HEIGHT) {
                gravity.set(i, (int) (Math.abs(gravity.get(i) + velocityY) * -1 * collisionDamping));
            } else {
                gravity.set(i, gravity.get(i) + 1);
            }
            coordinateY.set(i, coordinateY.get(i) + gravity.get(i));

            if (coordinateX.get(i) <= 45 || coordinateX.get(i) + imageWidth >= PANEL_WIDTH + 25) {
                velocityX.set(i, velocityX.get(i) * -1);
            }
            coordinateX.set(i, coordinateX.get(i) + velocityX.get(i));
        }

        checkCollision();

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
            case KeyEvent.VK_W:
                collisionDamping = Math.min(collisionDamping + 0.01, 1);
                break;
            case KeyEvent.VK_S:
                collisionDamping = Math.max(collisionDamping - 0.01, 0);
                break;
            case KeyEvent.VK_D:
                for (int i = 0; i < velocityX.size(); i++) {
                    velocityX.set(i, velocityX.get(i) + 1);
                }
                break;
            case KeyEvent.VK_A:
                for (int i = 0; i < velocityX.size(); i++) {
                    velocityX.set(i, velocityX.get(i) - 1);
                }
                break;
            case KeyEvent.VK_R:
                coordinateX = new ArrayList<>(Arrays.asList(268));
                coordinateY = new ArrayList<>(Arrays.asList(100));
                gravity = new ArrayList<>(Arrays.asList(1));
                velocityX = new ArrayList<>(Arrays.asList(0));
                velocityY = 0;
                collisionDamping = 1;
                break;
            case KeyEvent.VK_C:
                addCircle();
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}