package paqueteDAO;

import PaqueteControl.Conexion;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import paqueteVO.UserVO;

public class UserDAO {

    private static final String TABLA_USUARIO = "usuario";
    private static final List<String> PASSWORD_COL_CANDIDATES = Arrays.asList(
        "contraseña",
        "contrasena",
        "contraseÃ±a"
    );

    private static String quoteIdentifier(String identifier) {
        return "`" + identifier.replace("`", "``") + "`";
    }

    private static String resolvePasswordColumn(Connection con) throws SQLException {
        DatabaseColumns columns = DatabaseColumns.fromTable(con, TABLA_USUARIO);
        for (String candidate : PASSWORD_COL_CANDIDATES) {
            String found = columns.findExact(candidate);
            if (found != null) {
                return found;
            }
        }
        throw new SQLException("No se encontro columna de password en tabla '" + TABLA_USUARIO
            + "'. Columnas disponibles: " + columns.getAll());
    }

    public List<UserVO> obtenerUsuarios(Connection con) {
        List<UserVO> usuarios = new ArrayList<>();
        String sql;
        try {
            String passwordCol = resolvePasswordColumn(con);
            sql = "SELECT id_user, nombre, apellidos, email, " + quoteIdentifier(passwordCol) + " AS contrasena, "
                + "fecha_nacimiento, altura, peso, fechaCreacion, racha FROM " + TABLA_USUARIO;
        } catch (SQLException e) {
            e.printStackTrace();
            return usuarios;
        }

        try (PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Timestamp ts = rs.getTimestamp("fechaCreacion");
                usuarios.add(new UserVO(
                    rs.getString("apellidos"),
                    rs.getString("contrasena"),
                    rs.getString("email"),
                    rs.getInt("id_user"),
                    rs.getString("nombre"),
                    rs.getDate("fecha_nacimiento").toLocalDate(),
                    rs.getDouble("altura"),
                    rs.getDouble("peso"),
                    ts != null ? ts.toLocalDateTime().toLocalDate() : null,
                    rs.getInt("racha")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return usuarios;
    }

    // Metodo para registrar un usuario
    public boolean registrarUsuario(UserVO user) {
        try (Connection con = Conexion.getConnection()) {
            String passwordCol = resolvePasswordColumn(con);
            String sqluser = "INSERT INTO " + TABLA_USUARIO + " (apellidos, " + quoteIdentifier(passwordCol)
                + ", email, nombre, fecha_nacimiento, altura, peso, fechaCreacion, racha) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

            try (PreparedStatement ps = con.prepareStatement(sqluser)) {
                ps.setString(1, user.getApellidos());
                ps.setString(2, user.getContrasena());
                ps.setString(3, user.getEmail());
                ps.setString(4, user.getNombre());
                ps.setDate(5, Date.valueOf(user.getFecha_nacimiento()));
                ps.setDouble(6, user.getAltura());
                ps.setDouble(7, user.getPeso());
                ps.setDate(8, Date.valueOf(user.getFechaCreacion()));
                ps.setInt(9, user.getRacha());

                int filasAfectadas = ps.executeUpdate();
                return filasAfectadas > 0;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Metodo que busca si un usuario existe
    public UserVO validarUsuario(String email, String password) {
        try (Connection con = Conexion.getConnection()) {
            String passwordCol = resolvePasswordColumn(con);
            String sqluser = "SELECT id_user, nombre, apellidos, email, " + quoteIdentifier(passwordCol)
                + " AS contrasena, fecha_nacimiento, altura, peso, fechaCreacion, racha "
                + "FROM " + TABLA_USUARIO + " WHERE email = ? AND " + quoteIdentifier(passwordCol) + " = ?";

            try (PreparedStatement ps = con.prepareStatement(sqluser)) {
                ps.setString(1, email);
                ps.setString(2, password);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return new UserVO(
                            rs.getString("apellidos"),
                            rs.getString("contrasena"),
                            rs.getString("email"),
                            rs.getInt("id_user"),
                            rs.getString("nombre"),
                            rs.getDate("fecha_nacimiento").toLocalDate(),
                            rs.getDouble("altura"),
                            rs.getDouble("peso"),
                            rs.getDate("fechaCreacion").toLocalDate(),
                            rs.getInt("racha")
                        );
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean actualizarUsuario(UserVO user){
        try (Connection con = Conexion.getConnection()) {
            String passwordCol = resolvePasswordColumn(con);
            String sqluser = "UPDATE " + TABLA_USUARIO + " SET nombre = ?, apellidos = ?, email = ?, "
                + quoteIdentifier(passwordCol) + " = ?, fecha_nacimiento = ?, altura = ?, peso = ?, "
                + "fechaCreacion = ?, racha = ? WHERE id_user = ?";

            try (PreparedStatement ps = con.prepareStatement(sqluser)) {
                ps.setString(1, user.getNombre());
                ps.setString(2, user.getApellidos());
                ps.setString(3, user.getEmail());
                ps.setString(4, user.getContrasena());
                ps.setDate(5, Date.valueOf(user.getFecha_nacimiento()));
                ps.setDouble(6, user.getAltura());
                ps.setDouble(7, user.getPeso());
                ps.setDate(8, Date.valueOf(user.getFechaCreacion()));
                ps.setInt(9, user.getRacha());
                ps.setInt(10, user.getId_user());

                ps.executeUpdate();
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static final class DatabaseColumns {
        private final Set<String> lowerCaseNames;
        private final Set<String> originalNames;

        private DatabaseColumns(Set<String> lowerCaseNames, Set<String> originalNames) {
            this.lowerCaseNames = lowerCaseNames;
            this.originalNames = originalNames;
        }

        static DatabaseColumns fromTable(Connection con, String tableName) throws SQLException {
            Set<String> lower = new HashSet<>();
            Set<String> original = new HashSet<>();
            try (ResultSet rs = con.getMetaData().getColumns(con.getCatalog(), null, tableName, null)) {
                while (rs.next()) {
                    String col = rs.getString("COLUMN_NAME");
                    if (col == null) {
                        continue;
                    }
                    original.add(col);
                    lower.add(col.toLowerCase(Locale.ROOT));
                }
            }
            return new DatabaseColumns(lower, original);
        }

        String findExact(String candidate) {
            if (candidate == null) {
                return null;
            }
            String target = candidate.toLowerCase(Locale.ROOT);
            if (!lowerCaseNames.contains(target)) {
                return null;
            }
            for (String original : originalNames) {
                if (original.equalsIgnoreCase(candidate)) {
                    return original;
                }
            }
            for (String original : originalNames) {
                if (original.toLowerCase(Locale.ROOT).equals(target)) {
                    return original;
                }
            }
            return null;
        }

        Set<String> getAll() {
            return originalNames;
        }
    }
}
