package paqueteVO;
public class Registro_diario_comidaVO {
    private int registro_id;
    private int id_comida;

    public Registro_diario_comidaVO(int id_comida, int registro_id) {
        this.id_comida = id_comida;
        this.registro_id = registro_id;
    }

    public int getRegistro_id() {
        return registro_id;
    }

    public int getId_comida() {
        return id_comida;
    }

    public void setRegistro_id(int registro_id) {
        this.registro_id = registro_id;
    }

    public void setId_comida(int id_comida) {
        this.id_comida = id_comida;
    }

    @Override
    public String toString() {
        return "Rregistro_diario_comidaVO [registro_id=" + registro_id + ", id_comida=" + id_comida + "]";
    }
}
