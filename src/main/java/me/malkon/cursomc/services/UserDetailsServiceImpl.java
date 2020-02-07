package me.malkon.cursomc.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import me.malkon.cursomc.domain.Cliente;
import me.malkon.cursomc.repositories.ClienteRepository;
import me.malkon.cursomc.security.UserSS;

/*Classe serve para buscar usuário e retornar detalhes.
 * Sobrescreve metodo padrao de UserDetailsService p buscar um usuario na base de dados*/
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private ClienteRepository repo;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Cliente cli = repo.findByEmail(email);
		if (cli == null) {
			throw new UsernameNotFoundException(email);
		}
		return new UserSS(cli.getId(), cli.getEmail(), cli.getSenha(), cli.getPerfis());
	}
}
