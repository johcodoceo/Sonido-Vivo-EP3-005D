package cl.duoc.sonidovivo;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties={
 "spring.datasource.url=jdbc:h2:mem:sonidovivotest;MODE=PostgreSQL;DB_CLOSE_DELAY=-1",
 "spring.datasource.driver-class-name=org.h2.Driver",
 "spring.datasource.username=sa",
 "spring.datasource.password=",
 "spring.jpa.hibernate.ddl-auto=create-drop",
 "app.jwt-secret=clave-solo-para-prueba-123456789012345678901234567890",
 "app.demo-password=contrasenaTest99",
 "app.cors-origin=http://localhost:5173"
})
class ApiIntegrationTest {
 @Autowired MockMvc mvc;
 @Autowired ObjectMapper mapper;
 private String token(String email) throws Exception {
   var res=mvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON)
      .content(mapper.writeValueAsString(java.util.Map.of("email",email,"password","contrasenaTest99"))))
      .andExpect(status().isOk()).andReturn();
   return mapper.readTree(res.getResponse().getContentAsString()).get("token").asText();
 }
 @Test void productosRequierenAutenticacion() throws Exception {
   mvc.perform(get("/api/products")).andExpect(status().isUnauthorized());
 }
 @Test void catalogoImportadoDesdeFormaB() throws Exception {
   var res=mvc.perform(get("/api/products").header("Authorization","Bearer "+token("cliente@sonidovivo.example")))
    .andExpect(status().isOk()).andReturn();
   JsonNode arr=mapper.readTree(res.getResponse().getContentAsString());
   assertTrue(arr.isArray());assertTrue(arr.size()>=51);
 }
 @Test void vendedorNoPuedeCrearProductos() throws Exception {
   mvc.perform(post("/api/products").header("Authorization","Bearer "+token("vendedor@sonidovivo.example"))
      .contentType(MediaType.APPLICATION_JSON).content("""
        {"name":"Producto Test Permisos","brand":"Yamaha","model":"TEST-ROLE","categorySlug":"guitarras-electricas","description":"Prueba de permisos del vendedor para EP3.","price":100000,"stock":5}
        """))
     .andExpect(status().isForbidden());
 }
 @Test void clienteSoloVeSusPedidos() throws Exception {
   mvc.perform(get("/api/orders").header("Authorization","Bearer "+token("cliente@sonidovivo.example")))
     .andExpect(status().isOk());
   mvc.perform(get("/api/users").header("Authorization","Bearer "+token("cliente@sonidovivo.example")))
     .andExpect(status().isForbidden());
 }
 @Test void noAceptaCompraConStockInsuficiente() throws Exception {
   String body="""
     {"items":[{"productId":"GA001","quantity":999999}],"name":"Cliente Prueba","email":"prueba@example.com","fulfillment":"retiro","city":"","address":"","notes":""}
     """;
   mvc.perform(post("/api/orders").header("Authorization","Bearer "+token("cliente@sonidovivo.example"))
     .contentType(MediaType.APPLICATION_JSON).content(body))
     .andExpect(status().isConflict());
 }
 @Test void registroSiempreTieneRolCliente() throws Exception {
   String unique="ep3-"+java.util.UUID.randomUUID()+"@example.com";
   String body=mapper.writeValueAsString(java.util.Map.of("name","Usuario de Prueba","email",unique,
      "phone","","role","administrador","password","contrasena123"));
   var res=mvc.perform(post("/api/auth/register").contentType(MediaType.APPLICATION_JSON).content(body))
      .andExpect(status().isCreated()).andReturn();
   assertEquals("cliente",mapper.readTree(res.getResponse().getContentAsString()).get("role").asText());
 }

 @Test void administradorPuedeCrearProducto() throws Exception {
   String uniqueName="Producto Test Admin-" + java.util.UUID.randomUUID();
   String payload=mapper.writeValueAsString(java.util.Map.of(
      "name",uniqueName,"brand","Yamaha","model","TEST-ADMIN",
      "categorySlug","guitarras-electricas",
      "description","Prueba de creacion del producto mediante POST autorizado.",
      "price",150000,"stock",10));
   mvc.perform(post("/api/products")
      .header("Authorization","Bearer "+token("admin@sonidovivo.example"))
      .contentType(MediaType.APPLICATION_JSON).content(payload))
     .andExpect(status().isCreated())
     .andExpect(jsonPath("$.id").exists())
     .andExpect(jsonPath("$.name").value(uniqueName));
 }
}
