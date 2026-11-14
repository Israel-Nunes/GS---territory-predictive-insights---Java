
package br.com.gs.territory_predictive_insights.dominio;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PrevisaoComparacaoTest {

    @Test
    void deveProjetarVariacaoEntreAsDuasUltimasMedicoes() {
        // Arrange
        PrevisaoComparacao estrategia = new PrevisaoComparacao();
        List<Double> historico = List.of(20.0, 30.0, 40.0);

        // Act
        double resultado = estrategia.prever(historico);

        // Assert
        assertEquals(50.0, resultado, 0.001);
    }

    @Test
    void deveRetornarUltimoValorQuandoExisteApenasUmaMedicao() {
        // Arrange
        PrevisaoComparacao estrategia = new PrevisaoComparacao();
        List<Double> historico = List.of(35.0);

        // Act
        double resultado = estrategia.prever(historico);

        // Assert
        assertEquals(35.0, resultado, 0.001);
    }
}
