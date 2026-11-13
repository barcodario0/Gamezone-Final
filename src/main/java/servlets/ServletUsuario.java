package servlets;

import gestion.GestionarUsuario;
import modelo.Usuario;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

@WebServlet(name = "ServletUsuario", urlPatterns = {"/usuario"})
public class ServletUsuario extends HttpServlet {
    private static final Pattern EMAIL_PAT = Pattern.compile("^[\\w.+-]+@[\\w-]+(\\.[\\w-]+)+$");

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

        String action = param(request, "action", "listar");
        GestionarUsuario gest = new GestionarUsuario(getServletContext());

        switch (action) {
            case "listar":
            case "filtrar":
                String campo = param(request, "campo", "nombre");
                String valor = param(request, "valor", "");
                List<Usuario> lista = gest.filtrar(campo, valor);
                request.setAttribute("usuarios", lista);
                request.setAttribute("campo", campo);
                request.setAttribute("valor", valor);
                request.getRequestDispatcher("/listUsers.jsp").forward(request, response);
                break;

            case "editar":
                int idEdit = parseInt(request.getParameter("id"), -1);
                Usuario u = gest.buscarPorId(idEdit);
                if (u == null) {
                    request.setAttribute("error", "Usuario no encontrado.");
                    response.sendRedirect(request.getContextPath() + "/usuario?action=listar");
                    return;
                }
                request.setAttribute("usuarioEdit", u);
                request.setAttribute("modo", "editar");
                request.getRequestDispatcher("/addUsers.jsp").forward(request, response);
                break;

            case "nuevo":
                request.setAttribute("modo", "nuevo");
                request.getRequestDispatcher("/addUsers.jsp").forward(request, response);
                break;

            case "eliminar":
                int idDel = parseInt(request.getParameter("id"), -1);
                if (gest.eliminar(idDel)) {
                    request.getSession().setAttribute("msg", "Usuario eliminado correctamente.");
                } else {
                    request.getSession().setAttribute("error", "No se pudo eliminar el usuario.");
                }
                response.sendRedirect(request.getContextPath() + "/usuario?action=listar");
                break;

            default:
                response.sendRedirect(request.getContextPath() + "/usuario?action=listar");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (requiereAdmin(request, response)) return;
        request.setCharacterEncoding("UTF-8");

        String action = param(request, "action", "crear");
        GestionarUsuario gest = new GestionarUsuario(getServletContext());

        String nombre = param(request, "nombre", "").trim();
        String email = param(request, "email", "").trim();
        String password = param(request, "password", "");
        String rol = param(request, "rol", "jugador");
        String nickname = param(request, "nickname", "").trim();
        String telefono = param(request, "telefono", "").trim();
        int id = parseInt(request.getParameter("id"), 0);

        StringBuilder errores = new StringBuilder();
        if (nombre.isEmpty()) errores.append("El nombre es obligatorio. ");
        if (email.isEmpty()) errores.append("El email es obligatorio. ");
        else if (!EMAIL_PAT.matcher(email).matches()) errores.append("Email inválido. ");
        if ("crear".equals(action) && password.isEmpty()) errores.append("La contraseña es obligatoria. ");
        if (nickname.isEmpty()) errores.append("El nickname es obligatorio. ");

        if (errores.length() > 0) {
            request.setAttribute("error", errores.toString());
            request.setAttribute("modo", "crear".equals(action) ? "nuevo" : "editar");
            if ("actualizar".equals(action)) {
                Usuario tmp = gest.buscarPorId(id);
                if (tmp != null) request.setAttribute("usuarioEdit", tmp);
            }
            request.getRequestDispatcher("/addUsers.jsp").forward(request, response);
            return;
        }

        if ("crear".equals(action)) {
            int nuevoId = gest.siguienteId();
            Usuario u = new Usuario(nuevoId, nombre, email, password, rol, nickname, telefono);
            if (gest.agregar(u)) {
                request.getSession().setAttribute("msg", "Usuario creado con ID " + nuevoId);
            } else {
                request.getSession().setAttribute("error", "No se pudo crear. Email o ID duplicado.");
            }
        } else if ("actualizar".equals(action)) {
            Usuario existente = gest.buscarPorId(id);
            if (existente == null) {
                request.getSession().setAttribute("error", "Usuario no existe.");
            } else {
                String passFinal = password.isEmpty() ? existente.getPassword() : password;
                Usuario u = new Usuario(id, nombre, email, passFinal, rol, nickname, telefono);
                if (gest.actualizar(u)) {
                    request.getSession().setAttribute("msg", "Usuario actualizado correctamente.");
                } else {
                    request.getSession().setAttribute("error", "No se pudo actualizar (email duplicado?).");
                }
            }
        }

        response.sendRedirect(request.getContextPath() + "/usuario?action=listar");
    }

    private String param(HttpServletRequest r, String name, String def) {
        String v = r.getParameter(name);
        return v != null ? v : def;
    }

    private int parseInt(String s, int def) {
        try { return Integer.parseInt(s); } catch (Exception e) { return def; }
    }
}
