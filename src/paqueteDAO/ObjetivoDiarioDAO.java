package paqueteDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import paqueteVO.Objetivo_diarioVO;

public class ObjetivoDiarioDAO {
    private Connection getConnection() throws SQLException {
        // return Conexion.obtenerConexion();
        return null; 
    }

    /**
     * Obtiene el objetivo diario de un usuario específico.
     * param idUsuario ID del usuario a buscar.
     * return El objeto Objetivo_diarioVO o null si no existe.
     */
    public Objetivo_diarioVO obtenerPorIdUsuario(int idUsuario) {
        String sql = "SELECT * FROM objetivo_diario WHERE id_user = ?";
        Objetivo_diarioVO vo = null;

        try (Connection con = getConnection();
            PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, idUsuario);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    vo = new Objetivo_diarioVO(
                        rs.getDouble("carbohidratos"),
                        rs.getDouble("grasas"),
                        rs.getInt("id_user"),
                        rs.getInt("kcal"),
                        rs.getDouble("proteinas")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return vo;
    }

    /**
        Realiza un "upsert": actualiza el registro si el id_user existe,
        o lo inserta si es nuevo.
        param vo El objeto con los datos del objetivo.
     */
    public void inserta(Objetivo_diarioVO vo) {
        String sqlUpdate = "UPDATE objetivo_diario SET kcal = ?, proteinas = ?, carbohidratos = ?, grasas = ? WHERE id_user = ?";
        String sqlInsert = "INSERT INTO objetivo_diario (kcal, proteinas, carbohidratos, grasas, id_user) VALUES (?, ?, ?, ?, ?)";

        try (Connection con = getConnection()) {
            // 1. Intentamos actualizar
            try (PreparedStatement psUpdate = con.prepareStatement(sqlUpdate)) {
                psUpdate.setInt(1, vo.getKcal());
                psUpdate.setDouble(2, vo.getProteinas());
                psUpdate.setDouble(3, vo.getCarbohidratos());
                psUpdate.setDouble(4, vo.getGrasas());
                psUpdate.setInt(5, vo.getId_user());

                int filasAfectadas = psUpdate.executeUpdate();

                // 2. Si filasAfectadas es 0, significa que no existe el usuario, procedemos a insertar
                if (filasAfectadas == 0) {
                    try (PreparedStatement psInsert = con.prepareStatement(sqlInsert)) {
                        psInsert.setInt(1, vo.getKcal());
                        psInsert.setDouble(2, vo.getProteinas());
                        psInsert.setDouble(3, vo.getCarbohidratos());
                        psInsert.setDouble(4, vo.getGrasas());
                        psInsert.setInt(5, vo.getId_user());
                        
                        psInsert.executeUpdate();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
