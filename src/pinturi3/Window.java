/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pinturi3;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 *
 * @author agustin
 */
public class Window {

    private DrawPane draw;
    private JFrame window, windowcolor;
    private JPanel tools, aoc, panel_personas;
    private Graphics g;
    private JButton borrar, goma, lapiz, fibron, color, aceptar, cancelar;
    private JColorChooser ecolor;
    private JTextArea personas_txt;
    private JScrollPane personas;
    private JLabel top;
    private String user;
    private Component[] contenido_txt;
    private ConexionMysql cnx;
    private Thread t = new Thread(new Lector());
    private ActionListener action = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == borrar) {
                draw.clear();
            } else if (e.getSource() == goma) {
                draw.goma();
            } else if (e.getSource() == lapiz) {
                draw.lapiz();
            } else if (e.getSource() == fibron) {
                draw.fibron();
            } else if (e.getSource() == color) {
                windowcolor.setVisible(true);
            }
        }
    };

    private ActionListener action2 = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == aceptar) {
                draw.setColor(ecolor.getColor());
                windowcolor.setVisible(false);
            } else if (e.getSource() == cancelar) {
                windowcolor.setVisible(false);
            }
        }
    };

    public Window(String user) throws IOException, ClassNotFoundException, SQLException {
        this.user=user;
        initComponents();
        cnx.setPersonas(cnx.obtener(), user,draw.getDireccion());
        t.start();
        window.setVisible(true);
    }

    public void initComponents() throws IOException, ClassNotFoundException, SQLException {
        cnx=new ConexionMysql();
        window = new JFrame();
        window.setResizable(false);
        ecolor = new JColorChooser();
        windowcolor = new JFrame();
        Container content2 = windowcolor.getContentPane();
        Container content = window.getContentPane();
        

        content.setLayout(new BorderLayout());
        draw = new DrawPane();
        content.add(draw, BorderLayout.CENTER);

        borrar = new JButton("Borrar");
        borrar.addActionListener(action);

        fibron = new JButton("Fibron");
        fibron.addActionListener(action);

        goma = new JButton("Goma");
        goma.addActionListener(action);

        lapiz = new JButton("Lapiz");
        lapiz.addActionListener(action);

        aceptar = new JButton("Aceptar");
        aceptar.addActionListener(action2);
        cancelar = new JButton("Cancelar");
        cancelar.addActionListener(action2);

        color = new JButton("Color");
        color.addActionListener(action);

        tools = new JPanel();
        tools.setLayout(new BoxLayout(tools, BoxLayout.Y_AXIS));
        tools.add(lapiz);
        tools.add(goma);
        tools.add(fibron);
        tools.add(color);
        tools.add(borrar, BorderLayout.SOUTH);
        content.add(tools, BorderLayout.WEST);

        aoc = new JPanel();
        aoc.setLayout(new FlowLayout(FlowLayout.CENTER));
        aoc.add(aceptar);
        aoc.add(cancelar);
        content2.setLayout(new BorderLayout());
        content2.add(ecolor, BorderLayout.CENTER);

        top = new JLabel();
        top.setText("Personas:");

        content2.add(aoc, BorderLayout.SOUTH);
        personas_txt = new JTextArea();
        personas_txt.setSize(200, 200);
        personas_txt.setEditable(false);

        personas = new JScrollPane(personas_txt);

        panel_personas = new JPanel();
        panel_personas.setPreferredSize(new Dimension(100, window.getWidth()));
        panel_personas.setLayout(new BoxLayout(panel_personas, BoxLayout.Y_AXIS));
        panel_personas.add(top);
        panel_personas.add(personas);
        content.add(panel_personas, BorderLayout.EAST);

        window.setSize(800, 600);
        window.setLocationRelativeTo(null);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        windowcolor.setSize(600, 400);
        windowcolor.setLocationRelativeTo(null);

    }
    
    private int cerrarVentana() throws SQLException, ClassNotFoundException{
        cnx.cerrar(cnx.obtener(), this.user);
        return 3;
    }

    private class Lector implements Runnable {

        public void run() {
            while (true) {
                try {
                    personas_txt.setText(cnx.getPersonas(cnx.obtener()));
                } catch (SQLException ex) {
                    Logger.getLogger(Window.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(Window.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }
    }

}
