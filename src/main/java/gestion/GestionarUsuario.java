package gestion;

import modelo.Usuario;
import util.Rutas;

import jakarta.servlet.ServletContext;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GestionarUsuario {
    private final ArrayList<Usuario> lista = new ArrayList<>();
    private final ServletContext context;
    private final File archivo;

    public GestionarUsuario(ServletContext context) {
        this.context = context;
        this.archivo = Rutas.obtenerArchivo(context, Rutas.ARCHIVO_USUARIOS);
        cargarDesdeArchivo();
        if (lista.isEmpty()) {
            lista.add(new Usuario(1, "Administrador GameZone", "admin@gamezone.com",
                    "admin123", "admin", "AdminGZ", "3000000000"));
            lista.add(new Usuario(2, "Andrés Mora", "andres@correo.com",
                    "jugador123", "jugador", "CyberWolf", "3101112233"));
            lista.add(new Usuario(3, "Valentina Ruiz", "valentina@correo.com",
                    "jugador123", "jugador", "NeonVal", "3204445566"));
            guardarEnArchivo();
        }
    }

    public synchronized void cargarDesdeArchivo() {
        lista.clear();
        if (!archivo.exists()) {
            return;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                Usuario u = Usuario.fromLinea(linea);
                if (u != null) {
                    lista.add(u);
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("[GestionarUsuario] Archivo no encontrado: " + archivo.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("[GestionarUsuario] Error de lectura: " + e.getMessage());
        }
    }

    public synchronized void guardarEnArchivo() {
        try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(archivo)))) {
            for (Usuario u : lista) {
                pw.println(u.toLinea());
            }
        } catch (IOException e) {
            System.err.println("[GestionarUsuario] Error al guardar: " + e.getMessage());
            throw new RuntimeException("No se pudo guardar usuarios.txt: " + e.getMessage());
        }
    }

    public synchronized List<Usuario> listarTodos() {
        return new ArrayList<>(lista);
    }

    public synchronized Usuario buscarPorId(int id) {
        for (Usuario u : lista) {
            if (u.getIdUsuario() == id) return u;
        }
        return null;
    }

    public synchronized Usuario buscarPorEmail(String email) {
        if (email == null) return null;
        for (Usuario u : lista) {
            if (email.equalsIgnoreCase(u.getEmail())) return u;
        }
        return null;
    }

    public synchronized Usuario autenticar(String email, String password) {
        Usuario u = buscarPorEmail(email);
        if (u != null && password != null && password.equals(u.getPassword())) {
            return u;
        }
        return null;
    }

    public synchronized int siguienteId() {
        int max = 0;
        for (Usuario u : lista) {
            max = Math.max(max, u.getIdUsuario());
        }
        return max + 1;
    }

    public synchronized boolean existeId(int id) {
        return buscarPorId(id) != null;
    }

    public synchronized boolean agregar(Usuario u) {
        if (u == null) return false;
        if (existeId(u.getIdUsuario())) {
            return false;
        }
        if (buscarPorEmail(u.getEmail()) != null) {
            return false;
        }
        lista.add(u);
        guardarEnArchivo();
        return true;
    }

    public synchronized boolean actualizar(Usuario u) {
        if (u == null) return false;
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getIdUsuario() == u.getIdUsuario()) {
                Usuario otro = buscarPorEmail(u.getEmail());
                if (otro != null && otro.getIdUsuario() != u.getIdUsuario()) {
                    return false;
                }
                lista.set(i, u);
                guardarEnArchivo();
                return true;
            }
        }
        return false;
    }

    public synchronized boolean eliminar(int id) {
        boolean removed = lista.removeIf(u -> u.getIdUsuario() == id);
        if (removed) {
            guardarEnArchivo();
        }
        return removed;
    }

    public synchronized List<Usuario> filtrar(String campo, String valor) {
        if (valor == null || valor.trim().isEmpty()) {
            return listarTodos();
        }
        String v = valor.trim().toLowerCase();
        return lista.stream().filter(u -> {
            switch (campo != null ? campo.toLowerCase() : "nombre") {
                case "email":
                    return u.getEmail() != null && u.getEmail().toLowerCase().contains(v);
                case "rol":
                    return u.getRol() != null && u.getRol().toLowerCase().contains(v);
                case "nickname":
                    return u.getNickname() != null && u.getNickname().toLowerCase().contains(v);
                case "id":
                    return String.valueOf(u.getIdUsuario()).contains(v);
                default:
                    return u.getNombre() != null && u.getNombre().toLowerCase().contains(v);
            }
        }).collect(Collectors.toList());
    }
}
