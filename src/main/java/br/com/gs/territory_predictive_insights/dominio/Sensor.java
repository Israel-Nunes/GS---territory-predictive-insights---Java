package br.com.gs.territory_predictive_insights.dominio;

public interface Sensor {

    double lerValor();

    boolean estaFuncionando();

    String getTipo();
}
