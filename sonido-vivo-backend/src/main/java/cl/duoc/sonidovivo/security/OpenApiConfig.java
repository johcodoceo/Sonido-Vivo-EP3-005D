package cl.duoc.sonidovivo.security;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.*;
import org.springframework.context.annotation.*;
@Configuration
public class OpenApiConfig {
  @Bean OpenAPI api() {return new OpenAPI().info(new Info().title("Sonido Vivo · EP3 API REST").version("1.0.0")
    .description("Catálogo Forma B, JWT con roles, CRUD y pedidos simulados"))
    .components(new Components().addSecuritySchemes("bearerAuth",new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")))
    .addSecurityItem(new SecurityRequirement().addList("bearerAuth"));}
}
