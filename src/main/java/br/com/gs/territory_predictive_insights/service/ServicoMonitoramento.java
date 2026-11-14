
package br.com.gs.territory_predictive_insights.service;

import br.com.gs.territory_predictive_insights.config.ConfiguracaoSistema;
import br.com.gs.territory_predictive_insights.dominio.EstrategiaPrevisao;
import br.com.gs.territory_predictive_insights.dominio.Sensor;
import br.com.gs.territory_predictive_insights.entidade.Alerta;
import br.com.gs.territory_predictive_insights.entidade.Medicao;
import br.com.gs.territory_predictive_insights.excecao.DadosInvalidosException;
import br.com.gs.territory_predictive_insights.excecao.FonteIndisponivelException;
import br.com.gs.territory_predictive_insights.factory.FabricaPrevisao;
import br.com.gs.territory_predictive_insights.repository.AlertaRepository;
import br.com.gs.territory_predictive_insights.repository.MedicaoRepository;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class ServicoMonitoramento {

    private final MedicaoRepository medicaoRepository;
    private final AlertaRepository alertaRepository;

    private final ConfiguracaoSistema configuracao =
            ConfiguracaoSistema.getInstancia();

    public ServicoMonitoramento(
            MedicaoRepository medicaoRepository,
            AlertaRepository alertaRepository) {

        this.medicaoRepository = medicaoRepository;
        this.alertaRepository = alertaRepository;
    }

    public Medicao registrarMedicao(Sensor sensor, String regiao)
            throws FonteIndisponivelException, DadosInvalidosException {

        if (sensor == null || !sensor.estaFuncionando()) {
            throw new FonteIndisponivelException(
                    "Sensor indisponível para realizar a leitura."
            );
        }

        double valor = sensor.lerValor();

        if (!Double.isFinite(valor) || valor < 0) {
            throw new DadosInvalidosException(
                    "O sensor retornou uma medição inválida."
            );
        }

        Medicao medicao = new Medicao(
                sensor.getTipo(),
                regiao,
                valor
        );

        medicaoRepository.save(medicao);

        gerarAlertaSeNecessario(
                valor,
                regiao,
                sensor.getTipo()
        );

        return medicao;
    }

    private void gerarAlertaSeNecessario(
            double valor,
            String regiao,
            String tipoSensor) {

        String nivel = null;
        String descricao = null;

        double limiteAtencao =
                configuracao.getLimiteAtencao(tipoSensor);

        double limiteAlerta =
                configuracao.getLimiteAlerta(tipoSensor);

        double limiteCritico =
                configuracao.getLimiteCritico(tipoSensor);

        if (valor >= limiteCritico) {

            nivel = "CRITICO";

            descricao =
                    "Valor crítico identificado. A região exige atenção imediata.";

        } else if (valor >= limiteAlerta) {

            nivel = "ALERTA";

            descricao =
                    "Valor elevado identificado. Recomenda-se verificar a região.";

        } else if (valor >= limiteAtencao) {

            nivel = "ATENCAO";

            descricao =
                    "Valor acima do limite de atenção. Continue monitorando.";
        }

        if (nivel != null) {

            Alerta alerta = new Alerta(
                    regiao,
                    nivel,
                    descricao
            );

            alertaRepository.save(alerta);
        }
    }

    public double executarPrevisao(
            String tipoSensor,
            String tipoEstrategia
    ) throws FonteIndisponivelException {

        List<Medicao> medicoes =
                medicaoRepository.findByTipoSensorOrderByDataHoraDesc(
                        tipoSensor
                );

        if (medicoes.isEmpty()) {
            throw new FonteIndisponivelException(
                    "Não existem medições disponíveis para esse sensor."
            );
        }

        List<Double> historico = new ArrayList<>();

        for (Medicao medicao : medicoes) {
            historico.add(medicao.getValor());
        }

        // O repositório retorna da medição mais recente
        // para a mais antiga. Invertendo, obtemos a ordem cronológica.
        Collections.reverse(historico);

        EstrategiaPrevisao estrategia =
                FabricaPrevisao.criarEstrategia(tipoEstrategia);

        return estrategia.prever(historico);
    }

    public List<Medicao> listarMedicoesPorSensor(String tipoSensor) {

        return medicaoRepository
                .findByTipoSensorOrderByDataHoraDesc(tipoSensor);
    }

    public List<Alerta> listarTodosAlertas() {

        return alertaRepository.findAllByOrderByDataHoraDesc();
    }

    public List<Medicao> listarTodasMedicoes() {

        return medicaoRepository.findAllByOrderByDataHoraDesc();
    }
}
