/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import com.google.common.collect.Maps;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Map;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

/**
 *
 * @author ZULAY
 */
public  class Util {
    //TNS keys
    public static final String HOST_TNS = "HostTNS";
    public static final String PORT_TNS = "PortTNS";
    public static final String DB_TNS = "DataBaseTNS";
    public static final String USER_TNS = "UserTNS";
    public static final String PASSWORD_TNS = "PasswordTNS";
    //MySQL keys
    public static final String HOST_MYSQL = "HostMySQL";
    public static final String PORT_MYSQL = "PortMySQL";
    public static final String DB_MYSQL = "DataBaseMySQL";
    public static final String USER_MYSQL = "UserMySQL";
    public static final String PASSWORD_MYSQL = "PasswordMySQL";
    public static final Map<String, String> parametros = getParametros();
    public static Stage stage = null;
    public static final String ruta = "Parametros.properties";
    public static String fechaUltimaAct = "";
    
    private static Map<String, String> getParametros() {
        ArchivoLeer archivo = new ArchivoLeer(ruta);
        
        try {
            return archivo.leerParametros();
        } 
        catch (IOException ex) {
            System.err.println("Error al cargar elarchivo e parametros [Parametros.properties]");
            return Maps.newHashMap();
        }
    }

 public static boolean guardarParametros(){
    ArchivoEscribir archivo = new ArchivoEscribir(ruta);
    try {
        archivo.crearArchivo(true);

        for (Map.Entry<String, String> entry : parametros.entrySet()) {
            archivo.escribir(entry.getKey()+"="+entry.getValue());
        }
        archivo.close();
        return true;
    } 
    catch (IOException ex) {
        DialogBuilder.create().title("¡Exception!").contentText("Error actualizar el arcivo "+ruta)
            .alert(Alert.AlertType.ERROR).exception(ex).build().showAndWait();       
    }
    return false;
  }    
    /**
     * 
     * @return Fecha en formato mm/dd/yyyy sin hora.
     */
    public static String getFechaLocal(){
        Date fecha = new Date();        
        LocalDateTime fec = LocalDateTime.ofInstant(new Date(fecha.getTime()).toInstant(), ZoneId.systemDefault());
            return fec.format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
    }
    /**
     * 
     * @param fecha a formatear
     * @return Fecha en formato mm/dd/yyyy HH:mm:ss
     */
    public static String getFecha(Date fecha){
        if(fecha == null){
                return "";
        }

        LocalDateTime fec = LocalDateTime.ofInstant(new Date(fecha.getTime()).toInstant(), ZoneId.systemDefault());
        String result = fec.format(DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss"));
        int hora = Integer.parseInt(result.substring(11,13));
        if(hora < 10){
                result = result.substring(0, 11)+result.substring(12); 
        }
        return result;
    } 
}
