package me.malkon.cursomc.resources;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import me.malkon.cursomc.security.JWTUtil;
import me.malkon.cursomc.security.UserSS;
import me.malkon.cursomc.services.UserService;

/*Quando o token do cliente está perto de expirar, a aplicação acessa o endpoint do refresh e gera um novo token.
 * Este endpoint é protegido então o usuário tem que estar logado para acessá-lo*/
@RestController
@RequestMapping(value = "/auth")
public class AuthResource {

	@Autowired
	private JWTUtil jwtUtil;

	@RequestMapping(value = "/refresh_token", method = RequestMethod.POST)
	public ResponseEntity<Void> refreshToken(HttpServletResponse response) {
		UserSS user = UserService.authenticated();
		String token = jwtUtil.generateToken(user.getUsername());
		response.addHeader("Authorization", "Bearer " + token);// add o token na resposta da requisição
		return ResponseEntity.noContent().build();
	}

}
