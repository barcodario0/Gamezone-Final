package gestion;

import modelo.Item;
import util.Rutas;

import jakarta.servlet.ServletContext;
import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GestionarItem {
    private final ArrayList<Item> lista = new ArrayList<>();
    private final ServletContext context;
    private final File archivo;

    public GestionarItem(ServletContext context) {
        this.context = context;
        this.archivo = Rutas.obtenerArchivo(context, Rutas.ARCHIVO_ITEMS);
        cargarDesdeArchivo();
        if (lista.isEmpty()) {
            LocalDate hoy = LocalDate.now();
            lista.add(new Item(1, "Copa Neón Valorant", "Torneo oficial de Valorant - 5v5",
                    50000.0, 16, "Valorant", hoy.plusDays(15), "Abierto"));
            lista.add(new Item(2, "Liga Nariño LoL", "Liga regional de League of Legends",
                    30000.0, 8, "League of Legends", hoy.plusDays(30), "Abierto"));
            lista.add(new Item(3, "Arena Free Fire Pasto", "Torneo express Free Fire",
                    15000.0, 12, "Free Fire", hoy.minusDays(2), "En curso"));
            guardarEnArchivo();
        }
    }

    public synchronized void cargarDesdeArchivo() {
        lista.clear();
        if (!archivo.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                Item it = Item.fromLinea(linea);
                if (it != null) lista.add(it);
            }
        } catch (FileNotFoundException e) {
            System.err.println("[GestionarItem] Archivo no encontrado: " + archivo.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("[GestionarItem] Error de lectura: " + e.getMessage());
        }
    }

    public synchronized void guardarEnArchivo() {
        try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(archivo)))) {
            for (Item it : lista) {
                pw.println(it.toLinea());
            }
        } catch (IOException e) {
            System.err.println("[GestionarItem] Error al guardar: " + e.getMessage());
            throw new RuntimeException("No se pudo guardar items.txt: " + e.getMessage());
        }
    }

    public synchronized List<Item> listarTodos() {
        return new ArrayList<>(lista);
    }

    public synchronized Item buscarPorId(int id) {
        for (Item it : lista) {
            if (it.getIdItem() == id) return it;
        }
        return null;
    }

    public synchronized int siguienteId() {
        int max = 0;
        for (Item it : lista) max = Math.max(max, it.getIdItem());
        return max + 1;
    }

    public synchronized boolean existeId(int id) {
        return buscarPorId(id) != null;
    }

    public synchronized boolean agregar(Item it) {
        if (it == null) return false;
        if (existeId(it.getIdItem())) return false;
        lista.add(it);
        guardarEnArchivo();
        return true;
    }

    public synchronized boolean actualizar(Item it) {
        if (it == null) return false;
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getIdItem() == it.getIdItem()) {
                lista.set(i, it);
                guardarEnArchivo();
                return true;
            }
        }
        return false;
    }

    public synchronized boolean eliminar(int id) {
        boolean removed = lista.removeIf(it -> it.getIdItem() == id);
        if (removed) guardarEnArchivo();
        return removed;
    }

    public synchronized boolean decrementarStock(int idItem) {
        Item it = buscarPorId(idItem);
        if (it == null || it.getStock() <= 0) return false;
        it.setStock(it.getStock() - 1);
        guardarEnArchivo();
        return true;
    }

    public synchronized List<Item> filtrar(String campo, String valor) {
        if (valor == null || valor.trim().isEmpty()) return listarTodos();
        String v = valor.trim().toLowerCase();
        return lista.stream().filter(it -> {
            switch (campo != null ? campo.toLowerCase() : "nombre") {
                case "juego":
                    return it.getJuego() != null && it.getJuego().toLowerCase().contains(v);
                case "estado":
                    return it.getEstado() != null && it.getEstado().toLowerCase().contains(v);
                case "descripcion":
                    return it.getDescripcion() != null && it.getDescripcion().toLowerCase().contains(v);
                case "id":
                    return String.valueOf(it.getIdItem()).contains(v);
                default:
                    return it.getNombre() != null && it.getNombre().toLowerCase().contains(v);
            }
        }).collect(Collectors.toList());
    }
}
