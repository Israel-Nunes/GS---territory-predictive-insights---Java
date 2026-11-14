
package br.com.gs.territory_predictive_insights.entidade;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ALERTAS")
@SequenceGenerator(
        name = "alerta_seq",
        sequenceName = "SEQ_ALERTAS",
        allocationSize = 1
)
public class Alerta {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "alerta_seq"
    )
    private Long id;

    @Column(nullable = false)
    private String regiao;

    @Column(nullable = false)
    private String nivel;

    @Column(nullable = false, length = 500)
    private String descricao;

    @Column(nullable = false)
    private LocalDateTime dataHora;

    public Alerta() {
    }

    public Alerta(String regiao, String nivel, String descricao) {
        this.regiao = regiao;
        this.nivel = nivel;
        this.descricao = descricao;
        this.dataHora = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getRegiao() {
        return regiao;
    }

    public void setRegiao(String regiao) {
        this.regiao = regiao;
    }

    public String getNivel() {
        return nivel;
    }

    public void setNivel(String nivel) {
        this.nivel = nivel;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }
}
