/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pinturi3;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Image;
import javax.swing.JComponent;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Stroke;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author agustin
 */
public class DrawPane extends JComponent {

    private Image image;
    private Graphics2D gp;
    private BasicStroke stroke_gom = new BasicStroke(30f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
    private BasicStroke stroke_lap = new BasicStroke(1f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
    private BasicStroke stroke_fib = new BasicStroke(10f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
    private int currentx;
    private int currenty;
    private int oldx;
    private int oldy;
    private Color color;
    private Conexion con = new Conexion();
    private Thread t = new Thread(new Lector());
    private String valores_env;

    public DrawPane() {
        
        t.start();

        setDoubleBuffered(false);
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                oldy = e.getY();
                oldx = e.getX();
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                currentx = e.getX();
                currenty = e.getY();
                if (gp != null) {
                    gp.drawLine(oldx, oldy, currentx, currenty);
                    int[] valores = {oldx, oldy, currentx, currenty};

                    oldx = currentx;
                    oldy = currenty;
                    repaint();
                    try {
                        con.sendValores(valores);
                    } catch (IOException ex) {
                        Logger.getLogger(DrawPane.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
            }
        });
    }

    protected void paintComponent(Graphics g) {
        if (image == null) {
            image = createImage(getSize().width, getSize().height);
            gp = (Graphics2D) image.getGraphics();
            gp.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            clear();
        }
        g.drawImage(image, 0, 0, null);
    }

    public void clear() {
        gp.setPaint(Color.white);
        gp.fillRect(0, 0, getSize().width, getSize().height);
        gp.setPaint(this.color);
        repaint();
    }

    public void setColor(Color c) {
        gp.setPaint(c);
        this.color = c;
    }

    public void goma() {
        gp.setStroke(stroke_gom);
        gp.setPaint(Color.WHITE);
    }

    public void lapiz() {
        gp.setStroke(stroke_lap);
        gp.setPaint(this.color);
    }

    public void fibron() {
        gp.setStroke(stroke_fib);
        gp.setPaint(this.color);
    }

    public void getDraw(int[] valores) {
        /**int[] fin = new int[4];
        for (int i = 0; i < valores.length; i++) {
            fin[i] = (Integer)valores[i];}**/
        gp.drawLine(valores[0], valores[1], valores[2], valores[3]);

    }

    private class Lector implements Runnable {

        public void run() {
            try {
                getDraw(con.getValores());
            } catch (IOException ex) {
                Logger.getLogger(DrawPane.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(DrawPane.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
