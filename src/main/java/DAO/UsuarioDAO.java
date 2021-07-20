    package DAO;

    import model.Usuario;

    public class UsuarioDAO {

        public boolean cadastroUsuario(Usuario usuario) {

            Conexao con = new Conexao();

            String sql = "INSERT into usuario (nome, matricula, setor_id, senha, tipo_usuario_id)"
                    + " VALUES('"
                    + usuario.getNome()
                    + "', '" + usuario.getMatricula()
                    + "', '" + usuario.getSetor().getId()
                    + "', '" + usuario.getSenha()
                    + "', '" + usuario.getTipoUsuario().getId()
                    + "');";

            System.out.println(sql);

            int res = con.ExecutaSQL(sql);

            con.fecharConexao();

            if (res != 0) {
                return true;
            } else {
                return false;
            }
        }

    }
