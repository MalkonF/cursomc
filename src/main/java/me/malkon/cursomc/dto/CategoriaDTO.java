package me.malkon.cursomc.dto;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;

import me.malkon.cursomc.domain.Categoria;

/*É um objeto que vai definir os dados que vai trafegar qnd fzr operações básicas de categoria
 * 
 * serializable faz com que o obj fique fácil trafegar em rede e ser gravado em arquivos*/
public class CategoriaDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;
	/*
	 * validacao é recomendado fazer no frontend p evitar o envio de dados
	 * desnecessários p o backend. Se for fazer no backend é recomendavel fazer
	 * validacao basica(p n ficar muito verboso) na camada resources e validações q
	 * exigem mais codigo fazer um pct a parte na camada de serviços. Qnd, por
	 * exemplo, vc for fazer um POST de um objDTO, la no resources será interceptado
	 * e validado antes de enviar
	 */
	@NotEmpty(message = "Preenchimento obrigatório") // lança a msg se tiver vazio
	@Length(min = 5, max = 80, message = "O tamanho deve ser entre 5 e 80 caracteres")
	private String nome;

	public CategoriaDTO() {

	}

	public CategoriaDTO(Categoria obj) {// aqui ele recebe um objeto Categoria e converte para CategoriaDTO
		id = obj.getId();
		nome = obj.getNome();
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

}
