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
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ArchivoEscribir {

    private final String ruta;
    private File archivo;
    private BufferedWriter bWriter;
    
    public ArchivoEscribir(String ruta){
        this.ruta = ruta;
    }
    /**
     * 
     * @return La ruta completa del archivo ejemplo: C:\Ruta\Archiuvo.log.
     */
    public String getRuta(){
        return this.ruta;
    }
    /**
     * Metodo para crear el objeto BufferedWriter el cual se usa para escribir en el archivo.
     * @throws IOException Exepcion de salida en caso de algun evento inesperado.
     */
    private void crearBWriter() throws IOException{
        if(this.archivo != null && this.archivo.exists()){
            FileWriter fWriter = new FileWriter(archivo, false);
	    bWriter = new BufferedWriter(fWriter);
        }
    }
    /**
    * Metodo para cerrar el objeto BufferedWriter al invocar este metodo cierra 
    * la edicion del archivo y guarda las modificaciones realizadas.
    * @throws IOException Exepcion de salida en caso de algun evento inesperado.
    */
    public void close() throws IOException{
        if(bWriter != null){
            bWriter.close();
        }
    }
    /**
     * Consulta la ruta especificada al crear el objeto si no existe el archivo 
     * lo crea.
     * @param crearArchivo Indica si creo el archivo nuevamente en caso de que exista, esto con el fin
     * de eliminar el contenido del archivo para escribir nuevos valores.
     * @throws IOException Exepcion de salida en caso de algun evento inesperado al crear el archivo.
     */
    public void crearArchivo(boolean crearArchivo)throws IOException{
        archivo = new File(ruta);

        if(crearArchivo){
            archivo.delete();
            archivo.createNewFile();
        }
        crearBWriter();
	if(!archivo.exists() && archivo.isFile()){
	    archivo.createNewFile();
	}
        
    }//Fin metodo crearArchivo.
    /**
     * Permite agregar una linea adicional al archivo.
     * @param linea Linea a insertar en el archivo.
     * @throws IOException Exepcion de salida en caso de algun evento inesperado.
     */
    public void escribir(String linea) throws IOException{
        if(this.archivo != null && this.archivo.exists()){
            bWriter.write(linea);
            bWriter.newLine();
            bWriter.flush();
        }
        
    }//Fin metodo escribir.    

}//Fin clase ArchivoEscribir.
