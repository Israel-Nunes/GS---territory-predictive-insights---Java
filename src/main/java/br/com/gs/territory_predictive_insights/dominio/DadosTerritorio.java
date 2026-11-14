
package br.com.gs.territory_predictive_insights.dominio;

import br.com.gs.territory_predictive_insights.config.ConfiguracaoSistema;
import br.com.gs.territory_predictive_insights.excecao.DadosInvalidosException;

public class DadosTerritorio {

    private static final String CODIGO_ACESSO = "TPI2026";

    private final ConfiguracaoSistema configuracao =
            ConfiguracaoSistema.getInstancia();

    public void alterarLimites(
            String tipoSensor,
            double atencao,
            double alerta,
            double critico,
            String codigo) throws DadosInvalidosException {

        if (!CODIGO_ACESSO.equals(codigo)) {
            throw new DadosInvalidosException(
                    "Código de acesso incorreto. Alteração não autorizada."
            );
        }

        try {
            configuracao.atualizarLimites(
                    tipoSensor,
                    atencao,
                    alerta,
                    critico
            );

        } catch (IllegalArgumentException e) {
            throw new DadosInvalidosException(e.getMessage());
        }
    }

    public double getLimiteAtencao(String tipoSensor) {
        return configuracao.getLimiteAtencao(tipoSensor);
    }

    public double getLimiteAlerta(String tipoSensor) {
        return configuracao.getLimiteAlerta(tipoSensor);
    }

    public double getLimiteCritico(String tipoSensor) {
        return configuracao.getLimiteCritico(tipoSensor);
    }
}
