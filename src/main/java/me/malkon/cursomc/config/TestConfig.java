package me.malkon.cursomc.config;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import me.malkon.cursomc.services.DBService;
import me.malkon.cursomc.services.EmailServices;
import me.malkon.cursomc.services.MockEmailServices;

@Configuration
@Profile("test")
public class TestConfig {

	@Autowired
	private DBService dbService;

	@Bean
	public boolean instantiateDatabase() throws ParseException {

		dbService.instantiateTestDatabase();
		return true;
	}

	@Bean//metodo vai estar disponivel como componente no sistema. Se em outra classe vc faz uma injeção de dependencia
	//o spring vai procurar por um componente que pode ser um Bean e retorna uma nova instancia no MockMailService
	public EmailServices emailService() {

		return new MockEmailServices();
	}

}
