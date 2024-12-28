package cms.cf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cms.cf.model.domain.ResetToken;

@Repository
public interface ResetTokenRepository extends JpaRepository<ResetToken, String> {

	ResetToken findByEmail(String email);

}
