package me.malkon.cursomc.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

import me.malkon.cursomc.domain.enums.EstadoPagamento;

/*
 * mapeamento da herança existem duas estratégias:faço uma unica tabela
 * onde vai ter os campos de pagamento com boleto e pag com cartao ai
 * qnd instanciar um pag c cartao vc coloca nulo nos campos do pag com
 * boleto e vice versa. Essa tem mais performance, mas a tabela fica com
 * valores nulos. Outra estratégia é gerar uma tabela p cada subclasse,
 * vai ter uma tabela pag boleto, outra com cartao, e ai qnd for
 * pesquisar os pagamentos vai ter que fazer os joins(junção) das
 * tabelas. Qnd tem muito atributo na subclasse é bom colocar tabelas
 * independentes se tem pouco atributos a única tabela é mais viável.
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Pagamento implements Serializable {// n vai conseguir instanciar objeto com pagamento, so com suas
															// subclasses

	private static final long serialVersionUID = 1L;
	/*
	 * aqui não especifica a estratégia de geração de id pq usamos @MapsId abaixo,
	 * ele vai ter o mesmo id do pedido
	 */
	@Id
	private Integer id;
	private Integer estado;// internamente vai ser armazenado como inteiro mas p externo vai mostrar tipo
							// EstadoPagamento

	@JsonIgnore
	@OneToOne
	@JoinColumn(name = "pedido_id")
	@MapsId // mapeia com ids iguais em ambos os lados
	private Pedido pedido;

	public Pagamento() {

	}

	public Pagamento(Integer id, EstadoPagamento estado, Pedido pedido) {
		super();
		this.id = id;
		this.estado = (estado == null) ? null : estado.getCod();
		this.pedido = pedido;
	}

	public EstadoPagamento getEstado() {
		return EstadoPagamento.toEnum(estado);
	}

	public void setEstado(EstadoPagamento estado) {
		this.estado = estado.getCod();
	}

	public Pedido getPedido() {
		return pedido;
	}

	public void setPedido(Pedido pedido) {
		this.pedido = pedido;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Pagamento other = (Pagamento) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
