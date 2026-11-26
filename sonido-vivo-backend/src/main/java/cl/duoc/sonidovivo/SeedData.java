package cl.duoc.sonidovivo;
import cl.duoc.sonidovivo.model.*;
import cl.duoc.sonidovivo.repo.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.*;
@Configuration
public class SeedData {
 @Bean CommandLineRunner seed(ProductRepository products,UserRepository users,PasswordEncoder encoder,ObjectMapper mapper,@Value("${app.demo-password}") String demoPassword){
   return args->{
     if(products.count()==0){
       try(var stream=new ClassPathResource("seed-products.json").getInputStream()) {
         List<Product> catalog=mapper.readValue(stream,new TypeReference<List<Product>>(){});
         products.saveAll(catalog);
       }
     }
     if(demoPassword.length()<8)throw new IllegalStateException("DEMO_PASSWORD debe tener al menos 8 caracteres");
     String hash=encoder.encode(demoPassword);
     for(var fixture:List.of(new String[]{"demo-cliente","Cliente demo","cliente@sonidovivo.example","cliente"},
         new String[]{"demo-vendedor","Vendedor demo","vendedor@sonidovivo.example","vendedor"},
         new String[]{"demo-administrador","Administrador demo","admin@sonidovivo.example","administrador"})) {
       if(!users.existsById(fixture[0])&&!users.existsByEmailIgnoreCase(fixture[2]))
         users.save(new AppUser(fixture[0],fixture[1],fixture[2],"",fixture[3],hash));
     }
   };
 }
}
