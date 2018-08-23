/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pinturi3;


import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Image;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author agustin
 */
public final class Conexion{

    private Socket s;
    private Object[] a;
    private ObjectInputStream mensaje_rec;
    private final int port; //estamos usando el reto de los 65534 puertos
    private final String host;
    private ObjectOutputStream mensaje_env;

    public Conexion(String host,int puerto) {
        this.host=host;
        this.port=puerto;
        initClient();
    }

    public void initClient() {
        try {
            s = new Socket(host, port);
            mensaje_env = new ObjectOutputStream(s.getOutputStream());
            mensaje_rec = new ObjectInputStream(s.getInputStream());
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public Image getValores() throws IOException, ClassNotFoundException {
        return (Image) mensaje_rec.readObject();
        
    }

    public void sendValores(Image image) throws IOException {
        mensaje_env.writeObject(image);
    }
    
    public String getPersonas() throws IOException, ClassNotFoundException{
    a=(Object[])mensaje_rec.readObject();
    return (String) a[6];}
    
}
