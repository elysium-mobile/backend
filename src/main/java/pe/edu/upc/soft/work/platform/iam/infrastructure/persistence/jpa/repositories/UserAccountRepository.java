package pe.edu.upc.soft.work.platform.iam.infrastructure.persistence.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.soft.work.platform.iam.domain.model.aggregates.UserAccount;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount,Long> {

}
