/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

/**
 *
 * @author ZULAY
 */
public class ConnectionFireBird {
    
    private Connection connection;
    private String host;
    private String port;
    private String database;
    private String user;
    private String password;    

    public ConnectionFireBird(Map<String, String> parametros) {
            this.host = parametros.get(Util.HOST_TNS);
            this.port = parametros.get(Util.PORT_TNS);
            this.database = parametros.get(Util.DB_TNS);
            this.user = parametros.get(Util.USER_TNS);
            this.password = parametros.get(Util.PASSWORD_TNS);            
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    private String getUrl(){
        return "jdbc:firebirdsql:"+this.host+"/"+this.port+":"+this.database;
    }

    public synchronized Statement getStatement() throws SQLException, ClassNotFoundException {
        if(connection == null || connection.isClosed()){
          getConnection();
        }
        return connection.createStatement();
    }
    
    public void cerrarConexion() throws SQLException{
        if(this.connection != null && !connection.isClosed()){
            connection.close();
        }
    }   
   
    public Connection getConnection() throws ClassNotFoundException, SQLException{
        Class.forName("org.firebirdsql.jdbc.FBDriver");
        this.connection = DriverManager.getConnection(this.getUrl(), this.user, this.password);
                System.out.println("Conexion a Base de Datos Firebird "+getUrl()+" . . . . .Ok");
        return this.connection;
    }
    
    public ResultSet consultar(String sql)throws ClassNotFoundException, SQLException{
       connection = getConnection();       
       return connection.prepareStatement(sql).executeQuery();
    }

    /**
     * Ejecuta una instruccion update SQL dada.
     * @param sql Instruccion update SQL a ejecutar.
     * @throws java.lang.ClassNotFoundException
     * @throws java.sql.SQLException
     */    
    public void actualizar(String sql) throws ClassNotFoundException, SQLException{
            Statement st = getStatement();
            st.executeUpdate(sql);
            st.close();
            this.cerrarConexion();
    }
    
}
