package PaqueteAPP;

import PaqueteControl.Conexion;
import paqueteDAO.ComidaDAO;
import paqueteDAO.UserDAO;
import paqueteVO.ComidaVO;
import paqueteVO.UserVO;

import java.sql.Connection;
import java.util.List;
import java.util.Scanner;

public class AppController {

    private final Scanner sc = new Scanner(System.in);
    private final UserDAO userDAO = new UserDAO();
    private final ComidaDAO comidaDAO = new ComidaDAO();

    public void iniciar() {
        int opcion;

        do {
            mostrarMenu();
            opcion = sc.nextInt();
            sc.nextLine();

            switch (opcion) {
                case 1:
                    listarUsuarios();
                    break;
                case 2:
                    registrarDieta();
                    break;
                case 3:
                    registrarComida();
                    break;
                case 4:
                    registrarAlimento();
                    break;
                case 5:
                    verMacros();
                    break;
                case 6:
                    objetivoDiario();
                    break;
                case 7:
                    registroDiario();
                    break;
                case 0:
                    System.out.println("Saliendo...");
                    break;
                default:
                    System.out.println("Opcion invalida");
            }

        } while (opcion != 0);
    }

    private void mostrarMenu() {
        System.out.println("\n=== FOOD TRACKER ===");
        System.out.println("1. Ver usuarios");
        System.out.println("2. Registrar dieta");
        System.out.println("3. Registrar comida");
        System.out.println("4. Registrar alimento");
        System.out.println("5. Ver macros");
        System.out.println("6. Objetivo diario");
        System.out.println("7. Registro");
        System.out.println("0. Salir");
    }

    private void listarUsuarios() {
        try (Connection con = Conexion.getConexion()) {
            List<UserVO> usuarios = userDAO.obtenerUsuarios(con);
            for (UserVO u : usuarios) {
                System.out.println(u);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void registrarComida() {
        try {
            System.out.println("Tipo comida (ej: desayuno/comida/cena):");
            String tipo = sc.nextLine();

            ComidaVO comida = new ComidaVO(tipo, 'S');

            try (Connection con = Conexion.getConexion()) {
                comidaDAO.insertarComida(con, comida);
            }

            System.out.println("Comida registrada!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void verMacros() {
        try (Connection con = Conexion.getConexion()) {
            List<ComidaVO> comidas = comidaDAO.obtenerComidas(con);

            System.out.println("Comidas registradas: " + comidas.size());
            for (ComidaVO c : comidas) {
                System.out.println(c.getId_comida() + " - " + c.getTipoComida() + " (" + c.getVisiblesn() + ")");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void registrarDieta() {
        System.out.println("Registrar dieta: no implementado");
    }

    private void registrarAlimento() {
        System.out.println("Registrar alimento: no implementado");
    }

    private void objetivoDiario() {
        System.out.println("Objetivo diario: no implementado");
    }

    private void registroDiario() {
        System.out.println("Registro diario: no implementado");
    }
}
