
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
import java.util.Locale;

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

        return medicaoRepository.save(medicao);
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

        // Considera a região da medição mais recente.
        String regiao = medicoes.get(0).getRegiao();

        List<Double> historico = new ArrayList<>();

        for (Medicao medicao : medicoes) {

            // Não mistura medições de regiões diferentes.
            if (regiao.equals(medicao.getRegiao())) {
                historico.add(medicao.getValor());
            }
        }

        // O histórico precisa estar em ordem cronológica.
        Collections.reverse(historico);

        EstrategiaPrevisao estrategia =
                FabricaPrevisao.criarEstrategia(tipoEstrategia);

        double previsao = estrategia.prever(historico);

        if (!Double.isFinite(previsao) || previsao < 0) {
            throw new FonteIndisponivelException(
                    "Não foi possível gerar uma previsão válida."
            );
        }

        // Os alertas agora são gerados com base na previsão.
        gerarAlertaSeNecessario(
                previsao,
                regiao,
                tipoSensor
        );

        return previsao;
    }

    private void gerarAlertaSeNecessario(
            double previsao,
            String regiao,
            String tipoSensor) {

        String nivel = null;
        double limiteUltrapassado = 0;
        String classificacao = null;

        double limiteAtencao =
                configuracao.getLimiteAtencao(tipoSensor);

        double limiteAlerta =
                configuracao.getLimiteAlerta(tipoSensor);

        double limiteCritico =
                configuracao.getLimiteCritico(tipoSensor);

        if (previsao >= limiteCritico) {

            nivel = "CRITICO";
            limiteUltrapassado = limiteCritico;
            classificacao = "limite crítico";

        } else if (previsao >= limiteAlerta) {

            nivel = "ALERTA";
            limiteUltrapassado = limiteAlerta;
            classificacao = "limite de alerta";

        } else if (previsao >= limiteAtencao) {

            nivel = "ATENCAO";
            limiteUltrapassado = limiteAtencao;
            classificacao = "limite de atenção";
        }

        if (nivel != null) {

            String descricao = String.format(
                    Locale.forLanguageTag("pt-BR"),
                    "Previsão para o sensor %s na região %s: %.2f. "
                            + "O valor previsto ultrapassou o %s de %.2f.",
                    tipoSensor,
                    regiao,
                    previsao,
                    classificacao,
                    limiteUltrapassado
            );

            Alerta alerta = new Alerta(
                    regiao,
                    nivel,
                    descricao
            );

            alertaRepository.save(alerta);
        }
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
