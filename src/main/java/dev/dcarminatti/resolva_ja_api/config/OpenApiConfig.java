package dev.dcarminatti.resolva_ja_api.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "ResolvaJá API",
                version = "v1",
                description = "API para gestão do ResolvaJá",
                contact = @Contact(name = "Equipe", email = "suporte@exemplo.com")
        ),
        servers = {
                @Server(url = "http://localhost:8080", description = "Dev"),
                @Server(url = "https://api.suaempresa.com", description = "Prod")
        }
)
public class OpenApiConfig {}
