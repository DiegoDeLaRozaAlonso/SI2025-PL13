package administracion;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import giis.demo.util.ApplicationException;
import giis.demo.util.Database;

public class ReservaAdminModelo {

    private final Database db = new Database();

    // Buscar socio por nombre o email (case-insensitive)
    private static final String SQL_ID_SOCIO_BY_NOMBRE_O_EMAIL =
        "select id_socio from Socios " +
        "where lower(nombre)=lower(?) or lower(email)=lower(?) " +
        "limit 1";

    // Buscar socio por id_socio (input numérico)
    private static final String SQL_EXISTE_SOCIO_BY_ID =
        "select id_socio from Socios where id_socio=?";

    private static final String SQL_ID_INSTALACION =
        "select id_instalacion from Instalaciones where nombre=?";

    private static final String SQL_PRECIO =
        "select precioInstalacion from Instalaciones where id_instalacion=?";

    private static final String SQL_INS_RESERVA =
        "insert into Reservas(id_socio,id_instalacion,fecha_hora_inicio,duracion,costo,pagado,estado) " +
        "values(?,?,?,?,?,0,'activa')";

    private static final String SQL_COUNT_SOLAPAMIENTO =
        "select count(*) from Reservas " +
        "where id_instalacion=? and estado='activa' " +
        "and fecha_hora_inicio < ? " +
        "and datetime(fecha_hora_inicio, '+' || duracion || ' minutes') > ?";

    // ---------------------------------------------------
    // 1️⃣ Calcular precio
    // ---------------------------------------------------
    public double calcularPrecio(String nombreInstalacion, int horas) {
        Connection conn = null;
        try {
            conn = db.getConnection();
            QueryRunner qr = new QueryRunner();

            Number idInst = qr.query(conn, SQL_ID_INSTALACION, new ScalarHandler<>(), nombreInstalacion);
            if (idInst == null) return 0;

            Number precio = qr.query(conn, SQL_PRECIO, new ScalarHandler<>(), idInst.intValue());
            if (precio == null) return 0;

            return precio.doubleValue() * horas;

        } catch (SQLException e) {
            throw new ApplicationException(e);
        } finally {
            DbUtils.closeQuietly(conn);
        }
    }

    // ---------------------------------------------------
    // 2️⃣ Guardar reserva completa (busca en SOCIOS)
    //    El parámetro socioInput puede ser: id_socio, email o nombre
    // ---------------------------------------------------
    public boolean guardarReserva(String socioInput,
                                  String nombreInstalacion,
                                  String fecha,
                                  String hora,
                                  int horas) {

        Connection conn = null;
        try {
            conn = db.getConnection();
            QueryRunner qr = new QueryRunner();

            Integer idSocio = findIdSocio(conn, socioInput);
            Number idInst   = qr.query(conn, SQL_ID_INSTALACION, new ScalarHandler<>(), nombreInstalacion);

            if (idSocio == null || idInst == null)
                return false;

            LocalDate fechaLocal = LocalDate.parse(fecha); // yyyy-MM-dd
            LocalTime horaLocal  = LocalTime.parse(hora);  // HH:mm
            LocalDateTime inicio = LocalDateTime.of(fechaLocal, horaLocal);

            int duracionMinutos = horas * 60;
            LocalDateTime fin   = inicio.plusMinutes(duracionMinutos);

            // Comprobar solapamiento
            Number count = qr.query(conn, SQL_COUNT_SOLAPAMIENTO, new ScalarHandler<>(),
                                    idInst.intValue(),
                                    fin.toString(),     // fin B
                                    inicio.toString()); // inicio B

            if (count != null && count.intValue() > 0)
                return false;

            // Insertar reserva
            qr.update(conn, SQL_INS_RESERVA,
                      idSocio.intValue(),
                      idInst.intValue(),
                      inicio.toString(),
                      duracionMinutos,
                      calcularPrecio(nombreInstalacion, horas));

            return true;

        } catch (SQLException e) {
            throw new ApplicationException(e);
        } finally {
            DbUtils.closeQuietly(conn);
        }
    }

    // ---------------------------------------------------
    // 🔎 Buscar id_socio de forma flexible:
    //    - Si es numérico: intenta id_socio
    //    - Si no: por email o por nombre (case-insensitive)
    // ---------------------------------------------------
    private Integer findIdSocio(Connection conn, String socioInput) throws SQLException {
        QueryRunner qr = new QueryRunner();

        if (socioInput == null || socioInput.trim().isEmpty())
            return null;

        String trimmed = socioInput.trim();

        // 1) Si es numérico, asumir que es id_socio
        if (trimmed.matches("\\d+")) {
            Number id = qr.query(conn, SQL_EXISTE_SOCIO_BY_ID, new ScalarHandler<>(), Integer.parseInt(trimmed));
            return id == null ? null : id.intValue();
        }

        // 2) Si no es numérico, intentar por email o por nombre
        Number id = qr.query(conn, SQL_ID_SOCIO_BY_NOMBRE_O_EMAIL, new ScalarHandler<>(), trimmed, trimmed);
        return id == null ? null : id.intValue();
    }
}