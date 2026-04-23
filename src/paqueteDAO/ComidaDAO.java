package paqueteDAO;

import PaqueteControl.Conexion;
import paqueteVO.ComidaVO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ComidaDAO {

    public List<ComidaVO> listVisibles() {
        List<ComidaVO> lista = new ArrayList<>();
        String sql = "SELECT id_comida, tipo_comida, visiblesn FROM comida WHERE visiblesn = 'S'";

        try (Connection conn = Conexion.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(new ComidaVO(
                    rs.getInt("id_comida"),
                    rs.getString("tipo_comida"),
                    rs.getString("visiblesn").charAt(0)
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    public ComidaVO findById(int id) {
        String sql = "SELECT id_comida, tipo_comida, visiblesn FROM comida WHERE id_comida = ?";

        try (Connection conn = Conexion.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new ComidaVO(
                        rs.getInt("id_comida"),
                        rs.getString("tipo_comida"),
                        rs.getString("visiblesn").charAt(0)
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // API usada desde AppController (conexiÃ³n externa)
    public List<ComidaVO> obtenerComidas(Connection conn) throws SQLException {
        List<ComidaVO> lista = new ArrayList<>();
        String sql = "SELECT id_comida, tipo_comida, visiblesn FROM comida";

        try (PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(new ComidaVO(
                    rs.getInt("id_comida"),
                    rs.getString("tipo_comida"),
                    rs.getString("visiblesn").charAt(0)
                ));
            }
        }

        return lista;
    }

    //almacena los valores de crear comida
    public void insertarComida(Connection conn, ComidaVO comida) throws SQLException {
        String sql = "INSERT INTO comida (tipo_comida, visiblesn) VALUES (?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, comida.getTipoComida());
            ps.setString(2, String.valueOf(comida.getVisiblesn()));
            ps.executeUpdate();
        }
    }

    //inserta las comidas
    public void insert(ComidaVO comida) {
        try (Connection conn = Conexion.getConnection()) {
            insertarComida(conn, comida);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setVisible(int id, char visible) {
        String sql = "UPDATE comida SET visiblesn = ? WHERE id_comida = ?";

        try (Connection conn = Conexion.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, String.valueOf(visible));
            ps.setInt(2, id);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
