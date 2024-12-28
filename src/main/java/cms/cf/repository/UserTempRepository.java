package cms.cf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cms.cf.model.domain.UserTemp;

@Repository
public interface UserTempRepository extends JpaRepository<UserTemp, String> {


}
