package cl.duoc.sonidovivo.api;
import cl.duoc.sonidovivo.model.Product;
import cl.duoc.sonidovivo.repo.ProductRepository;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import java.util.stream.*;
@RestController @RequestMapping("/api/products")
public class ProductController {
 private final ProductRepository repo;
 private static final Set<String> CATEGORIES=Set.of("guitarras-acusticas","guitarras-electricas","bajos-electricos","baterias","teclados-y-pianos","amplificadores","microfonos","pedales-de-efectos","accesorios","estudio-y-grabacion");
 public ProductController(ProductRepository repo){this.repo=repo;}
 @GetMapping public List<Product> all(){return repo.findAll().stream().sorted(Comparator.comparing(p->p.name)).toList();}
 @GetMapping("/{id}") public Product one(@PathVariable String id){return find(id);}
 @PostMapping @PreAuthorize("hasRole('ADMINISTRADOR')") @Transactional
 public ResponseEntity<Product> add(@Valid @RequestBody Dto.ProductForm form){
   check(form,null);
   Product p=new Product();p.id="SV-"+UUID.randomUUID();copy(p,form);
   return ResponseEntity.status(201).body(repo.save(p));
 }
 @PutMapping("/{id}") @PreAuthorize("hasRole('ADMINISTRADOR')") @Transactional
 public Product edit(@PathVariable String id,@Valid @RequestBody Dto.ProductForm form){Product p=find(id);check(form,id);copy(p,form);return repo.save(p);}
 @DeleteMapping("/{id}") @PreAuthorize("hasRole('ADMINISTRADOR')") @Transactional
 public ResponseEntity<Void> delete(@PathVariable String id){find(id);repo.deleteById(id);return ResponseEntity.noContent().build();}
 @PatchMapping("/{id}/stock") @PreAuthorize("hasAnyRole('VENDEDOR','ADMINISTRADOR')") @Transactional
 public Product stock(@PathVariable String id,@Valid @RequestBody Dto.StockForm form){Product p=repo.findForUpdate(id).orElseThrow(()->new ApiException(HttpStatus.NOT_FOUND,"Producto no encontrado."));p.stock=form.stock();return repo.save(p);}
 private Product find(String id){return repo.findById(id).orElseThrow(()->new ApiException(HttpStatus.NOT_FOUND,"Producto no encontrado."));}
 private void check(Dto.ProductForm f,String except){
   if(!CATEGORIES.contains(f.categorySlug()))throw new ApiException(HttpStatus.BAD_REQUEST,"Categoría no válida.");
   boolean duplicated=repo.findAll().stream().anyMatch(p->!p.id.equals(except)&&p.name.equalsIgnoreCase(f.name().trim()));
   if(duplicated)throw new ApiException(HttpStatus.CONFLICT,"Ya existe un producto con ese nombre.");
 }
 private void copy(Product p,Dto.ProductForm f){p.name=f.name().trim();p.brand=f.brand().trim();p.model=f.model().trim();p.categorySlug=f.categorySlug();p.description=f.description().trim();p.price=f.price();p.stock=f.stock();}
}
