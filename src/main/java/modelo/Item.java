package modelo;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Item implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private int idItem;
    private String nombre;
    private String descripcion;
    private double precio;
    private int stock;
    private String juego;
    private LocalDate fecha;
    private String estado;

    public Item() {
    }

    public Item(int idItem, String nombre, String descripcion, double precio, int stock,
                String juego, LocalDate fecha, String estado) {
        this.idItem = idItem;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.stock = stock;
        this.juego = juego;
        this.fecha = fecha;
        this.estado = estado;
    }

    public int getIdItem() { return idItem; }
    public void setIdItem(int idItem) { this.idItem = idItem; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    public String getJuego() { return juego; }
    public void setJuego(String juego) { this.juego = juego; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String toLinea() {
        String fechaStr = (fecha != null) ? fecha.format(FMT) : "";
        return idItem + "|" +
               (nombre != null ? nombre : "") + "|" +
               (descripcion != null ? descripcion : "") + "|" +
               precio + "|" +
               stock + "|" +
               (juego != null ? juego : "") + "|" +
               fechaStr + "|" +
               (estado != null ? estado : "Abierto");
    }

    public static Item fromLinea(String linea) {
        if (linea == null || linea.trim().isEmpty() || linea.startsWith("#")) {
            return null;
        }
        String[] p = linea.split("\\|", -1);
        if (p.length < 5) return null;
        try {
            int id = Integer.parseInt(p[0].trim());
            String nombre = p[1].trim();
            String descripcion = p[2].trim();
            double precio = Double.parseDouble(p[3].trim());
            int stock = Integer.parseInt(p[4].trim());
            String juego = p.length > 5 ? p[5].trim() : "";
            LocalDate fecha = null;
            if (p.length > 6 && !p[6].trim().isEmpty()) {
                try {
                    fecha = LocalDate.parse(p[6].trim(), FMT);
                } catch (DateTimeParseException ignored) {}
            }
            String estado = p.length > 7 ? p[7].trim() : "Abierto";
            return new Item(id, nombre, descripcion, precio, stock, juego, fecha, estado);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Override
    public String toString() {
        return "Item/Torneo{id=" + idItem + ", nombre='" + nombre + "', juego='" + juego +
               "', cupos=" + stock + ", estado='" + estado + "'}";
    }
}
