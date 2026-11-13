package modelo;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Venta implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private int idVenta;
    private int idUsuario;
    private int idItem;
    private String nombreUsuario;
    private String nombreItem;
    private double monto;
    private LocalDateTime fechaRegistro;
    private String observaciones;

    public Venta() {
        this.fechaRegistro = LocalDateTime.now();
    }

    public Venta(int idVenta, int idUsuario, int idItem, String nombreUsuario,
                 String nombreItem, double monto, LocalDateTime fechaRegistro, String observaciones) {
        this.idVenta = idVenta;
        this.idUsuario = idUsuario;
        this.idItem = idItem;
        this.nombreUsuario = nombreUsuario;
        this.nombreItem = nombreItem;
        this.monto = monto;
        this.fechaRegistro = fechaRegistro != null ? fechaRegistro : LocalDateTime.now();
        this.observaciones = observaciones;
    }

    public int getIdVenta() { return idVenta; }
    public void setIdVenta(int idVenta) { this.idVenta = idVenta; }

    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }

    public int getIdItem() { return idItem; }
    public void setIdItem(int idItem) { this.idItem = idItem; }

    public String getNombreUsuario() { return nombreUsuario; }
    public void setNombreUsuario(String nombreUsuario) { this.nombreUsuario = nombreUsuario; }

    public String getNombreItem() { return nombreItem; }
    public void setNombreItem(String nombreItem) { this.nombreItem = nombreItem; }

    public double getMonto() { return monto; }
    public void setMonto(double monto) { this.monto = monto; }

    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDateTime fechaRegistro) { this.fechaRegistro = fechaRegistro; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }

    public String toLinea() {
        String fechaStr = (fechaRegistro != null) ? fechaRegistro.format(FMT) : "";
        return idVenta + "|" +
               idUsuario + "|" +
               (nombreUsuario != null ? nombreUsuario : "") + "|" +
               idItem + "|" +
               (nombreItem != null ? nombreItem : "") + "|" +
               monto + "|" +
               fechaStr + "|" +
               (observaciones != null ? observaciones : "");
    }

    public static Venta fromLinea(String linea) {
        if (linea == null || linea.trim().isEmpty() || linea.startsWith("#")) {
            return null;
        }
        String[] p = linea.split("\\|", -1);
        if (p.length < 6) return null;
        try {
            int idVenta = Integer.parseInt(p[0].trim());
            int idUsuario = Integer.parseInt(p[1].trim());
            String nombreUsuario = p[2].trim();
            int idItem = Integer.parseInt(p[3].trim());
            String nombreItem = p[4].trim();
            double monto = Double.parseDouble(p[5].trim());
            LocalDateTime fecha = null;
            if (p.length > 6 && !p[6].trim().isEmpty()) {
                try {
                    fecha = LocalDateTime.parse(p[6].trim(), FMT);
                } catch (Exception ignored) {}
            }
            String obs = p.length > 7 ? p[7].trim() : "";
            return new Venta(idVenta, idUsuario, idItem, nombreUsuario, nombreItem, monto, fecha, obs);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Override
    public String toString() {
        return "Venta/Inscripcion{id=" + idVenta + ", usuario=" + nombreUsuario +
               ", torneo=" + nombreItem + ", monto=" + monto + "}";
    }
}
