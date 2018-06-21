package util;

/**
 *
 * @author Jaider Adriam Serrano Sepulveda
 */
public class Material {
    
    private String codigo;
    private String total;
    private String existenciaBodega;
    private String precio;

    public Material(String codigo, String existenciaBodega, String total, String precio) {
        this.codigo = codigo;
        this.total = total;
        this.existenciaBodega = existenciaBodega;
        this.precio = precio;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getExistenciaBodega() {
        return existenciaBodega;
    }

    public void setExistenciaBodega(String existenciaBodega) {
        this.existenciaBodega += existenciaBodega;
    }

    public String getTotal(){
        return this.total;
    }
    
    public void setTotal(String total){
        this.total = total;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    @Override
    public String toString() {
        return "Material{" + "Codigo = " + codigo + ", Total = " + total + ", Precio = "+precio+"}";
    }

}
