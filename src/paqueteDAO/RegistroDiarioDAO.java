package paqueteDAO;

import PaqueteControl.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import paqueteVO.Registro_diarioVO;

public class RegistroDiarioDAO {

    private static final String SQL_INSERT =
        "INSERT INTO registro_diario (hora_registro, id_user) VALUES (?, ?)";

    private static final String SQL_LISTAR_TODOS =
        "SELECT registro_id, hora_registro, id_user FROM registro_diario ORDER BY hora_registro DESC";

    private static final String SQL_LISTAR_HOY =
        "SELECT registro_id, hora_registro, id_user FROM registro_diario WHERE DATE(hora_registro) = CURDATE() ORDER BY hora_registro DESC";

    private static final String SQL_LISTAR_HOY_POR_USUARIO =
        "SELECT registro_id, hora_registro, id_user FROM registro_diario WHERE id_user = ? AND DATE(hora_registro) = CURDATE() ORDER BY hora_registro DESC";

    private static final String SQL_BUSCAR_POR_FECHA =
        "SELECT registro_id, hora_registro, id_user FROM registro_diario WHERE DATE(hora_registro) = ? ORDER BY hora_registro DESC";

    private static final String SQL_BUSCAR_POR_FECHA_POR_USUARIO =
        "SELECT registro_id, hora_registro, id_user FROM registro_diario WHERE id_user = ? AND DATE(hora_registro) = ? ORDER BY hora_registro DESC";

    public List<Registro_diarioVO> obtenerTodosLosRegistros() {
        List<Registro_diarioVO> registros = new ArrayList<>();

        try (Connection con = Conexion.getConnection();
            PreparedStatement ps = con.prepareStatement(SQL_LISTAR_TODOS);
            ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                registros.add(mapRow(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return registros;
    }

    public int crearRegistro(LocalDateTime horaRegistro, int idUser) {
        LocalDateTime hora = (horaRegistro != null) ? horaRegistro : LocalDateTime.now();

        try (Connection con = Conexion.getConnection();
            PreparedStatement ps = con.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {

            ps.setTimestamp(1, Timestamp.valueOf(hora));
            ps.setInt(2, idUser);
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1;
    }

    public List<Registro_diarioVO> obtenerRegistrosDelDiaActual() {
        List<Registro_diarioVO> registros = new ArrayList<>();

        try (Connection con = Conexion.getConnection();
            PreparedStatement ps = con.prepareStatement(SQL_LISTAR_HOY);
            ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                registros.add(mapRow(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return registros;
    }

    public List<Registro_diarioVO> obtenerRegistrosDelDiaActual(int idUser) {
        List<Registro_diarioVO> registros = new ArrayList<>();

        try (Connection con = Conexion.getConnection();
            PreparedStatement ps = con.prepareStatement(SQL_LISTAR_HOY_POR_USUARIO)) {

            ps.setInt(1, idUser);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    registros.add(mapRow(rs));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return registros;
    }

    public List<Registro_diarioVO> buscarRegistrosPorFecha(LocalDate fecha) {
        List<Registro_diarioVO> registros = new ArrayList<>();

        if (fecha == null) {
            return registros;
        }

        try (Connection con = Conexion.getConnection();
            PreparedStatement ps = con.prepareStatement(SQL_BUSCAR_POR_FECHA)) {

            ps.setDate(1, java.sql.Date.valueOf(fecha));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    registros.add(mapRow(rs));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return registros;
    }

    public List<Registro_diarioVO> buscarRegistrosPorFecha(int idUser, LocalDate fecha) {
        List<Registro_diarioVO> registros = new ArrayList<>();

        if (fecha == null) {
            return registros;
        }

        try (Connection con = Conexion.getConnection();
            PreparedStatement ps = con.prepareStatement(SQL_BUSCAR_POR_FECHA_POR_USUARIO)) {

            ps.setInt(1, idUser);
            ps.setDate(2, java.sql.Date.valueOf(fecha));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    registros.add(mapRow(rs));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return registros;
    }

    private Registro_diarioVO mapRow(ResultSet rs) throws Exception {
        int registroId = rs.getInt("registro_id");
        int idUser = rs.getInt("id_user");

        Timestamp ts = rs.getTimestamp("hora_registro");
        LocalDateTime horaRegistro = (ts != null) ? ts.toLocalDateTime() : null;

        return new Registro_diarioVO(horaRegistro, idUser, registroId);
    }
}
