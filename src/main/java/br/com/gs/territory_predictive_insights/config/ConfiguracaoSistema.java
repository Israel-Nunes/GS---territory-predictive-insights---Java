
package br.com.gs.territory_predictive_insights.config;

public class ConfiguracaoSistema {

    private static ConfiguracaoSistema instancia;

    // Limites do pluviômetro (mm)
    private double chuvaAtencao = 50.0;
    private double chuvaAlerta = 70.0;
    private double chuvaCritico = 90.0;

    // Limites do nível da água (cm)
    private double aguaAtencao = 100.0;
    private double aguaAlerta = 150.0;
    private double aguaCritico = 180.0;

    // Limites da umidade do solo (%)
    private double umidadeAtencao = 50.0;
    private double umidadeAlerta = 70.0;
    private double umidadeCritico = 90.0;

    private ConfiguracaoSistema() {
    }

    public static ConfiguracaoSistema getInstancia() {
        if (instancia == null) {
            instancia = new ConfiguracaoSistema();
        }
        return instancia;
    }

    public double getLimiteAtencao(String sensor) {
        switch (sensor) {
            case "PLUVIOMETRO":
                return chuvaAtencao;
            case "SENSOR_NIVEL_AGUA":
                return aguaAtencao;
            case "SENSOR_UMIDADE_SOLO":
                return umidadeAtencao;
            default:
                throw new IllegalArgumentException("Sensor desconhecido.");
        }
    }

    public double getLimiteAlerta(String sensor) {
        switch (sensor) {
            case "PLUVIOMETRO":
                return chuvaAlerta;
            case "SENSOR_NIVEL_AGUA":
                return aguaAlerta;
            case "SENSOR_UMIDADE_SOLO":
                return umidadeAlerta;
            default:
                throw new IllegalArgumentException("Sensor desconhecido.");
        }
    }

    public double getLimiteCritico(String sensor) {
        switch (sensor) {
            case "PLUVIOMETRO":
                return chuvaCritico;
            case "SENSOR_NIVEL_AGUA":
                return aguaCritico;
            case "SENSOR_UMIDADE_SOLO":
                return umidadeCritico;
            default:
                throw new IllegalArgumentException("Sensor desconhecido.");
        }
    }

    public void atualizarLimites(
            String sensor,
            double atencao,
            double alerta,
            double critico) {

        if (!Double.isFinite(atencao)
                || !Double.isFinite(alerta)
                || !Double.isFinite(critico)
                || atencao <= 0
                || alerta <= atencao
                || critico <= alerta) {
            throw new IllegalArgumentException(
                    "Os limites devem ser positivos e crescentes."
            );
        }

        switch (sensor) {
            case "PLUVIOMETRO":
                chuvaAtencao = atencao;
                chuvaAlerta = alerta;
                chuvaCritico = critico;
                break;

            case "SENSOR_NIVEL_AGUA":
                aguaAtencao = atencao;
                aguaAlerta = alerta;
                aguaCritico = critico;
                break;

            case "SENSOR_UMIDADE_SOLO":
                umidadeAtencao = atencao;
                umidadeAlerta = alerta;
                umidadeCritico = critico;
                break;

            default:
                throw new IllegalArgumentException("Sensor desconhecido.");
        }
    }
}
