/*created by Mohamed Khalefa
 * MIT Open Courseware
 * Intro to Programming in Java
 *
 * Assignment 5:graphics
 *
 */

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;


/**
 * Displays a window and delegates drawing to DrawGraphics.
 */
public class SimpleDraw extends JPanel implements Runnable {
    private static final long serialVersionUID = -7469734580960165754L;
    private DrawGraphics draw;
    private boolean animate = true;
    private final int FRAME_DELAY = 50; // 50 ms = 20 FPS
    public static final int WIDTH = 300;
    public static final int HEIGHT = 300;


    public SimpleDraw(DrawGraphics drawer) {
        this.draw = drawer;
    }

    /**
     * Paint callback from Swing. Draw graphics using g.
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

// Enable anti-aliasing for better looking graphics
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        draw.draw(g2);
    }

    /**
     * Enables periodic repaint calls.
     */
    public synchronized void start() {
        animate = true;
    }

    /**
     * Pauses animation.
     */
    public synchronized void stop() {
        animate = false;
    }

    private synchronized boolean animationEnabled() {
        return animate;
    }

    public void run() {
        while (true) {
            if (animationEnabled()) {
                repaint();
            }

            try {
                Thread.sleep(FRAME_DELAY);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }

    }

    public static void main(String args[]) {
        final SimpleDraw content = new SimpleDraw(new DrawGraphics());
        JFrame frame = new JFrame("Graphics!");
        Color bgColor = Color.white;
        frame.setBackground(bgColor);
        content.setBackground(bgColor);
        content.setSize(WIDTH, HEIGHT);
        content.setMinimumSize(new Dimension(WIDTH, HEIGHT));
        content.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        frame.setSize(WIDTH, HEIGHT);
        frame.setContentPane(content);
        frame.setResizable(false);
        frame.pack();
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }

            public void windowDeiconified(WindowEvent e) {
                content.start();
            }

            public void windowIconified(WindowEvent e) {
                content.stop();
            }
        });
        new Thread(content).start();
        frame.setVisible(true);

    }

    public DrawGraphics getDraw() {
        return draw;
    }

    public void setDraw(DrawGraphics draw) {
        this.draw = draw;
    }
}

class BouncingBox {
    int x;
    int y;
    Color color;
    int xDirection = 0;
    int yDirection = 0;
    final int SIZE = 20;

    /**
     * Initialize a new box with its center located at (startX, startY), filled * with startColor.
     */
    public BouncingBox(int startX, int startY, Color startColor) {
        x = startX;
        y = startY;
        color = startColor;
    }

    /**
     * Draws the box at its current position on to surface.
     */
    public void draw(Graphics surface) { // Draw the object surface.setColor(color);
        surface.fillRect(x - SIZE / 2, y - SIZE / 2, SIZE, SIZE);
        surface.setColor(Color.BLACK);
        ((Graphics2D) surface).setStroke(new BasicStroke(3.0f));
        surface.drawRect(x - SIZE / 2, y - SIZE / 2, SIZE, SIZE);

// Move the center of the object each time we draw it
        x += xDirection;
        y += yDirection;

// If we have hit the edge and are moving in the wrong direction, reverse direction // We check the direction because if a box is placed near the wall, we would get "stuck" // rather than moving in the right direction
        if ((x - SIZE / 2 <= 0 && xDirection < 0) ||
                (x + SIZE / 2 >= SimpleDraw.WIDTH && xDirection > 0)) {
            xDirection = -xDirection;
        }
        if ((y - SIZE / 2 <= 0 && yDirection < 0) ||
                (y + SIZE / 2 >= SimpleDraw.HEIGHT && yDirection > 0)) {
            yDirection = -yDirection;
        }

    }

    public void setMovementVector(int xIncrement, int yIncrement) {
        xDirection = xIncrement;
        yDirection = yIncrement;
    }

}


class DrawGraphics {
    BouncingBox box;

    /**
     * Initializes this class for drawing.
     */
    public DrawGraphics() {
        box = new BouncingBox(200, 50, Color.RED);
    }

    /**
     * Draw the contents of the window on surface. Called 20 times per second.
     */
    public void draw(Graphics surface) {
        surface.drawLine(50, 50, 250, 250);
        box.draw(surface);
    }

}

