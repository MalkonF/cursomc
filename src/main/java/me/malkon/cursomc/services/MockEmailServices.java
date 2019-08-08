package me.malkon.cursomc.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;

//classe vai somente simular envio de email através do Logger
public class MockEmailServices extends AbstractEmailServices {

	private static final Logger LOG = LoggerFactory.getLogger(MockEmailServices.class);

	@Override /*
				 * classe abstrata implementa 1 dos metodos da interface. Essa classe extende a
				 * abstrata e herda o direito de implementar o outro método sendEmail
				 */
	public void sendEmail(SimpleMailMessage msg) {
		LOG.info("Simulando envio de email...");
		LOG.info(msg.toString());
		LOG.info("Email enviado");
	}

}
