package me.malkon.cursomc.services;

import java.util.Date;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import me.malkon.cursomc.domain.Cliente;
import me.malkon.cursomc.domain.Pedido;

@Service
public abstract class AbstractEmailService implements EmailService {

	@Value("${default.sender}") // pega o valor de application.properties
	private String sender;

	@Autowired
	private TemplateEngine templateEngine;

	@Autowired
	private JavaMailSender javaMailSender;
	// implementacao comum do mockmail e smtpmail por isso esta na classe abstrata
	@Override
	public void sendOrderConfirmationEmail(Pedido obj) {
		SimpleMailMessage sm = prepareSimpleMailMessageFromPedido(obj);
		sendEmail(sm);
	}
	/*
	 * Padrao template method
	 * 
	 * Antes de enviar o email ele tem que ser prepararado a parti de pedido prepara
	 * o e-mail com assunto, remetente etc
	 */
	protected SimpleMailMessage prepareSimpleMailMessageFromPedido(Pedido obj) {

		SimpleMailMessage sm = new SimpleMailMessage();
		sm.setTo(obj.getCliente().getEmail());
		sm.setFrom(sender);
		sm.setSubject("Pedido confirmado! Código: " + obj.getId());
		sm.setSentDate(new Date(System.currentTimeMillis()));
		sm.setText(obj.toString());// corpo do email
		return sm;
	}



	/* Aqui o template será preechido com as info do backend */
	protected String htmlFromTemplatePedido(Pedido obj) {
		/* Context vai permitir acessar template thymeleaf */
		Context context = new Context();
		context.setVariable("pedido", obj);// obj será referenciado como pedido no template
		return templateEngine.process("email/confirmacaoPedido", context);
		// retorna html na forma de string
	}

	@Override
	public void sendOrderConfirmationHtmlEmail(Pedido obj) {
		try {
			MimeMessage mm = prepareMimeMailMessageFromPedido(obj);
			sendHtmlEmail(mm);
		} catch (MessagingException o) {
			sendOrderConfirmationEmail(obj);// tenta mandar email html se n der manda txt plano
		}
	}
	/* Protected abre a possibilidade do metodo ser implementado numa subclasse */
	protected MimeMessage prepareMimeMailMessageFromPedido(Pedido obj) throws MessagingException {
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		MimeMessageHelper mmh = new MimeMessageHelper(mimeMessage, true);// p poder atrribuir valores na msg
		mmh.setTo(obj.getCliente().getEmail());
		mmh.setFrom(sender);
		mmh.setSubject("Pedido confirmado! Código " + obj.getId());
		mmh.setSentDate(new Date(System.currentTimeMillis()));
		mmh.setText(htmlFromTemplatePedido(obj), true);
		return mimeMessage;
	}
	
	// prepara o e-mail com assunto, remetente etc
	@Override
	public void sendNewPasswordEmail(Cliente cliente, String newPass) {
		SimpleMailMessage sm = prepareNewPasswordEmail(cliente, newPass);
		sendEmail(sm);
	}

	protected SimpleMailMessage prepareNewPasswordEmail(Cliente cliente, String newPass) {
		SimpleMailMessage sm = new SimpleMailMessage();
		sm.setTo(cliente.getEmail());
		sm.setFrom(sender);
		sm.setSubject("Solicitação de nova senha");
		sm.setSentDate(new Date(System.currentTimeMillis()));
		sm.setText("Nova senha: " + newPass);// conteúdo do email
		return sm;
	}
}
