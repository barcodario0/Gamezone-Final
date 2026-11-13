<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="modelo.Usuario, java.util.List" %>
<%
    modelo.Usuario sesionActual = (modelo.Usuario) session.getAttribute("usuario");
    if (sesionActual == null) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }
    if (!"admin".equals(sesionActual.getRol())) {
        session.setAttribute("error", "Acceso exclusivo para administradores.");
        response.sendRedirect(request.getContextPath() + "/index.jsp");
        return;
    }
    String ctx = request.getContextPath();
    List<Usuario> usuarios = (List<Usuario>) request.getAttribute("usuarios");
    String campo = (String) request.getAttribute("campo");
    String valor = (String) request.getAttribute("valor");
    if (campo == null) campo = "nombre";
    if (valor == null) valor = "";
%>
<%@ include file="lib/header.jsp" %>

<div class="d-flex justify-content-between align-items-center mb-4 flex-wrap gap-2">
    <h2 class="gz-page-title mb-0"><i class="bi bi-people"></i> Usuarios</h2>
    <a href="<%= ctx %>/usuario?action=nuevo" class="btn btn-neon-green">
        <i class="bi bi-person-plus"></i> Nuevo usuario
    </a>
</div>

<form class="row g-2 mb-4 gz-form gz-filter" method="get" action="<%= ctx %>/usuario">
    <input type="hidden" name="action" value="filtrar">
    <div class="col-auto">
        <select name="campo" class="form-select form-select-sm">
            <option value="nombre" <%= "nombre".equals(campo) ? "selected" : "" %>>Nombre</option>
            <option value="email" <%= "email".equals(campo) ? "selected" : "" %>>Email</option>
            <option value="rol" <%= "rol".equals(campo) ? "selected" : "" %>>Rol</option>
            <option value="nickname" <%= "nickname".equals(campo) ? "selected" : "" %>>Nickname</option>
            <option value="id" <%= "id".equals(campo) ? "selected" : "" %>>ID</option>
        </select>
    </div>
    <div class="col-auto">
        <input type="text" name="valor" class="form-control form-control-sm" placeholder="Buscar..."
               value="<%= valor %>">
    </div>
    <div class="col-auto">
        <button type="submit" class="btn btn-neon btn-sm"><i class="bi bi-search"></i> Filtrar</button>
        <a href="<%= ctx %>/usuario?action=listar" class="btn btn-neon-purple btn-sm">Limpiar</a>
    </div>
</form>

<div class="gz-card">
<div class="table-responsive">
    <table class="table table-hover table-gz align-middle">
        <thead>
        <tr>
            <th>ID</th>
            <th>Nombre</th>
            <th>Nickname</th>
            <th>Email</th>
            <th>Rol</th>
            <th>Teléfono</th>
            <th class="text-end">Acciones</th>
        </tr>
        </thead>
        <tbody>
        <% if (usuarios == null || usuarios.isEmpty()) { %>
        <tr><td colspan="7" class="gz-empty">No hay usuarios registrados.</td></tr>
        <% } else {
            for (Usuario u : usuarios) { %>
        <tr>
            <td class="gz-id">#<%= u.getIdUsuario() %></td>
            <td><%= u.getNombre() %></td>
            <td><span class="gz-id"><%= u.getNickname() %></span></td>
            <td><%= u.getEmail() %></td>
            <td>
                <span class="gz-badge <%= "admin".equals(u.getRol()) ? "gz-badge-cancel" : "gz-badge-abierto" %>">
                    <%= u.getRol() %>
                </span>
            </td>
            <td><%= u.getTelefono() != null ? u.getTelefono() : "-" %></td>
            <td class="text-end">
                <a href="<%= ctx %>/usuario?action=editar&id=<%= u.getIdUsuario() %>"
                   class="btn btn-sm btn-neon" title="Editar">
                    <i class="bi bi-pencil"></i>
                </a>
                <a href="<%= ctx %>/usuario?action=eliminar&id=<%= u.getIdUsuario() %>"
                   class="btn btn-sm btn-neon-danger" title="Eliminar"
                   onclick="return confirm('¿Eliminar al usuario <%= u.getNombre() %>?');">
                    <i class="bi bi-trash"></i>
                </a>
            </td>
        </tr>
        <% } } %>
        </tbody>
    </table>
</div>
</div>

<%@ include file="lib/footer.jsp" %>
