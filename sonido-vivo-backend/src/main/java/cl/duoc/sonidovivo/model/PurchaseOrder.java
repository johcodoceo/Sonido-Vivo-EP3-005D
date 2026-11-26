package cl.duoc.sonidovivo.model;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
@Entity
@Table(name="purchase_orders")
public class PurchaseOrder {
  @Id public String id;
  @Column(nullable=false) public String userId;
  @Column(nullable=false) public Instant createdAt=Instant.now();
  @Column(nullable=false) public String status="preparacion";
  @Column(nullable=false) public String fulfillment;
  @Column(nullable=false) public Long totalProducts;
  public String contactName;
  public String contactEmail;
  public String destinationCity;
  public String destinationAddress;
  @Column(length=250) public String notes="";
  @ElementCollection(fetch=FetchType.EAGER) @CollectionTable(name="order_lines",joinColumns=@JoinColumn(name="order_id"))
  @OrderColumn(name="line_position") public List<OrderLine> lines=new ArrayList<>();
  @ElementCollection(fetch=FetchType.EAGER) @CollectionTable(name="order_history",joinColumns=@JoinColumn(name="order_id"))
  @OrderColumn(name="event_position") public List<StatusEvent> statusHistory=new ArrayList<>();
}
