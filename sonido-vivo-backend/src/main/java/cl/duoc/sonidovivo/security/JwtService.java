package cl.duoc.sonidovivo.security;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.*;
import java.util.*;
@Service
public class JwtService {
  private final SecretKey key;
  private final long expiryMinutes;
  public JwtService(@Value("${app.jwt-secret}") String secret,@Value("${app.jwt-expiry-minutes}") long expiryMinutes) {
    if(secret.getBytes(StandardCharsets.UTF_8).length<32) throw new IllegalArgumentException("JWT_SECRET debe tener al menos 32 bytes aleatorios");
    this.key=Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    this.expiryMinutes=expiryMinutes;
  }
  public String issue(String userId) {
    Instant now=Instant.now();
    return Jwts.builder().subject(userId).issuedAt(Date.from(now)).expiration(Date.from(now.plus(Duration.ofMinutes(expiryMinutes)))).signWith(key).compact();
  }
  public String verify(String token) {
    return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload().getSubject();
  }
}
