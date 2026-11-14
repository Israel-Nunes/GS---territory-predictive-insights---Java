package br.com.gs.territory_predictive_insights.excecao;

public class FonteIndisponivelException extends Exception {

    public FonteIndisponivelException(String mensagem) {
        super(mensagem);
    }
}