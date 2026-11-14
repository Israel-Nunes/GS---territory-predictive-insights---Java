
package br.com.gs.territory_predictive_insights.dominio;

import java.util.List;

public abstract class EstrategiaPrevisao {

    private String nomeEstrategia;

    public EstrategiaPrevisao(String nomeEstrategia) {
        this.nomeEstrategia = nomeEstrategia;
    }

    public String getNomeEstrategia() {
        return nomeEstrategia;
    }

    public abstract double prever(List<Double> historico);
}
