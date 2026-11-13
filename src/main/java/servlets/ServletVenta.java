package servlets;

import gestion.GestionarItem;
import gestion.GestionarUsuario;
import gestion.GestionarVenta;
import modelo.Item;
import modelo.Usuario;
import modelo.Venta;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "ServletVenta", urlPatterns = {"/venta"})
public class ServletVenta extends HttpServlet {
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
        if (requiereAdmin(request, response)) return;
        request.setCharacterEncoding("UTF-8");

        GestionarUsuario gestU = new GestionarUsuario(getServletContext());
        GestionarItem gestI = new GestionarItem(getServletContext());
        GestionarVenta gestV = new GestionarVenta(getServletContext(), gestU, gestI);

        String action = param(request, "action", "form");

        if ("listar".equals(action)) {
            List<Venta> ventas = gestV.listarTodos();
            request.setAttribute("ventas", ventas);
            request.getRequestDispatcher("/venta.jsp").forward(request, response);
            return;
        }

        List<Usuario> usuarios = gestU.listarTodos();
        List<Item> items = gestI.listarTodos();
        List<Venta> ventas = gestV.listarTodos();

        request.setAttribute("usuarios", usuarios);
        request.setAttribute("items", items);
        request.setAttribute("ventas", ventas);
        request.getRequestDispatcher("/venta.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (requiereAdmin(request, response)) return;
        request.setCharacterEncoding("UTF-8");

        GestionarUsuario gestU = new GestionarUsuario(getServletContext());
        GestionarItem gestI = new GestionarItem(getServletContext());
        GestionarVenta gestV = new GestionarVenta(getServletContext(), gestU, gestI);

        String action = param(request, "action", "registrar");

        if ("eliminar".equals(action)) {
            int id = parseInt(request.getParameter("id"), -1);
            if (gestV.eliminar(id)) {
                request.getSession().setAttribute("msg", "Registro de inscripción eliminado.");
            } else {
                request.getSession().setAttribute("error", "No se pudo eliminar.");
            }
            response.sendRedirect(request.getContextPath() + "/venta?action=listar");
            return;
        }

        int idUsuario = parseInt(request.getParameter("idUsuario"), -1);
        int idItem = parseInt(request.getParameter("idItem"), -1);
        String observaciones = param(request, "observaciones", "").trim();

        if (idUsuario <= 0 || idItem <= 0) {
            request.getSession().setAttribute("error", "Debes seleccionar un usuario y un torneo.");
            response.sendRedirect(request.getContextPath() + "/venta");
            return;
        }

        String resultado = gestV.registrarInscripcion(idUsuario, idItem, observaciones);
        if (resultado.startsWith("OK")) {
            request.getSession().setAttribute("msg", resultado);
        } else {
            request.getSession().setAttribute("error", resultado);
        }
        response.sendRedirect(request.getContextPath() + "/venta");
    }

    private String param(HttpServletRequest r, String name, String def) {
        String v = r.getParameter(name);
        return v != null ? v : def;
    }

    private int parseInt(String s, int def) {
        try { return Integer.parseInt(s); } catch (Exception e) { return def; }
    }
}
