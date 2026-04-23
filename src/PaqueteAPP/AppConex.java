package PaqueteAPP;

import PaqueteControl.Conexion;

import java.sql.Connection;

public class AppConex {
    public static void main(String[] args) {
        try (Connection conex = Conexion.getConexion()) {
            System.out.println("Conexion realizada con exito");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

