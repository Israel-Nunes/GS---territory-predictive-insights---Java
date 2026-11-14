
package br.com.gs.territory_predictive_insights.dominio;

import java.util.List;

public class PrevisaoMediaMovel extends EstrategiaPrevisao {

    public PrevisaoMediaMovel() {
        super("Previsão por Média Móvel");
    }

    @Override
    public double prever(List<Double> historico) {

        if (historico == null || historico.isEmpty()) {
            return 0.0;
        }

        int quantidade = Math.min(3, historico.size());

        double soma = 0.0;

        for (int i = historico.size() - quantidade;
             i < historico.size(); i++) {

            soma += historico.get(i);
        }

        return soma / quantidade;
    }
}
