/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import com.google.common.collect.Lists;
import com.mysql.jdbc.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Jaider Serrano
 */
public class ConnectionMySQL {
    private Connection conexion;
    private String host;
    private String port;
    private String database;
    private String user;
    private String password;
    
    public ConnectionMySQL(Map<String, String> parametros) {
            this.host = parametros.get(Util.HOST_MYSQL);
            this.port = parametros.get(Util.PORT_MYSQL);
            this.database = parametros.get(Util.DB_MYSQL);
            this.user = parametros.get(Util.USER_MYSQL);
            this.password = parametros.get(Util.PASSWORD_MYSQL);            
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
        return "jdbc:mysql://"+this.host+":"+this.port+"/"+this.database;
    }

    //**********************************************************************************//
    
    public synchronized Statement getStatement() throws SQLException, ClassNotFoundException {
        if(conexion == null || conexion.isClosed()){
          getConexion();
        }
        return conexion.createStatement();
    }
    
    public Connection getConexion() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        conexion=(Connection) DriverManager.getConnection(getUrl(), user, password);
        //System.out.println("Conexion a Base de Datos MySQL "+getUrl()+" . . . . .Ok");
        return conexion;
    }

    public void cerrarConexion() throws SQLException{
        if(this.conexion != null && !conexion.isClosed()){
            conexion.close();
        }
    }   
    
    public ResultSet consultar(String sql)throws ClassNotFoundException, SQLException{
       conexion = getConexion();
       return conexion.prepareStatement(sql).executeQuery();
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
            //this.cerrarConexion();
    }
    
    /**
     *
     * @return Lista con todas las tablas de la conexion con Oracle.
     * @throws java.sql.SQLException
     * @throws java.lang.ClassNotFoundException
     */
    public List<String> getTablas() throws SQLException, ClassNotFoundException {
        List<String> tablas = Lists.newArrayList();
        getConexion();
        DatabaseMetaData metaDatos = conexion.getMetaData();
        
        String types[]={"TABLE"};
        ResultSet rs = metaDatos.getTables(null, database, "%", types);
        while(rs.next()){
            tablas.add(rs.getString(3));
        }
        rs.close();
        this.cerrarConexion();
        return tablas;
    }    
    
    /**
     *
     * @param nomTabla Nombre de la tabla a consultar el nombre de sus campos
     * @return Lista con todas las columnas de la tabla dada.
     * @throws java.sql.SQLException
     * @throws java.lang.ClassNotFoundException
     */    
    public List<String> getCamposTabla(String nomTabla) throws SQLException, ClassNotFoundException {
        List<String> campos = Lists.newArrayList();
        getConexion();
        DatabaseMetaData metaDatos = conexion.getMetaData();
        ResultSet rs = metaDatos.getColumns(null, database, nomTabla, null);
        while(rs.next()){
            campos.add(rs.getString(4));
        }
        rs.close();
        this.cerrarConexion();
        return campos;
    }    
   
}
