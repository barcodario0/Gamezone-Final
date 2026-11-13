<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="modelo.Usuario" %>
<%
    Usuario usuarioSesion = (Usuario) session.getAttribute("usuario");
    boolean esAdmin = usuarioSesion != null && "admin".equals(usuarioSesion.getRol());
    String ruta = request.getServletPath();
    if (ruta == null) { ruta = ""; }
    String activo = "";
    if (ruta.contains("index")) { activo = "inicio"; }
    else if (ruta.contains("Users")) { activo = "usuarios"; }
    else if (ruta.contains("Items")) { activo = "torneos"; }
    else if (ruta.contains("venta")) { activo = "inscripciones"; }
%>
<!DOCTYPE html>
<html lang="es" data-bs-theme="dark">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>GAMEZONE</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css" rel="stylesheet">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Orbitron:wght@500;700;900&family=Rajdhani:wght@400;500;600;700&display=swap" rel="stylesheet">
    <link href="<%= ctx %>/css/style.css" rel="stylesheet">
</head>
<body>
<nav class="navbar navbar-expand-lg gz-navbar sticky-top">
    <div class="container">
        <a class="navbar-brand gz-brand" href="<%= ctx %>/index.jsp">GAME<span>ZONE</span></a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navMain"
                aria-controls="navMain" aria-expanded="false" aria-label="Abrir menú">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navMain">
            <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                <li class="nav-item"><a class="nav-link <%= "inicio".equals(activo) ? "active" : "" %>" href="<%= ctx %>/index.jsp"><i class="bi bi-house"></i> Inicio</a></li>
                <% if (usuarioSesion != null) { %>
                <li class="nav-item"><a class="nav-link <%= "torneos".equals(activo) ? "active" : "" %>" href="<%= ctx %>/item?action=listar"><i class="bi bi-trophy"></i> Torneos</a></li>
                <% } %>
                <% if (esAdmin) { %>
                <li class="nav-item"><a class="nav-link <%= "usuarios".equals(activo) ? "active" : "" %>" href="<%= ctx %>/usuario?action=listar"><i class="bi bi-people"></i> Usuarios</a></li>
                <li class="nav-item"><a class="nav-link <%= "inscripciones".equals(activo) ? "active" : "" %>" href="<%= ctx %>/venta"><i class="bi bi-ticket-perforated"></i> Inscripciones</a></li>
                <% } %>
            </ul>
            <div class="d-flex align-items-center gap-2">
                <% if (usuarioSesion != null) { %>
                <span class="gz-user"><i class="bi bi-person-circle"></i> <%= usuarioSesion.getNickname() %></span>
                <span class="gz-badge gz-badge-juego"><%= usuarioSesion.getRol() %></span>
                <a class="btn btn-sm btn-neon-danger" href="<%= ctx %>/logout"><i class="bi bi-box-arrow-right"></i> Salir</a>
                <% } else { %>
                <a class="btn btn-sm btn-neon" href="<%= ctx %>/login.jsp">Ingresar</a>
                <% } %>
            </div>
        </div>
    </div>
</nav>
<main class="container gz-main">
<%
    String msg = (String) session.getAttribute("msg");
    String err = (String) session.getAttribute("error");
    if (msg != null) {
        session.removeAttribute("msg");
%>
    <div class="alert gz-alert gz-alert-success alert-dismissible fade show" role="alert">
        <i class="bi bi-check-circle"></i> <%= msg %>
        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Cerrar"></button>
    </div>
<% } if (err != null) { session.removeAttribute("error"); %>
    <div class="alert gz-alert gz-alert-danger alert-dismissible fade show" role="alert">
        <i class="bi bi-exclamation-triangle"></i> <%= err %>
        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Cerrar"></button>
    </div>
<% } %>
