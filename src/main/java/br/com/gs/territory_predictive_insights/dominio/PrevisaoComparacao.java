
package br.com.gs.territory_predictive_insights.dominio;

import java.util.List;

public class PrevisaoComparacao extends EstrategiaPrevisao {

    public PrevisaoComparacao() {
        super("Previsão por Comparação");
    }

    @Override
    public double prever(List<Double> historico) {

        if (historico == null || historico.isEmpty()) {
            return 0.0;
        }

        double ultimaMedicao = historico.get(historico.size() - 1);

        if (historico.size() < 2) {
            return ultimaMedicao;
        }

        double medicaoAnterior = historico.get(historico.size() - 2);

        double variacao = ultimaMedicao - medicaoAnterior;

        return ultimaMedicao + variacao;
    }
}
