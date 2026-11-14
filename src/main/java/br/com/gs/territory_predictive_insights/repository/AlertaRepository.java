
package br.com.gs.territory_predictive_insights.repository;

import br.com.gs.territory_predictive_insights.entidade.Alerta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlertaRepository extends JpaRepository<Alerta, Long> {

    List<Alerta> findByRegiaoOrderByDataHoraDesc(String regiao);

    List<Alerta> findAllByOrderByDataHoraDesc();
}
