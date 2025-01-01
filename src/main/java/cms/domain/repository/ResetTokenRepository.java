package cms.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cms.domain.model.ResetToken;

@Repository
public interface ResetTokenRepository extends JpaRepository<ResetToken, String> {

	ResetToken findByEmail(String email);

}
