package paqueteDAO;

import PaqueteControl.Conexion;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import paqueteVO.UserVO;

public class UserDAO {

    public List<UserVO> obtenerUsuarios(Connection con) {
        List<UserVO> usuarios = new ArrayList<>();
        String sql = "SELECT id_user, nombre, apellidos, email, contraseÃ±a, fecha_nacimiento, altura, peso, fechaCreacion, racha FROM usuario";

        try (PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Timestamp ts = rs.getTimestamp("fechaCreacion");
                usuarios.add(new UserVO(
                    rs.getString("apellidos"),
                    rs.getString("contraseÃ±a"),
                    rs.getString("email"),
                    rs.getInt("id_user"),
                    rs.getString("nombre"),
                    rs.getDate("fecha_nacimiento").toLocalDate(),
                    rs.getDouble("altura"),
                    rs.getDouble("peso"),
                    //ts es una abreviatura de Timestamp. 
                    ts != null ? ts.toLocalDateTime().toLocalDate() : null,
                    rs.getInt("racha")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return usuarios;
    }

    // MÃ©todo para registrar un usuario
    public boolean registrarUsuario(UserVO user) {
        String sqluser = "INSERT INTO usuario (apellidos, contrasena, email, nombre, fecha_nacimiento, altura, peso, fechaCreacion, racha) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = Conexion.getConnection();
            PreparedStatement ps = con.prepareStatement(sqluser)) {

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

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // MÃ©todo que busca si un usuario existe
    public UserVO validarUsuario(String email, String password) {
        String sqluser = "SELECT * FROM usuario WHERE email = ? AND contrasena = ?";

        try (Connection con = Conexion.getConnection();
            PreparedStatement ps = con.prepareStatement(sqluser)) {

            ps.setString(1, email);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new UserVO(
                        rs.getString("apellidos"),
                        rs.getString("contraseÃ±a"),
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
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean actualizarUsuario(UserVO user){
        String sqluser = "UPDATE usuario SET nombre = ?, apellidos = ?, email = ?, contrasena = ?, " +
            "fecha_nacimiento = ?, altura = ?, peso = ?, fechaCreacion = ?, racha = ? " +
            "WHERE id_user = ?";

        try (Connection con = Conexion.getConnection();
            PreparedStatement ps = con.prepareStatement(sqluser)){

            ps.setString(1, user.getNombre());
            ps.setString(2, user.getApellidos());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getContrasena());
            ps.setDate(5, Date.valueOf(user.getFecha_nacimiento()));
            ps.setDouble(6, user.getAltura());
            ps.setDouble(7, user.getPeso());
            ps.setDate(8, Date.valueOf(user.getFechaCreacion()));
            ps.setInt(9, user.getRacha());
            ps.setInt(10,user.getId_user());

            ps.executeUpdate();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
