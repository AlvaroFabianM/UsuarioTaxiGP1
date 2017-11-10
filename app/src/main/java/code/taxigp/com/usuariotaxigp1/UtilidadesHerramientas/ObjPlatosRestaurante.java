package code.taxigp.com.usuariotaxigp1.UtilidadesHerramientas;

/**
 * Created by Asus on 31/10/2017.
 */

public class ObjPlatosRestaurante {

    private String nombrePlato;
    private String descripcionPlato;
    private String precio;
    private String fotoPlato;





    public ObjPlatosRestaurante(String nombrePlato, String descripcionPlato, String precio, String fotoPlato) {
        this.nombrePlato = nombrePlato;
        this.descripcionPlato = descripcionPlato;
        this.precio = precio;
        this.fotoPlato = fotoPlato;



    }
    public ObjPlatosRestaurante()
    {

    }

    public String getNombrePlato() {
        return nombrePlato;
    }

    public void setNombrePlato(String nombrePlato) {
        this.nombrePlato = nombrePlato;
    }

    public String getDescripcionPlato() {
        return descripcionPlato;
    }

    public void setDescripcionPlato(String descripcionPlato) {
        this.descripcionPlato = descripcionPlato;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getFotoPlato() {
        return fotoPlato;
    }

    public void setFotoPlato(String fotoPlato) {
        this.fotoPlato = fotoPlato;
    }
}
