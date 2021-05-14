package config.swagger;

import br.com.alura.forum.entites.Usuario;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Arrays;

@Configuration
public class SwaggerConfigurations {

    // Config para documentação da api com swagger, usando o springfoxswagger
    @Bean
    public Docket forumApi() {
        // Criação do docket, indicando um documento do swagger 2
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                // indica a partir de qual pacote o swagger começa a ler as classes pra documentar
                .apis(RequestHandlerSelectors.basePackage("br.com.alura.forum"))
                // indica quais endpoints devem ser documentados, no caso está permitindo todos.
                .paths(PathSelectors.ant("/*"))
                .build()
                // ignora as classe que tem Usuario, para evitar mostrar na doc infos do usuário.
                .ignoredParameterTypes(Usuario.class)
                // define um parametro global que irá aparecer para todas as controller e os campos desse parametro
                // definindo o header do access token e permitindo passar o access para validar chamadas que exigem autenticação via swagger.
                .globalOperationParameters(Arrays.asList(
                        new ParameterBuilder()
                        .name("Authorization")
                        .description("Header para token JWT")
                        .modelRef(new ModelRef("string"))
                        .parameterType("header")
                        .required(false)
                        .build()));
    }
}
