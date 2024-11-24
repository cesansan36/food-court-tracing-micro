package plazadecomidas.tracing;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@SecurityScheme(
		name = "bearerAuth",
		type = SecuritySchemeType.HTTP,
		in = SecuritySchemeIn.HEADER,
		bearerFormat = "JWT",
		scheme = "bearer"
)
public class TracingApplication {

	public static void main(String[] args) {
		SpringApplication.run(TracingApplication.class, args);
	}

}
