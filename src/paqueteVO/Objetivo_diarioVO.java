package paqueteVO;
public class Objetivo_diarioVO{
    private int id_user;
    private int kcal;
    private double proteinas;
    private double carbohidratos;
    private double grasas;

    public Objetivo_diarioVO(double carbohidratos, double grasas, int id_user, int kcal, double proteinas) {
        this.carbohidratos = carbohidratos;
        this.grasas = grasas;
        this.id_user = id_user;
        this.kcal = kcal;
        this.proteinas = proteinas;
    }

    public int getId_user() {
        return id_user;
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

    public void setId_user(int id_user) {
        this.id_user = id_user;
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
        return "Objetivo_diarioVO [id_user=" + id_user + ", kcal=" + kcal + ", proteinas=" + proteinas
            + ", carbohidratos=" + carbohidratos + ", grasas=" + grasas + "]";
    }  
}