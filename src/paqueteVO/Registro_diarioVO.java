package paqueteVO;

import java.time.LocalDateTime;

public class Registro_diarioVO {
    private int registro_id;
    private LocalDateTime hora_registro;
    private int id_user;

    public Registro_diarioVO(LocalDateTime hora_registro, int id_user, int registro_id) {
        this.hora_registro = hora_registro;
        this.id_user = id_user;
        this.registro_id = registro_id;
    }

    public int getRegistro_id() {
        return registro_id;
    }

    public LocalDateTime getHora_registro() {
        return hora_registro;
    }

    public int getId_user() {
        return id_user;
    }

    public void setRegistro_id(int registro_id) {
        this.registro_id = registro_id;
    }

    public void setHora_registro(LocalDateTime hora_registro) {
        this.hora_registro = hora_registro;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    @Override
    public String toString() {
        return "Registro_diarioVO [registro_id=" + registro_id + ", hora_registro=" + hora_registro + ", id_user="
            + id_user + "]";
    }
}
