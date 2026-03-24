package administracion;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;
// PDFBox
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;

import giis.demo.util.ApplicationException;
import giis.demo.util.Database;

public class ReservaAdminModelo {

    private final Database db = new Database();

    // Formato consistente para SQLite: "YYYY-MM-DD HH:MM:SS"
    private static final DateTimeFormatter FMT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private static final String SQL_ID_SOCIO_BY_NOMBRE_O_EMAIL =
        "select id_socio, nombre from Socios " +
        "where lower(nombre)=lower(?) or lower(email)=lower(?) " +
        "limit 1";

    private static final String SQL_EXISTE_SOCIO_BY_ID =
        "select id_socio, nombre from Socios where id_socio=?";

    private static final String SQL_ID_INSTALACION =
        "select id_instalacion from Instalaciones where nombre=?";

    private static final String SQL_PRECIO =
        "select precioInstalacion from Instalaciones where id_instalacion=?";

    private static final String SQL_INS_RESERVA =
        "insert into Reservas(id_socio,id_instalacion,fecha_hora_inicio,duracion,costo,pagado,estado) " +
        "values(?,?,?,?,?,0,'activa')";



    // CALCULAR PRECIO
    public double calcularPrecio(String nombreInstalacion, int horas) {
        try (Connection conn = db.getConnection()) {
            QueryRunner qr = new QueryRunner();

            Number idInst = qr.query(conn, SQL_ID_INSTALACION,
                    new ScalarHandler<>(), nombreInstalacion);
            if (idInst == null) return 0;

            Number precio = qr.query(conn, SQL_PRECIO,
                    new ScalarHandler<>(), idInst.intValue());
            if (precio == null) return 0;

            return precio.doubleValue() * horas;

        } catch (SQLException e) {
            throw new ApplicationException(e);
        }
    }

    // GUARDAR RESERVA
    public boolean guardarReserva(
            String socioInput,
            String nombreInstalacion,
            LocalDate fecha,
            LocalTime horaInicio,
            int horas) {

        try (Connection conn = db.getConnection()) {

            QueryRunner qr = new QueryRunner();

            // Validaciones
            //Entre 1 y 3 horas
            if (horas < 1 || horas > 3)
                throw new ApplicationException("Solo se permiten 1, 2 o 3 horas");
            //No es en horas intermedias
            if (horaInicio.getMinute() != 0)
                throw new ApplicationException("La hora debe ser en punto");
            //Fijamos variable para las horas de apertura y de cierre
            LocalTime apertura = LocalTime.of(8, 0);
            LocalTime cierre   = LocalTime.of(20, 0);
            //Comprobamos que no sea ni antes ni despues de lo anterior
            if (horaInicio.isBefore(apertura) || horaInicio.isAfter(cierre))
                throw new ApplicationException("Las reservas solo pueden empezar entre 08:00 y 20:00");
            
            // Comprobamos para no permitir reservar mas de 3 meses desde hoy
            LocalDate limite = LocalDate.now().plusMonths(3);
            if (fecha.isAfter(limite)) {
                throw new ApplicationException(
                    "No se pueden hacer reservas más de 3 meses después de hoy (límite: " + limite + ")"
                );
            }
            // Mioramos que no se reserve en el pasado
            LocalDateTime inicio = LocalDateTime.of(fecha, horaInicio);
            if (inicio.isBefore(LocalDateTime.now()))
                throw new ApplicationException("No se puede reservar en el pasado");
            //Miramos que no acabe despues de que cierre
            int duracionMin = horas * 60;
            LocalDateTime fin = inicio.plusMinutes(duracionMin);

            if (fin.toLocalTime().isAfter(cierre))
                throw new ApplicationException("La reserva no puede terminar después de las 20:00");

            // Buscar socio (id , nombre)
            int idSocio;
            String nombreSocio;
            Object[] socio = new QueryRunner().query(
                    conn,
                    SQL_ID_SOCIO_BY_NOMBRE_O_EMAIL,
                    rs -> {
                        if (rs.next()) {
                            return new Object[]{rs.getInt(1), rs.getString(2)};
                        }
                        return null;
                    },
                    socioInput, socioInput
            );

            if (socio == null)
                throw new ApplicationException("Socio no encontrado");

            idSocio = (int) socio[0];
            nombreSocio = (String) socio[1];

            // Buscar instalación
            Number idInst = qr.query(conn, SQL_ID_INSTALACION,
                    new ScalarHandler<>(), nombreInstalacion);
            if (idInst == null)
                throw new ApplicationException("Instalación no encontrada");

            // Normalizar fechas a TEXT (SQLite friendly)
            String inicioStr = inicio.format(FMT);
            String finStr    = fin.format(FMT);

            // Lo pasamos
            boolean oldAutoCommit = conn.getAutoCommit();
            conn.setAutoCommit(false);
            try {
                // Chequeo de que no choque (reservas + sesiones + planificaciones)
                boolean conflicto = hayConflicto(conn,
                        idInst.intValue(),
                        inicioStr,
                        finStr);

                if (conflicto) {
                    conn.rollback();
                    throw new ApplicationException(
                        "Conflicto de horario: ya existe una reserva/sesión/planificación en ese tramo");
                }

                // Insert
                double precio = calcularPrecio(nombreInstalacion, horas);

                qr.update(conn, SQL_INS_RESERVA,
                        idSocio,
                        idInst.intValue(),
                        inicioStr,   // sin 'T'
                        duracionMin,
                        precio);

                // Obtener id de la nueva reserva
                Number idNuevaReserva = qr.query(conn,
                        "SELECT last_insert_rowid()",
                        new ScalarHandler<>());
                int idReserva = idNuevaReserva.intValue();

                // Generar resguardo PDF
                generarResguardoPDF(
                        idReserva,
                        nombreSocio,
                        idSocio,
                        nombreInstalacion,
                        fecha.toString(),
                        horaInicio.toString(),
                        horas,
                        precio,
                        false // pagado (lo gestionas aparte)
                );

                conn.commit();
            } catch (SQLException | RuntimeException ex) {
                try { conn.rollback(); } catch (SQLException ignore) {}
                throw ex;
            } finally {
                try { conn.setAutoCommit(oldAutoCommit); } catch (SQLException ignore) {}
            }

            return true;

        } catch (SQLException e) {
            throw new ApplicationException(e);
        }
    }


    // COMPROBAR CHOQUES (Reservas + Sesiones + Planificación)
    private boolean hayConflicto(Connection conn, int idInstalacion, String inicioStr,  // yyyy-MM-dd HH:mm:ss
                                 String finStr)     // yyyy-MM-dd HH:mm:ss
            throws SQLException {

        String sql =
            // RESERVAS (usa fecha_hora_inicio + duracion)
            "SELECT 1 FROM Reservas " +
            "WHERE id_instalacion = ? " +
            "AND estado = 'activa' " +
            "AND datetime(?) < datetime(fecha_hora_inicio, '+' || duracion || ' minutes') " + // nueva_inicio < existente_fin
            "AND datetime(?) > datetime(fecha_hora_inicio) " +                                // nueva_fin    > existente_inicio
            "UNION " +
            // SESIONES ACTIVIDAD (fecha + hora)
            "SELECT 1 FROM SesionesActividad " +
            "WHERE id_instalacion = ? " +
            "AND datetime(?) < datetime(fecha || ' ' || hora_fin) " +
            "AND datetime(?) > datetime(fecha || ' ' || hora_inicio) " +
            "UNION " +
            // PLANIFICACION ACTIVIDADES (fecha + hora)
            "SELECT 1 FROM PlanificacionActividades " +
            "WHERE id_instalacion = ? " +
            "AND datetime(?) < datetime(fecha || ' ' || hora_fin) " +
            "AND datetime(?) > datetime(fecha || ' ' || hora_inicio)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            // RESERVAS
            ps.setInt(1,  idInstalacion);
            ps.setString(2, inicioStr);
            ps.setString(3, finStr);

            // SESIONES
            ps.setInt(4,  idInstalacion);
            ps.setString(5, inicioStr);
            ps.setString(6, finStr);

            // PLANIFICACIÓN
            ps.setInt(7,  idInstalacion);
            ps.setString(8, inicioStr);
            ps.setString(9, finStr);

            ResultSet rs = ps.executeQuery();
            return rs.next(); // true → hay conflicto
        }
    }

    private void generarResguardoPDF(
         int idReserva,
         String nombreSocio,
         int idSocio,
         String instalacion,
         String fecha,
         String horaInicio,
         int horas,
         double precio,
         boolean pagado) {

    	// Crear carpeta si no existe y mete los resgurados en PDF
    	//NO TOCAR
     File carpeta = new File("resguardos");
     if (!carpeta.exists())
         carpeta.mkdirs();

     String ruta = "resguardos/resguardo_" + idReserva + ".pdf";

     try (PDDocument doc = new PDDocument()) {
         PDPage page = new PDPage();
         doc.addPage(page);

         try (PDPageContentStream cs = new PDPageContentStream(doc, page)) {

             PDFont fontTitle = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);
             PDFont fontBody  = new PDType1Font(Standard14Fonts.FontName.HELVETICA);

             // Título
             cs.beginText();
             cs.setFont(fontTitle, 20);
             cs.newLineAtOffset(40, 750);
             cs.showText("Resguardo de Reserva #" + idReserva);
             cs.endText();

             // Separador
             cs.moveTo(40, 742);
             cs.lineTo(555, 742);
             cs.stroke();

             // Contenido
             int y = 720;
             cs.setFont(fontBody, 12);

             String[] texto = {
                 "Socio: " + nombreSocio + " (ID " + idSocio + ")",
                 "Instalación: " + instalacion,
                 "Fecha reserva: " + fecha,
                 "Hora inicio: " + horaInicio,
                 "Duración: " + horas + " hora(s)",
                 String.format("Precio: %.2f €", precio),
                 "Pagado: " + (pagado ? "Sí" : "No"),
                 "Fecha de emisión: " + LocalDate.now()
             };

             for (String linea : texto) {
                 cs.beginText();
                 cs.newLineAtOffset(40, y);
                 cs.showText(linea);
                 cs.endText();
                 y -= 22;
             }
         }

         doc.save(ruta);

         // (PRUEBA) abrir automáticamente el PDF en el escritorio (NO FUNCA)
         // java.awt.Desktop.getDesktop().open(new File(ruta));
     } catch (IOException e) {
         throw new ApplicationException("Error creando PDF: " + e.getMessage());
     }
 }
}