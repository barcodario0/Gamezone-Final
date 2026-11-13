package modelo;

import java.io.Serializable;

public class Usuario implements Serializable {
    private static final long serialVersionUID = 1L;

    private int idUsuario;
    private String nombre;
    private String email;
    private String password;
    private String rol;
    private String nickname;
    private String telefono;

    public Usuario() {
    }

    public Usuario(int idUsuario, String nombre, String email, String password,
                   String rol, String nickname, String telefono) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.email = email;
        this.password = password;
        this.rol = rol;
        this.nickname = nickname;
        this.telefono = telefono;
    }

    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }

    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String toLinea() {
        return idUsuario + "|" +
               (nombre != null ? nombre : "") + "|" +
               (email != null ? email : "") + "|" +
               (password != null ? password : "") + "|" +
               (rol != null ? rol : "jugador") + "|" +
               (nickname != null ? nickname : "") + "|" +
               (telefono != null ? telefono : "");
    }

    public static Usuario fromLinea(String linea) {
        if (linea == null || linea.trim().isEmpty() || linea.startsWith("#")) {
            return null;
        }
        String[] p = linea.split("\\|", -1);
        if (p.length < 5) return null;
        try {
            int id = Integer.parseInt(p[0].trim());
            String nombre = p[1].trim();
            String email = p[2].trim();
            String password = p[3].trim();
            String rol = p[4].trim();
            String nickname = p.length > 5 ? p[5].trim() : "";
            String telefono = p.length > 6 ? p[6].trim() : "";
            return new Usuario(id, nombre, email, password, rol, nickname, telefono);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Override
    public String toString() {
        return "Usuario{id=" + idUsuario + ", nombre='" + nombre + "', email='" + email +
               "', rol='" + rol + "', nickname='" + nickname + "'}";
    }
}
