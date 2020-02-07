package me.malkon.cursomc.services;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import me.malkon.cursomc.domain.Cliente;
import me.malkon.cursomc.repositories.ClienteRepository;
import me.malkon.cursomc.services.exceptions.ObjectNotFoundException;

/*Usuario envia email p o sistema e o sistema envia um e-mail com a nova senha do usuário.*/
@Service
public class AuthService {

	@Autowired
	private ClienteRepository clienteRepository;

	@Autowired
	private BCryptPasswordEncoder pe;

	@Autowired
	private EmailServices emailService;

	private Random rand = new Random();

	public void sendNewPassword(String email) {
		// verifica se o e-mail existe
		Cliente cliente = clienteRepository.findByEmail(email);
		if (cliente == null) {
			throw new ObjectNotFoundException("Email não encontrado");
		}
		// retorna uma nova senha aleatória
		String newPass = newPassword();
		cliente.setSenha(pe.encode(newPass));// define nova senha para o cliente

		clienteRepository.save(cliente);// salva o cliente com a nova senha
		emailService.sendNewPasswordEmail(cliente, newPass);// envia o email p o cliente com a senha
	}

	// gera senha de 10 caracteres com digitos e letras
	private String newPassword() {
		char[] vet = new char[10];
		for (int i = 0; i < 10; i++) {
			vet[i] = randomChar();
		}
		return new String(vet);
	}

	// gera caractere aleatório
	private char randomChar() {
		int opt = rand.nextInt(3); // gera 0, 1 ou 2
		if (opt == 0) { // gera um digito
			return (char) (rand.nextInt(10) + 48);
		} else if (opt == 1) { // gera letra maiuscula
			return (char) (rand.nextInt(26) + 65);
		} else { // gera letra minuscula
			return (char) (rand.nextInt(26) + 97);
		}
	}

}
