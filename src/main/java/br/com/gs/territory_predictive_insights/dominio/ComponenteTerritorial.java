
package br.com.gs.territory_predictive_insights.dominio;

public abstract class ComponenteTerritorial {

    private Long id;
    private String nome;
    private String regiao;
    private String status;

    public ComponenteTerritorial(Long id, String nome, String regiao) {
        this.id = id;
        this.nome = nome;
        this.regiao = regiao;
        this.status = "INICIALIZANDO";
    }

    public abstract String verificarStatus();

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getRegiao() {
        return regiao;
    }

    public String getStatus() {
        return status;
    }

    protected void setStatus(String status) {
        this.status = status;
    }
}
