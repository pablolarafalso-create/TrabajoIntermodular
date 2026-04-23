package paqueteVO;

public class Comida_alimentoVO {
    private int id_comida;
    private int id_alimento;
    private int cantidad;

    public Comida_alimentoVO(int id_alimento, int id_comida, int cantidad) {
        this.id_alimento = id_alimento;
        this.id_comida = id_comida;
        this.cantidad = cantidad;
    }

    public int getId_comida() {
        return id_comida;
    }

    public int getId_alimento() {
        return id_alimento;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setId_comida(int id_comida) {
        this.id_comida = id_comida;
    }

    public void setId_alimento(int id_alimento) {
        this.id_alimento = id_alimento;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    @Override
    public String toString() {
        return "Comida_alimentoVO [id_comida=" + id_comida + ", id_alimento=" + id_alimento + ", cantidad=" + cantidad
            + "]";
    }
}
