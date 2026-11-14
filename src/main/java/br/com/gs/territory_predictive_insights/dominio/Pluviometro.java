
package br.com.gs.territory_predictive_insights.dominio;

import java.util.Random;

public class Pluviometro extends ComponenteTerritorial implements Sensor {

    private final Random random = new Random();

    public Pluviometro(Long id, String nome, String regiao) {
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
        return "PLUVIOMETRO";
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
