package servlets;

import gestion.GestionarItem;
import modelo.Item;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

@WebServlet(name = "ServletItem", urlPatterns = {"/item"})
public class ServletItem extends HttpServlet {
    private boolean requiereLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession s = request.getSession(false);
        if (s == null || s.getAttribute("usuario") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return true;
        }
        return false;
    }

    private boolean requiereAdmin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession s = request.getSession(false);
        if (s == null || s.getAttribute("usuario") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return true;
        }
        modelo.Usuario actual = (modelo.Usuario) s.getAttribute("usuario");
        if (!"admin".equals(actual.getRol())) {
            s.setAttribute("error", "Acceso exclusivo para administradores.");
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return true;
        }
        return false;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String accionPedida = request.getParameter("action");
        boolean soloLectura = accionPedida == null || accionPedida.isEmpty()
                || "listar".equals(accionPedida) || "filtrar".equals(accionPedida);
        if (soloLectura) {
            if (requiereLogin(request, response)) return;
        } else if (requiereAdmin(request, response)) {
            return;
        }
        request.setCharacterEncoding("UTF-8");

        String action = param(request, "action", "listar");
        GestionarItem gest = new GestionarItem(getServletContext());

        switch (action) {
            case "listar":
            case "filtrar":
                String campo = param(request, "campo", "nombre");
                String valor = param(request, "valor", "");
                List<Item> lista = gest.filtrar(campo, valor);
                request.setAttribute("items", lista);
                request.setAttribute("campo", campo);
                request.setAttribute("valor", valor);
                request.getRequestDispatcher("/listItems.jsp").forward(request, response);
                break;

            case "editar":
                int idEdit = parseInt(request.getParameter("id"), -1);
                Item it = gest.buscarPorId(idEdit);
                if (it == null) {
                    request.getSession().setAttribute("error", "Torneo no encontrado.");
                    response.sendRedirect(request.getContextPath() + "/item?action=listar");
                    return;
                }
                request.setAttribute("itemEdit", it);
                request.setAttribute("modo", "editar");
                request.getRequestDispatcher("/addItems.jsp").forward(request, response);
                break;

            case "nuevo":
                request.setAttribute("modo", "nuevo");
                request.getRequestDispatcher("/addItems.jsp").forward(request, response);
                break;

            case "eliminar":
                int idDel = parseInt(request.getParameter("id"), -1);
                if (gest.eliminar(idDel)) {
                    request.getSession().setAttribute("msg", "Torneo eliminado correctamente.");
                } else {
                    request.getSession().setAttribute("error", "No se pudo eliminar el torneo.");
                }
                response.sendRedirect(request.getContextPath() + "/item?action=listar");
                break;

            default:
                response.sendRedirect(request.getContextPath() + "/item?action=listar");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (requiereAdmin(request, response)) return;
        request.setCharacterEncoding("UTF-8");

        String action = param(request, "action", "crear");
        GestionarItem gest = new GestionarItem(getServletContext());

        String nombre = param(request, "nombre", "").trim();
        String descripcion = param(request, "descripcion", "").trim();
        String juego = param(request, "juego", "").trim();
        String estado = param(request, "estado", "Abierto");
        double precio = parseDouble(request.getParameter("precio"), 0);
        int stock = parseInt(request.getParameter("stock"), 0);
        int id = parseInt(request.getParameter("id"), 0);
        LocalDate fecha = null;
        String fechaStr = param(request, "fecha", "");
        if (!fechaStr.isEmpty()) {
            try { fecha = LocalDate.parse(fechaStr); } catch (DateTimeParseException ignored) {}
        }

        StringBuilder errores = new StringBuilder();
        if (nombre.isEmpty()) errores.append("El nombre del torneo es obligatorio. ");
        if (juego.isEmpty()) errores.append("El juego es obligatorio. ");
        if (precio < 0) errores.append("El precio no puede ser negativo. ");
        if (stock < 0) errores.append("Los cupos no pueden ser negativos. ");

        if (errores.length() > 0) {
            request.setAttribute("error", errores.toString());
            request.setAttribute("modo", "crear".equals(action) ? "nuevo" : "editar");
            if ("actualizar".equals(action)) {
                Item tmp = gest.buscarPorId(id);
                if (tmp != null) request.setAttribute("itemEdit", tmp);
            }
            request.getRequestDispatcher("/addItems.jsp").forward(request, response);
            return;
        }

        if ("crear".equals(action)) {
            int nuevoId = gest.siguienteId();
            Item it = new Item(nuevoId, nombre, descripcion, precio, stock, juego, fecha, estado);
            if (gest.agregar(it)) {
                request.getSession().setAttribute("msg", "Torneo creado con ID " + nuevoId);
            } else {
                request.getSession().setAttribute("error", "No se pudo crear (ID duplicado).");
            }
        } else if ("actualizar".equals(action)) {
            Item it = new Item(id, nombre, descripcion, precio, stock, juego, fecha, estado);
            if (gest.actualizar(it)) {
                request.getSession().setAttribute("msg", "Torneo actualizado correctamente.");
            } else {
                request.getSession().setAttribute("error", "No se pudo actualizar.");
            }
        }

        response.sendRedirect(request.getContextPath() + "/item?action=listar");
    }

    private String param(HttpServletRequest r, String name, String def) {
        String v = r.getParameter(name);
        return v != null ? v : def;
    }

    private int parseInt(String s, int def) {
        try { return Integer.parseInt(s); } catch (Exception e) { return def; }
    }

    private double parseDouble(String s, double def) {
        try { return Double.parseDouble(s); } catch (Exception e) { return def; }
    }
}
