# GameZoneFinal — Proyecto Final JSP + Servlets + MVC

Aplicación web Java para administración de **torneos de videojuegos** (GameZone).

Cumple todos los requisitos del caso de estudio:

1. **Diagrama de clases / requerimientos** (ver sección al final).
2. **Arquitectura MVC**:
   - Vista: JSP + Bootstrap 5
   - Modelo: `Usuario`, `Item` (Torneo), `Venta` (asociación)
   - Gestión: `GestionarUsuario`, `GestionarItem`, `GestionarVenta`
   - Servlets: `ServletLogin`, `ServletUsuario`, `ServletItem`, `ServletVenta`, `ServletLogout`
3. **Interfaces**: `index.jsp`, `login.jsp`, `addUsers.jsp`, `listUsers.jsp`, `addItems.jsp`, `listItems.jsp`, `venta.jsp`
4. **CRUD completo** con `ArrayList` + sincronización a archivos `.txt`
5. **Persistencia**:
   - `webapp/datos/usuarios.txt`
   - `webapp/datos/items.txt`
   - `webapp/datos/reporte.txt`
6. **Excepciones** controladas en lectura/escritura de archivos
7. **Códigos únicos**, validación cliente + servidor, sesiones

---

## Cómo importar y ejecutar

### Opción A — NetBeans / Eclipse / IntelliJ (Maven)

1. Abrir el proyecto como **Maven project** (pom.xml).
2. Configurar servidor: **Apache Tomcat 10+** (Jakarta EE 9/10).
3. Clean & Build.
4. Deploy / Run.

### Opción B — WAR

```bash
mvn clean package
# El WAR queda en target/GameZoneFinal.war
# Copiar a $CATALINA_HOME/webapps/
```

### Login demo

| Email                | Contraseña | Rol    |
|----------------------|------------|--------|
| admin@gamezone.com   | admin123   | admin  |
| andres@correo.com    | jugador123 | jugador|

---

## Estructura del proyecto

```
GameZoneFinal/
├── pom.xml
├── src/main/java/
│   ├── modelo/
│   │   ├── Usuario.java
│   │   ├── Item.java          ← representa un Torneo
│   │   └── Venta.java         ← asociación Usuario–Item (inscripción)
│   ├── gestion/
│   │   ├── GestionarUsuario.java
│   │   ├── GestionarItem.java
│   │   └── GestionarVenta.java
│   ├── util/
│   │   └── Rutas.java
│   └── servlets/
│       ├── ServletLogin.java
│       ├── ServletUsuario.java
│       ├── ServletItem.java
│       ├── ServletVenta.java
│       └── ServletLogout.java
└── src/main/webapp/
    ├── WEB-INF/web.xml
    ├── css/style.css
    ├── datos/
    │   ├── usuarios.txt
    │   ├── items.txt
    │   └── reporte.txt
    ├── lib/header.jsp, footer.jsp
    ├── index.jsp
    ├── login.jsp
    ├── addUsers.jsp
    ├── listUsers.jsp
    ├── addItems.jsp
    ├── listItems.jsp
    └── venta.jsp
```

---

## Flujo MVC típico

```
JSP (formulario) → Servlet → Clase de Gestión → ArrayList + .txt → respuesta / redirect → JSP
```

La clase `Rutas` resuelve la ruta física de `datos/` mediante `ServletContext.getRealPath()`.

---

## Notas importantes

- **Tomcat 10+** es obligatorio (Jakarta namespace). Tomcat 9 usa `javax.*` y no compilará.
- Los archivos `.txt` se crean/actualizan dentro del directorio desplegado. En desarrollo con IDE, suele ser `target/.../datos/` o la carpeta del servidor.
- Si `getRealPath` devuelve `null` (algunos servidores embebidos), se usa un fallback a `user.dir/datos`.

¡Éxitos!
```
