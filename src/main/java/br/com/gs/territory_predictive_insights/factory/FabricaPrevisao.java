package br.com.gs.territory_predictive_insights.factory;

import br.com.gs.territory_predictive_insights.dominio.EstrategiaPrevisao;
import br.com.gs.territory_predictive_insights.dominio.PrevisaoComparacao;
import br.com.gs.territory_predictive_insights.dominio.PrevisaoMediaMovel;

public class FabricaPrevisao {

    public static EstrategiaPrevisao criarEstrategia(String tipo) {

        if (tipo == null) {
            throw new IllegalArgumentException("Tipo de previsão não informado.");
        }

        switch (tipo.toUpperCase()) {
            case "MEDIA_MOVEL":
                return new PrevisaoMediaMovel();

            case "COMPARACAO":
                return new PrevisaoComparacao();

            default:
                throw new IllegalArgumentException(
                        "Estratégia de previsão desconhecida: " + tipo
                );
        }
    }
}