package me.malkon.cursomc.services;

import java.util.Calendar;
import java.util.Date;

import org.springframework.stereotype.Service;

import me.malkon.cursomc.domain.PagamentoComBoleto;

/*No mundo real teriamos um webservice que gerava o boleto e já mandava a data de vencimento*/
@Service
public class BoletoService {

	public void preencherPagamentoComBoleto(PagamentoComBoleto pagto, Date instanteDoPedido) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(instanteDoPedido);
		cal.add(Calendar.DAY_OF_MONTH, 7);// acrescenta 7 dias a frente
		pagto.setDataVencimento(cal.getTime());
	}
}