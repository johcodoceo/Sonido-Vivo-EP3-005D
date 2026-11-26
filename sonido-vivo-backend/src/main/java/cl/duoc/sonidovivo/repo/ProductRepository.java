package cl.duoc.sonidovivo.repo;
import cl.duoc.sonidovivo.model.Product;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import jakarta.persistence.LockModeType;
import java.util.*;
public interface ProductRepository extends JpaRepository<Product,String> {
  boolean existsByNameIgnoreCase(String name);
  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query("select p from Product p where p.id = :id")
  Optional<Product> findForUpdate(@Param("id") String id);
}
