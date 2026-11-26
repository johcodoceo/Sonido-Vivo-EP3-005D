package cl.duoc.sonidovivo.api;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;
import cl.duoc.sonidovivo.model.*;
import java.time.Instant;
import java.util.List;
public final class Dto {
 private Dto() {}
 public record Login(@NotBlank String email, @NotBlank String password) {}
 public record UserForm(@NotBlank @Size(min=3,max=80) String name,
    @NotBlank @Email @Size(max=130) String email,
    @Pattern(regexp="^$|^[+]?[0-9 -]{8,20}$") String phone,
    String role, String password) {}
 public record UserView(String id,String name,String email,String phone,String role,boolean active,Instant createdAt) {
    public static UserView of(AppUser u) {return new UserView(u.id,u.name,u.email,u.phone,u.role,u.active,u.createdAt);}
 }
 public record AuthResponse(String token,UserView user) {}
 public record ProductForm(@NotBlank @Size(min=3,max=110) String name,
   @NotBlank @Size(min=2,max=80) String brand,
   @NotBlank @Size(min=1,max=80) String model,
   @NotBlank String categorySlug,
   @NotBlank @Size(min=10,max=450) String description,
   @NotNull @Min(1) Long price,
   @NotNull @Min(0) Integer stock) {}
 public record StockForm(@NotNull @Min(0) Integer stock) {}
 public record OrderItem(@NotBlank String productId,@NotNull @Min(1) Integer quantity) {}
 public record OrderForm(@NotEmpty List<@Valid OrderItem> items,
    @NotBlank @Size(min=3,max=80) String name,
    @NotBlank @Email String email,
    @NotBlank String fulfillment,
    String city,String address,String notes) {}
 public record OrderView(String id,String userId,Instant createdAt,String status,String fulfillment,
     long totalProducts,Contact contact,Destination destination,String notes,List<OrderLine> lines,List<StatusEvent> statusHistory) {
   public static OrderView of(PurchaseOrder o) {return new OrderView(o.id,o.userId,o.createdAt,o.status,o.fulfillment,o.totalProducts,
     new Contact(o.contactName,o.contactEmail),new Destination(o.destinationCity,o.destinationAddress),o.notes,o.lines,o.statusHistory);}
 }
 public record Contact(String name,String email) {}
 public record Destination(String city,String address) {}
 public record StatusForm(@NotBlank String status) {}
}
