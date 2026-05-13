package PaqueteAPP;

import PaqueteControl.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import paqueteDAO.AlimentoDAO;
import paqueteDAO.ComidaAlimentoDAO;
import paqueteDAO.ComidaDAO;
import paqueteDAO.ObjetivoDiarioDAO;
import paqueteDAO.RegistroDiarioComidaDAO;
import paqueteDAO.RegistroDiarioDAO;
import paqueteDAO.UserDAO;
import paqueteVO.AlimentoVO;
import paqueteVO.ComidaVO;
import paqueteVO.Objetivo_diarioVO;
import paqueteVO.Registro_diarioVO;
import paqueteVO.UserVO;
public class AppController {

    private final Scanner sc = new Scanner(System.in);
    private final UserDAO userDAO = new UserDAO();
    private final ComidaDAO comidaDAO = new ComidaDAO();
    private final AlimentoDAO alimentoDAO = new AlimentoDAO();
    private final ComidaAlimentoDAO comidaAlimentoDAO = new ComidaAlimentoDAO();
    private final ObjetivoDiarioDAO objetivoDiarioDAO = new ObjetivoDiarioDAO();
    private final RegistroDiarioDAO registroDiarioDAO = new RegistroDiarioDAO();
    private final RegistroDiarioComidaDAO registroDiarioComidaDAO = new RegistroDiarioComidaDAO();
    

    private boolean esquemaAsegurado = false;

    public static void main(String[] args) {
        new AppController().iniciar();
    }

    public void iniciar() {
        int opcion;

        do {
            asegurarDatosBase();
            mostrarMenu();
            opcion = leerEntero();

            switch (opcion) {
                case 1:
                    crearUsuario();
                    break;
                case 2:
                    verUsuarios();
                    break;
                case 3:
                    registrarDieta();
                    break;
                case 4:
                    registrarComida();
                    break;
                case 5:
                    registrarAlimento();
                    break;
                case 6:
                    verMacros();
                    break;
                case 7:
                    objetivoDiario();
                    break;
                case 8:
                    verRegistro();
                    break;
                case 9:
                    verDietas();
                    break;
                case 0:
                    System.out.println("Saliendo...");
                    break;
                default:
                    System.out.println("Opcion invalida");
            }

        } while (opcion != 0);
    }

    private void asegurarDatosBase() {
        if (esquemaAsegurado) {
            return;
        }

        try (Connection con = Conexion.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT COUNT(*) AS total FROM comida");
            ResultSet rs = ps.executeQuery()) {

            int total = 0;
            if (rs.next()) {
                total = rs.getInt("total");
            }

            if (total == 0) {
                comidaDAO.insert(new ComidaVO("Desayuno", 'S'));
                comidaDAO.insert(new ComidaVO("Comida", 'S'));
                comidaDAO.insert(new ComidaVO("Cena", 'S'));
                comidaDAO.insert(new ComidaVO("Snack", 'S'));
            }

            esquemaAsegurado = true;

        } catch (Exception e) {
            System.out.println("Aviso: no se pudo comprobar/precargar datos base (BD no disponible).");
        }
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

    private int leerEntero() {
        while (true) {
            String linea = sc.nextLine().trim();
            try {
                return Integer.parseInt(linea);
            } catch (NumberFormatException e) {
                System.out.print("Entrada no valida. Introduce un numero: ");
            }
        }
    }

    private int leerEnteroConPrompt(String prompt) {
        System.out.print(prompt);
        return leerEntero();
    }

    private double leerDouble(String prompt) {
        System.out.print(prompt);
        while (true) {
            String linea = sc.nextLine().trim();
            try {
                // Acepta coma decimal (p.ej. "1,75")
                String normalizada = linea.replace(',', '.');
                return Double.parseDouble(normalizada);
            } catch (NumberFormatException e) {
                System.out.print("Entrada no valida. Introduce un numero: ");
            }
        }
    }

    private double normalizarAlturaEnMetros(double alturaIntroducida) {
        // Si el usuario introduce altura en mm o cm, conviertelo a metros.
        double altura = alturaIntroducida;
        if (altura > 1000) {
            // mm -> m (ej. 1700 mm -> 1.7 m)
            altura = altura / 1000.0;
        } else if (altura > 10) {
            // cm -> m (ej. 170 cm -> 1.7 m)
            altura = altura / 100.0;
        }
        // Redondeo a 2 decimales para encajar bien en DECIMAL(3,2)
        return Math.round(altura * 100.0) / 100.0;
    }

    private double normalizarPesoEnKg(double pesoIntroducido) {
        // Si el usuario introduce peso en gramos (ej. 70000), conviertelo a kg.
        double peso = pesoIntroducido;
        if (peso > 1000) {
            peso = peso / 1000.0;
        }
        return Math.round(peso * 100.0) / 100.0;
    }

    private double redondear(double valor, int decimales) {
        if (decimales < 0) {
            return valor;
        }
        double factor = Math.pow(10, decimales);
        return Math.round(valor * factor) / factor;
    }

    private DecimalSpec obtenerDecimalSpec(Connection con, String tabla, String columna) throws SQLException {
        int precision = -1;
        int scale = -1;

        try (ResultSet rs = con.getMetaData().getColumns(con.getCatalog(), null, tabla, null)) {
            while (rs.next()) {
                String col = rs.getString("COLUMN_NAME");
                if (col == null || !col.equalsIgnoreCase(columna)) {
                    continue;
                }
                precision = rs.getInt("COLUMN_SIZE");
                scale = rs.getInt("DECIMAL_DIGITS");
                break;
            }
        }

        return new DecimalSpec(precision, scale);
    }

    private double maximoParaDecimal(int precision, int scale) {
        if (precision <= 0 || scale < 0 || scale > precision) {
            return Double.POSITIVE_INFINITY;
        }
        int enteros = precision - scale;
        // Max = 10^enteros - 10^-scale (ej. DECIMAL(6,2) => 9999.99)
        double max = Math.pow(10, enteros) - Math.pow(10, -scale);
        return max;
    }

    private double leerDecimalDentroDeRango(String prompt, int precision, int scale) {
        double max = maximoParaDecimal(precision, scale);
        while (true) {
            double valor = leerDouble(prompt);
            if (Double.isNaN(valor) || Double.isInfinite(valor)) {
                System.out.println("Valor no valido. Intentalo de nuevo.");
                continue;
            }
            if (!Double.isInfinite(max) && Math.abs(valor) > max) {
                System.out.println("Valor fuera de rango. Max permitido: " + max + ". Intentalo de nuevo.");
                continue;
            }
            return redondear(valor, scale >= 0 ? scale : 2);
        }
    }

    private static final class DecimalSpec {
        final int precision;
        final int scale;

        DecimalSpec(int precision, int scale) {
            this.precision = precision;
            this.scale = scale;
        }
    }

    private String leerTexto(String prompt) {
        System.out.print(prompt);
        return sc.nextLine().trim();
    }

    private LocalDate leerFecha(String prompt) {
        System.out.print(prompt);
        while (true) {
            String linea = sc.nextLine().trim();
            try {
                return LocalDate.parse(linea);
            } catch (DateTimeParseException e) {
                System.out.print("Formato no valido. Usa AAAA-MM-DD: ");
            }
        }
    }

    private void crearUsuario() {
        System.out.println("\n--- Crear usuario ---");
        String nombre = leerTexto("Nombre: ");
        String apellidos = leerTexto("Apellidos: ");
        String email = leerTexto("Email: ");
        String contrasena = leerTexto("Contrasena: ");
        LocalDate fechaNacimiento = leerFecha("Fecha nacimiento (AAAA-MM-DD): ");
        double altura = normalizarAlturaEnMetros(leerDouble("Altura (cm): "));
        double peso = normalizarPesoEnKg(leerDouble("Peso (kg): "));

        UserVO user = new UserVO(
            apellidos,
            contrasena,
            email,
            0,
            nombre,
            fechaNacimiento,
            altura,
            peso,
            LocalDate.now(),
            0
        );

        boolean ok = userDAO.registrarUsuario(user);
        System.out.println(ok ? "Usuario creado correctamente." : "No se pudo crear el usuario.");
    }

    private void verUsuarios() {
        System.out.println("\n--- Usuarios ---");
        try (Connection con = Conexion.getConnection()) {
            List<UserVO> usuarios = userDAO.obtenerUsuarios(con);
            if (usuarios.isEmpty()) {
                System.out.println("No hay usuarios.");
                return;
            }
            for (UserVO u : usuarios) {
                System.out.println(u);
            }
        } catch (Exception e) {
            System.out.println("No se pudieron cargar los usuarios (BD no disponible).");
        }
    }

    private void registrarDieta() {
        System.out.println("\n--- Registrar dieta (registro diario) ---");
        int idUser = leerEnteroConPrompt("Id usuario: ");

        int registroId = registroDiarioDAO.crearRegistro(LocalDateTime.now(), idUser);
        if (registroId <= 0) {
            System.out.println("No se pudo crear el registro diario.");
            return;
        }

        System.out.println("Registro creado con id: " + registroId);
        System.out.println("Elige una comida por tipo (0 para saltar cada tipo).");

        List<ComidaVO> comidasVisibles = comidaDAO.listVisibles();
        if (comidasVisibles.isEmpty()) {
            System.out.println("(No hay comidas visibles)");
            return;
        }

        elegirYAnadirComidaPorTipo(registroId, "Desayuno", comidasVisibles);
        elegirYAnadirComidaPorTipo(registroId, "Comida", comidasVisibles);
        elegirYAnadirComidaPorTipo(registroId, "Cena", comidasVisibles);
        elegirYAnadirComidaPorTipo(registroId, "Snack", comidasVisibles);
    }

    private void registrarComida() {
        System.out.println("\n--- Registrar comida ---");
        String tipo = leerTexto("Tipo de comida: ");
        String visibleStr = leerTexto("Visible? (S/N): ");
        char visible = (visibleStr.isEmpty() ? 'S' : Character.toUpperCase(visibleStr.charAt(0)));
        if (visible != 'S' && visible != 'N') {
            visible = 'S';
        }

        int comidaId = comidaDAO.insertAndReturnId(new ComidaVO(tipo, visible));
        if (comidaId <= 0) {
            System.out.println("No se pudo crear la comida.");
            return;
        }

        System.out.println("Comida creada con id: " + comidaId);
        System.out.println("Anade alimentos a la comida (0 para terminar).");
        listarAlimentos();

        while (true) {
            int alimentoId = leerEnteroConPrompt("Id alimento: ");
            if (alimentoId == 0) {
                break;
            }
            int cantidad = leerEnteroConPrompt("Cantidad (unidades): ");
            comidaAlimentoDAO.addAlimento(comidaId, alimentoId, cantidad);
            System.out.println("Alimento vinculado.");
        }
    }

    private void registrarAlimento() {
        System.out.println("\n--- Registrar alimento ---");
        String nombre = leerTexto("Nombre: ");
        int kcal = leerEnteroConPrompt("Kcal: ");
        double proteinas = leerDouble("Proteinas: ");
        double carbohidratos = leerDouble("Carbohidratos: ");
        double grasas = leerDouble("Grasas: ");

        AlimentoVO alimento = new AlimentoVO(carbohidratos, grasas, 0, kcal, nombre, proteinas);
        int id = alimentoDAO.insertar(alimento);
        System.out.println(id > 0 ? ("Alimento creado con id: " + id) : "No se pudo crear el alimento.");
    }

    private void verMacros() {
        System.out.println("\n--- Ver macros de una comida ---");
        listarComidasVisibles();
        int comidaId = leerEnteroConPrompt("Id comida: ");

        String sql =
            "SELECT a.nombre, a.kcal, a.proteinas, a.carbohidratos, a.grasas, ca.cantidad " +
                "FROM comida_alimento ca " +
                "JOIN alimento a ON a.id_alimento = ca.id_alimento " +
                "WHERE ca.id_comida = ?";

        double totalKcal = 0;
        double totalP = 0;
        double totalC = 0;
        double totalG = 0;

        try (Connection con = Conexion.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, comidaId);
            try (ResultSet rs = ps.executeQuery()) {
                boolean hay = false;
                while (rs.next()) {
                    hay = true;
                    int cantidad = rs.getInt("cantidad");
                    double kcal = rs.getDouble("kcal") * cantidad;
                    double p = rs.getDouble("proteinas") * cantidad;
                    double c = rs.getDouble("carbohidratos") * cantidad;
                    double g = rs.getDouble("grasas") * cantidad;

                    totalKcal += kcal;
                    totalP += p;
                    totalC += c;
                    totalG += g;

                    System.out.println(
                        rs.getString("nombre") + " x" + cantidad +
                            " -> kcal=" + kcal +
                            ", P=" + p +
                            ", C=" + c +
                            ", G=" + g
                    );
                }

                if (!hay) {
                    System.out.println("La comida no tiene alimentos asociados.");
                    return;
                }
            }

            System.out.println("TOTAL -> kcal=" + totalKcal + ", P=" + totalP + ", C=" + totalC + ", G=" + totalG);

        } catch (Exception e) {
            System.out.println("No se pudieron calcular los macros (BD no disponible).");
        }
    }

    private void objetivoDiario() {
        System.out.println("\n--- Objetivo diario ---");
        int idUser = leerEnteroConPrompt("Id usuario: ");

        Objetivo_diarioVO existente = objetivoDiarioDAO.obtenerPorIdUsuario(idUser);
        if (existente != null) {
            System.out.println("Actual: " + existente);
        } else {
            System.out.println("No hay objetivo registrado para este usuario.");
        }

        String editar = leerTexto("Quieres crear/actualizar objetivo? (S/N): ");
        if (editar.isEmpty() || Character.toUpperCase(editar.charAt(0)) != 'S') {
            return;
        }

        int kcal = leerEnteroConPrompt("Kcal: ");

        // Validar contra el rango real del DECIMAL en MySQL para evitar "Out of range".
        DecimalSpec specProte = new DecimalSpec(-1, 2);
        DecimalSpec specCarbs = new DecimalSpec(-1, 2);
        DecimalSpec specGrasas = new DecimalSpec(-1, 2);
        try (Connection con = Conexion.getConnection()) {
            DecimalSpec p = obtenerDecimalSpec(con, "objetivo_diario", "proteinas");
            DecimalSpec c = obtenerDecimalSpec(con, "objetivo_diario", "carbohidratos");
            DecimalSpec g = obtenerDecimalSpec(con, "objetivo_diario", "grasas");
            if (p.precision > 0) {
                specProte = p;
            }
            if (c.precision > 0) {
                specCarbs = c;
            }
            if (g.precision > 0) {
                specGrasas = g;
            }
        } catch (Exception e) {
            // Si la BD no esta disponible, seguimos sin validacion por esquema.
        }

        double proteinas = leerDecimalDentroDeRango("Proteinas: ", specProte.precision, specProte.scale);
        double carbohidratos = leerDecimalDentroDeRango("Carbohidratos: ", specCarbs.precision, specCarbs.scale);
        double grasas = leerDecimalDentroDeRango("Grasas: ", specGrasas.precision, specGrasas.scale);

        Objetivo_diarioVO nuevo = new Objetivo_diarioVO(carbohidratos, grasas, idUser, kcal, proteinas);
        objetivoDiarioDAO.inserta(nuevo);
        System.out.println("Objetivo guardado.");
    }

    private void verRegistro() {
        System.out.println("\n--- Registro ---");
        int idUser = leerEnteroConPrompt("Id usuario: ");
        String usarFecha = leerTexto("Filtrar por fecha? (S/N): ");

        List<Registro_diarioVO> registros;
        if (!usarFecha.isEmpty() && Character.toUpperCase(usarFecha.charAt(0)) == 'S') {
            LocalDate fecha = leerFecha("Fecha (AAAA-MM-DD): ");
            registros = registroDiarioDAO.buscarRegistrosPorFecha(idUser, fecha);
        } else {
            registros = registroDiarioDAO.obtenerRegistrosDelDiaActual(idUser);
        }

        if (registros.isEmpty()) {
            System.out.println("No hay registros para mostrar.");
            return;
        }

        for (Registro_diarioVO r : registros) {
            System.out.println(r);
            List<ComidaVO> comidas = registroDiarioComidaDAO.listarComidasDeRegistro(r.getRegistro_id());
            for (ComidaVO c : comidas) {
                System.out.println("  - " + c);
            }
        }
    }

    private void verDietas() {
        System.out.println("\n--- Dietas (registros diarios) ---");
        String usarHoy = leerTexto("Ver solo hoy? (S/N): ");
        List<Registro_diarioVO> registros =
            (!usarHoy.isEmpty() && Character.toUpperCase(usarHoy.charAt(0)) == 'S')
                ? registroDiarioDAO.obtenerRegistrosDelDiaActual()
                : registroDiarioDAO.obtenerTodosLosRegistros();

        if (registros.isEmpty()) {
            System.out.println("No hay dietas/registros.");
            return;
        }

        for (Registro_diarioVO r : registros) {
            System.out.println(r);
            List<ComidaVO> comidas = registroDiarioComidaDAO.listarComidasDeRegistro(r.getRegistro_id());
            for (ComidaVO c : comidas) {
                System.out.println("  - " + c);
            }
        }
    }

    private void elegirYAnadirComidaPorTipo(int registroId, String tipo, List<ComidaVO> comidasVisibles) {
        List<ComidaVO> opciones = filtrarComidasPorTipo(comidasVisibles, tipo);
        if (opciones.isEmpty()) {
            System.out.println("\n(No hay opciones para '" + tipo + "')");
            return;
        }

        System.out.println("\nElige un/a " + tipo + " (0 para saltar):");
        for (ComidaVO c : opciones) {
            System.out.println(c);
        }

        while (true) {
            int comidaId = leerEnteroConPrompt("Id " + tipo + ": ");
            if (comidaId == 0) {
                return;
            }
            if (!contieneId(opciones, comidaId)) {
                System.out.println("Id no valido para '" + tipo + "'. Prueba otra vez (0 para saltar).");
                continue;
            }

            boolean ok = registroDiarioComidaDAO.anadirComidaARegistro(registroId, comidaId);
            System.out.println(ok ? (tipo + " anadido/a.") : ("No se pudo anadir '" + tipo + "'."));
            return;
        }
    }

    private boolean contieneId(List<ComidaVO> comidas, int id) {
        for (ComidaVO c : comidas) {
            if (c.getId_comida() == id) {
                return true;
            }
        }
        return false;
    }

    private List<ComidaVO> filtrarComidasPorTipo(List<ComidaVO> comidas, String tipo) {
        List<ComidaVO> filtradas = new ArrayList<>();
        String clave = (tipo == null) ? "" : tipo.trim().toLowerCase(Locale.ROOT);

        for (ComidaVO c : comidas) {
            if (c == null) {
                continue;
            }
            String valor = c.getTipoComida();
            if (valor == null) {
                continue;
            }
            String normalized = valor.trim().toLowerCase(Locale.ROOT);

            boolean matchExact = normalized.equals(clave);
            boolean matchPrefijo = normalized.startsWith(clave + " ")
                || normalized.startsWith(clave + "-")
                || normalized.startsWith(clave + ":");

            if (matchExact || matchPrefijo) {
                filtradas.add(c);
            }
        }

        return filtradas;
    }

    private void listarComidasVisibles() {
        List<ComidaVO> comidas = comidaDAO.listVisibles();
        if (comidas.isEmpty()) {
            System.out.println("(No hay comidas visibles)");
            return;
        }
        for (ComidaVO c : comidas) {
            System.out.println(c);
        }
    }

    private void listarAlimentos() {
        List<AlimentoVO> alimentos = alimentoDAO.obtenerAlimentos();
        if (alimentos.isEmpty()) {
            System.out.println("(No hay alimentos)");
            return;
        }
        for (AlimentoVO a : alimentos) {
            System.out.println(a);
        }
    }
}
