package cd.socio.luismi.reservainstalacion;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import giis.demo.util.ApplicationException;
import giis.demo.util.Database;

// PDFBox
import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.font.*;

public class ReservaClienteModelo {

    private final Database db = new Database();

    // Formato SQLite con segundos
    private static final DateTimeFormatter FMT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    private static final String SQL_ID_SOCIO =
            "SELECT id_socio, nombre FROM Socios WHERE lower(nombre)=lower(?) OR lower(email)=lower(?) LIMIT 1";

    private static final String SQL_ID_INST =
            "SELECT id_instalacion FROM Instalaciones WHERE nombre=?";

    private static final String SQL_PRECIO =
            "SELECT precioInstalacion FROM Instalaciones WHERE id_instalacion=?";

    private static final String SQL_INSERT =
            "INSERT INTO Reservas(id_socio,id_instalacion,fecha_hora_inicio,duracion,costo,pagado,estado) " +
            "VALUES(?,?,?,?,?,0,'activa')";


    // CALCULAR PRECIO
<<<<<<< HEAD
    public double calcularPrecio(String nombreInstalacion, int horas) {
=======
    // ==========================================================
    public double calcularPrecio(String nombreInst, int horas) {
>>>>>>> refs/heads/main
        try (Connection conn = db.getConnection()) {
            QueryRunner qr = new QueryRunner();

            Number idInst = qr.query(conn, SQL_ID_INST, new ScalarHandler<>(), nombreInst);
            if (idInst == null) return 0;

            Number precio = qr.query(conn, SQL_PRECIO, new ScalarHandler<>(), idInst.intValue());
            if (precio == null) return 0;

            return precio.doubleValue() * horas;

        } catch (SQLException e) {
            throw new ApplicationException(e);
        }
    }


<<<<<<< HEAD
    // GUARDAR RESERVA
=======
    // ==========================================================
    // GUARDAR RESERVA CON TODAS LAS REGLAS
    // ==========================================================
>>>>>>> refs/heads/main
    public boolean guardarReserva(
            String socioInput,
            String nombreInst,
            LocalDate fecha,
            LocalTime horaInicio,
            int horas
    ) {

        try (Connection conn = db.getConnection()) {

            QueryRunner qr = new QueryRunner();

            // ------------------------------
            // Validaciones básicas
            // ------------------------------
            if (horas < 1 || horas > 3)
                throw new ApplicationException("Solo se pueden reservar 1, 2 o 3 horas.");

            if (horaInicio.getMinute() != 0)
                throw new ApplicationException("La hora debe ser en punto (ej. 17:00)");

            LocalTime apertura = LocalTime.of(8, 0);
            LocalTime cierre = LocalTime.of(20, 0);

            if (horaInicio.isBefore(apertura) || horaInicio.isAfter(cierre))
                throw new ApplicationException("Las reservas solo pueden comenzar entre 08:00 y 20:00.");

            // ✅ REGLA 1: No más de 15 días desde hoy
            LocalDate limite = LocalDate.now().plusDays(15);
            if (fecha.isAfter(limite))
                throw new ApplicationException("Solo se puede reservar dentro de los próximos 15 días (límite: " + limite + ")");

            LocalDateTime inicio = LocalDateTime.of(fecha, horaInicio);
            if (inicio.isBefore(LocalDateTime.now()))
                throw new ApplicationException("No se puede reservar en el pasado.");

            int durMin = horas * 60;
            LocalDateTime fin = inicio.plusMinutes(durMin);

            if (fin.toLocalTime().isAfter(cierre))
                throw new ApplicationException("La reserva no puede acabar después de las 20:00.");


            // ------------------------------
            // Obtener socio
            // ------------------------------
            Object[] socio = qr.query(conn, SQL_ID_SOCIO,
                    rs -> rs.next() ? new Object[]{rs.getInt(1), rs.getString(2)} : null,
                    socioInput, socioInput);

            if (socio == null)
                throw new ApplicationException("No existe un socio con ese nombre/email.");

            int idSocio = (int) socio[0];
            String nombreSocio = (String) socio[1];


            // ------------------------------
            // Obtener instalación
            // ------------------------------
            Number idInst = qr.query(conn, SQL_ID_INST, new ScalarHandler<>(), nombreInst);

            if (idInst == null)
                throw new ApplicationException("Instalación no encontrada.");


            // ==========================================================
            // ✅ REGLA 2: Máximo 4 horas en el mismo día
            // ==========================================================
            String sqlHorasDia = """
                SELECT COALESCE(SUM(duracion),0)
                FROM Reservas
                WHERE id_socio=? AND DATE(fecha_hora_inicio)=? AND estado='activa'
            """;

            int minDia = qr.query(conn, sqlHorasDia, new ScalarHandler<Number>(), idSocio, fecha.toString()).intValue();
            int horasDia = minDia / 60;

            if (horasDia + horas > 4)
                throw new ApplicationException("No puedes reservar más de 4 horas en el mismo día.");


         // ==========================================================
         // ✅ REGLA 3: No más de 3 horas continuadas
         // ==========================================================

         // Sacar todas las reservas activas del día del socio
         String sqlReservasDia = """
                 SELECT fecha_hora_inicio, duracion
                 FROM Reservas
                 WHERE id_socio=? 
                 AND DATE(fecha_hora_inicio)=?
                 AND estado='activa'
                 """;

         List<LocalDateTime[]> bloques = qr.query(conn, sqlReservasDia, rs -> {
             List<LocalDateTime[]> list = new ArrayList<>();
             while (rs.next()) {
                 String raw = rs.getString(1);

                 // Normalizar formato (quitar segundos)
                 if (raw.length() > 16) raw = raw.substring(0, 16);

                 LocalDateTime ini = LocalDateTime.parse(raw.replace(" ", "T"));
                 LocalDateTime fi  = ini.plusMinutes(rs.getInt(2));

                 list.add(new LocalDateTime[]{ini, fi});
             }
             return list;
         }, idSocio, fecha.toString());

         // ✅ Añadir la NUEVA reserva a la lista
         bloques.add(new LocalDateTime[]{inicio, fin});

         // ✅ Ordenar todos los bloques por hora de inicio
         bloques.sort((a, b) -> a[0].compareTo(b[0]));

         // ✅ Fusionar intervalos solapados o contiguos y comprobar
         LocalDateTime bloqueIni = bloques.get(0)[0];
         LocalDateTime bloqueFin = bloques.get(0)[1];

         for (int i = 1; i < bloques.size(); i++) {

             LocalDateTime ini = bloques.get(i)[0];
             LocalDateTime fi  = bloques.get(i)[1];

             // Se solapan o son contiguos: (ini <= bloqueFin)
             if (!ini.isAfter(bloqueFin)) {

                 // Ampliar bloque
                 if (fi.isAfter(bloqueFin))
                     bloqueFin = fi;

             } else {

                 // Antes de pasar al siguiente bloque: comprobar duración
                 long horasBloque = Duration.between(bloqueIni, bloqueFin).toHours();
                 if (horasBloque > 3)
                     throw new ApplicationException("No se pueden reservar más de 3 horas seguidas.");

                 // Nuevo bloque
                 bloqueIni = ini;
                 bloqueFin = fi;
             }
         }

         // ✅ Comprobar el ÚLTIMO bloque resultante
         long horasBloque = Duration.between(bloqueIni, bloqueFin).toHours();
         if (horasBloque > 3)
             throw new ApplicationException("No se pueden reservar más de 3 horas seguidas.");


            // ==========================================================
            // ✅ REGLA 4: Máximo 6 horas activas en total
            // ==========================================================
            String sqlHorasTotal = """
                SELECT COALESCE(SUM(duracion),0)
                FROM Reservas
                WHERE id_socio=? AND estado='activa'
            """;

            int minTot = qr.query(conn, sqlHorasTotal, new ScalarHandler<Number>(), idSocio).intValue();
            int horasActivas = minTot / 60;

            if (horasActivas + horas > 6)
                throw new ApplicationException("No puedes tener más de 6 horas activas en total.");


            // ==========================================================
            // CHECK CONFLICTOS INSTALACIÓN / ACTIVIDADES
            // ==========================================================
            String iniStr = inicio.format(FMT);
            String finStr = fin.format(FMT);

            if (hayConflicto(conn, idInst.intValue(), iniStr, finStr))
                throw new ApplicationException("Conflicto de horario con otra reserva o actividad.");


            // ==========================================================
            // INSERTAR RESERVA
            // ==========================================================
            boolean oldAC = conn.getAutoCommit();
            conn.setAutoCommit(false);

            try {

                double precio = calcularPrecio(nombreInst, horas);

                qr.update(conn, SQL_INSERT,
                        idSocio, idInst.intValue(), iniStr, durMin, precio);

                Number idReserva = qr.query(conn,
                        "SELECT last_insert_rowid()", new ScalarHandler<>());

                generarResguardoPDF(
                        idReserva.intValue(),
                        nombreSocio,
                        idSocio,
                        nombreInst,
                        fecha.toString(),
                        horaInicio.toString(),
                        horas,
                        precio,
<<<<<<< HEAD
                        false 
=======
                        false
>>>>>>> refs/heads/main
                );

                conn.commit();
            }
            catch (Exception ex) {
                conn.rollback();
                throw ex;
            }
            finally {
                conn.setAutoCommit(oldAC);
            }

            return true;


        } catch (SQLException e) {
            throw new ApplicationException(e);
        }
    }


    // ==========================================================
    // DETECCIÓN DE CONFLICTOS
    // ==========================================================
    private boolean hayConflicto(Connection conn, int idInst, String ini, String fin)
            throws SQLException {

        String sql =
                "SELECT 1 FROM Reservas " +
                "WHERE id_instalacion=? AND estado='activa' " +
                "AND datetime(?) < datetime(fecha_hora_inicio, '+'||duracion||' minutes') " +
                "AND datetime(?) > datetime(fecha_hora_inicio) " +
                "UNION " +
                "SELECT 1 FROM SesionesActividad " +
                "WHERE id_instalacion=? " +
                "AND datetime(?) < datetime(fecha||' '||hora_fin) " +
                "AND datetime(?) > datetime(fecha||' '||hora_inicio) " +
                "UNION " +
                "SELECT 1 FROM PlanificacionActividades " +
                "WHERE id_instalacion=? " +
                "AND datetime(?) < datetime(fecha||' '||hora_fin) " +
                "AND datetime(?) > datetime(fecha||' '||hora_inicio)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idInst);
            ps.setString(2, ini);
            ps.setString(3, fin);

            ps.setInt(4, idInst);
            ps.setString(5, ini);
            ps.setString(6, fin);

            ps.setInt(7, idInst);
            ps.setString(8, ini);
            ps.setString(9, fin);

            ResultSet rs = ps.executeQuery();
<<<<<<< HEAD
            return rs.next(); 
=======
            return rs.next();
>>>>>>> refs/heads/main
        }
    }


    // ==========================================================
    // PDF RESGUARDO
    // ==========================================================
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

        File carpeta = new File("resguardos");
        if (!carpeta.exists())
            carpeta.mkdirs();

        String ruta = "resguardos/resguardo_" + idReserva + ".pdf";

        try (PDDocument doc = new PDDocument()) {

            PDPage page = new PDPage();
            doc.addPage(page);

            try (PDPageContentStream cs = new PDPageContentStream(doc, page)) {

                PDFont fontTitle = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);
                PDFont fontBody = new PDType1Font(Standard14Fonts.FontName.HELVETICA);


                cs.beginText();
                cs.setFont(fontTitle, 20);
                cs.newLineAtOffset(40, 750);
                cs.showText("Resguardo de Reserva #" + idReserva);
                cs.endText();

                cs.moveTo(40, 742);
                cs.lineTo(555, 742);
                cs.stroke();

                int y = 720;

                String[] texto = {
                        "Socio: " + nombreSocio + " (ID " + idSocio + ")",
                        "Instalación: " + instalacion,
                        "Fecha reserva: " + fecha,
                        "Hora inicio: " + horaInicio,
                        "Duración: " + horas + " hora(s)",
                        String.format("Precio: %.2f €", precio),
                        "Pagado: " + (pagado ? "Sí" : "No"),
                        "Fecha emisión: " + LocalDate.now()
                };

<<<<<<< HEAD
     } catch (IOException e) {
         throw new ApplicationException("Error creando PDF: " + e.getMessage());
     }
 }
=======
                for (String linea : texto) {
                    cs.beginText();
                    cs.setFont(fontBody, 12);
                    cs.newLineAtOffset(40, y);
                    cs.showText(linea);
                    cs.endText();
                    y -= 22;
                }
            }

            doc.save(ruta);

        } catch (IOException e) {
            throw new ApplicationException("Error generando PDF: " + e.getMessage());
        }
    }
>>>>>>> refs/heads/main
}