package cl.duoc.sonidovivo.model;
import jakarta.persistence.*;
@Embeddable
public class OrderLine {
  public String productId;
  public String name;
  public String brand;
  public String model;
  public Integer quantity;
  public Long unitPrice;
  public Long lineTotal;
  public OrderLine() {}
  public OrderLine(Product p,int qty) {
    productId=p.id; name=p.name;brand=p.brand;model=p.model;
    quantity=qty;unitPrice=p.price;lineTotal=Math.multiplyExact(p.price,qty);
  }
}
