package me.malkon.cursomc.domain;

import java.util.Date;

import javax.persistence.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonTypeName;

import me.malkon.cursomc.domain.enums.EstadoPagamento;

/*Aqui n tem ch primaria, ela é herdada da superclasse
 * 
 * Qual é a palavra que vai identificar a classe no campo adicional?*/
@Entity
@JsonTypeName("pagamentoComBoleto")
public class PagamentoComBoleto extends Pagamento {

	private static final long serialVersionUID = 1L;

	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date dataVencimento;
	private Date dataPagamento;

	public PagamentoComBoleto() {
		super();

	}

	public PagamentoComBoleto(Integer id, EstadoPagamento estado, Pedido pedido, Date dataVencimento,
			Date dataPagamento) {
		super(id, estado, pedido);

		this.setDataPagamento(dataPagamento);
		this.setDataVencimento(dataVencimento);

	}

	public Date getDataVencimento() {
		return dataVencimento;
	}

	public void setDataVencimento(Date dataVencimento) {
		this.dataVencimento = dataVencimento;
	}

	public Date getDataPagamento() {
		return dataPagamento;
	}

	public void setDataPagamento(Date dataPagamento) {
		this.dataPagamento = dataPagamento;
	}

}
/*
 * Nas subclasses n precisam ser implementados hashcode e equals pq o id ta na
 * classe Pagamento e a comparação já é feita na superclasse, essa aqui herda.
 */
