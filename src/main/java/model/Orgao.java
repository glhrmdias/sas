package model;

public class Orgao {

    public int id;
    public String orgao;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrgao() {
        return orgao;
    }

    public void setOrgao(String orgao) {
        this.orgao = orgao;
    }

    @Override
    public String toString() {
        return orgao;
    }
}
