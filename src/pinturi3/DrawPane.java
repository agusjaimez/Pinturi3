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
import java.awt.Paint;
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
    private BasicStroke current_stroke = stroke_lap;
    private int currentx;
    private int currenty;
    private int oldx;
    private int oldy;
    private Color color = Color.BLACK;
    private Color color_prev;
    private Conexion con = new Conexion("localhost",42066);
    private Thread t = new Thread(new Lector());
    private int num_stroke;

    public DrawPane(String persona) {

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
                    if (current_stroke == stroke_lap) {
                        num_stroke = 0;
                    } else if (current_stroke == stroke_fib) {
                        num_stroke = 1;
                    } else {
                        num_stroke = 2;
                    }
                    Object[] valores = {oldx, oldy, currentx, currenty, num_stroke, color,persona};
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
        
        color_prev=color;
        setColor(Color.WHITE);
        gp.setStroke(stroke_gom);
        current_stroke = stroke_gom;
    }

    public void lapiz() {
        if(gp.getStroke()==stroke_gom){
        setColor(color_prev);}
        gp.setStroke(stroke_lap);
        gp.setPaint(this.color);
        current_stroke = stroke_lap;
    }

    public void fibron() {
        if(gp.getStroke()==stroke_gom){
        setColor(color_prev);}
        gp.setStroke(stroke_fib);
        gp.setPaint(this.color);
        current_stroke = stroke_fib;
    }

    public void getDraw(Object[] valores) {
        if ((int) valores[4] == 0) {
            gp.setStroke(stroke_lap);
        } else if ((int) valores[4] == 1) {
            gp.setStroke(stroke_fib);
        } else {
            gp.setStroke(stroke_gom);
        }
        gp.setPaint((Color) valores[5]);
        gp.drawLine((int) valores[0], (int) valores[1], (int) valores[2], (int) valores[3]);
        repaint();
        gp.setStroke(current_stroke);
        gp.setPaint(this.color);

    }
    


    private class Lector implements Runnable {

        public void run() {
            try {
                while (true) {
                    getDraw(con.getValores());
                }
            } catch (IOException ex) {
                Logger.getLogger(DrawPane.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(DrawPane.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
