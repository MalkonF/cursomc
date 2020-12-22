package me.malkon.cursomc.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

import me.malkon.cursomc.domain.enums.Perfil;
import me.malkon.cursomc.domain.enums.TipoCliente;

@Entity
public class Cliente implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String nome;
	/*
	 * Garante que o campo email vai ser único no bd. N pode ter 2 email iguais msm
	 * p clientes diferentes. Implementando somente essa anotação a exceção n vai
	 * ser personalizada, vai ser excecao do framework. Unique mantem integridade do
	 * bd, mas podemos fazer notacao personalizada p alem da integridade, fazer tb
	 * validacao se ja tem o e-mail(isso é implementado em ClientInsertValidator).
	 * 
	 */
	@Column(unique = true)
	private String email;
	private String cpfOuCnpj;
	/*
	 * O TipoCliente vai ser armazenado internamente como um inteiro, mas p o mundo
	 * externo a classe vai expor um TipoCliente
	 */
	private Integer tipo;
	// N vai aparecer campo senha nos dados JSON qnd der get, mesmo q criptografada
	@JsonIgnore
	private String senha;

	/*
	 * @JsonManagedReference cli serializa vários endereços, mas end n podem
	 * serializar mais de 1 cli. Na classe endereço fica o @JsonBackReference
	 * dizendo que ele n pode serializar os clientes. E na parte cliente fica
	 * @JsonManagedReference
	 * 
	 * cascadeType.All toda operação q for feita no cliente vai ser refletida no
	 * endereço, se excluir cliente os endereços anexados vao ser excluídos. Qd tem
	 * relacionamento um p muitos, na delecao de algo, vc vai ter q decidir se barra
	 * a exclusao se um depender do outro, ex pedidos feitos depende de clientes ou
	 * se apaga a dependencia, ex endereco depende de cliente, qnd for excluir
	 * cliente apague endereco
	 * 
	 * List é uma interface e n pode ser instanciada, por isso tem que achar uma
	 * classe que implementa ela(ArrayList)
	 */
	@OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL)
	private List<Endereco> enderecos = new ArrayList<>();

	/*
	 * Como o modelo telefone tem somente 1 campo string n vai ser necessário criar
	 * uma classe para ele. Vai ser implementado uma coleção de strings associadas
	 * ao cliente. Set não aceita repetição.
	 * 
	 * @ElementCollection - P mapear e o jpa criar a tabela como entidade fraca
	 * associada a id do cliente; classe embutida.
	 * 
	 * @CollectionTable - o nome da tabela que vai guardar os telefones
	 */
	@ElementCollection
	@CollectionTable(name = "TELEFONE")
	private Set<String> telefones = new HashSet<>();
	// eager garante que sempre que buscar o cliente no bd vai ser buscado os perfis
	// tb
	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "PERFIS")
	private Set<Integer> perfis = new HashSet<>();// inteiro pq so vai guardar o codigo do perfil q ta em enum

	@JsonIgnore
	@OneToMany(mappedBy = "cliente")
	private List<Pedido> pedidos = new ArrayList<>();

	public Cliente() {
		// Todo cliente por padrão já vai ter perfil do tipo cliente
		addPerfil(Perfil.CLIENTE);

	}

	public Cliente(Integer id, String nome, String email, String cpfOuCnpj, TipoCliente tipo, String senha) {
		super();

		this.id = id;
		this.nome = nome;
		this.email = email;
		this.cpfOuCnpj = cpfOuCnpj;
		this.tipo = (tipo == null) ? null : tipo.getCod();
		this.senha = senha;
		addPerfil(Perfil.CLIENTE);
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCpfOuCnpj() {
		return cpfOuCnpj;
	}

	public void setCpfOuCnpj(String cpfOuCnpj) {
		this.cpfOuCnpj = cpfOuCnpj;
	}

	public TipoCliente getTipo() {
		return TipoCliente.toEnum(tipo);
	}

	public void setTipo(TipoCliente tipo) {
		this.tipo = tipo.getCod();
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	// retorna o perfil associado ao cliente
	public Set<Perfil> getPerfis() {
		// converte numero inteiro para o tipo perfil
		return perfis.stream().map(x -> Perfil.toEnum(x)).collect(Collectors.toSet());
	}

	// add perfil ao cliente
	public void addPerfil(Perfil perfil) {
		perfis.add(perfil.getCod());
	}

	public List<Endereco> getEnderecos() {
		return enderecos;
	}

	public void setEnderecos(List<Endereco> enderecos) {
		this.enderecos = enderecos;
	}

	public Set<String> getTelefones() {
		return telefones;
	}

	public void setTelefones(Set<String> telefones) {
		this.telefones = telefones;
	}

	public List<Pedido> getPedidos() {
		return pedidos;
	}

	public void setPedidos(List<Pedido> pedidos) {
		this.pedidos = pedidos;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
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
		Cliente other = (Cliente) obj;
		if (id != other.id)
			return false;
		return true;
	}
}
