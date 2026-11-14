
package br.com.gs.territory_predictive_insights.dominio;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PrevisaoMediaMovelTest {

    @Test
    void deveCalcularMediaDasTresUltimasMedicoes() {
        // Arrange
        PrevisaoMediaMovel estrategia = new PrevisaoMediaMovel();
        List<Double> historico = List.of(10.0, 20.0, 30.0, 40.0);

        // Act
        double resultado = estrategia.prever(historico);

        // Assert
        assertEquals(30.0, resultado, 0.001);
    }

    @Test
    void deveRetornarZeroQuandoHistoricoEstiverVazio() {
        // Arrange
        PrevisaoMediaMovel estrategia = new PrevisaoMediaMovel();
        List<Double> historico = List.of();

        // Act
        double resultado = estrategia.prever(historico);

        // Assert
        assertEquals(0.0, resultado, 0.001);
    }
}
