package code.taxigp.com.usuariotaxigp1.UtilidadesHerramientas;

/**
 * Created by Asus on 31/10/2017.
 */

public class ObjSitiosRestaurantes {

    private String nombre;
    private String descripcion;
    private String telefono;
    private String direccion;
    private String urlFotoPrincipal;
    private boolean suscripcion;
    private String coordenadas;
    private String fechaPago;
    private String colorCoorporativo;
    private String idRestaurante;



    public ObjSitiosRestaurantes(String coordenadas, String nomSitio,
                                 String desSitio, String telSitio, String dirSitio,
                                 String urlFotoPrincipal, boolean suscripcion, String fechaPago,
                                 String colorCoorporativo, String idRestaurante) {
        this.nombre = nomSitio;
        this.descripcion = desSitio;
        this.telefono = telSitio;
        this.direccion = dirSitio;
        this.urlFotoPrincipal = urlFotoPrincipal;
        this.suscripcion=suscripcion;
        this.coordenadas = coordenadas;
        this.fechaPago= fechaPago;
        this.colorCoorporativo = colorCoorporativo;
        this.idRestaurante = idRestaurante;


    }
    public ObjSitiosRestaurantes()
    {

    }
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getUrlFotoPrincipal() {
        return urlFotoPrincipal;
    }

    public void setUrlFotoPrincipal(String urlFotoPrincipal) {
        this.urlFotoPrincipal = urlFotoPrincipal;
    }

    public boolean isSuscripcion() {
        return suscripcion;
    }

    public void setSuscripcion(boolean suscripcion) {
        this.suscripcion = suscripcion;
    }

    public String getCoordenadas() {
        return coordenadas;
    }

    public void setCoordenadas(String coordenadas) {
        this.coordenadas = coordenadas;
    }

    public String getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(String fechaPago) {
        this.fechaPago = fechaPago;
    }

    public String getColorCoorporativo() {
        return colorCoorporativo;
    }

    public void setColorCoorporativo(String colorCoorporativo) {
        this.colorCoorporativo = colorCoorporativo;
    }

    public String getIdRestaurante() {
        return idRestaurante;
    }

    public void setIdRestaurante(String idRestaurante) {
        this.idRestaurante = idRestaurante;
    }










}
