package cl.duoc.sonidovivo.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
@Entity
@Table(name="products")
public class Product {
  @Id public String id;
  @NotBlank @Size(min=3,max=110) @Column(nullable=false,length=110) public String name;
  @NotBlank @Size(min=2,max=80) @Column(nullable=false,length=80) public String brand;
  @NotBlank @Size(max=80) @Column(nullable=false,length=80) public String model;
  @NotBlank @Column(nullable=false,length=80) public String categorySlug;
  @NotBlank @Size(min=10,max=450) @Column(nullable=false,length=450) public String description;
  @NotNull @Min(1) @Column(nullable=false) public Long price;
  @NotNull @Min(0) @Column(nullable=false) public Integer stock;
  public Product() {}
}
// feat 