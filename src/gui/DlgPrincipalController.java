/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import com.google.common.collect.Lists;
import java.io.File;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import util.ConnectionFireBird;
import util.ConnectionMySQL;
import util.DialogBuilder;
import util.Material;
import util.Sincronizar;
import util.Util;

/**
 *
 * @author ZULAY
 */
public class DlgPrincipalController implements Initializable {
    @FXML
    private TabPane tab;
    @FXML
    private TextField txtFechaAct;
    @FXML
    private Label lblProcesando;
    @FXML
    private ProgressBar pBarUpdate;
    @FXML
    private ProgressIndicator progressId;
    @FXML
    private Button btnSincronizar;
    @FXML
    private Button btnConexiones;    
    // Datos conexion FireBird    
    @FXML
    private TextField txtHost;
    @FXML
    private TextField txtPuerto;
    @FXML
    private TextField txtUsuario;
    @FXML
    private PasswordField txtClave;
    @FXML
    private TextField txtRutaGDB;
    // Datos conexion MySQL
    @FXML
    private TextField txtHostMS;
    @FXML
    private TextField txtPuertoMS;
    @FXML
    private TextField txtNombreBD;
    @FXML
    private TextField txtUsuarioMS;
    @FXML
    private PasswordField txtClaveMS;
    @FXML
    private Button btnGuardar;
    @FXML
    private Button btnSalir;
    
    private Map<String, String> params;
    private Sincronizar sincronizar;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.pBarUpdate.setProgress(-1);
        this.progressId.setProgress(-1);
        
        this.params = Util.parametros;
        this.cargarFirebirdInfo();
        this.cargarMySQLInfo();
        this.cargarFechaUltimaAct();
        this.sincronizar = new Sincronizar(new ConnectionFireBird(params), new ConnectionMySQL(params));

        this.sincronizar.getFinalizado().addListener((observable, oldValue, newValue) -> {
            if(newValue){
                Platform.runLater(() -> {
                    this.habilitarGUI(false);

                    this.pBarUpdate.progressProperty().unbind();
                    this.progressId.progressProperty().unbind();
                    this.lblProcesando.textProperty().unbind();                    

                    this.actualizarFechaSincronizacion();
                    DialogBuilder.create().title("¡Sincronizacion Finalizada!").
                        contentText("¡Se actualizason "+this.sincronizar.getItems().size()+" Articulos en MySQL!")
                        .alert(Alert.AlertType.INFORMATION).build().showAndWait();
                    this.sincronizar.interrupt();
                });
            }
        });
    }
    
    private void cargarFirebirdInfo(){
        txtHost.setText(params.get(Util.HOST_TNS));
        txtPuerto.setText(params.get(Util.PORT_TNS));
        txtUsuario.setText(params.get(Util.USER_TNS));
        txtClave.setText(params.get(Util.PASSWORD_TNS));
        txtRutaGDB.setText(params.get(Util.DB_TNS));
    }
    private void cargarMySQLInfo(){
        txtHostMS.setText(params.get(Util.HOST_MYSQL));
        txtPuertoMS.setText(params.get(Util.PORT_MYSQL));
        txtUsuarioMS.setText(params.get(Util.USER_MYSQL));
        txtClaveMS.setText(params.get(Util.PASSWORD_MYSQL));
        txtNombreBD.setText(params.get(Util.DB_MYSQL));
    }    
    private void cargarFechaUltimaAct(){
        String sql = "SELECT FECHA FROM VARIOS WHERE VARIAB = 'FECHA_ACT_MYSQL'";
        ConnectionFireBird conTNS = new ConnectionFireBird(params);
        
        try {
            ResultSet rs = conTNS.consultar(sql);
            
            while(rs.next()){
                Date fecha = rs.getDate("FECHA");
                txtFechaAct.setText(Util.getFecha(fecha));
                Util.fechaUltimaAct = txtFechaAct.getText().substring(0, 10);
                System.out.println("Fecha ultima actualizacion: "+Util.fechaUltimaAct);
            }            
        } 
        catch (ClassNotFoundException | SQLException ex) {
            DialogBuilder.create().title("¡Consulta ultima fecha de actualizacion con TNS Fallida!").
                    contentText("¡Error intentando onexion con TNS!\nFavor revise parametros de conexion")
                    .exception(ex).alert(Alert.AlertType.ERROR).build().showAndWait();
            tab.getSelectionModel().select(1);        
        }        
    }
    
    @FXML
    private void cursorMano(){
        txtHost.getScene().setCursor(Cursor.HAND);
    }
    @FXML
    private void cursorNormal(){
        txtHost.getScene().setCursor(Cursor.DEFAULT);
    }        
    @FXML
    private void salirAction(ActionEvent event) {
        this.pBarUpdate.progressProperty().unbind();
        this.pBarUpdate.setProgress(-1);
        this.progressId.progressProperty().unbind();
        this.progressId.setProgress(-1);        
        System.exit(0);
    }    
    @FXML
    private void guardarAction(ActionEvent event) {
        if(!this.validarCamposTNS() || !this.validarCamposMySQL()){
            return;
        }
        tab.getSelectionModel().select(0);
        
        this.params.put(Util.HOST_TNS, txtHost.getText());
        this.params.put(Util.PORT_TNS, txtPuerto.getText());
        this.params.put(Util.USER_TNS, txtUsuario.getText());        
        this.params.put(Util.PASSWORD_TNS, txtClave.getText());
        this.params.put(Util.DB_TNS, txtRutaGDB.getText());

        this.params.put(Util.HOST_MYSQL, txtHostMS.getText());
        this.params.put(Util.PORT_MYSQL, txtPuertoMS.getText());
        this.params.put(Util.USER_MYSQL, txtUsuarioMS.getText());        
        this.params.put(Util.PASSWORD_MYSQL, txtClaveMS.getText());
        this.params.put(Util.DB_MYSQL, txtNombreBD.getText());
        
        if(Util.guardarParametros()){
            DialogBuilder.create().title("¡Parametros Guardados!").contentText("Datos Actualizados correctamente. Archivo: "+Util.ruta)
                .alert(Alert.AlertType.INFORMATION).build().showAndWait();       
        }
    }        
    @FXML
    private void rutaGDBAction(){
        this.seleccionarRutaArchivo(txtRutaGDB);
    }        
    
    //******************************************************************//
    private void seleccionarRutaArchivo(TextField textField){
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Todos los Archivos", "*.*"),
                new FileChooser.ExtensionFilter("Firebird files", "*.GDB"));
        
        fileChooser.setTitle("Buscar ruta del archivo ");
        File selectedFile = fileChooser.showOpenDialog(Util.stage);
                 
        if(selectedFile == null){
            textField.setText("");
        }else{
            textField.setText(selectedFile.getAbsolutePath());
        }        
    }
    
    private boolean validarCamposTNS(){
        tab.getSelectionModel().select(1);
        
        if(txtHost.getText().isEmpty()){
            DialogBuilder.create().title("¡Faltan Datos!").contentText("Debe digitar el valor del campo Host").
                    alert(Alert.AlertType.ERROR).build().showAndWait();
            txtHost.requestFocus();
            return false;
        }
        if(txtPuerto.getText().isEmpty()){
            DialogBuilder.create().title("¡Faltan Datos!").contentText("Debe digitar el valor del campo Host").
                    alert(Alert.AlertType.ERROR).build().showAndWait();
            txtPuerto.requestFocus();
            return false;
        }
        if(txtUsuario.getText().isEmpty()){
            DialogBuilder.create().title("¡Faltan Datos!").contentText("Debe digitar el valor del campo Host").
                    alert(Alert.AlertType.ERROR).build().showAndWait();
            txtUsuario.requestFocus();
            return false;
        }
        if(txtClave.getText().isEmpty()){
            DialogBuilder.create().title("¡Faltan Datos!").contentText("Debe digitar el valor del campo Host").
                    alert(Alert.AlertType.ERROR).build().showAndWait();
            txtClave.requestFocus();
            return false;
        }
        if(txtRutaGDB.getText().isEmpty()){
            DialogBuilder.create().title("¡Faltan Datos!").contentText("Debe digitar el valor del campo Host").
                    alert(Alert.AlertType.ERROR).build().showAndWait();
            txtRutaGDB.requestFocus();
            return false;
        }
        return true;
    }
    
    private boolean validarCamposMySQL(){
        tab.getSelectionModel().select(2);
        
        if(txtHostMS.getText().isEmpty()){
            DialogBuilder.create().title("¡Faltan Datos!").contentText("Debe digitar el valor del campo Host").
                    alert(Alert.AlertType.ERROR).build().showAndWait();
            txtHostMS.requestFocus();
            return false;
        }
        if(txtPuertoMS.getText().isEmpty()){
            DialogBuilder.create().title("¡Faltan Datos!").contentText("Debe digitar el valor del campo Host").
                    alert(Alert.AlertType.ERROR).build().showAndWait();
            txtPuertoMS.requestFocus();
            return false;
        }
        if(txtUsuarioMS.getText().isEmpty()){
            DialogBuilder.create().title("¡Faltan Datos!").contentText("Debe digitar el valor del campo Host").
                    alert(Alert.AlertType.ERROR).build().showAndWait();
            txtUsuarioMS.requestFocus();
            return false;
        }
        if(txtClaveMS.getText().isEmpty()){
            DialogBuilder.create().title("¡Faltan Datos!").contentText("Debe digitar el valor del campo Host").
                    alert(Alert.AlertType.ERROR).build().showAndWait();
            txtClaveMS.requestFocus();
            return false;
        }
        if(txtNombreBD.getText().isEmpty()){
            DialogBuilder.create().title("¡Faltan Datos!").contentText("Debe digitar el valor del campo Host").
                    alert(Alert.AlertType.ERROR).build().showAndWait();
            txtNombreBD.requestFocus();
            return false;
        }
        return true;        
    }
    
    @FXML
    private void probarConecionesAction(){
        try {
            this.sincronizar.probarConecionTNS(params);
        } catch (SQLException | ClassNotFoundException ex) {
            DialogBuilder.create().title("¡Conexion TNS Fallida!").
                    contentText("¡Error intentando onexion con TNS!\nFavor revise parametros de conexion")
                    .exception(ex).alert(Alert.AlertType.ERROR).build().showAndWait();
            tab.getSelectionModel().select(1);
        } 
        
        try {
            this.sincronizar.probarConecionMySQL(params);
        } catch (SQLException | ClassNotFoundException ex) {
            DialogBuilder.create().title("¡Conexion MySQL Fallida!").
                    contentText("¡Error intentando conexion con MySQL!\nFavor revise parametros de conexion")
                    .exception(ex).alert(Alert.AlertType.ERROR).build().showAndWait();
            tab.getSelectionModel().select(2);
            return;
        }
        DialogBuilder.create().title("¡Conexiones OK!").
                contentText("¡Conexion con bases de datos establecidas!")
                .alert(Alert.AlertType.INFORMATION).build().showAndWait();
    }
    
    private List<Material> articulosParaSincronizar(){
        List<Material> items = Lists.newArrayList();
        try{
            items = this.sincronizar.articulosParaSincronizar();
        } 
        catch (SQLException |ClassNotFoundException ex) {
            DialogBuilder.create().title("¡Conexion TNS Interrumpida!").
                    contentText("¡Error intentando consultar en TNS!")
                    .exception(ex).alert(Alert.AlertType.ERROR).build().showAndWait();
        }
        return items;
    }
    
    private void actualizarFechaSincronizacion(){
        try {
            this.sincronizar.actualizarFechaSincronizacion();
        } 
        catch (ClassNotFoundException | SQLException ex) {
            Platform.runLater(() -> {
                DialogBuilder.create().title("¡Actualizacion TNS Interrumpida!").
                        contentText("¡Error intentando actualizar ultia fecha de sincronizacion en TNS!")
                        .exception(ex).alert(Alert.AlertType.ERROR).build().showAndWait();
            });
        }
    }    

    @FXML
    private void sincronizarAction(){
        this.pBarUpdate.progressProperty().unbind();
        this.pBarUpdate.progressProperty().bind(this.sincronizar.getProgressProperty());
        this.progressId.progressProperty().unbind();
        this.progressId.progressProperty().bind(pBarUpdate.progressProperty());
        this.lblProcesando.textProperty().unbind();
        this.lblProcesando.textProperty().bind(this.sincronizar.getCantProcesados());
        this.habilitarGUI(true);
        
        List<Material> items = this.articulosParaSincronizar();
        if(items != null && !items.isEmpty()){
            this.sincronizar.setItems(items);            
            this.sincronizar.start();
        }
    }
    
    private void habilitarGUI(boolean value){
        if(value){
           this.btnSincronizar.setText("Procesando");
        }
        else{
            this.btnSincronizar.setText("Sincronizar");
        }
        this.btnSincronizar.setDisable(value);
        this.btnConexiones.setDisable(value);
        this.btnGuardar.setDisable(value);
        this.btnSalir.setDisable(value);
    }

}
