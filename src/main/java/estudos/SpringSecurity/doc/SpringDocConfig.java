package estudos.SpringSecurity.doc;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class SpringDocConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API do Protótipo de Rede Social")
                        .version("v1")
                        .description("Requisições HTTP's de interação com Users, Posts e Login")
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://springdoc.org")
                        )
                )
                .externalDocs(new ExternalDocumentation()
                        .description("Github")
                        .url("https://github.com/Trevizan1203/ProjetoRedeSocial"))
                .tags(
                        Arrays.asList(
                                new Tag().name("Posts").description("Requisicoes de Posts"),
                                new Tag().name("User").description("Requisicoes de Users"),
                                new Tag().name("Login").description("Requisicoes de Login")
                        )
                );
    }
}
