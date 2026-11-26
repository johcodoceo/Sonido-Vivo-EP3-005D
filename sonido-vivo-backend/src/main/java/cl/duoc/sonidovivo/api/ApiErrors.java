package cl.duoc.sonidovivo.api;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.dao.DataIntegrityViolationException;
import jakarta.validation.ConstraintViolationException;
import java.util.*;
@RestControllerAdvice
public class ApiErrors {
  @ExceptionHandler(ApiException.class)
  public ResponseEntity<Map<String,Object>> domain(ApiException e) {
    return ResponseEntity.status(e.status).body(Map.of("error",e.getMessage(),"errors",Map.of()));
  }
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String,Object>> validation(MethodArgumentNotValidException e) {
    Map<String,String> errors=new HashMap<>();
    e.getBindingResult().getFieldErrors().forEach(field->errors.putIfAbsent(field.getField(),field.getDefaultMessage()));
    return ResponseEntity.badRequest().body(Map.of("error","Revisa los campos del formulario.","errors",errors));
  }
  @ExceptionHandler({DataIntegrityViolationException.class,ConstraintViolationException.class})
  public ResponseEntity<Map<String,Object>> constraint(Exception e) {
    return ResponseEntity.status(409).body(Map.of("error","La operación incumple una restricción de los datos.","errors",Map.of()));
  }
  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<Map<String,Object>> badValue(IllegalArgumentException e) {
    return ResponseEntity.badRequest().body(Map.of("error",e.getMessage(),"errors",Map.of()));
  }
}
