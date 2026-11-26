package cl.duoc.sonidovivo.security;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.http.*;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.*;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.*;
import jakarta.servlet.DispatcherType;
import java.util.*;
@Configuration @EnableMethodSecurity
public class SecurityConfig {
  @Bean PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }
  @Bean SecurityFilterChain securityFilterChain(HttpSecurity http,JwtFilter filter) throws Exception {
    return http.csrf(c->c.disable())
      .cors(cors->cors.configurationSource(corsConfigurationSource()))
      .sessionManagement(s->s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
      .authorizeHttpRequests(a->a
        .dispatcherTypeMatchers(DispatcherType.ERROR).permitAll()
        .requestMatchers("/error").permitAll()
        .requestMatchers("/api/auth/login","/api/auth/register","/swagger-ui/**","/swagger-ui.html","/v3/api-docs/**").permitAll()
        .anyRequest().authenticated())
      .exceptionHandling(e->e
        .authenticationEntryPoint((req,res,ex)->jsonError(res,401,"Autenticación requerida o sesión vencida."))
        .accessDeniedHandler((req,res,ex)->jsonError(res,403,"Tu rol no tiene permiso para esta operación.")))
      .addFilterBefore(filter,UsernamePasswordAuthenticationFilter.class).build();
  }
  private static void jsonError(jakarta.servlet.http.HttpServletResponse res,int status,String msg)throws java.io.IOException {
    res.setStatus(status);res.setContentType("application/json;charset=UTF-8");
    res.getWriter().write("{\"error\":\""+msg+"\"}");
  }
  @Value("${app.cors-origin}") String origin;
  @Bean CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration config=new CorsConfiguration();
    config.setAllowedOrigins(List.of(origin));
    config.setAllowedMethods(List.of("GET","POST","PUT","PATCH","DELETE","OPTIONS"));
    config.setAllowedHeaders(List.of("Authorization","Content-Type"));
    UrlBasedCorsConfigurationSource source=new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**",config);return source;
  }
}
