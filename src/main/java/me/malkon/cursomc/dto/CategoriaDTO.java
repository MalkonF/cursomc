package me.malkon.cursomc.dto;

import java.io.Serializable;

import me.malkon.cursomc.domain.Categoria;

public class CategoriaDTO implements Serializable {// serializable faz com que o obj fiquei fácil trafegar em rede e ser
													// gravado em arquivos

	private static final long serialVersionUID = 1L;

	private Integer id;
	private String nome;

	public CategoriaDTO() {

	}

	public CategoriaDTO(Categoria obj) {
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
