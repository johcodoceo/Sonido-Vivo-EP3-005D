package cl.duoc.sonidovivo.model;
import jakarta.persistence.*;
import java.time.Instant;
@Embeddable
public class StatusEvent {
  public String status;
  public Instant at;
  public StatusEvent() {}
  public StatusEvent(String status) { this.status=status; this.at=Instant.now(); }
}
