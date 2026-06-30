package pe.edu.upc.soft.work.platform.worker.forum.infrastructure.persistence.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.aggregates.Report;

import java.util.List;

/**
 * Repository interface for managing Report entities
 */
@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {

  List<Report> findByUserAccountId(Long userAccountId);

  List<Report> findByAreaCompanyIdAreaCompanyIdIn(List<Long> areaIds);
}
