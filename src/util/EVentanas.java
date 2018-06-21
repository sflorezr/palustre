/*
 * Proyecto Robot FX Propiedad de CHOUCAIR CARDENAS TESTING S. A.
 * el presente proyecto fue iniciativa del equipo de Migracion - BI
 * agradecimiento es pecial al colaborador Jaider Adriam Serrano Sepulveda.
 * Medellin - Colombia 2016.
 */
package util;

/**
 *
 * @author Jaider Adriam Serrano Sepúlveda.
 */
public enum EVentanas {
    PRINCIPAL("FXMLMenu", "FXMLMenu.fxml"),
    CONFIG_BDS("DlgConfiguraciones", "DlgConfiguraciones.fxml");
    
    private final String idVentana;
    private final String fxmlVentana;

    /**
     * @param idVentana Nombre (único) con el cual se identifica una ventana.
     * @param fxmlVentana Nombre del archivo FXML el cual contiene la estructura de la ventana.
     */
    private EVentanas(final String idVentana, final String fxmlVentana) {
        this.idVentana = idVentana;
        this.fxmlVentana = fxmlVentana;
    }

    public String getIdVentana() {
        return idVentana;
    }   

    public String getFxmlVentana() {
        return fxmlVentana;
    } 
}
