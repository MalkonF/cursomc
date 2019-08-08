package me.malkon.cursomc.services;

import org.springframework.mail.SimpleMailMessage;

import me.malkon.cursomc.domain.Pedido;

public interface EmailServices {

	void sendOrderConfirmationEmail(Pedido obj);

	void sendEmail(SimpleMailMessage msg);

}
