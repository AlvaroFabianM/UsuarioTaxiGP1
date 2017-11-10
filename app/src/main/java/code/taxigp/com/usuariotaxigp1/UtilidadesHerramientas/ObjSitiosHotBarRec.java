package code.taxigp.com.usuariotaxigp1.UtilidadesHerramientas;

/**
 * Created by Asus on 31/10/2017.
 */

public class ObjSitiosHotBarRec {

    private String nombre;
    private String descripcion;
    private String telefono;
    private String direccion;
    private String urlFotoPrincipal;
    private boolean suscripcion;
    private String coordenadas;
    private String tipoLugar;
    private String fotoUno;
    private String fotoDos;
    private String fotoTres;
    private String fotoCuatro;
    private String fotoCinco;

    public ObjSitiosHotBarRec(String coordenadas, String tipoLugar,
                              String fotoUno, String fotoDos, String fotoTres,
                              String fotoCuatro, String fotoCinco, String nomSitio,
                              String desSitio, String telSitio, String dirSitio,
                              String urlFotoPrincipal, boolean suscripcion) {

        this.nombre = nomSitio;
        this.descripcion = desSitio;
        this.telefono = telSitio;
        this.direccion = dirSitio;
        this.suscripcion=suscripcion;
        this.coordenadas = coordenadas;
        this.tipoLugar = tipoLugar;
        this.fotoUno = fotoUno;
        this.fotoDos = fotoDos;
        this.fotoTres = fotoTres;
        this.fotoCuatro = fotoCuatro;
        this.fotoCinco = fotoCinco;
        this.urlFotoPrincipal = urlFotoPrincipal;

    }
    public ObjSitiosHotBarRec()
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

    public String getCoordenadas() {
        return coordenadas;
    }

    public void setCoordenadas(String coordenadas) {
        this.coordenadas = coordenadas;
    }

    public String getTipoLugar() {
        return tipoLugar;
    }

    public void setTipoLugar(String tipoLugar) {
        this.tipoLugar = tipoLugar;
    }

    public boolean isSuscripcion() {
        return suscripcion;
    }

    public void setSuscripcion(boolean suscripcion) {
        this.suscripcion = suscripcion;
    }

    public String getFotoUno() {
        return fotoUno;
    }

    public void setFotoUno(String fotoUno) {
        this.fotoUno = fotoUno;
    }

    public String getFotoDos() {
        return fotoDos;
    }

    public void setFotoDos(String fotoDos) {
        this.fotoDos = fotoDos;
    }

    public String getFotoTres() {
        return fotoTres;
    }

    public void setFotoTres(String fotoTres) {
        this.fotoTres = fotoTres;
    }

    public String getFotoCuatro() {
        return fotoCuatro;
    }

    public void setFotoCuatro(String fotoCuatro) {
        this.fotoCuatro = fotoCuatro;
    }

    public String getFotoCinco() {
        return fotoCinco;
    }

    public void setFotoCinco(String fotoCinco) {
        this.fotoCinco = fotoCinco;
    }





}
