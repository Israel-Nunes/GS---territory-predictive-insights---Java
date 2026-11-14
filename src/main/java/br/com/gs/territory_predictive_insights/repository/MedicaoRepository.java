
package br.com.gs.territory_predictive_insights.repository;

import br.com.gs.territory_predictive_insights.entidade.Medicao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MedicaoRepository extends JpaRepository<Medicao, Long> {

    List<Medicao> findByTipoSensorOrderByDataHoraDesc(String tipoSensor);

    List<Medicao> findByRegiaoOrderByDataHoraDesc(String regiao);

    List<Medicao> findAllByOrderByDataHoraDesc();
}
