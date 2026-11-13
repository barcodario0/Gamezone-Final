package gestion;

import modelo.Item;
import modelo.Usuario;
import modelo.Venta;
import util.Rutas;

import jakarta.servlet.ServletContext;
import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class GestionarVenta {
    private final ArrayList<Venta> lista = new ArrayList<>();
    private final ServletContext context;
    private final File archivo;
    private final GestionarUsuario gestUsuario;
    private final GestionarItem gestItem;

    public GestionarVenta(ServletContext context, GestionarUsuario gestUsuario, GestionarItem gestItem) {
        this.context = context;
        this.archivo = Rutas.obtenerArchivo(context, Rutas.ARCHIVO_REPORTE);
        this.gestUsuario = gestUsuario;
        this.gestItem = gestItem;
        cargarDesdeArchivo();
    }

    public synchronized void cargarDesdeArchivo() {
        lista.clear();
        if (!archivo.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                Venta v = Venta.fromLinea(linea);
                if (v != null) lista.add(v);
            }
        } catch (FileNotFoundException e) {
            System.err.println("[GestionarVenta] Archivo no encontrado: " + archivo.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("[GestionarVenta] Error de lectura: " + e.getMessage());
        }
    }

    public synchronized void guardarEnArchivo() {
        try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(archivo)))) {
            for (Venta v : lista) {
                pw.println(v.toLinea());
            }
        } catch (IOException e) {
            System.err.println("[GestionarVenta] Error al guardar reporte.txt: " + e.getMessage());
            throw new RuntimeException("No se pudo guardar reporte.txt: " + e.getMessage());
        }
    }

    public synchronized List<Venta> listarTodos() {
        return new ArrayList<>(lista);
    }

    public synchronized int siguienteId() {
        int max = 0;
        for (Venta v : lista) max = Math.max(max, v.getIdVenta());
        return max + 1;
    }

    public synchronized String registrarInscripcion(int idUsuario, int idItem, String observaciones) {
        Usuario u = gestUsuario.buscarPorId(idUsuario);
        Item it = gestItem.buscarPorId(idItem);

        if (u == null) return "ERROR: Usuario no encontrado.";
        if (it == null) return "ERROR: Torneo no encontrado.";
        if (it.getStock() <= 0) return "ERROR: No hay cupos disponibles para este torneo.";
        if (!"Abierto".equalsIgnoreCase(it.getEstado()) && !"En curso".equalsIgnoreCase(it.getEstado())) {
            return "ERROR: El torneo no acepta inscripciones (estado: " + it.getEstado() + ").";
        }

        for (Venta v : lista) {
            if (v.getIdUsuario() == idUsuario && v.getIdItem() == idItem) {
                return "ERROR: El usuario ya está inscrito en este torneo.";
            }
        }

        double monto = it.getPrecio();
        Venta venta = new Venta(
                siguienteId(),
                idUsuario,
                idItem,
                u.getNombre() + " (" + u.getNickname() + ")",
                it.getNombre() + " - " + it.getJuego(),
                monto,
                LocalDateTime.now(),
                observaciones != null ? observaciones : ""
        );

        lista.add(venta);
        gestItem.decrementarStock(idItem);
        guardarEnArchivo();

        return "OK: Inscripción registrada. ID Venta: " + venta.getIdVenta() +
               " | Monto: $" + String.format("%.0f", monto);
    }

    public synchronized boolean eliminar(int idVenta) {
        boolean removed = lista.removeIf(v -> v.getIdVenta() == idVenta);
        if (removed) guardarEnArchivo();
        return removed;
    }
}
