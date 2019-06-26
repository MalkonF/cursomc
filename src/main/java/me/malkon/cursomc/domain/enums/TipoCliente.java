package me.malkon.cursomc.domain.enums;

public enum TipoCliente {
	/*
	 * Por padrão, se tiver somente as constantes como: PESSOAFISICA,
	 * PESSOAJURIDICA, no banco de dados vai gravar ou um campo string na tabela q
	 * ele é referenciado(Cliente) com o próprio nome da constante ou um número int
	 * começando a primeira constante com 0. No caso do valor em int n é recomendado
	 * pq se for adicionado mais alguma constante a ordem das ids vão ser
	 * modificadas tb e isso vai invalidar as informações no bd. Por isso é
	 * importante fzr um controle manual com ids fixa p cada constante.
	 */
	PESSOAFISICA(1, "Pessoa Física"), PESSOAJURIDICA(2, "Pessoa Jurídica");

	private int cod;
	private String descricao;

	private TipoCliente(int cod, String descricao) {

		this.cod = cod;
		this.descricao = descricao;

	}

	public int getCod() {

		return cod;
	}

	public String getDescricao() {

		return descricao;

	}

	/*
	 *recebe o cod e retorna um obj TipoCliente já instanciado. É static pq essa
	 * operação vai ser possível mesmo sem instanciar obj
	 */
	public static TipoCliente toEnum(Integer cod) {

		if (cod == null)
			return null;

		for (TipoCliente x : TipoCliente.values()) {

			if (cod.equals(x.getCod()))
				return x;

		}

		throw new IllegalArgumentException("Id inválido " + cod);

	}

}
