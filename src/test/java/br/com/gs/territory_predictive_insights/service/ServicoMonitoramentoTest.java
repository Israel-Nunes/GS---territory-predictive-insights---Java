
package br.com.gs.territory_predictive_insights.service;

import br.com.gs.territory_predictive_insights.dominio.Sensor;
import br.com.gs.territory_predictive_insights.entidade.Alerta;
import br.com.gs.territory_predictive_insights.entidade.Medicao;
import br.com.gs.territory_predictive_insights.excecao.FonteIndisponivelException;
import br.com.gs.territory_predictive_insights.repository.AlertaRepository;
import br.com.gs.territory_predictive_insights.repository.MedicaoRepository;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

        when(medicaoRepository.save(any(Medicao.class)))
                .thenAnswer(invocacao -> invocacao.getArgument(0));

        ServicoMonitoramento servico =
                new ServicoMonitoramento(medicaoRepository, alertaRepository);

        // Act
        Medicao resultado = servico.registrarMedicao(sensor, "Centro");

        // Assert
        assertEquals(30.0, resultado.getValor(), 0.001);
        assertEquals("Centro", resultado.getRegiao());

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
    void deveGerarAlertaCriticoQuandoPrevisaoUltrapassarLimite()
            throws Exception {

        // Arrange
        MedicaoRepository medicaoRepository = mock(MedicaoRepository.class);
        AlertaRepository alertaRepository = mock(AlertaRepository.class);

        List<Medicao> historico = List.of(
                new Medicao("PLUVIOMETRO", "Centro", 95.0),
                new Medicao("PLUVIOMETRO", "Centro", 90.0)
        );

        when(medicaoRepository.findByTipoSensorOrderByDataHoraDesc(
                "PLUVIOMETRO"
        )).thenReturn(historico);

        ServicoMonitoramento servico =
                new ServicoMonitoramento(medicaoRepository, alertaRepository);

        ArgumentCaptor<Alerta> captor =
                ArgumentCaptor.forClass(Alerta.class);

        // Act
        double previsao = servico.executarPrevisao(
                "PLUVIOMETRO",
                "COMPARACAO"
        );

        // Assert
        assertEquals(100.0, previsao, 0.001);

        verify(alertaRepository).save(captor.capture());

        Alerta alertaGerado = captor.getValue();

        assertEquals("CRITICO", alertaGerado.getNivel());
        assertEquals("Centro", alertaGerado.getRegiao());

        assertTrue(alertaGerado.getDescricao().contains("PLUVIOMETRO"));
        assertTrue(alertaGerado.getDescricao().contains("100,00"));
        assertTrue(alertaGerado.getDescricao().contains("90,00"));
    }
}
