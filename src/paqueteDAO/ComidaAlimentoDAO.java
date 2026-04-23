package paqueteDAO;

import PaqueteControl.Conexion;
import paqueteVO.Comida_alimentoVO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ComidaAlimentoDAO {

    //
    public void addAlimento(int comidaId, int alimentoId, int cantidad) {
        try (Connection conn = Conexion.getConnection()) {
            addAlimento(conn, comidaId, alimentoId, cantidad);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean addAlimento(Connection conn, int comidaId, int alimentoId, int cantidad) throws SQLException {
        String sql = "INSERT INTO comida_alimento (id_comida, id_alimento, cantidad) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, comidaId);
            ps.setInt(2, alimentoId);
            ps.setInt(3, cantidad);
            return ps.executeUpdate() > 0;
        }
    }

    public void updateCantidad(int comidaId, int alimentoId, int nuevaCantidad) {
        try (Connection conn = Conexion.getConnection()) {
            updateCantidad(conn, comidaId, alimentoId, nuevaCantidad);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateCantidad(Connection conn, int comidaId, int alimentoId, int nuevaCantidad) throws SQLException {
        String sql = "UPDATE comida_alimento SET cantidad = ? WHERE id_comida = ? AND id_alimento = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, nuevaCantidad);
            ps.setInt(2, comidaId);
            ps.setInt(3, alimentoId);
            ps.executeUpdate();
        }
    }

    public void removeAlimento(int comidaId, int alimentoId) {
        try (Connection conn = Conexion.getConnection()) {
            removeAlimento(conn, comidaId, alimentoId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeAlimento(Connection conn, int comidaId, int alimentoId) throws SQLException {
        String sql = "DELETE FROM comida_alimento WHERE id_comida = ? AND id_alimento = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, comidaId);
            ps.setInt(2, alimentoId);
            ps.executeUpdate();
        }
    }

    public List<Comida_alimentoVO> listAlimentosDeComida(int comidaId) {
        try (Connection conn = Conexion.getConnection()) {
            return listAlimentosDeComida(conn, comidaId);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<Comida_alimentoVO> listAlimentosDeComida(Connection conn, int comidaId) throws SQLException {
        List<Comida_alimentoVO> lista = new ArrayList<>();
        String sql = "SELECT id_comida, id_alimento, cantidad FROM comida_alimento WHERE id_comida = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, comidaId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(new Comida_alimentoVO(
                        rs.getInt("id_alimento"),
                        rs.getInt("id_comida"),
                        rs.getInt("cantidad")
                    ));
                }
            }
        }

        return lista;
    }
}

