/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import util.Util;

/**
 *
 * @author ZULAY
 */
public class DlgPrincipal extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("DlgPrincipal.fxml"));
        
        Scene scene = new Scene(root);        
        stage.setScene(scene);
        stage.setTitle("INTERFAZ DE CONEXION TNS - MySQL");
        stage.getIcons().add(new Image(Util.parametros.get("logo")));
        stage.setMaxHeight(435);
        stage.setMaxWidth(635); 
        Util.stage = stage;
        
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
