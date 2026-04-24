package PaqueteAPP;

import java.util.Scanner;
import paqueteDAO.ComidaDAO;
import paqueteDAO.UserDAO;
public class AppController {

    private final Scanner sc = new Scanner(System.in);
    private final UserDAO userDAO = new UserDAO();
    private final ComidaDAO comidaDAO = new ComidaDAO();
    

    private boolean esquemaAsegurado = false;

    public static void main(String[] args) {
        new AppController().iniciar();
    }

    public void iniciar() {
        int opcion;

        do {
            mostrarMenu();
            opcion = sc.nextInt();

            switch (opcion) {
                case 1:
                    
                    break;
                case 2:
                    
                    break;
                case 3:
                    
                    break;
                case 4:
                    
                    break;
                case 5:
                    
                    break;
                case 6:
                    
                    break;
                case 7:
                    
                    break;
                case 8:
                    
                    break;
                case 9:
                    
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
        System.out.println("1. Crear usuario");
        System.out.println("2. Ver usuarios");
        System.out.println("3. Registrar dieta");
        System.out.println("4. Registrar comida");
        System.out.println("5. Registrar alimento");
        System.out.println("6. Ver macros");
        System.out.println("7. Objetivo diario");
        System.out.println("8. Registro");
        System.out.println("9. Ver dietas");
        System.out.println("0. Salir");
        System.out.print("Selecciona una opcion: ");
    }
}