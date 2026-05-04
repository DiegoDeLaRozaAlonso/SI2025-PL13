package cd.pablo.pruebas;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Calendar;
import giis.demo.util.Util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import cd.admin.pablo.inscripcionActividad.InscribirAdminModel;
import cd.socio.pablo.inscripcionActividad.ActividadDTO;
import giis.demo.util.ApplicationException;

public class TestInscribirAdmin {

    private InscribirAdminModel model;

    @BeforeEach
    public void setUp() {
        // Inicializamos el modelo antes de cada prueba
        model = new InscribirAdminModel();
    }

    // ==========================================
    // CE1: EL USUARIO ES SOCIO (esSocio = true)
    // ==========================================

    @Test
    public void testSocio_InscripcionTemprana_LanzaExcepcion() {
        // CE1.1: La fecha actual es anterior a fecha_inicio_periodo
        ActividadDTO actividad = new ActividadDTO();
        
        //Creamos una fecha lejana en el futuro para que no deje inscrbir por ser temprano
        actividad.setFecha_inicio_periodo(calcularFechaISO(2));
        actividad.setFecha_fin_periodo(calcularFechaISO(5));

        // Verificamos que lanza la excepción exacta del modelo
        ApplicationException exception = assertThrows(ApplicationException.class, () -> {
            model.enPlazo(actividad, true);
        });
        assertEquals("No ha empezado el periodo de inscripcion de Socios", exception.getMessage());
    }

    @Test
    public void testSocio_InscripcionEnPlazo_Exito() {
        // CE1.2: La fecha actual está entre inicio y fin
        ActividadDTO actividad = new ActividadDTO();
        
        actividad.setFecha_inicio_periodo(calcularFechaISO(-2));
        actividad.setFecha_fin_periodo(calcularFechaISO(2));

        // Si el método no lanza ninguna excepción, la prueba pasa.
        assertDoesNotThrow(() -> {
            model.enPlazo(actividad, true);
        });
    }

    @Test
    public void testSocio_InscripcionTardia_LanzaExcepcion() {
        // CE1.3: La fecha actual es posterior a fecha_fin_periodo
        ActividadDTO actividad = new ActividadDTO();
        
        actividad.setFecha_inicio_periodo(calcularFechaISO(-5));
        actividad.setFecha_fin_periodo(calcularFechaISO(-2));

        ApplicationException exception = assertThrows(ApplicationException.class, () -> {
            model.enPlazo(actividad, true);
        });
        assertEquals("Ya termino el periodo de inscripcion de Socios", exception.getMessage());
    }

    // ==========================================
    // CE2: EL USUARIO NO ES SOCIO (esSocio = false)
    // ==========================================

    @Test
    public void testNoSocio_InscripcionTemprana_LanzaExcepcion() {
        // CE2.1: La fecha actual es anterior a fecha_fin_periodo (inicio para no socios)
        ActividadDTO actividad = new ActividadDTO();
        
        actividad.setFecha_fin_periodo(calcularFechaISO(2));
        actividad.setFecha_fin_no_socio(calcularFechaISO(5));
        
        ApplicationException exception = assertThrows(ApplicationException.class, () -> {
            model.enPlazo(actividad, false);
        });
        assertEquals("No ha empezado el periodo de inscripcion de No Socio", exception.getMessage());
    }

    @Test
    public void testNoSocio_InscripcionEnPlazo_Exito() {
        // CE2.2: La fecha actual está entre fecha_fin_periodo y fecha_fin_no_socio
        ActividadDTO actividad = new ActividadDTO();
        
        actividad.setFecha_fin_periodo(calcularFechaISO(-2));
        actividad.setFecha_fin_no_socio(calcularFechaISO(3));

        assertDoesNotThrow(() -> {
            model.enPlazo(actividad, false);
        });
    }

    @Test
    public void testNoSocio_InscripcionTardia_LanzaExcepcion() {
        // CE2.3: La fecha actual es posterior a fecha_fin_no_socio
        ActividadDTO actividad = new ActividadDTO();

        actividad.setFecha_fin_periodo(calcularFechaISO(-4));
        actividad.setFecha_fin_no_socio(calcularFechaISO(-2));

        ApplicationException exception = assertThrows(ApplicationException.class, () -> {
            model.enPlazo(actividad, false);
        });
        assertEquals("Ya termino el periodo de inscripcion de No socio", exception.getMessage());
    }
    
    /**
     * Método auxiliar para calcular días antes o después de forma automatizada 
     * con respecto al día que se ejecutan las pruebas, que se apoya en la clase auxiliar
     * ya proporciona en el proyecto dado
     * @param desfaseHorario
     * @return
     */
    private String calcularFechaISO(int desfaseHorario) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, desfaseHorario);
        return Util.dateToIsoString(cal.getTime());
    }
}