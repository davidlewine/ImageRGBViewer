package zoomer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import java.io.*;
import java.util.Arrays;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputAdapter;

public class Zoomer implements Runnable {

    private JFrame frame;

    public BufferedImage img;
    public BufferedImage zoomImage;

    private MyZoomPanel zoomPanel;

    private int r = 5;//radius of included points around target point
    private int zoomFactor = 60;

    public Zoomer() {

        try {
            //String path = "http://media.gamespy.com/columns/image/article/114/1141493/platformer-makeover-making-stale-stage-themes-interesting-again-20101222100119621.jpg";
            String path = "http://picsoff.com/files/funzug/imgs/artwork/interesting_traces_stone_01.jpg";
            URL url = new URL(path);
            img = ImageIO.read(url);
            zoomImage = new BufferedImage((r * 2 + 1) * zoomFactor, (r * 2 + 1) * zoomFactor, img.getType());
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(this);
    }

    public Zoomer(BufferedImage imgArg) {
        img = imgArg;
        zoomImage = new BufferedImage((r * 2 + 1) * zoomFactor, (r * 2 + 1) * zoomFactor, img.getType());
        SwingUtilities.invokeLater(this);
    }

    public static void main(String[] args) {
        new Zoomer();
        //SwingUtilities.invokeLater(new Zoomer());
    }

    public void zoom(BufferedImage img) {
        SwingUtilities.invokeLater(new Zoomer(img));
    }

    public void setImage(BufferedImage newImg) {
        img = newImg;
        zoomImage = new BufferedImage((r * 2 + 1) * zoomFactor, (r * 2 + 1) * zoomFactor, img.getType());
    }

    @Override
    public void run() {
        frame = new JFrame("Zoom Panel");

        zoomPanel = new MyZoomPanel(this);
        MyListener alpha = new MyListener(this);
        zoomPanel.addMouseMotionListener(alpha);
        zoomPanel.addMouseListener(alpha);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(zoomPanel);
        frame.setSize(1200, 700);
        frame.setVisible(true);
    }

    public JPanel getZoomPanel() {
        return zoomPanel;
    }

    public void drawZoomImage(int mx, int my, boolean pixInfo) {
        Graphics g = zoomImage.getGraphics();
        for (int i = 0; i < 2 * r + 1; i++) {
            for (int j = 0; j < 2 * r + 1; j++) {
                if (mx < img.getWidth() - r && mx > r && my < img.getHeight() - r && my > r) {
                    int x = mx - r + j;
                    int y = my - r + i;
                    g.setColor(new Color(img.getRGB(x, y)));
                    g.fillRect(j * zoomFactor, i * zoomFactor, zoomFactor, zoomFactor);
                    if (pixInfo) {
                        g.setColor(Color.RED);
                        Color pixColor = new Color(img.getRGB(x, y));
                        g.drawString("" + pixColor.getRed(), j * zoomFactor + 5, i * zoomFactor + 15);
                        g.drawString("" + pixColor.getGreen(), j * zoomFactor + 5, i * zoomFactor + 25);
                        g.drawString("" + pixColor.getBlue(), j * zoomFactor + 5, i * zoomFactor + 35);
                    }
                }
            }
        }
    }

    private double[] sepColors(int n) {
        double[] colors = new double[3];
        colors[0] = (n >> 16) & 0xFF;
        colors[1] = (n >> 8) & 0xFF;
        colors[2] = n & 0xFF;
        return colors;
    }

    private double featureValue(ArrayList<double[][]> pixelRegion) {
        double val = 0;

        return val;
    }

    private class MyListener extends MouseInputAdapter {

        private Zoomer zoomer;

        public MyListener(Zoomer zoomer) {
            this.zoomer = zoomer;
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            zoomer.drawZoomImage(e.getX(), e.getY(), true);

            zoomer.getZoomPanel().repaint();
        }

    }

    private class MyZoomPanel extends JPanel {

        private Zoomer zoomer;

        public MyZoomPanel(Zoomer zoomer) {
            this.zoomer = zoomer;
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            g.setColor(Color.WHITE);
            g.fillRect(0, 0, this.getWidth(), this.getHeight());
            g.drawImage(zoomer.img, 0, 0, this);
            g.drawImage(zoomer.zoomImage, 40 + zoomer.img.getWidth(), 20, this);

        }
    }
}
