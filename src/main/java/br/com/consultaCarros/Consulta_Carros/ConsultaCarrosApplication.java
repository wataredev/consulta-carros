package br.com.consultaCarros.Consulta_Carros;

import br.com.consultaCarros.Consulta_Carros.principal.Principal;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ConsultaCarrosApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ConsultaCarrosApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Principal principal = new Principal();
		principal.iniciarConsulta();
	}
}
