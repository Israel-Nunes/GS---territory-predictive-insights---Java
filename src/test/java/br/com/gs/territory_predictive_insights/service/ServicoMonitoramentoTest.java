
package br.com.gs.territory_predictive_insights.service;

import br.com.gs.territory_predictive_insights.dominio.Sensor;
import br.com.gs.territory_predictive_insights.entidade.Alerta;
import br.com.gs.territory_predictive_insights.entidade.Medicao;
import br.com.gs.territory_predictive_insights.excecao.FonteIndisponivelException;
import br.com.gs.territory_predictive_insights.repository.AlertaRepository;
import br.com.gs.territory_predictive_insights.repository.MedicaoRepository;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ServicoMonitoramentoTest {

    @Test
    void deveRegistrarMedicaoComSucessoUsandoMocks() throws Exception {
        // Arrange
        MedicaoRepository medicaoRepository = mock(MedicaoRepository.class);
        AlertaRepository alertaRepository = mock(AlertaRepository.class);
        Sensor sensor = mock(Sensor.class);

        when(sensor.estaFuncionando()).thenReturn(true);
        when(sensor.lerValor()).thenReturn(30.0);
        when(sensor.getTipo()).thenReturn("PLUVIOMETRO");

        ServicoMonitoramento servico =
                new ServicoMonitoramento(medicaoRepository, alertaRepository);

        // Act
        Medicao resultado = servico.registrarMedicao(sensor, "Centro");

        // Assert
        assertEquals(30.0, resultado.getValor(), 0.001);

        verify(medicaoRepository).save(any(Medicao.class));
        verify(alertaRepository, never()).save(any(Alerta.class));
    }

    @Test
    void deveLancarExcecaoQuandoSensorEstiverIndisponivel() {
        // Arrange
        MedicaoRepository medicaoRepository = mock(MedicaoRepository.class);
        AlertaRepository alertaRepository = mock(AlertaRepository.class);
        Sensor sensor = mock(Sensor.class);

        when(sensor.estaFuncionando()).thenReturn(false);

        ServicoMonitoramento servico =
                new ServicoMonitoramento(medicaoRepository, alertaRepository);

        // Act e Assert
        assertThrows(
                FonteIndisponivelException.class,
                () -> servico.registrarMedicao(sensor, "Centro")
        );

        verifyNoInteractions(medicaoRepository, alertaRepository);
    }

    @Test
    void deveGerarAlertaCriticoQuandoMedicaoForElevada() throws Exception {
        // Arrange
        MedicaoRepository medicaoRepository = mock(MedicaoRepository.class);
        AlertaRepository alertaRepository = mock(AlertaRepository.class);
        Sensor sensor = mock(Sensor.class);

        when(sensor.estaFuncionando()).thenReturn(true);
        when(sensor.lerValor()).thenReturn(95.0);
        when(sensor.getTipo()).thenReturn("PLUVIOMETRO");

        ServicoMonitoramento servico =
                new ServicoMonitoramento(medicaoRepository, alertaRepository);

        ArgumentCaptor<Alerta> captor = ArgumentCaptor.forClass(Alerta.class);

        // Act
        servico.registrarMedicao(sensor, "Centro");

        // Assert
        verify(alertaRepository).save(captor.capture());

        assertEquals("CRITICO", captor.getValue().getNivel());
        assertEquals("Centro", captor.getValue().getRegiao());
    }
}
