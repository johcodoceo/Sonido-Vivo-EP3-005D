package cl.duoc.sonidovivo.api;
import cl.duoc.sonidovivo.model.AppUser;
import cl.duoc.sonidovivo.repo.UserRepository;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import java.util.*;
@RestController @RequestMapping("/api/users") @PreAuthorize("hasRole('ADMINISTRADOR')")
public class UserController {
 private final UserRepository repo;private final PasswordEncoder encoder;
 public UserController(UserRepository repo,PasswordEncoder encoder){this.repo=repo;this.encoder=encoder;}
 @GetMapping public List<Dto.UserView> all(){return repo.findAll().stream().map(Dto.UserView::of).toList();}
 @PostMapping public ResponseEntity<Dto.UserView> add(@Valid @RequestBody Dto.UserForm f) {
   checkRole(f.role());checkNewPassword(f.password());
   if(repo.existsByEmailIgnoreCase(f.email().trim()))throw new ApiException(HttpStatus.CONFLICT,"El correo ya está registrado.");
   var user=new AppUser("SVU-"+UUID.randomUUID(),f.name().trim(),f.email().trim().toLowerCase(),f.phone()==null?"":f.phone().trim(),f.role(),encoder.encode(f.password()));
   return ResponseEntity.status(201).body(Dto.UserView.of(repo.save(user)));
 }
 @PutMapping("/{id}") @Transactional
 public Dto.UserView edit(@PathVariable String id,@Valid @RequestBody Dto.UserForm f,Authentication auth) {
   AppUser user=find(id);checkRole(f.role());
   if(user.id.equals(auth.getName())&&!user.role.equals(f.role()))throw new ApiException(HttpStatus.CONFLICT,"No puedes modificar tu propio rol.");
   if(user.active&&user.role.equals("administrador")&&!f.role().equals("administrador")&&repo.countByRoleAndActiveTrue("administrador")<=1)
     throw new ApiException(HttpStatus.CONFLICT,"Debe quedar un administrador activo.");
   if(repo.findByEmailIgnoreCase(f.email().trim()).filter(other->!other.id.equals(id)).isPresent())
     throw new ApiException(HttpStatus.CONFLICT,"El correo ya está registrado.");
   user.name=f.name().trim();user.email=f.email().trim().toLowerCase();user.phone=f.phone()==null?"":f.phone().trim();user.role=f.role();
   if(f.password()!=null&&!f.password().isBlank()){checkNewPassword(f.password());user.passwordHash=encoder.encode(f.password());}
   return Dto.UserView.of(repo.save(user));
 }
 @PatchMapping("/{id}/toggle") @Transactional
 public Dto.UserView toggle(@PathVariable String id,Authentication auth){
   AppUser user=find(id);
   if(user.active&&user.id.equals(auth.getName()))throw new ApiException(HttpStatus.CONFLICT,"No puedes desactivar tu propia cuenta.");
   if(user.active&&user.role.equals("administrador")&&repo.countByRoleAndActiveTrue("administrador")<=1)
     throw new ApiException(HttpStatus.CONFLICT,"Debe quedar un administrador activo.");
   user.active=!user.active;return Dto.UserView.of(repo.save(user));
 }
 private AppUser find(String id){return repo.findById(id).orElseThrow(()->new ApiException(HttpStatus.NOT_FOUND,"Usuario no encontrado."));}
 private void checkRole(String role){if(!Set.of("cliente","vendedor","administrador").contains(role))throw new ApiException(HttpStatus.BAD_REQUEST,"Rol no válido.");}
 private void checkNewPassword(String password){if(password==null||password.length()<8)throw new ApiException(HttpStatus.BAD_REQUEST,"La contraseña debe tener al menos 8 caracteres.");}
}
