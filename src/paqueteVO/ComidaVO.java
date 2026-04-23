package paqueteVO;

public class ComidaVO {
    private int id_comida;
    private String tipoComida;
    private char visiblesn;

    public ComidaVO(int id_comida, String tipoComida, char visiblesn) {
        this.id_comida = id_comida;
        this.tipoComida = tipoComida;
        this.visiblesn = visiblesn;
    }

    public ComidaVO(String tipoComida, char visiblesn) {
        this(0, tipoComida, visiblesn);
    }

    public ComidaVO(String tipoComida) {
        this(0, tipoComida, 'S');
    }
    
    public int getId_comida() {
        return id_comida;
    }
    public String getTipoComida() {
        return tipoComida;
    }
    public char getVisiblesn() {
        return visiblesn;
    }

    public void setId_comida(int id_comida) {
        this.id_comida = id_comida;
    }

    public void setTipoComida(String tipoComida) {
        this.tipoComida = tipoComida;
    }

    public void setVisiblesn(char visiblesn) {
        this.visiblesn = visiblesn;
    }

    @Override
    public String toString() {
        return "ComidaVO [id_comida=" + id_comida + ", tipoComida=" + tipoComida + ", visiblesn=" + visiblesn + "]";
    }
}
