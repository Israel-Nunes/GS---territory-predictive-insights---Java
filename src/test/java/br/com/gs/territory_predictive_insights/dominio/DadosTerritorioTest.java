
package br.com.gs.territory_predictive_insights.dominio;

import br.com.gs.territory_predictive_insights.excecao.DadosInvalidosException;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DadosTerritorioTest {

    @Test
    void deveImpedirAlteracaoComCodigoIncorreto() {

        // Arrange
        DadosTerritorio dados = new DadosTerritorio();

        double limiteOriginal =
                dados.getLimiteAtencao("PLUVIOMETRO");

        // Act e Assert
        assertThrows(
                DadosInvalidosException.class,
                () -> dados.alterarLimites(
                        "PLUVIOMETRO",
                        60.0,
                        80.0,
                        100.0,
                        "CODIGOERRADO"
                )
        );

        assertEquals(
                limiteOriginal,
                dados.getLimiteAtencao("PLUVIOMETRO")
        );
    }

    @Test
    void deveAlterarLimitesComCodigoCorreto() throws Exception {

        // Arrange
        DadosTerritorio dados = new DadosTerritorio();

        // Act
        dados.alterarLimites(
                "PLUVIOMETRO",
                60.0,
                80.0,
                100.0,
                "TPI2026"
        );

        // Assert
        assertEquals(60.0, dados.getLimiteAtencao("PLUVIOMETRO"));
        assertEquals(80.0, dados.getLimiteAlerta("PLUVIOMETRO"));
        assertEquals(100.0, dados.getLimiteCritico("PLUVIOMETRO"));

        // Restaura os limites originais para não afetar outros testes.
        dados.alterarLimites(
                "PLUVIOMETRO",
                50.0,
                70.0,
                90.0,
                "TPI2026"
        );
    }
}
