/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pinturi3;

/**
 *
 * @author agustin
 */
import java.sql.*;

public class ConexionMysql {

    private static Connection cnx = null;

    public ConexionMysql(){
        
    }
    
    public static Connection obtener() throws SQLException, ClassNotFoundException {
        if (cnx == null) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                cnx = DriverManager.getConnection("jdbc:mysql://localhost/Pinturi3", "root", "43811871");
            } catch (SQLException ex) {
                throw new SQLException(ex);
            } catch (ClassNotFoundException ex) {
                throw new ClassCastException(ex.getMessage());
            }
        }
        return cnx;
    }

    public boolean logUser(Connection conection, String user) throws SQLException {
        PreparedStatement consulta;
        try {
            consulta = conection.prepareStatement("select persona from personas where persona='" + user + "'");
            ResultSet resultado = consulta.executeQuery();
            if (resultado.next() && resultado.getString("persona").equals(user)) {
                return false;
            } else {
                return true;
            }
        } catch (SQLException ex) {
            throw new SQLException(ex);
        }
    }

    public void setPersonas(Connection conexion, String persona, String direccion) throws SQLException {
        PreparedStatement consulta;
        consulta = conexion.prepareStatement("insert into personas (persona, direccion) values('" + persona + "','"+direccion+"')");
        consulta.execute();
    }
    

    public String getPersonas(Connection conexion) throws SQLException, ClassNotFoundException {
        PreparedStatement consulta;
        consulta = conexion.prepareStatement("select persona from personas");
        ResultSet resultado = consulta.executeQuery();
        String texto = null;
        try {
            while (resultado.next()) {
                if (texto == null) {
                    texto = resultado.getString("persona") + "\n";
                } else {
                    texto += resultado.getString("persona") + "\n";
                }
            }
            return texto;
        } catch (SQLException e) {
            return e.getMessage();
        }
    }

    public static void cerrar(Connection conexion, String persona) throws SQLException {
        if (cnx != null) {
            PreparedStatement consulta;
            consulta=conexion.prepareStatement("delete from personas where persona='"+persona+"'");
            cnx.close();
        }
    }
}
