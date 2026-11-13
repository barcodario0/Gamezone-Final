<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="modelo.Item" %>
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
    Item edit = (Item) request.getAttribute("itemEdit");
    boolean esEditar = "editar".equals(modo) && edit != null;
    String error = (String) request.getAttribute("error");
%>
<%@ include file="lib/header.jsp" %>

<div class="d-flex justify-content-between align-items-center mb-4">
    <h2 class="gz-page-title mb-0">
        <i class="bi bi-trophy"></i>
        <%= esEditar ? "Editar torneo" : "Nuevo torneo" %>
    </h2>
    <a href="<%= ctx %>/item?action=listar" class="btn btn-neon btn-sm">
        <i class="bi bi-arrow-left"></i> Volver al listado
    </a>
</div>

<% if (error != null) { %>
<div class="alert gz-alert gz-alert-danger"><%= error %></div>
<% } %>

<div class="gz-card col-lg-9">
  <div class="gz-card-body">
    <form method="post" action="<%= ctx %>/item" id="formItem" class="gz-form" novalidate>
        <input type="hidden" name="action" value="<%= esEditar ? "actualizar" : "crear" %>">
        <% if (esEditar) { %>
        <input type="hidden" name="id" value="<%= edit.getIdItem() %>">
        <% } %>

        <div class="row g-3">
            <div class="col-md-8">
                <label class="form-label">Nombre del torneo *</label>
                <input type="text" name="nombre" class="form-control" required
                       value="<%= esEditar ? edit.getNombre() : "" %>" minlength="3" maxlength="100">
            </div>
            <div class="col-md-4">
                <label class="form-label">Juego *</label>
                <select name="juego" class="form-select" required>
                    <%
                        String[] juegos = {"Valorant", "League of Legends", "Free Fire", "CS2", "Fortnite", "Rocket League", "Otro"};
                        String juegoSel = esEditar ? edit.getJuego() : "";
                        for (String j : juegos) {
                    %>
                    <option value="<%= j %>" <%= j.equals(juegoSel) ? "selected" : "" %>><%= j %></option>
                    <% } %>
                </select>
            </div>
            <div class="col-12">
                <label class="form-label">Descripción</label>
                <textarea name="descripcion" class="form-control" rows="2"
                          maxlength="250"><%= esEditar && edit.getDescripcion() != null ? edit.getDescripcion() : "" %></textarea>
            </div>
            <div class="col-md-3">
                <label class="form-label">Precio de inscripción *</label>
                <input type="number" name="precio" class="form-control" required min="0" step="1000"
                       value="<%= esEditar ? (int) edit.getPrecio() : "0" %>">
            </div>
            <div class="col-md-3">
                <label class="form-label">Cupos *</label>
                <input type="number" name="stock" class="form-control" required min="0"
                       value="<%= esEditar ? edit.getStock() : "8" %>">
            </div>
            <div class="col-md-3">
                <label class="form-label">Fecha</label>
                <input type="date" name="fecha" class="form-control"
                       value="<%= esEditar && edit.getFecha() != null ? edit.getFecha().toString() : "" %>">
            </div>
            <div class="col-md-3">
                <label class="form-label">Estado *</label>
                <select name="estado" class="form-select" required>
                    <%
                        String[] estados = {"Abierto", "En curso", "Finalizado", "Cancelado"};
                        String estadoSel = esEditar ? edit.getEstado() : "Abierto";
                        for (String e : estados) {
                    %>
                    <option value="<%= e %>" <%= e.equals(estadoSel) ? "selected" : "" %>><%= e %></option>
                    <% } %>
                </select>
            </div>
        </div>

        <div class="mt-4 d-flex gap-2">
            <button type="submit" class="btn btn-neon-green">
                <i class="bi bi-save"></i> <%= esEditar ? "Actualizar" : "Guardar" %>
            </button>
            <a href="<%= ctx %>/item?action=listar" class="btn btn-neon-purple">Cancelar</a>
        </div>
    </form>
  </div>
</div>

<script>
document.getElementById('formItem').addEventListener('submit', function(e) {
    if (!this.checkValidity()) {
        e.preventDefault();
        alert('Completa los campos obligatorios.');
    }
    this.classList.add('was-validated');
});
</script>

<%@ include file="lib/footer.jsp" %>
