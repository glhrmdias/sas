package model;

public class UsuarioSingleton {
    public Usuario usuario;
    private static UsuarioSingleton instancia = null;

    private UsuarioSingleton(Usuario usuario) {

    }

    public static UsuarioSingleton getInstance(Usuario usuario) {
        System.out.println("Instancia criada...");
        if (instancia == null) {
            instancia = new UsuarioSingleton(usuario);
        }
        return instancia;
    }
}
