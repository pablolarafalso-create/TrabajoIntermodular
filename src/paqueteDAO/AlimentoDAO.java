package paqueteDAO;

import PaqueteControl.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import paqueteVO.AlimentoVO;

public class AlimentoDAO {

    public List<AlimentoVO> obtenerAlimentos() {
        String consulta = "SELECT id_alimento, nombre, kcal, proteinas, carbohidratos, grasas FROM alimento";
        List<AlimentoVO> listaAlimentos = new ArrayList<>();

        // Usamos try-with-resources para cerrar la conexión automáticamente al terminar
        try (Connection con = Conexion.getConnection();
            PreparedStatement ps = con.prepareStatement(consulta);
            ResultSet resultado = ps.executeQuery()) {

            while (resultado.next()) {
                // Creamos el objeto VO con los datos obtenidos
                AlimentoVO alim = new AlimentoVO(
                    resultado.getDouble("carbohidratos"),
                    resultado.getDouble("grasas"),
                    resultado.getInt("id_alimento"),
                    resultado.getInt("kcal"),
                    resultado.getString("nombre"),
                    resultado.getDouble("proteinas")
                );
                
                listaAlimentos.add(alim);
            }

        } catch (Exception e) {
            e.printStackTrace();
            // Dependiendo de tu arquitectura, podrías querer lanzar una RuntimeException aquí
        }
        
        return listaAlimentos;
    }
    
    public AlimentoVO encontrarPorId(int id) {
        String consulta = "SELECT id_alimento, nombre, kcal, proteinas, carbohidratos, grasas FROM alimento WHERE id_alimento = ?";
        try (Connection con = Conexion.getConnection();
            PreparedStatement ps = con.prepareStatement(consulta)) {
            ps.setInt(1, id);
            try (ResultSet resultado = ps.executeQuery()) {
                if (resultado.next()) {
                    return new AlimentoVO(
                        resultado.getDouble("carbohidratos"),
                        resultado.getDouble("grasas"),
                        resultado.getInt("id_alimento"),
                        resultado.getInt("kcal"),
                        resultado.getString("nombre"),
                        resultado.getDouble("proteinas")
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public int insertar(AlimentoVO alimento) {
        String sql = "INSERT INTO alimento (nombre, kcal, proteinas, carbohidratos, grasas) VALUES (?, ?, ?, ?, ?)";

        try (Connection con = Conexion.getConnection();
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, alimento.getNombre());
            ps.setInt(2, alimento.getKcal());
            ps.setDouble(3, alimento.getProteinas());
            ps.setDouble(4, alimento.getCarbohidratos());
            ps.setDouble(5, alimento.getGrasas());

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
}
