<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="modelo.Item, java.util.List" %>
<%
    modelo.Usuario sesionActual = (modelo.Usuario) session.getAttribute("usuario");
    if (sesionActual == null) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }
    boolean puedeEditar = "admin".equals(sesionActual.getRol());
    String ctx = request.getContextPath();
    List<Item> items = (List<Item>) request.getAttribute("items");
    String campo = (String) request.getAttribute("campo");
    String valor = (String) request.getAttribute("valor");
    if (campo == null) campo = "nombre";
    if (valor == null) valor = "";
%>
<%@ include file="lib/header.jsp" %>

<div class="d-flex justify-content-between align-items-center mb-4 flex-wrap gap-2">
    <h2 class="gz-page-title mb-0"><i class="bi bi-trophy"></i> Torneos</h2>
    <% if (puedeEditar) { %>
    <a href="<%= ctx %>/item?action=nuevo" class="btn btn-neon-green">
        <i class="bi bi-plus-lg"></i> Nuevo torneo
    </a>
    <% } %>
</div>

<form class="row g-2 mb-4 gz-form gz-filter" method="get" action="<%= ctx %>/item">
    <input type="hidden" name="action" value="filtrar">
    <div class="col-auto">
        <select name="campo" class="form-select form-select-sm">
            <option value="nombre" <%= "nombre".equals(campo) ? "selected" : "" %>>Nombre</option>
            <option value="juego" <%= "juego".equals(campo) ? "selected" : "" %>>Juego</option>
            <option value="estado" <%= "estado".equals(campo) ? "selected" : "" %>>Estado</option>
            <option value="id" <%= "id".equals(campo) ? "selected" : "" %>>ID</option>
        </select>
    </div>
    <div class="col-auto">
        <input type="text" name="valor" class="form-control form-control-sm" placeholder="Buscar..."
               value="<%= valor %>">
    </div>
    <div class="col-auto">
        <button type="submit" class="btn btn-neon btn-sm"><i class="bi bi-search"></i> Filtrar</button>
        <a href="<%= ctx %>/item?action=listar" class="btn btn-neon-purple btn-sm">Limpiar</a>
    </div>
</form>

<div class="gz-card">
<div class="table-responsive">
    <table class="table table-hover table-gz align-middle">
        <thead>
        <tr>
            <th>ID</th>
            <th>Nombre</th>
            <th>Juego</th>
            <th>Precio</th>
            <th>Cupos</th>
            <th>Fecha</th>
            <th>Estado</th>
            <% if (puedeEditar) { %><th class="text-end">Acciones</th><% } %>
        </tr>
        </thead>
        <tbody>
        <% if (items == null || items.isEmpty()) { %>
        <tr><td colspan="<%= puedeEditar ? 8 : 7 %>" class="gz-empty">No hay torneos registrados.</td></tr>
        <% } else {
            for (Item it : items) {
                String badgeClass = "gz-badge-abierto";
                if ("En curso".equals(it.getEstado())) badgeClass = "gz-badge-curso";
                else if ("Finalizado".equals(it.getEstado())) badgeClass = "gz-badge-fin";
                else if ("Cancelado".equals(it.getEstado())) badgeClass = "gz-badge-cancel";
        %>
        <tr>
            <td class="gz-id">#<%= it.getIdItem() %></td>
            <td>
                <strong><%= it.getNombre() %></strong>
                <% if (it.getDescripcion() != null && !it.getDescripcion().isEmpty()) { %>
                <br><small class="text-secondary"><%= it.getDescripcion() %></small>
                <% } %>
            </td>
            <td><span class="gz-badge gz-badge-juego"><%= it.getJuego() %></span></td>
            <td>$<%= String.format("%,.0f", it.getPrecio()) %></td>
            <td>
                <span class="<%= it.getStock() <= 2 ? "text-warning" : "" %>">
                    <%= it.getStock() %>
                </span>
            </td>
            <td><%= it.getFecha() != null ? it.getFecha().toString() : "-" %></td>
            <td><span class="gz-badge <%= badgeClass %>"><%= it.getEstado() %></span></td>
            <% if (puedeEditar) { %>
            <td class="text-end">
                <a href="<%= ctx %>/item?action=editar&id=<%= it.getIdItem() %>"
                   class="btn btn-sm btn-neon" title="Editar">
                    <i class="bi bi-pencil"></i>
                </a>
                <a href="<%= ctx %>/item?action=eliminar&id=<%= it.getIdItem() %>"
                   class="btn btn-sm btn-neon-danger" title="Eliminar"
                   onclick="return confirm('¿Eliminar el torneo <%= it.getNombre() %>?');">
                    <i class="bi bi-trash"></i>
                </a>
            </td>
            <% } %>
        </tr>
        <% } } %>
        </tbody>
    </table>
</div>
</div>

<%@ include file="lib/footer.jsp" %>
