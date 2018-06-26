/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import com.google.common.collect.Lists;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author JAIDER ADRIAM SERRANO SEPULVEDA
 */
public class Sincronizar extends Thread{
    
    private ConnectionFireBird conTNS;
    private ConnectionMySQL conMySQL;
    private final DoubleProperty progressProperty;
    private final DoubleProperty progressIdProperty;
    private final StringProperty cantProcesados;
    private final BooleanProperty finalizado;
    private final IntegerProperty cantidad;
    private List<Material> items;

    public Sincronizar(ConnectionFireBird conTNS, ConnectionMySQL conMySQL) {
        this.conTNS = conTNS;
        this.conMySQL = conMySQL;
        this.progressProperty = new SimpleDoubleProperty();
        this.progressIdProperty = new SimpleDoubleProperty();
        this.cantProcesados = new SimpleStringProperty("0");
        this.finalizado = new SimpleBooleanProperty(false);
        this.cantidad = new SimpleIntegerProperty(0);
    }

    public ConnectionFireBird getConTNS() {
        return conTNS;
    }

    public void setConTNS(ConnectionFireBird conTNS) {
        this.conTNS = conTNS;
    }

    public ConnectionMySQL getConMySQL() {
        return conMySQL;
    }

    public void setConMySQL(ConnectionMySQL conMySQL) {
        this.conMySQL = conMySQL;
    }

    public DoubleProperty getProgressProperty() {
        return progressProperty;
    }

    public DoubleProperty getProgressIdProperty() {
        return progressIdProperty;
    }

    public StringProperty getCantProcesados() {
        return cantProcesados;
    }

    public List<Material> getItems(){
        return this.items;
    }
    
    public void setItems(List<Material> items){
        this.items = items;
    }

    public BooleanProperty getFinalizado() {
        return finalizado;
    }
    
    /*************************************************************************/
    
    public void actualizarFechaSincronizacion() throws ClassNotFoundException, SQLException{
            Date fecha = new Date();
            String sql = "UPDATE VARIOS SET CONTENIDO = '"+Util.getFechaLocal()+"', "+
                         "FECHA = (select cast('Now' as date) from rdb$database)   "+
                         "WHERE VARIAB = 'FECHA_ACT_MYSQL'";
            
            Util.fechaUltimaAct = Util.getFecha(fecha);            
            conTNS.actualizar(sql);
    }        
    
    public List<Material> articulosParaSincronizar() throws ClassNotFoundException, SQLException{
        List<Material> items = Lists.newArrayList();
        String sql = "select m.codigo model, b.codigo bodega, sm.existenc cant_bog,    "+
                     "ms.existenc cant_gen, ms.precio1 precio   "+
                     "from material m inner join materialsuc ms on ms.matid = m.matid  "+
                     "inner join salmaterial sm on sm.matid = m.matid  "+
                     "inner join bodega b on b.bodid = sm.bodid        "+
                     "where ms.fecultcli>='"+Util.fechaUltimaAct+"'    "+
                     " or ms.fecact>='"+Util.fechaUltimaAct+"'    "+
                     "   or ms.fecultprov>='"+Util.fechaUltimaAct+"'   ";
           
            String articulo = "";
            ResultSet rs = conTNS.consultar(sql);
            Material item = null;
            while(rs.next()){
                if(!articulo.equals(rs.getString("model"))){
                    item = new Material(rs.getString("model"), rs.getString("bodega")+"-"+rs.getString("cant_bog"), 
                                        rs.getString("cant_gen"), rs.getString("precio"));
                    items.add(item);
                }
                else{
                    item.setExistenciaBodega(","+rs.getString("bodega")+"-"+rs.getString("cant_bog"));
                }
                articulo = rs.getString("model");
            }
        return items;
    }    
    
    @Override
    public void run(){
        this.actualizarArticulos(0);
    }

    public void actualizarArticulos(int pos){
        if(pos >= items.size()){
            this.terminar();
            return;
        }

        if(!items.isEmpty()){
            this.conMySQL = new ConnectionMySQL(Util.parametros);
            String sql = null;

            for(int x = pos; x < items.size(); x++ ){
                Material item = null;
                item = items.get(x);

                sql = " UPDATE oc_product SET isbn = '"+item.getExistenciaBodega()+"', quantity = "+item.getTotal()+", "+
                      " price = "+item.getPrecio()+"   WHERE model = '"+item.getCodigo()+"'";
                try {
                    conMySQL.actualizar(sql);
                    progressProperty.setValue(progressProperty.getValue() + (1.0 / items.size()));
                    progressIdProperty.setValue(progressIdProperty.getValue() + (1.0 / items.size()));
                    cantidad.setValue(x + 1);
                    
                    Platform.runLater(() -> {
                       cantProcesados.setValue("Procesando "+cantidad.getValue()+" de "+items.size()); 
                    });
                    Thread.sleep(5);
                } 
                catch (InterruptedException ex) {
                    this.esperar(x);
                    this.actualizarArticulos(x);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(Sincronizar.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SQLException ex) {
                    Logger.getLogger(Sincronizar.class.getName()).log(Level.SEVERE, null, ex);
                }
                System.out.println("Run "+(x + 1)+" de "+items.size());
            }
        }
        this.terminar();
    }
    
    public void probarConecionTNS(Map<String, String> params) throws ClassNotFoundException, SQLException{
        conTNS = new ConnectionFireBird(params);
        conTNS.getConnection();
    }    
    
    public void probarConecionMySQL(Map<String, String> params) throws ClassNotFoundException, SQLException{
        conMySQL = new ConnectionMySQL(params);
        conMySQL.getConexion();
    }
    
    private void terminar(){
        progressProperty.setValue(1);
        progressIdProperty.setValue(1);
        finalizado.setValue(Boolean.TRUE);        
    }

    private void esperar(int x){
        try{
            Thread.sleep(5000);
        } 
        catch (InterruptedException ex) {
            System.err.println("Exception esperando 5 segundos en metodo esperar(int x) -> "+ex.getMessage());
        }
    }
}
