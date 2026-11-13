<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String ctx = request.getContextPath();
    String error = (String) request.getAttribute("error");
    String email = (String) request.getAttribute("email");
    if (email == null) email = "";
%>
<!DOCTYPE html>
<html lang="es" data-bs-theme="dark">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Ingresar | GAMEZONE</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css" rel="stylesheet">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Orbitron:wght@500;700;900&family=Rajdhani:wght@400;500;600;700&display=swap" rel="stylesheet">
    <link href="<%= ctx %>/css/style.css" rel="stylesheet">
</head>
<body>
<div class="container gz-login-wrap my-auto py-4">
    <div class="gz-login">
        <section class="gz-hero">
            <h1>GAMEZONE</h1>
        </section>
        <div class="gz-card gz-card-purple">
            <div class="gz-card-body">
                <% if (error != null) { %>
                <div class="alert gz-alert gz-alert-danger" role="alert"><%= error %></div>
                <% } %>
                <form method="post" action="<%= ctx %>/login" class="gz-form" novalidate>
                    <div class="mb-3">
                        <label for="login-email" class="form-label">Correo</label>
                        <input type="email" id="login-email" name="email" class="form-control" required
                               value="<%= email %>" placeholder="tucorreo@correo.com" autocomplete="username">
                    </div>
                    <div class="mb-4">
                        <label for="login-password" class="form-label">Contraseña</label>
                        <input type="password" id="login-password" name="password" class="form-control" required
                               placeholder="••••••••" autocomplete="current-password">
                    </div>
                    <div class="d-grid">
                        <button type="submit" class="btn btn-neon-green">
                            <i class="bi bi-box-arrow-in-right"></i> Entrar a la arena
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
</body>
</html>
