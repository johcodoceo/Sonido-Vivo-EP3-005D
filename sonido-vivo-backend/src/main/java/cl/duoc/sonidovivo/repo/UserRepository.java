package cl.duoc.sonidovivo.repo;
import cl.duoc.sonidovivo.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface UserRepository extends JpaRepository<AppUser,String> {
  Optional<AppUser> findByEmailIgnoreCase(String email);
  boolean existsByEmailIgnoreCase(String email);
  long countByRoleAndActiveTrue(String role);
}
