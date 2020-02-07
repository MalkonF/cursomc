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

/*Classe vai definir o que é bloqueado ou liberado por padrão*/
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) // permite colocar anotações de pré-autorização nos endpoints
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private Environment env;

	@Autowired
	private JWTUtil jwtUtil;

	private static final String[] PUBLIC_MATCHERS = { "/h2-console/**" };
	// quais endpoints vão estar liberados publicamente p consulta
	private static final String[] PUBLIC_MATCHERS_GET = { "/produtos/**", "/categorias/**" };
	private static final String[] PUBLIC_MATCHERS_POST = { "/clientes/**", "/auth/forgot/**" };

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// se nos profiles ativos tiver o test vai liberar o acesso ao bd h2
		if (Arrays.asList(env.getActiveProfiles()).contains("test")) {
			http.headers().frameOptions().disable();
		}
		// vai ativar as configurações de http. e desabilitar ataques csrf
		http.cors().and().csrf().disable();
		/*
		 * todos os caminhos que estiver em PUBLIC_MATCHERS_GET vão ser liberados e para
		 * os demais vai ter que ter autenticação - anyRequest().authenticated(). Os que
		 * não estão listados aqui vão ser bloqueados
		 */
		http.authorizeRequests().antMatchers(HttpMethod.POST, PUBLIC_MATCHERS_POST).permitAll()
				.antMatchers(HttpMethod.GET, PUBLIC_MATCHERS_GET).permitAll().antMatchers(PUBLIC_MATCHERS).permitAll()
				.anyRequest().authenticated();
		// registra o filtro de autenticação
		http.addFilter(new JWTAuthenticationFilter(authenticationManager(), jwtUtil));
		http.addFilter(new JWTAuthorizationFilter(authenticationManager(), jwtUtil, userDetailsService));
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		// acima diz que nunca vai ser criada uma sessão http com usuário
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

	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
		return source;
	}

	/*
	 * Bean vai estar disponível para ser injetado em qlqr classe do sistema. A
	 * função desse método é encodar a senha do usuário
	 */
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
}