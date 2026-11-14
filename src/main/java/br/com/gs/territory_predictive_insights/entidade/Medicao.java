
package br.com.gs.territory_predictive_insights.entidade;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "MEDICOES")
@SequenceGenerator(
        name = "medicao_seq",
        sequenceName = "SEQ_MEDICOES",
        allocationSize = 1
)
public class Medicao {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "medicao_seq"
    )
    private Long id;

    @Column(nullable = false)
    private String tipoSensor;

    @Column(nullable = false)
    private String regiao;

    @Column(nullable = false)
    private Double valor;

    @Column(nullable = false)
    private LocalDateTime dataHora;

    public Medicao() {
    }

    public Medicao(String tipoSensor, String regiao, Double valor) {
        this.tipoSensor = tipoSensor;
        this.regiao = regiao;
        this.valor = valor;
        this.dataHora = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getTipoSensor() {
        return tipoSensor;
    }

    public void setTipoSensor(String tipoSensor) {
        this.tipoSensor = tipoSensor;
    }

    public String getRegiao() {
        return regiao;
    }

    public void setRegiao(String regiao) {
        this.regiao = regiao;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }
}
