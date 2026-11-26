package cl.duoc.sonidovivo.api;
import cl.duoc.sonidovivo.model.AppUser;
import cl.duoc.sonidovivo.repo.UserRepository;
import cl.duoc.sonidovivo.security.JwtService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;
@RestController @RequestMapping("/api/auth")
public class AuthController {
  private final UserRepository users;private final PasswordEncoder encoder;private final JwtService jwt;
  public AuthController(UserRepository users,PasswordEncoder encoder,JwtService jwt){this.users=users;this.encoder=encoder;this.jwt=jwt;}
  @PostMapping("/login") public Dto.AuthResponse login(@Valid @RequestBody Dto.Login form) {
    AppUser user=users.findByEmailIgnoreCase(form.email().trim()).orElseThrow(()->new ApiException(HttpStatus.UNAUTHORIZED,"Credenciales incorrectas."));
    if(!user.active||!encoder.matches(form.password(),user.passwordHash)) throw new ApiException(HttpStatus.UNAUTHORIZED,"Credenciales incorrectas.");
    return new Dto.AuthResponse(jwt.issue(user.id),Dto.UserView.of(user));
  }
  @PostMapping("/register") public ResponseEntity<Dto.UserView> register(@Valid @RequestBody Dto.UserForm form) {
    if(form.password()==null||form.password().length()<8) throw new ApiException(HttpStatus.BAD_REQUEST,"La contraseña debe tener 8 caracteres o más.");
    if(users.existsByEmailIgnoreCase(form.email().trim())) throw new ApiException(HttpStatus.CONFLICT,"El correo ya está registrado.");
    AppUser user=new AppUser("SVU-"+UUID.randomUUID(),form.name().trim(),form.email().trim().toLowerCase(),form.phone()==null?"":form.phone().trim(),"cliente",encoder.encode(form.password()));
    return ResponseEntity.status(201).body(Dto.UserView.of(users.save(user)));
  }
  @GetMapping("/me") public Dto.UserView me(Authentication auth) {
    AppUser u=users.findById(auth.getName()).orElseThrow(()->new ApiException(HttpStatus.UNAUTHORIZED,"Sesión inválida."));
    return Dto.UserView.of(u);
  }
}
