/*
 * Proyecto Robot FX Propiedad de CHOUCAIR CARDENAS TESTING S. A.
 * el presente proyecto fue iniciativa del equipo de Migracion - BI
 * agradecimiento es pecial al colaborador Jaider Adriam Serrano Sepulveda.
 * Medellin - Colombia 2016.
 */
package util;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ArchivoLeer {

    private final String ruta;
    
    public ArchivoLeer(String ruta){
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
     * Permite leer el contenido de un archivo y guardar su contenido en una lista
     * agregando solo los valores unicos.
     * @return Lista con el contenido del archivo sin valores repetidos.
     * @throws IOException Exepcion de salida en caso de algun evento inesperado.
     */
    public List<String> leerValoresUnicos() throws IOException{
        List<String> result = Lists.newArrayList();
        Map<String, String> mapa = Maps.newHashMap();
        File file = new File(this.ruta);
        FileReader fReader = new FileReader(file);
        BufferedReader bReader = new BufferedReader(fReader);
        String linea;
        
        while((linea = bReader.readLine()) != null){
            if(!linea.isEmpty()){
                if(mapa.get(linea) == null){
                    mapa.put(linea, linea);
                }
                else{
                    System.out.println("Valor Repetido: "+linea);
                }
            }                
        }
        mapa.values().forEach((valor) -> {            
            result.add(valor);
            //System.err.println(valor);
                    });
        fReader.close();
        Collections.sort(result);        
        return result;
    }//Fin metodo leerValoresUnicos.

    /**
     * Permite leer el contenido de un archivo y guardar en una lista
     * agregando solo los valores repetidos dentro del archivo.
     * @return Lista con el contenido de los valores repetidos de un archivo.
     * @throws IOException Exepcion de salida en caso de algun evento inesperado.
     */
    public List<String> leerValoresRepetidos() throws IOException{
        List<String> result = Lists.newArrayList();
        File file = new File(this.ruta);
        FileReader fReader = new FileReader(file);
        BufferedReader bReader = new BufferedReader(fReader);
        String linea;
        
        while((linea = bReader.readLine()) != null){
            result.add(linea);
        }
        fReader.close();
        return result;        
    }//Fin metodo leerValoresRepetidos.
    
    /**
     * Permite leer el contenido de un archivo y guardar en una lista
     * agregando solo los valores repetidos dentro del archivo.
     * @return Lista con el contenido de los valores repetidos de un archivo.
     * @throws IOException Exepcion de salida en caso de algun evento inesperado.
     */
    public Map<String, String> leerParametros() throws IOException{
        Map<String, String> result = Maps.newHashMap();
        File file = new File(this.ruta);
        FileReader fReader = new FileReader(file);
        BufferedReader bReader = new BufferedReader(fReader);
        String linea;
        
        while((linea = bReader.readLine()) != null){
            String parametro[] = linea.split("=");
            result.put(parametro[0], parametro[1]);
        }
        fReader.close();
        return result;        
    }//Fin metodo leerValoresRepetidos.

    /**
     * Permite leer el contenido de un archivo y guardar en una lista
     * agregando todos los valores del archivo.
     * @param pos posicion desde donde se tomaran los datos.
     * @return Lista con el contenido de los valores repetidos de un archivo.
     * @throws IOException Exepcion de salida en caso de algun evento inesperado.
     */
    public List<String> leerValores(int pos) throws IOException{
        List<String> result = Lists.newArrayList();
        File file = new File(this.ruta);
        FileReader fReader = new FileReader(file);
        BufferedReader bReader = new BufferedReader(fReader);
        String linea;
        int posicion = 0;
        
        while((linea = bReader.readLine()) != null){
            if(posicion >= pos){
                result.add(linea);
            } 
            posicion++;
        }
        fReader.close();
        return result;
    }//Fin metodo leerValores.
    
    public StringBuilder getContenido() throws IOException{
        StringBuilder result = new StringBuilder();
        List<String> valores = leerValores(0);
        
        valores.forEach((valor) -> {
            result.append(valor+"\n");
        });
        return result;
    }

}//Fin class ArchivoLeer.