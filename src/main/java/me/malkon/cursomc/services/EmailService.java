package me.malkon.cursomc.services;

import javax.mail.internet.MimeMessage;

import org.springframework.mail.SimpleMailMessage;
import me.malkon.cursomc.domain.Cliente;
import me.malkon.cursomc.domain.Pedido;

/*Como vai ser implementado 2 tipos de email(google e fake), 
  melhor fazer uma interface e usar o polimorfismo.
  Vai ser utilizado padrao estrategy - implementar uma interface ai instancia alguma implementacao
  de acordo com o tipo de email q que*/
public interface EmailService {

	void sendOrderConfirmationEmail(Pedido obj);

	void sendEmail(SimpleMailMessage msg);
	// 2 metodos abaixo envia email html
	void sendOrderConfirmationHtmlEmail(Pedido obj);

	void sendHtmlEmail(MimeMessage msg);

	void sendNewPasswordEmail(Cliente cliente, String newPass);
}
