package me.malkon.cursomc.config;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import me.malkon.cursomc.services.DBService;
import me.malkon.cursomc.services.EmailServices;
import me.malkon.cursomc.services.MockEmailServices;

/*Configuacoes especificas p o profile de test. Tds os beans so vao ser ativados
 * qnd o profile test tiver ativo no application.properties*/
@Configuration
@Profile("test")
public class TestConfig {

	@Autowired
	private DBService dbService;

	@Bean // metodo responsavel por instanciar o banco de dados no profile test
	public boolean instantiateDatabase() throws ParseException {

		dbService.instantiateTestDatabase();
		return true;
	}

	// metodo vai estar disponivel como componente no sistema. Se em outra classe vc
	// faz uma injeção de dependencia
	// o spring vai procurar por um componente que pode ser um Bean e retorna uma
	// nova instancia no MockMailService
	@Bean
	public EmailServices emailService() {

		return new MockEmailServices();
	}

}
