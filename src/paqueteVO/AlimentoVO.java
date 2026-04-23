
package paqueteVO;

public class AlimentoVO {
    private int id_alimento;
    private String nombre;
    private int kcal;
    private double proteinas;
    private double carbohidratos;
    private double grasas;

    public AlimentoVO(double carbohidratos, double grasas, int id_alimento, int kcal, String nombre, double proteinas) {
        this.carbohidratos = carbohidratos;
        this.grasas = grasas;
        this.id_alimento = id_alimento;
        this.kcal = kcal;
        this.nombre = nombre;
        this.proteinas = proteinas;
    }

    public int getId_alimento() {
        return id_alimento;
    }

    public String getNombre() {
        return nombre;
    }

    public int getKcal() {
        return kcal;
    }

    public double getProteinas() {
        return proteinas;
    }

    public double getCarbohidratos() {
        return carbohidratos;
    }

    public double getGrasas() {
        return grasas;
    }

    public void setId_alimento(int id_alimento) {
        this.id_alimento = id_alimento;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setKcal(int kcal) {
        this.kcal = kcal;
    }

    public void setProteinas(double proteinas) {
        this.proteinas = proteinas;
    }

    public void setCarbohidratos(double carbohidratos) {
        this.carbohidratos = carbohidratos;
    }

    public void setGrasas(double grasas) {
        this.grasas = grasas;
    }

    @Override
    public String toString() {
        return "AlimentoVO [id_alimento=" + id_alimento + ", nombre=" + nombre + ", kcal=" + kcal + ", proteinas="
            + proteinas + ", carbohidratos=" + carbohidratos + ", grasas=" + grasas + "]";
    }
}
