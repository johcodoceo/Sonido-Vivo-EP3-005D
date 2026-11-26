package cl.duoc.sonidovivo.repo;
import cl.duoc.sonidovivo.model.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface OrderRepository extends JpaRepository<PurchaseOrder,String> {
  List<PurchaseOrder> findByUserIdOrderByCreatedAtDesc(String userId);
  List<PurchaseOrder> findAllByOrderByCreatedAtDesc();
}
