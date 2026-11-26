package cl.duoc.sonidovivo.model;
import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.Instant;
@Entity
@Table(name="app_users")
public class AppUser {
  @Id public String id;
  @Column(nullable=false,length=80) public String name;
  @Column(nullable=false,unique=true,length=130) public String email;
  @Column(nullable=false,length=20) public String phone = "";
  @Column(nullable=false,length=20) public String role;
  @Column(nullable=false) public Boolean active = true;
  @Column(nullable=false) public Instant createdAt = Instant.now();
  @JsonIgnore @Column(nullable=false) public String passwordHash;
  protected AppUser() {}
  public AppUser(String id,String name,String email,String phone,String role,String hash) {
    this.id=id;this.name=name;this.email=email;this.phone=phone;this.role=role;this.passwordHash=hash;
  }
}
