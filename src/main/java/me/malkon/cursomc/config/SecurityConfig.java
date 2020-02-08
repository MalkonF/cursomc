package me.malkon.cursomc.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import me.malkon.cursomc.security.JWTAuthenticationFilter;
import me.malkon.cursomc.security.JWTAuthorizationFilter;
import me.malkon.cursomc.security.JWTUtil;

/*Classe vai definir o que é bloqueado ou liberado por padrão.
 
 * JWT mantem as info de autorizacao do usuario por meio de um token q vai ser trafegado
 * no cabeçalho das requsiicoes. Vai ser armazenado o user e o tempo de expiracao do 
 * token de forma criptogafada e assinada pelo backend.
 * Backend gera uma chave por tempo determinado e so o user q tem aquela chave durante aquele
 * tempo q pode acessar certos endpoints.
 * */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) /*
													 * permite colocar anotações de pré-autorização nos endpoints .P
													 * poder configurar alguns endpoints q so poderao ser acessado por
													 * admin
													 */
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	/* O spring busca a implementação dessa interface */
	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private Environment env;

	@Autowired
	private JWTUtil jwtUtil;
	// quais endpoints vão estar liberados publicamente p consulta
	private static final String[] PUBLIC_MATCHERS = { "/h2-console/**" };
	// necessario liberar endpoint estado pq qnd o cliente for se cadastrar a app
	// vai buscar no bd os estados e cidades
	private static final String[] PUBLIC_MATCHERS_GET = { "/produtos/**", "/categorias/**", "/estados/**" };
	private static final String[] PUBLIC_MATCHERS_POST = { "/clientes/**", "/auth/forgot/**" };
	// clientes esta aqui pq n e liberado publico fzr get de clientes, mas é
	// liberado fazer post de um cliente(cadastrar cliente)

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// se nos profiles ativos tiver o test vai liberar o acesso ao bd h2
		if (Arrays.asList(env.getActiveProfiles()).contains("test")) {
			http.headers().frameOptions().disable();// libera acesso ao h2
		}
		// vai ativar as configurações de http. e desabilitar ataques csrf pq n armazena
		// sessao c usuario
		http.cors().and().csrf().disable();
		/*
		 * /* todos os caminhos que estiver em PUBLIC_MATCHERS_GET vão ser liberados p
		 * get todos os caminhos que estiver em PUBLIC_MATCHERS_GET vão ser liberados e
		 * para (o user n precisa estar logado) e para os demais caminho q n estao
		 * listados os demais vai ter que ter autenticação -
		 * anyRequest().authenticated(). Os que aqui, vai ter que ter autenticação -
		 * anyRequest().authenticated(). Os que não não estão listados aqui vão ser
		 * bloqueados estão listados aqui vão ser bloqueados. POST so vai ser permitido
		 * sem autenticar clientes e forgot(pq n precisam estar logado p criar um novo
		 * user ou recuperar a senha
		 */
		http.authorizeRequests().antMatchers(HttpMethod.POST, PUBLIC_MATCHERS_POST).permitAll()
				.antMatchers(HttpMethod.GET, PUBLIC_MATCHERS_GET).permitAll().antMatchers(PUBLIC_MATCHERS).permitAll()
				.anyRequest().authenticated();
		// registra o filtro de autenticação
		http.addFilter(new JWTAuthenticationFilter(authenticationManager(), jwtUtil));
		http.addFilter(new JWTAuthorizationFilter(authenticationManager(), jwtUtil, userDetailsService));
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		// acima diz que nunca vai ser criada uma sessão http com usuário
		/*
		 * /* Método sobrecarregadopq tem um igual acima - sobrescrevemos esse método p
		 * Método sobrecarregado - sobrescrevemos esse método p dizer duas coisas quem é
		 * dizer duas coisas quem é o userDetailsService que estamos usando e qual é o o
		 * userDetailsService que estamos usando e qual é o algoritmo de encriptação
		 * algoritmo de encriptação da senha que é o bCrypt. Ai poderiamos passar o user
		 * da senha que é o bCrypt diretamente p o spring, mas implementamos uma busca
		 * no UserDetailsService e colocamos p usar ele aqui
		 */
	}

	/*
	 * Método sobrecarregado - sobrescrevemos esse método p dizer duas coisas quem é
	 * o userDetailsService que estamos usando e qual é o algoritmo de encriptação
	 * da senha que é o bCrypt
	 */
	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
	}

	/* Permitir acesso aos endpoints por multiplas fontes com conf basicas. */
	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
		return source;
	}

	/*
	 * /* Está como bean pq tem q estar disponível para ser injetado(Autowired) em
	 * qlqr Bean vai estar disponível para ser injetado em qlqr classe do sistema. A
	 * classe do sistema. A função desse método é encodar a senha do usuário função
	 * desse método é encodar a senha do usuário
	 */
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
}