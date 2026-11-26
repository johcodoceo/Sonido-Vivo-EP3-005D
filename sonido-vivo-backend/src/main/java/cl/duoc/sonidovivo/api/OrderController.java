package cl.duoc.sonidovivo.api;
import cl.duoc.sonidovivo.model.*;
import cl.duoc.sonidovivo.repo.*;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import java.util.*;
@RestController @RequestMapping("/api/orders")
public class OrderController {
 private final OrderRepository orders;private final ProductRepository products;private final UserRepository users;
 public OrderController(OrderRepository orders,ProductRepository products,UserRepository users){this.orders=orders;this.products=products;this.users=users;}
 @GetMapping public List<Dto.OrderView> all(Authentication auth){
   AppUser user=users.findById(auth.getName()).orElseThrow();
   return (user.role.equals("cliente")?orders.findByUserIdOrderByCreatedAtDesc(user.id):orders.findAllByOrderByCreatedAtDesc()).stream().map(Dto.OrderView::of).toList();
 }
 @PostMapping @PreAuthorize("hasAnyRole('CLIENTE','ADMINISTRADOR')") @Transactional
 public ResponseEntity<Dto.OrderView> create(@Valid @RequestBody Dto.OrderForm f,Authentication auth) {
   if(!Set.of("retiro","despacho").contains(f.fulfillment()))throw new ApiException(HttpStatus.BAD_REQUEST,"Modalidad de entrega inválida.");
   if(f.fulfillment().equals("despacho")&&(f.city()==null||f.city().trim().length()<2||f.address()==null||f.address().trim().length()<8))
     throw new ApiException(HttpStatus.BAD_REQUEST,"Indica una ciudad y dirección válidas para el despacho.");
   if(f.notes()!=null&&f.notes().length()>250) throw new ApiException(HttpStatus.BAD_REQUEST,"Las observaciones no pueden superar 250 caracteres.");
   if(f.items().size()>100) throw new ApiException(HttpStatus.BAD_REQUEST,"Demasiados productos en el pedido.");
   PurchaseOrder o=new PurchaseOrder();o.id="SVP-"+UUID.randomUUID();o.userId=auth.getName();
   o.fulfillment=f.fulfillment();o.contactName=f.name().trim();o.contactEmail=f.email().trim();
   o.destinationCity=f.fulfillment().equals("despacho")?f.city().trim():"";
   o.destinationAddress=f.fulfillment().equals("despacho")?f.address().trim():"";
   o.notes=f.notes()==null?"":f.notes().trim();o.statusHistory.add(new StatusEvent("preparacion"));
   long total=0;
   // Se ordena por ID para que compras simultáneas bloqueen filas en orden determinista.
   var quantities=new TreeMap<String,Integer>();
   for(var item:f.items()) {
     try {quantities.merge(item.productId(),item.quantity(),Math::addExact);}catch(ArithmeticException e){throw new ApiException(HttpStatus.BAD_REQUEST,"Cantidad inválida.");}
   }
   for(var entry:quantities.entrySet()) {
     Product p=products.findForUpdate(entry.getKey()).orElseThrow(()->new ApiException(HttpStatus.NOT_FOUND,"Producto no encontrado: "+entry.getKey()));
     if(p.stock<entry.getValue())throw new ApiException(HttpStatus.CONFLICT,"Stock insuficiente para "+p.name);
     OrderLine line=new OrderLine(p,entry.getValue());
     try {total=Math.addExact(total,line.lineTotal);}catch(ArithmeticException e){throw new ApiException(HttpStatus.BAD_REQUEST,"Total fuera de rango.");}
     p.stock-=entry.getValue();products.save(p);o.lines.add(line);
   }
   o.totalProducts=total;
   return ResponseEntity.status(201).body(Dto.OrderView.of(orders.save(o)));
 }
 @PatchMapping("/{id}/status") @PreAuthorize("hasAnyRole('VENDEDOR','ADMINISTRADOR')") @Transactional
 public Dto.OrderView status(@PathVariable String id,@Valid @RequestBody Dto.StatusForm f){
   PurchaseOrder o=orders.findById(id).orElseThrow(()->new ApiException(HttpStatus.NOT_FOUND,"Pedido no encontrado."));
   String expected=o.status.equals("preparacion")?(o.fulfillment.equals("despacho")?"despachado":"listo-retiro"):
     (Set.of("despachado","listo-retiro").contains(o.status)?"entregado":"");
   if(!expected.equals(f.status()))throw new ApiException(HttpStatus.CONFLICT,"Transición de estado no permitida.");
   o.status=f.status();o.statusHistory.add(new StatusEvent(f.status()));return Dto.OrderView.of(orders.save(o));
 }
}
