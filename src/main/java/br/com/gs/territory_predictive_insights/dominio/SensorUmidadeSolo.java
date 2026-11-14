
package br.com.gs.territory_predictive_insights.dominio;

import java.util.Random;

public class SensorUmidadeSolo extends ComponenteTerritorial implements Sensor {

    private final Random random = new Random();

    public SensorUmidadeSolo(Long id, String nome, String regiao) {
        super(id, nome, regiao);
    }

    @Override
    public double lerValor() {
        return random.nextDouble() * 100;
    }

    @Override
    public boolean estaFuncionando() {
        return true;
    }

    @Override
    public String getTipo() {
        return "SENSOR_UMIDADE_SOLO";
    }

    @Override
    public String verificarStatus() {
        if (estaFuncionando()) {
            setStatus("OPERACIONAL");
        } else {
            setStatus("FALHA");
        }

        return getStatus();
    }
}
