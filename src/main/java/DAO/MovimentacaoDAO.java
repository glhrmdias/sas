package DAO;

import model.Movimentacao;
import model.Usuario;

public class MovimentacaoDAO {

    public boolean cadastroMovimentacao(Movimentacao movimentacao) {

        Conexao con = new Conexao();

        String sql = "INSERT into movimentacao (dt_registro, setor_id, atividade, assunto," +
                "processo, orgao_id, local_id, dt_inicio, dt_fim, tempo_atividade, conclusao, observacao, usuario)"
                + " VALUES('"
                + movimentacao.getDataRegistro()
                + "', '" + movimentacao.getSetor().getId()
                + "', '" + movimentacao.getAtividade()
                + "', '" + movimentacao.getAssunto()
                + "', '" + movimentacao.getProcesso()
                + "', '" + movimentacao.getOrgao().getId()
                + "', '" + movimentacao.getLocal().getId()
                + "', '" + movimentacao.getDataInicio()
                + "', " + (movimentacao.getDataFim() == null ? null : "'" + movimentacao.getDataFim() + "'")
                + ", '" + movimentacao.getTempoAtividade()
                + "', '" + movimentacao.getConclusao()
                + "', '" + movimentacao.getObervação()
                + "', '" + movimentacao.getUsuario()
                + "');";

                // (movimentacao.getDataFim() == null ? null : "'" + movimentacao.getDataFim())
                // (movimentacao.getHoraFim() == null ? null : "'" + movimentacao.getHoraFim())

        System.out.println(sql);

        int res = con.ExecutaSQL(sql);

        con.fecharConexao();

        if (res != 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean attMovimentacao(Movimentacao mov){
        Conexao con = new Conexao();

        String sql = "UPDATE movimentacao set dt_registro = '" + mov.getDataRegistro()
                + "', setor_id = '" + mov.getSetor().getId()
                + "', atividade = '" + mov.getAtividade()
                + "', assunto = '" + mov.getAssunto()
                + "', processo = '" + mov.getProcesso()
                + "', orgao_id = '" + mov.getOrgao().getId()
                + "', local_id = '" + mov.getLocal().getId()
                + "', dt_inicio = '" + mov.getDataInicio()
                + "', dt_fim = '" + mov.getDataFim()
                //+ "', hr_inicio = '" + mov.getHoraInicio()
                + "', tempo_atividade = '" + mov.getTempoAtividade()
                + "', conclusao = '" + mov.getConclusao()
                + "', observacao = '" + mov.getObervação()
                + "' WHERE id = '" + mov.getId() + "'";

        int res = con.ExecutaSQL(sql);
        System.out.println(sql);
        con.fecharConexao();
        if (res != 0) {
            return true;
        } else {
            return false;
        }
    }
}
