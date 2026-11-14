
package br.com.gs.territory_predictive_insights.dominio;

import java.util.Random;

public class SensorNivelAgua extends ComponenteTerritorial implements Sensor {

    private final Random random = new Random();

    public SensorNivelAgua(Long id, String nome, String regiao) {
        super(id, nome, regiao);
    }

    @Override
    public double lerValor() {
        return random.nextDouble() * 200;
    }

    @Override
    public boolean estaFuncionando() {
        return true;
    }

    @Override
    public String getTipo() {
        return "SENSOR_NIVEL_AGUA";
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
