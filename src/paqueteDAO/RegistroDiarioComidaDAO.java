package paqueteDAO;

import PaqueteControl.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import paqueteVO.ComidaVO;

public class RegistroDiarioComidaDAO {

    private static final String SQL_INSERT =
        "INSERT INTO registro_diario_comida (id_comida, registro_id) VALUES (?, ?)";

    private static final String SQL_DELETE =
        "DELETE FROM registro_diario_comida WHERE id_comida = ? AND registro_id = ?";

    private static final String SQL_DELETE_BY_REGISTRO =
        "DELETE FROM registro_diario_comida WHERE registro_id = ?";

    private static final String SQL_EXISTS =
        "SELECT 1 FROM registro_diario_comida WHERE id_comida = ? AND registro_id = ?";

    private static final String SQL_LIST_COMIDA_IDS_BY_REGISTRO =
        "SELECT id_comida FROM registro_diario_comida WHERE registro_id = ? ORDER BY id_comida";

    private static final String SQL_LIST_REGISTRO_IDS_BY_COMIDA =
        "SELECT registro_id FROM registro_diario_comida WHERE id_comida = ? ORDER BY registro_id";

    private static final String SQL_LIST_COMIDAS_BY_REGISTRO =
        "SELECT c.id_comida, c.tipo_comida, c.visiblesn " +
            "FROM registro_diario_comida rdc " +
            "JOIN comida c ON c.id_comida = rdc.id_comida " +
            "WHERE rdc.registro_id = ? " +
            "ORDER BY c.id_comida";

    public boolean anadirComidaARegistro(int registroId, int comidaId) {
        try (Connection con = Conexion.getConnection();
            PreparedStatement ps = con.prepareStatement(SQL_INSERT)) {

            ps.setInt(1, comidaId);
            ps.setInt(2, registroId);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean eliminarComidaDeRegistro(int registroId, int comidaId) {
        try (Connection con = Conexion.getConnection();
            PreparedStatement ps = con.prepareStatement(SQL_DELETE)) {

            ps.setInt(1, comidaId);
            ps.setInt(2, registroId);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean eliminarComidasDeRegistro(int registroId) {
        try (Connection con = Conexion.getConnection();
            PreparedStatement ps = con.prepareStatement(SQL_DELETE_BY_REGISTRO)) {

            ps.setInt(1, registroId);
            ps.executeUpdate();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean existeRelacion(int registroId, int comidaId) {
        try (Connection con = Conexion.getConnection();
            PreparedStatement ps = con.prepareStatement(SQL_EXISTS)) {

            ps.setInt(1, comidaId);
            ps.setInt(2, registroId);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Integer> listarIdsComidasDeRegistro(int registroId) {
        List<Integer> ids = new ArrayList<>();

        try (Connection con = Conexion.getConnection();
            PreparedStatement ps = con.prepareStatement(SQL_LIST_COMIDA_IDS_BY_REGISTRO)) {

            ps.setInt(1, registroId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ids.add(rs.getInt("id_comida"));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return ids;
    }

    public List<Integer> listarIdsRegistrosDeComida(int comidaId) {
        List<Integer> ids = new ArrayList<>();

        try (Connection con = Conexion.getConnection();
            PreparedStatement ps = con.prepareStatement(SQL_LIST_REGISTRO_IDS_BY_COMIDA)) {

            ps.setInt(1, comidaId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ids.add(rs.getInt("registro_id"));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return ids;
    }

    public List<ComidaVO> listarComidasDeRegistro(int registroId) {
        List<ComidaVO> comidas = new ArrayList<>();

        try (Connection con = Conexion.getConnection();
            PreparedStatement ps = con.prepareStatement(SQL_LIST_COMIDAS_BY_REGISTRO)) {

            ps.setInt(1, registroId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    comidas.add(mapComida(rs));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return comidas;
    }

    private ComidaVO mapComida(ResultSet rs) throws Exception {
        int id = rs.getInt("id_comida");
        String tipo = rs.getString("tipo_comida");
        String visibleStr = rs.getString("visiblesn");
        char visible = (visibleStr != null && !visibleStr.isEmpty()) ? visibleStr.charAt(0) : 'N';
        return new ComidaVO(id, tipo, visible);
    }
}
