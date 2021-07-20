package view;

import model.Setor;
import model.Usuario;

public class Sessao {

    private Usuario usuario;
    private Setor setor;

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Setor getSetor() {
        return setor;
    }

    public void setSetor(Setor setor) {
        this.setor = setor;
    }
}
