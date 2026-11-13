<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="modelo.Usuario, modelo.Item, modelo.Venta, java.util.List" %>
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
    List<Item> items = (List<Item>) request.getAttribute("items");
    List<Venta> ventas = (List<Venta>) request.getAttribute("ventas");
%>
<%@ include file="lib/header.jsp" %>

<div class="d-flex justify-content-between align-items-center mb-4">
    <h2 class="gz-page-title mb-0">
        <i class="bi bi-ticket-perforated"></i> Inscripciones
    </h2>
</div>

<div class="row g-4">
    <div class="col-lg-5">
        <div class="gz-card">
            <div class="gz-card-header">
                <h5 class="gz-card-title"><i class="bi bi-plus-circle"></i> Nueva inscripción</h5>
            </div>
            <div class="gz-card-body">
            <form method="post" action="<%= ctx %>/venta" class="gz-form">
                <input type="hidden" name="action" value="registrar">

                <div class="mb-3">
                    <label class="form-label">Jugador *</label>
                    <select name="idUsuario" class="form-select" required>
                        <option value="">— Seleccionar —</option>
                        <% if (usuarios != null) {
                            for (Usuario u : usuarios) { %>
                        <option value="<%= u.getIdUsuario() %>">
                            <%= u.getNombre() %> (<%= u.getNickname() %>) — <%= u.getEmail() %>
                        </option>
                        <% } } %>
                    </select>
                </div>

                <div class="mb-3">
                    <label class="form-label">Torneo *</label>
                    <select name="idItem" class="form-select" required>
                        <option value="">— Seleccionar —</option>
                        <% if (items != null) {
                            for (Item it : items) {
                                if (it.getStock() > 0 && ("Abierto".equals(it.getEstado()) || "En curso".equals(it.getEstado()))) {
                        %>
                        <option value="<%= it.getIdItem() %>">
                            <%= it.getNombre() %> — <%= it.getJuego() %>
                            ($<%= String.format("%,.0f", it.getPrecio()) %>, cupos: <%= it.getStock() %>)
                        </option>
                        <% } } } %>
                    </select>
                </div>

                <div class="mb-3">
                    <label class="form-label">Observaciones</label>
                    <textarea name="observaciones" class="form-control" rows="2"></textarea>
                </div>

                <button type="submit" class="btn btn-neon-green w-100">
                    <i class="bi bi-check2-circle"></i> Registrar inscripción
                </button>
            </form>
            </div>
        </div>
    </div>

    <div class="col-lg-7">
        <div class="gz-card">
            <div class="gz-card-header">
                <h5 class="gz-card-title"><i class="bi bi-list-ul"></i> Reporte de inscripciones</h5>
            </div>
            <div class="table-responsive">
                <table class="table table-hover table-gz align-middle">
                    <thead>
                    <tr>
                        <th>ID</th>
                        <th>Usuario</th>
                        <th>Torneo</th>
                        <th>Monto</th>
                        <th>Fecha</th>
                        <th></th>
                    </tr>
                    </thead>
                    <tbody>
                    <% if (ventas == null || ventas.isEmpty()) { %>
                    <tr><td colspan="6" class="gz-empty">Aún no hay inscripciones.</td></tr>
                    <% } else {
                        for (Venta v : ventas) { %>
                    <tr>
                        <td class="gz-id">#<%= v.getIdVenta() %></td>
                        <td><%= v.getNombreUsuario() %></td>
                        <td><%= v.getNombreItem() %></td>
                        <td>$<%= String.format("%,.0f", v.getMonto()) %></td>
                        <td class="small"><%= v.getFechaRegistro() != null ? v.getFechaRegistro().toString().replace("T", " ") : "-" %></td>
                        <td>
                            <form method="post" action="<%= ctx %>/venta" class="d-inline"
                                  onsubmit="return confirm('¿Eliminar este registro?');">
                                <input type="hidden" name="action" value="eliminar">
                                <input type="hidden" name="id" value="<%= v.getIdVenta() %>">
                                <button type="submit" class="btn btn-sm btn-neon-danger">
                                    <i class="bi bi-trash"></i>
                                </button>
                            </form>
                        </td>
                    </tr>
                    <% } } %>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>

<%@ include file="lib/footer.jsp" %>
