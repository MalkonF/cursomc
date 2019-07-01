package me.malkon.cursomc.domain;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class ItemPedido implements Serializable {

	private static final long serialVersionUID = 1L;
	/*
	 * Em @JsonIgnore não vai serializar ngm, nem o pedido nem o produto. Na outra
	 * ponta em Pedidos a referencia q aponta p aqui vai ser serializada normalmente
	 * 
	 * 
	 * Essa é uma classe de associação, por isso não tem id próprio. Quem identifica
	 * ela são os 2 objetos associados a ela(como ch estrangeira), no caso, o
	 * produto e o pedido, através da classe ItemPEdidoPK que representa uma chave
	 * composta. Qd uma entidade tem como atributo uma outra classe, vc tem que ir
	 * na outra classe e colocar a anotação @Embeddable p dizer q ela vai ser um
	 * subtipo. Vai ser criado somente 1 tabela ItemPedido e nessa tabela vai ter o
	 * campo pedido_id e produto_id referente a classe ItemPedidoPK. Assim consegue
	 * trabalhar com o id do item pedido como sendo apenas um valor, ou seja, uma
	 * entidade.
	 *
	 */

	@JsonIgnore
	@EmbeddedId
	private ItemPedidoPK id = new ItemPedidoPK();

	private Double desconto;
	private Integer quantidade;
	private Double preco;

	public ItemPedido() {
		super();
	}

	public ItemPedido(Pedido pedido, Produto produto, Double desconto, Integer quantidade, Double preco) {
		super();
		id.setPedido(pedido);
		id.setProduto(produto);
		this.desconto = desconto;
		this.quantidade = quantidade;
		this.preco = preco;
	}
	//como método começa com get vai ser serializado com JSON e será mostrado no endpoint
	public double getSubTotal() {
		return (preco - desconto) * quantidade;
	}

	/*
	 * Se deixar sem @JsonIgnore, vai ser serializado os pedidos associados aos
	 * produtos. Tudo q começa com get é serializado.
	 */
	@JsonIgnore
	public Pedido getPedido() {

		return id.getPedido();
	}

	public Produto getProduto() {

		return id.getProduto();
	}

	public ItemPedidoPK getId() {
		return id;
	}

	public void setId(ItemPedidoPK id) {
		this.id = id;
	}

	public Double getDesconto() {
		return desconto;
	}

	public void setDesconto(Double desconto) {
		this.desconto = desconto;
	}

	public Integer getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}

	public Double getPreco() {
		return preco;
	}

	public void setPreco(Double preco) {
		this.preco = preco;
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
		ItemPedido other = (ItemPedido) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
