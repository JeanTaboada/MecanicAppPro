package shifttec.mecanicapp.model;

public class Veh {
    String descripcion, nombre, placa, photo;
    public Veh(){
    }
    public Veh(String descripcion, String nombre, String placa, String photo) {
        this.descripcion = descripcion;
        this.nombre = nombre;
        this.placa = placa;
        this.photo = photo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getPhoto() {return photo;}
    public void setPhoto(String photo) {
        this.photo = photo;
    }

}
