package pe.edu.upc.soft.work.platform.shared.infrastructure.documentation.openapi.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for OpenAPI documentation.
 */
@Configuration
public class OpenApiConfiguration {

    // Inject the server URL from application properties
    @Value("${swagger.server.url}")
    private String swaggerServerUrl;

    /**
     * Creates the OpenAPI bean for Prime Fix Platform.
     *
     * @return the OpenAPI configuration
     */
    @Bean
    public OpenAPI primeFixPlatformOpenApi() {
        // Define the security scheme name
        final String securitySchemeName = "bearerAuth";

        // Build and return the OpenAPI configuration
        return new OpenAPI()
                // Configure the server
                .addServersItem(new Server()
                        .url(swaggerServerUrl)
                        .description("Prime Fix Platform server"))
                // Set API information
                .info(new Info()
                        .title("Prime Fix Platform API")
                        .description("Prime Fix Platform application REST API documentation.")
                        .version("v1.0.0")
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://springdoc.org")))
                // Set external documentation
                .externalDocs(new ExternalDocumentation()
                        .description("Prime Fix Platform Documentation")
                        .url("https://github.com/prime-fix/backend"))
                // Configure security requirements
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                // Define security schemes
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                        .name(securitySchemeName)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")));
    }
}
