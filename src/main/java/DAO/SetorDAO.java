package DAO;

import model.Setor;

public class SetorDAO {

    public boolean inserir(Setor setor) {
        Conexao conexao = new Conexao();

        String sql = "INSERT into setor(nome, sigla)"
                + " VALUES ('"
                + setor.getNome() +"', '"
                + setor.getSigla() + "');";

        int res = conexao.ExecutaSQL(sql);
        System.out.println(sql);
        conexao.fecharConexao();

        if (res != 0){
            return true;
        } else {
            return false;
        }

    }
}
