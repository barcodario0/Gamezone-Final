<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="modelo.Usuario" %>
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
    String modo = (String) request.getAttribute("modo");
    if (modo == null) modo = "nuevo";
    Usuario edit = (Usuario) request.getAttribute("usuarioEdit");
    boolean esEditar = "editar".equals(modo) && edit != null;
    String error = (String) request.getAttribute("error");
%>
<%@ include file="lib/header.jsp" %>

<div class="d-flex justify-content-between align-items-center mb-4">
    <h2 class="gz-page-title mb-0">
        <i class="bi bi-person-plus"></i>
        <%= esEditar ? "Editar usuario" : "Nuevo usuario" %>
    </h2>
    <a href="<%= ctx %>/usuario?action=listar" class="btn btn-neon btn-sm">
        <i class="bi bi-arrow-left"></i> Volver al listado
    </a>
</div>

<% if (error != null) { %>
<div class="alert gz-alert gz-alert-danger"><%= error %></div>
<% } %>

<div class="gz-card col-lg-8">
  <div class="gz-card-body">
    <form method="post" action="<%= ctx %>/usuario" id="formUsuario" class="gz-form" novalidate>
        <input type="hidden" name="action" value="<%= esEditar ? "actualizar" : "crear" %>">
        <% if (esEditar) { %>
        <input type="hidden" name="id" value="<%= edit.getIdUsuario() %>">
        <% } %>

        <div class="row g-3">
            <div class="col-md-6">
                <label class="form-label">Nombre completo *</label>
                <input type="text" name="nombre" class="form-control" required
                       value="<%= esEditar ? edit.getNombre() : "" %>"
                       minlength="3" maxlength="80">
            </div>
            <div class="col-md-6">
                <label class="form-label">Nickname *</label>
                <input type="text" name="nickname" class="form-control" required
                       value="<%= esEditar ? edit.getNickname() : "" %>"
                       minlength="3" maxlength="30">
            </div>
            <div class="col-md-6">
                <label class="form-label">Email *</label>
                <input type="email" name="email" class="form-control" required
                       value="<%= esEditar ? edit.getEmail() : "" %>">
            </div>
            <div class="col-md-6">
                <label class="form-label">Teléfono</label>
                <input type="text" name="telefono" class="form-control"
                       value="<%= esEditar ? edit.getTelefono() : "" %>"
                       pattern="[0-9]{7,15}">
            </div>
            <div class="col-md-6">
                <label class="form-label">Contraseña <%= esEditar ? "" : "*" %></label>
                <input type="password" name="password" class="form-control"
                       <%= esEditar ? "" : "required" %> minlength="4">
            </div>
            <div class="col-md-6">
                <label class="form-label">Rol *</label>
                <select name="rol" class="form-select" required>
                    <option value="jugador" <%= esEditar && "jugador".equals(edit.getRol()) ? "selected" : "" %>>Jugador</option>
                    <option value="admin" <%= esEditar && "admin".equals(edit.getRol()) ? "selected" : "" %>>Administrador</option>
                </select>
            </div>
        </div>

        <div class="mt-4 d-flex gap-2">
            <button type="submit" class="btn btn-neon-green">
                <i class="bi bi-save"></i> <%= esEditar ? "Actualizar" : "Guardar" %>
            </button>
            <a href="<%= ctx %>/usuario?action=listar" class="btn btn-neon-purple">Cancelar</a>
        </div>
    </form>
  </div>
</div>

<script>
document.getElementById('formUsuario').addEventListener('submit', function(e) {
    const form = this;
    if (!form.checkValidity()) {
        e.preventDefault();
        e.stopPropagation();
        alert('Por favor completa todos los campos obligatorios correctamente.');
    }
    form.classList.add('was-validated');
});
</script>

<%@ include file="lib/footer.jsp" %>
