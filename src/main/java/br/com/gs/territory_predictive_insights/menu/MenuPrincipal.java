
package br.com.gs.territory_predictive_insights.menu;

import br.com.gs.territory_predictive_insights.dominio.DadosTerritorio;
import br.com.gs.territory_predictive_insights.dominio.Pluviometro;
import br.com.gs.territory_predictive_insights.dominio.Sensor;
import br.com.gs.territory_predictive_insights.dominio.SensorNivelAgua;
import br.com.gs.territory_predictive_insights.dominio.SensorUmidadeSolo;
import br.com.gs.territory_predictive_insights.entidade.Alerta;
import br.com.gs.territory_predictive_insights.entidade.Medicao;
import br.com.gs.territory_predictive_insights.excecao.DadosInvalidosException;
import br.com.gs.territory_predictive_insights.excecao.FonteIndisponivelException;
import br.com.gs.territory_predictive_insights.service.ServicoMonitoramento;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Scanner;

@Component
@ConditionalOnProperty(name = "app.menu.enabled", havingValue = "true")
public class MenuPrincipal implements CommandLineRunner {

    private final ServicoMonitoramento servico;
    private final Scanner scanner = new Scanner(System.in);
    private final DadosTerritorio dadosTerritorio = new DadosTerritorio();

    public MenuPrincipal(ServicoMonitoramento servico) {
        this.servico = servico;
    }

    @Override
    public void run(String... args) {

        int opcao;

        do {
            exibirMenu();
            opcao = lerInteiro();

            switch (opcao) {
                case 1:
                    verificarSensores();
                    break;

                case 2:
                    executarPrevisoes();
                    break;

                case 3:
                    listarMedicoes();
                    break;

                case 4:
                    listarAlertas();
                    break;

                case 5:
                    exibirStatusGeral();
                    break;

                case 6:
                    gerenciarDadosTerritorio();
                    break;

                case 0:
                    System.out.println("Encerrando o sistema...");
                    break;

                default:
                    System.out.println("Opção inválida.");
            }

        } while (opcao != 0);
    }

    private void exibirMenu() {

        System.out.println("\n========================================");
        System.out.println("   TERRITORY PREDICTIVE INSIGHTS");
        System.out.println("========================================");
        System.out.println("1 - Verificar sensores e registrar medições");
        System.out.println("2 - Executar previsões");
        System.out.println("3 - Consultar histórico de medições");
        System.out.println("4 - Consultar alertas");
        System.out.println("5 - Exibir status geral");
        System.out.println("6 - Gerenciar dados do território");
        System.out.println("0 - Sair");
        System.out.print("Escolha uma opção: ");
    }

    private int lerInteiro() {

        while (!scanner.hasNextInt()) {
            System.out.println("Digite um número válido.");
            scanner.next();
            System.out.print("Opção: ");
        }

        int valor = scanner.nextInt();
        scanner.nextLine();

        return valor;
    }

    private double lerDouble() {

        while (true) {

            String entrada = scanner.nextLine().trim()
                    .replace(",", ".");

            try {
                return Double.parseDouble(entrada);

            } catch (NumberFormatException e) {
                System.out.println("Digite um número válido.");
                System.out.print("Novo valor: ");
            }
        }
    }

    private void verificarSensores() {

        System.out.println("\n--- VERIFICAÇÃO DOS SENSORES ---");

        Pluviometro pluviometro =
                new Pluviometro(1L, "Pluviômetro Central", "Centro");

        SensorNivelAgua nivelAgua =
                new SensorNivelAgua(2L, "Sensor de Nível", "Centro");

        SensorUmidadeSolo umidadeSolo =
                new SensorUmidadeSolo(3L, "Sensor de Umidade", "Centro");

        registrarSensor(pluviometro);
        registrarSensor(nivelAgua);
        registrarSensor(umidadeSolo);
    }

    private void registrarSensor(Sensor sensor) {

        try {

            System.out.println("\nSensor: " + sensor.getTipo());

            Medicao medicao =
                    servico.registrarMedicao(sensor, "Centro");

            System.out.println("Status: Medição registrada com sucesso.");
            System.out.println("Valor: " + medicao.getValor());

        } catch (FonteIndisponivelException | DadosInvalidosException e) {

            System.out.println(
                    "Não foi possível registrar: " + e.getMessage()
            );
        }
    }

    private void executarPrevisoes() {

        System.out.println("\n--- PREVISÕES ---");
        System.out.println("1 - Pluviômetro");
        System.out.println("2 - Nível da água");
        System.out.println("3 - Umidade do solo");
        System.out.print("Escolha o sensor: ");

        int opcao = lerInteiro();

        String tipoSensor;

        switch (opcao) {

            case 1:
                tipoSensor = "PLUVIOMETRO";
                break;

            case 2:
                tipoSensor = "SENSOR_NIVEL_AGUA";
                break;

            case 3:
                tipoSensor = "SENSOR_UMIDADE_SOLO";
                break;

            default:
                System.out.println("Sensor inválido.");
                return;
        }

        executarEstrategia(tipoSensor, "MEDIA_MOVEL");
        executarEstrategia(tipoSensor, "COMPARACAO");
    }

    private void executarEstrategia(
            String sensor,
            String estrategia) {

        try {

            double resultado =
                    servico.executarPrevisao(sensor, estrategia);

            System.out.println(
                    estrategia + ": previsão de " + resultado
            );

        } catch (FonteIndisponivelException e) {

            System.out.println(e.getMessage());
        }
    }

    private void listarMedicoes() {

        System.out.println("\n--- HISTÓRICO DE MEDIÇÕES ---");
        System.out.println("1 - Pluviômetro");
        System.out.println("2 - Nível da água");
        System.out.println("3 - Umidade do solo");
        System.out.println("4 - Todas as medições");
        System.out.println("0 - Voltar");
        System.out.print("Escolha uma opção: ");

        int opcao = lerInteiro();

        List<Medicao> medicoes;

        switch (opcao) {

            case 1:
                medicoes = servico.listarMedicoesPorSensor("PLUVIOMETRO");
                break;

            case 2:
                medicoes = servico.listarMedicoesPorSensor("SENSOR_NIVEL_AGUA");
                break;

            case 3:
                medicoes = servico.listarMedicoesPorSensor("SENSOR_UMIDADE_SOLO");
                break;

            case 4:
                medicoes = servico.listarTodasMedicoes();
                break;

            case 0:
                return;

            default:
                System.out.println("Opção inválida.");
                return;
        }

        if (medicoes.isEmpty()) {
            System.out.println("Nenhuma medição encontrada para essa opção.");
            return;
        }

        System.out.println("\nTotal de medições encontradas: " + medicoes.size());

        for (Medicao medicao : medicoes) {

            System.out.println("--------------------------------");
            System.out.println("Sensor: " + medicao.getTipoSensor());
            System.out.println("Região: " + medicao.getRegiao());
            System.out.println("Valor: " + medicao.getValor());
            System.out.println("Data: " + medicao.getDataHora());
        }
    }

    private void listarAlertas() {

        System.out.println("\n--- ALERTAS REGISTRADOS ---");

        List<Alerta> alertas = servico.listarTodosAlertas();

        if (alertas.isEmpty()) {
            System.out.println("Nenhum alerta registrado.");
            return;
        }

        for (Alerta alerta : alertas) {

            System.out.println("--------------------------------");
            System.out.println("Nível: " + alerta.getNivel());
            System.out.println("Região: " + alerta.getRegiao());
            System.out.println("Descrição: " + alerta.getDescricao());
            System.out.println("Data: " + alerta.getDataHora());
        }
    }

    private void exibirStatusGeral() {

        System.out.println("\n--- STATUS GERAL DO SISTEMA ---");

        Pluviometro pluviometro =
                new Pluviometro(1L, "Pluviômetro Central", "Centro");

        SensorNivelAgua nivelAgua =
                new SensorNivelAgua(2L, "Sensor de Nível", "Centro");

        SensorUmidadeSolo umidadeSolo =
                new SensorUmidadeSolo(3L, "Sensor de Umidade", "Centro");

        System.out.println("\n--- STATUS DOS SENSORES ---");

        System.out.println(
                "Pluviômetro: " + pluviometro.verificarStatus()
        );

        System.out.println(
                "Nível da água: " + nivelAgua.verificarStatus()
        );

        System.out.println(
                "Umidade do solo: " + umidadeSolo.verificarStatus()
        );

        System.out.println("\n--- LIMITES CONFIGURADOS ---");

        exibirLimites("PLUVIOMETRO", "Pluviômetro", "mm");
        exibirLimites("SENSOR_NIVEL_AGUA", "Nível da água", "cm");
        exibirLimites("SENSOR_UMIDADE_SOLO", "Umidade do solo", "%");

        System.out.println("\n--- DADOS DO BANCO DE DADOS ---");

        System.out.println(
                "Total de medições: " +
                servico.listarTodasMedicoes().size()
        );

        System.out.println(
                "Total de alertas: " +
                servico.listarTodosAlertas().size()
        );

        System.out.println("\nSistema de monitoramento ativo.");
    }

    private void exibirLimites(
            String tipoSensor,
            String nome,
            String unidade) {

        System.out.println("\n" + nome + ":");

        System.out.println(
                "  Atenção: " +
                dadosTerritorio.getLimiteAtencao(tipoSensor) +
                " " + unidade
        );

        System.out.println(
                "  Alerta: " +
                dadosTerritorio.getLimiteAlerta(tipoSensor) +
                " " + unidade
        );

        System.out.println(
                "  Crítico: " +
                dadosTerritorio.getLimiteCritico(tipoSensor) +
                " " + unidade
        );
    }

    private void gerenciarDadosTerritorio() {

        System.out.println("\n--- GERENCIAMENTO DO TERRITÓRIO ---");
        System.out.println("1 - Pluviômetro (mm)");
        System.out.println("2 - Nível da água (cm)");
        System.out.println("3 - Umidade do solo (%)");
        System.out.print("Escolha o sensor: ");

        int opcao = lerInteiro();

        String tipoSensor;
        String unidade;

        switch (opcao) {

            case 1:
                tipoSensor = "PLUVIOMETRO";
                unidade = "mm";
                break;

            case 2:
                tipoSensor = "SENSOR_NIVEL_AGUA";
                unidade = "cm";
                break;

            case 3:
                tipoSensor = "SENSOR_UMIDADE_SOLO";
                unidade = "%";
                break;

            default:
                System.out.println("Sensor inválido.");
                return;
        }

        System.out.println("\n--- LIMITES ATUAIS ---");

        System.out.println(
                "Atenção: " +
                dadosTerritorio.getLimiteAtencao(tipoSensor) +
                " " + unidade
        );

        System.out.println(
                "Alerta: " +
                dadosTerritorio.getLimiteAlerta(tipoSensor) +
                " " + unidade
        );

        System.out.println(
                "Crítico: " +
                dadosTerritorio.getLimiteCritico(tipoSensor) +
                " " + unidade
        );

        System.out.println("\n--- ALTERAR LIMITES ---");

        System.out.print("Novo limite de atenção: ");
        double atencao = lerDouble();

        System.out.print("Novo limite de alerta: ");
        double alerta = lerDouble();

        System.out.print("Novo limite crítico: ");
        double critico = lerDouble();

        System.out.print("Código de acesso: ");
        String codigo = scanner.nextLine();

        try {

            dadosTerritorio.alterarLimites(
                    tipoSensor,
                    atencao,
                    alerta,
                    critico,
                    codigo
            );

            System.out.println(
                    "Limites atualizados com sucesso!"
            );

        } catch (DadosInvalidosException e) {

            System.out.println(
                    "Não foi possível alterar os limites: " +
                    e.getMessage()
            );
        }
    }
}
