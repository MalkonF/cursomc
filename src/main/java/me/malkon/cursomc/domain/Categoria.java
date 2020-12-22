package me.malkon.cursomc.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

/*
		 * @Entity indica que a classe vai ser uma entidade JPA
		 *
		 * implements Serializable diz que os objetos dessa classe poderá ser convertido p uma sequencia de
		 * bytes. Aí os objetos podem ser gravados em arquivos, trafegados em rede.
		 * Nesse caso ele vai habilitar transmitir os dados dos objetos como JSON(já que o formato é feito por strings)
		 */
@Entity
public class Categoria implements Serializable {

	private static final long serialVersionUID = 1L;// diz que a versão da classe é 1

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // a forma de como vai gerar o campo id na tabela(ch primaria)
	private Integer id;
	private String nome;
	// categorias tem que ser o nome de onde foi mapeado na outra classe Produtos
	@ManyToMany(mappedBy = "categorias")
	private List<Produto> produtos = new ArrayList<>();

	public Categoria() {
		super();
	}

	public Categoria(Integer id, String nome) {
		super();
		this.id = id;
		this.nome = nome;
	}

	public List<Produto> getProdutos() {
		return produtos;
	}

	public void setProdutos(List<Produto> produtos) {
		this.produtos = produtos;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNome() {
        return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@Override
	public int hashCode() {// vai gerar um código numérico diferente p cada obj
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {// vai fzr a comparação dos obj considerando várias possibilidades: se for nulo

		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Categoria other = (Categoria) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
/*
 * hashcode e equals são necessários para os objetos serem comparados por
 * valor(por seu conteúdo) e n por referência. Normalmente é usado somente o
 * atributo id para diferenciar os objetos por conteúdo.
 */
