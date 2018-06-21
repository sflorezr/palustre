
import com.google.common.collect.Maps;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import util.ConnectionMySQL;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author ZULAY
 */
public class MainTest {
    
    public static void main(String... args) throws SQLException, ClassNotFoundException{
        Map<String, String> paams = Maps.newHashMap();
        paams.put("HostMySQL", "167.99.152.232");
        paams.put("PortMySQL", "3306");
        paams.put("DataBaseMySQL", "elpalustre_shop");
        paams.put("UserMySQL", "elpalustre_shop");
        paams.put("PasswordMySQL", "KvdBOvDry3");
        
        ConnectionMySQL con = new ConnectionMySQL(paams);
        
        /*List<String> tablas = con.getTablas();
        for(String tabla : tablas){
            System.out.println(tabla);
        }
        
        List<String> campos = con.getCamposTabla("oc_product");
        for(String tabla : campos){
            System.out.println(tabla);
        }*/
        
        String sql = "Select * From oc_product";
        ResultSet rs = con.consultar(sql);
        
        while(rs.next()){
            System.err.println("**********************************");
            System.err.println(rs.getString(1));
            System.err.println(rs.getString(2));
            System.err.println(rs.getString(3));
            System.err.println(rs.getString(4));
            System.err.println(rs.getString(5));
            System.err.println(rs.getString(6));
            System.err.println(rs.getString(7));
            System.err.println(rs.getString(8));
            System.err.println(rs.getString(9));
            System.err.println(rs.getString(10));
            System.err.println(rs.getString(11));
            System.err.println(rs.getString(12));
            System.err.println(rs.getString(13));
            System.err.println(rs.getString(14));
            System.err.println(rs.getString(15));
            System.err.println(rs.getString(16));
            System.err.println(rs.getString(17));
            System.err.println(rs.getString(18));
            System.err.println(rs.getString(19));
            System.err.println(rs.getString(20));
            
            
        }
        
        
        
        
        
        
        
    }
    
}
