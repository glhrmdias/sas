package DAO;

import controller.PrincipalController;
import model.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

public class BD {

    public List<Usuario> getUsuario() {
        Conexao con = new Conexao();

        String sql = "SELECT * FROM usuario ORDER BY nome";

        List<Usuario> usuarios = new ArrayList<>();

        List<Setor> setores = getSetor();
        Map<Integer, Setor> mapSetor = new HashMap<>();

        for(Setor setor : setores) {
            mapSetor.put(setor.getId(), setor);
        }

        List<TipoUsuario> tipoUsuarios = getTipoUsuario();
        Map<Integer, TipoUsuario> mapTipoUsuario = new HashMap<>();

        for (TipoUsuario tp : tipoUsuarios) {
            mapTipoUsuario.put(tp.getId(), tp);
        }

        ResultSet resultSet = con.ExecutaSelect(sql);

        if (resultSet != null ) {
            try {
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String matric = resultSet.getString("matricula");
                    String nome = resultSet.getString("nome");
                    String senha = resultSet.getString("senha");
                    int setor = resultSet.getInt("setor_id");
                    int tp = resultSet.getInt("tipo_usuario_id");
                    Usuario usuario = new Usuario();
                    usuario.setId(id);
                    usuario.setNome(nome);
                    usuario.setMatricula(matric);
                    usuario.setSetor(mapSetor.get(setor));
                    usuario.setSenha(senha);
                    usuario.setTipoUsuario(mapTipoUsuario.get(tp));
                    usuarios.add(usuario);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        con.fecharConexao();
        return usuarios;
    }

    public List<TipoUsuario> getTipoUsuario() {
        Conexao con = new Conexao();

        List<TipoUsuario> tipoUsuarios = new ArrayList<>();

        String sql = "SELECT * from tipo_usuario";

        ResultSet resultSet = con.ExecutaSelect(sql);

        if (resultSet != null) {
            try {
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String tipo = resultSet.getString("tipo");
                    TipoUsuario tp = new TipoUsuario();
                    tp.setId(id);
                    tp.setTipoUsuario(tipo);
                    tipoUsuarios.add(tp);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        con.fecharConexao();
        return tipoUsuarios;
    }

    public List<Setor> getSetor() {

        Conexao con = new Conexao();

        List<Setor> setores = new ArrayList<Setor>();

        String sql = "SELECT * from setor";

        ResultSet resultSet = con.ExecutaSelect(sql);

        if (resultSet != null) {
            try {
                while (resultSet.next()) {
                    int idSetor = resultSet.getInt("id");
                    String nome = resultSet.getString("nome");
                    String sigla = resultSet.getString("sigla");
                    Setor setor = new Setor();
                    setor.setId(idSetor);
                    setor.setNome(nome);
                    setor.setSigla(sigla);
                    setores.add(setor);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        con.fecharConexao();
        return setores;

    }

    public Usuario getUsuarioMatricula(String matricula) {

        Conexao con = new Conexao();

        Usuario usuario = null;

        String sql = "SELECT * from usuario WHERE matricula ='" + matricula + "'";

        System.out.println(sql);

        ResultSet resultSet = con.ExecutaSelect(sql);

        List<Setor> setores = getSetor();
        Map<Integer, Setor> mapSetor = new HashMap<>();

        for(Setor setor : setores) {
            mapSetor.put(setor.getId(), setor);
        }

        List<TipoUsuario> tipoUsuarios = getTipoUsuario();
        Map<Integer, TipoUsuario> mapTipoUsuario = new HashMap<>();

        for (TipoUsuario tp : tipoUsuarios) {
            mapTipoUsuario.put(tp.getId(), tp);
        }

        if (resultSet != null ) {
            try {
                if (resultSet.next()) {
                    String matric = resultSet.getString("matricula");
                    String nome = resultSet.getString("nome");
                    String senha = resultSet.getString("senha");
                    int setor = resultSet.getInt("setor_id");
                    int tp = resultSet.getInt("tipo_usuario_id");
                    usuario = new Usuario();
                    usuario.setNome(nome);
                    usuario.setMatricula(matricula);
                    usuario.setSetor(mapSetor.get(setor));
                    usuario.setSenha(senha);
                    usuario.setTipoUsuario(mapTipoUsuario.get(tp));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        con.fecharConexao();
        return usuario;

    }

    public List<Orgao> getOrgao() {

        Conexao con = new Conexao();

        List<Orgao> orgaos = new ArrayList<Orgao>();

        String sql = "SELECT * from orgao ORDER BY orgao";

        ResultSet resultSet = con.ExecutaSelect(sql);

        if (resultSet != null) {
            try {
                while (resultSet.next()) {
                    int idOrgao = resultSet.getInt("id");
                    String org = resultSet.getString("orgao");
                    Orgao orgao = new Orgao();
                    orgao.setId(idOrgao);
                    orgao.setOrgao(org);
                    orgaos.add(orgao);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        con.fecharConexao();
        return orgaos;

    }

    public List<Localidade> getLocal() {

        Conexao con = new Conexao();

        List<Localidade> locals = new ArrayList<Localidade>();

        String sql = "SELECT * from localidade ORDER BY localidade";

        ResultSet resultSet = con.ExecutaSelect(sql);

        if (resultSet != null) {
            try {
                while (resultSet.next()) {
                    int idOrgao = resultSet.getInt("id");
                    String local = resultSet.getString("localidade");
                    Localidade loc = new Localidade();
                    loc.setId(idOrgao);
                    loc.setLocalidade(local);
                    locals.add(loc);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        con.fecharConexao();
        return locals;

    }

    // método pegando por setor
    public List<Movimentacao> listarMovimentacao(int causa) {
        Conexao con = new Conexao();

        //String sql = "SELECT * from usuario WHERE matricula ='" + matricula + "'";
        String sql = "SELECT * from movimentacao WHERE setor_id ='" + causa + "'";

        List<Movimentacao> movimetacoes = new ArrayList<Movimentacao>();

        List<Setor> setores = getSetor();
        Map<Integer, Setor> mapSetor = new HashMap<>();

        List<Orgao> orgaos = getOrgao();
        Map<Integer, Orgao> mapOrgao = new HashMap<>();

        List<Localidade> localidades = getLocal();
        Map<Integer, Localidade> mapLocal = new HashMap<>();

        for (Setor setorr : setores) {
            mapSetor.put(setorr.getId(), setorr);
        }

        for (Orgao orgao : orgaos) {
            mapOrgao.put(orgao.getId(), orgao);
        }

        for (Localidade localidade : localidades) {
            mapLocal.put(localidade.getId(), localidade);
        }

        ResultSet resultSet = con.ExecutaSelect(sql);

        if (resultSet != null) {
            try {
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    LocalDate dtRegistro = resultSet.getDate("dt_registro").toLocalDate();
                    int setorId = resultSet.getInt("setor_id");
                    String atividade = resultSet.getString("atividade");
                    String assunto = resultSet.getString("assunto");
                    String processo = resultSet.getString("processo");
                    int orgaoId = resultSet.getInt("orgao_id");
                    int localId = resultSet.getInt("local_id");
                    LocalDate dtInicio = resultSet.getDate("dt_inicio").toLocalDate();
                    LocalDate dtFim = resultSet.getDate("dt_fim").toLocalDate();
                    String hrInicio = resultSet.getString("hr_inicio");
                    String hrFim = resultSet.getString("hr_fim");
                    String conclusao = resultSet.getString("conclusao");
                    String observacao = resultSet.getString("observacao");
                    String usuario = resultSet.getString("usuario");
                    Movimentacao movi = new Movimentacao();
                    movi.setDataRegistro(dtRegistro);
                    movi.setSetor(mapSetor.get(setorId));
                    movi.setAtividade(atividade);
                    movi.setAssunto(assunto);
                    movi.setProcesso(processo);
                    movi.setOrgao(mapOrgao.get(orgaoId));
                    movi.setLocal(mapLocal.get(localId));
                    movi.setDataInicio(dtInicio);
                    movi.setDataFim(dtFim);
                    movi.setHoraInicio(hrInicio);
                    movi.setHoraFim(hrFim);
                    movi.setConclusao(conclusao);
                    movi.setObervação(observacao);
                    movi.setUsuario(usuario);
                    movimetacoes.add(movi);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        con.fecharConexao();
        return movimetacoes;
    }

    public List<Movimentacao> listarMovimentacaoNome(String causa) {
        Conexao con = new Conexao();

        //String sql = "SELECT * from usuario WHERE matricula ='" + matricula + "'";
        String sql = "SELECT * from movimentacao WHERE usuario ='" + causa + "'";

        List<Movimentacao> movimetacoes = new ArrayList<Movimentacao>();

        List<Setor> setores = getSetor();
        Map<Integer, Setor> mapSetor = new HashMap<>();

        List<Orgao> orgaos = getOrgao();
        Map<Integer, Orgao> mapOrgao = new HashMap<>();

        List<Localidade> localidades = getLocal();
        Map<Integer, Localidade> mapLocal = new HashMap<>();

        for (Setor setorr : setores) {
            mapSetor.put(setorr.getId(), setorr);
        }

        for (Orgao orgao : orgaos) {
            mapOrgao.put(orgao.getId(), orgao);
        }

        for (Localidade localidade : localidades) {
            mapLocal.put(localidade.getId(), localidade);
        }

        ResultSet resultSet = con.ExecutaSelect(sql);

        if (resultSet != null) {
            try {
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    LocalDate dtRegistro = resultSet.getDate("dt_registro").toLocalDate();
                    int setorId = resultSet.getInt("setor_id");
                    String atividade = resultSet.getString("atividade");
                    String assunto = resultSet.getString("assunto");
                    String processo = resultSet.getString("processo");
                    int orgaoId = resultSet.getInt("orgao_id");
                    int localId = resultSet.getInt("local_id");
                    LocalDate dtInicio = resultSet.getDate("dt_inicio").toLocalDate();
                    LocalDate dtFim = resultSet.getDate("dt_fim") == null ? null : resultSet.getDate("dt_fim").toLocalDate();
                    //String hrInicio = resultSet.getString("hr_inicio");
                    String tempoAtv = resultSet.getString("tempo_atividade") == null ? null : resultSet.getString("tempo_atividade");
                    String conclusao = resultSet.getString("conclusao");
                    String observacao = resultSet.getString("observacao");
                    String usuario = resultSet.getString("usuario");
                    Movimentacao movi = new Movimentacao();
                    movi.setId(id);
                    movi.setDataRegistro(dtRegistro);
                    movi.setSetor(mapSetor.get(setorId));
                    movi.setAtividade(atividade);
                    movi.setAssunto(assunto);
                    movi.setProcesso(processo);
                    movi.setOrgao(mapOrgao.get(orgaoId));
                    movi.setLocal(mapLocal.get(localId));
                    movi.setDataInicio(dtInicio);
                    movi.setDataFim(dtFim);
                    movi.setTempoAtividade(tempoAtv);
                    movi.setConclusao(conclusao);
                    movi.setObervação(observacao);
                    movi.setUsuario(usuario);
                    movimetacoes.add(movi);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        con.fecharConexao();
        return movimetacoes;
    }

    public boolean excluir(Movimentacao movimentacao) {

        Conexao con = new Conexao();

        String sql = "DELETE from movimentacao WHERE id = '" + movimentacao.getId() + "'";

        con.ExecutaSQL(sql);

        int res = con.ExecutaSQL(sql);

        con.fecharConexao();

        if (res != 0) {
            return  true;
        } else {
            return false;
        }
    }
}
