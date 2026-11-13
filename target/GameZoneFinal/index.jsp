<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="modelo.Usuario" %>
<%
    Usuario u = (Usuario) session.getAttribute("usuario");
    if (u == null) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }
    String ctx = request.getContextPath();
%>
<%@ include file="lib/header.jsp" %>

<section class="gz-hero">
    <h1>GAMEZONE</h1>
    <p>La plataforma de torneos de eSports donde tu equipo escribe su leyenda</p>
    <p class="mb-0 mt-0">Hola, <strong><%= u.getNombre() %></strong>
        <span class="gz-badge gz-badge-juego ms-1"><%= u.getRol() %></span>
    </p>
</section>

<div class="row g-4 justify-content-center">
    <% if (esAdmin) { %>
    <div class="col-md-4">
        <div class="gz-card gz-card-hover h-100">
            <div class="gz-card-body text-center">
                <i class="bi bi-people display-4 gz-icon"></i>
                <h5 class="mt-3 gz-card-title">Usuarios</h5>
                <div class="mt-3 d-flex gap-2 justify-content-center flex-wrap">
                    <a href="<%= ctx %>/usuario?action=listar" class="btn btn-neon-purple btn-sm">Administrar</a>
                    <a href="<%= ctx %>/usuario?action=nuevo" class="btn btn-neon btn-sm">Nuevo</a>
                </div>
            </div>
        </div>
    </div>
    <% } %>
    <div class="col-md-4">
        <div class="gz-card gz-card-hover h-100">
            <div class="gz-card-body text-center">
                <i class="bi bi-trophy display-4 gz-icon"></i>
                <h5 class="mt-3 gz-card-title">Torneos</h5>
                <div class="mt-3 d-flex gap-2 justify-content-center flex-wrap">
                    <% if (esAdmin) { %>
                    <a href="<%= ctx %>/item?action=listar" class="btn btn-neon-purple btn-sm">Administrar</a>
                    <a href="<%= ctx %>/item?action=nuevo" class="btn btn-neon btn-sm">Nuevo</a>
                    <% } else { %>
                    <a href="<%= ctx %>/item?action=listar" class="btn btn-neon-purple btn-sm">Ver torneos</a>
                    <% } %>
                </div>
            </div>
        </div>
    </div>
    <% if (esAdmin) { %>
    <div class="col-md-4">
        <div class="gz-card gz-card-hover h-100">
            <div class="gz-card-body text-center">
                <i class="bi bi-ticket-perforated display-4 gz-icon"></i>
                <h5 class="mt-3 gz-card-title">Inscripciones</h5>
                <div class="mt-3 d-flex gap-2 justify-content-center flex-wrap">
                    <a href="<%= ctx %>/venta" class="btn btn-neon-purple btn-sm">Inscribir</a>
                </div>
            </div>
        </div>
    </div>
    <% } %>
</div>

<%@ include file="lib/footer.jsp" %>
