package cms.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cms.domain.model.UserTemp;

@Repository
public interface UserTempRepository extends JpaRepository<UserTemp, String> {


}
