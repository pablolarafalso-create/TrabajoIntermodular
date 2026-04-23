package PaqueteControl;

import java.sql.Connection;
import java.sql.DriverManager;

public class Conexion {
    public static Connection getConnection() throws Exception{
        try {
            
            String url = "jdbc:mysql://localhost:3306/foodtracker";
            String user = "root";
            String pwd = "mysql";

            return DriverManager.getConnection(url, user, pwd);

        } catch (Exception e) {
            throw (e);
        }
    }

    // Alias para mantener compatibilidad con el resto del cÃ³digo
    public static Connection getConexion() throws Exception {
        return getConnection();
    }
}
