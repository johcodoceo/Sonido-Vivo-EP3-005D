package cl.duoc.sonidovivo.security;
import cl.duoc.sonidovivo.repo.UserRepository;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.List;
@Component
public class JwtFilter extends OncePerRequestFilter {
  private final JwtService jwt;private final UserRepository users;
  public JwtFilter(JwtService jwt,UserRepository users){this.jwt=jwt;this.users=users;}
  @Override protected void doFilterInternal(HttpServletRequest request,HttpServletResponse response,FilterChain chain) throws ServletException,IOException {
    String bearer=request.getHeader("Authorization");
    if (bearer!=null && bearer.startsWith("Bearer ")) {
      try {
        String subject=jwt.verify(bearer.substring(7));
        users.findById(subject).filter(u->u.active).ifPresent(u->{
          UsernamePasswordAuthenticationToken auth=new UsernamePasswordAuthenticationToken(u.id,null,List.of(new SimpleGrantedAuthority("ROLE_"+u.role.toUpperCase(java.util.Locale.ROOT))));
          SecurityContextHolder.getContext().setAuthentication(auth);
        });
      } catch (io.jsonwebtoken.JwtException | IllegalArgumentException ex) {
        SecurityContextHolder.clearContext();
      }
    }
    chain.doFilter(request,response);
  }
}
