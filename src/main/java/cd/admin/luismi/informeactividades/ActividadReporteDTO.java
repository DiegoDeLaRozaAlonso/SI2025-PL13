package cd.admin.luismi.informeactividades;

public class ActividadReporteDTO {
    private String nombre;
    private String instalacion;
    private String estado;
    private String fechaInicio;
    private String fechaFin;
    private int numeroPlazas;
    private int numeroReservas;
    private int listaEspera;
    private int ediciones;

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getInstalacion() { return instalacion; }
    public void setInstalacion(String instalacion) { this.instalacion = instalacion; }
    
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(String fechaInicio) { this.fechaInicio = fechaInicio; }

    public String getFechaFin() { return fechaFin; }
    public void setFechaFin(String fechaFin) { this.fechaFin = fechaFin; }

    public int getNumeroPlazas() { return numeroPlazas; }
    public void setNumeroPlazas(int numeroPlazas) { this.numeroPlazas = numeroPlazas; }

    public int getNumeroReservas() { return numeroReservas; }
    public void setNumeroReservas(int numeroReservas) { this.numeroReservas = numeroReservas; }

    public int getListaEspera() { return listaEspera; }
    public void setListaEspera(int listaEspera) { this.listaEspera = listaEspera; }

    public int getEdiciones() { return ediciones; }
    public void setEdiciones(int ediciones) { this.ediciones = ediciones; }

    public double getPorcentajeOcupacion() {
        if (numeroPlazas == 0) return 0.0;
        return (double) numeroReservas / numeroPlazas * 100.0;
    }
}
