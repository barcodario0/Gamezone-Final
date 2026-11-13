package util;

import jakarta.servlet.ServletContext;
import java.io.File;

public final class Rutas {
    public static final String CARPETA_DATOS = "datos";
    public static final String ARCHIVO_USUARIOS = "usuarios.txt";
    public static final String ARCHIVO_ITEMS = "items.txt";
    public static final String ARCHIVO_REPORTE = "reporte.txt";

    private Rutas() {
    }

    public static File obtenerArchivo(ServletContext context, String nombreArchivo) {
        String realPath = context.getRealPath("/" + CARPETA_DATOS);
        if (realPath == null) {
            realPath = System.getProperty("user.dir") + File.separator + "datos";
        }
        File carpeta = new File(realPath);
        if (!carpeta.exists()) {
            carpeta.mkdirs();
        }
        return new File(carpeta, nombreArchivo);
    }
}
