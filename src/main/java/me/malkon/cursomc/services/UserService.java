package me.malkon.cursomc.services;

import org.springframework.security.core.context.SecurityContextHolder;

import me.malkon.cursomc.security.UserSS;

public class UserService {
	// método retorna o usuário autenticado
	public static UserSS authenticated() {
		try { // retorna o usuário logado. Pode dar exceção se n tiver user logado
			return (UserSS) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		} catch (Exception e) {// qualquer exceção que ocorrer vai capturar
			return null;
		}
	}

}
