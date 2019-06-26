package me.malkon.cursomc;

import java.text.SimpleDateFormat;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import me.malkon.cursomc.domain.Categoria;
import me.malkon.cursomc.domain.Cidade;
import me.malkon.cursomc.domain.Cliente;
import me.malkon.cursomc.domain.Endereco;
import me.malkon.cursomc.domain.Estado;
import me.malkon.cursomc.domain.ItemPedido;
import me.malkon.cursomc.domain.Pagamento;
import me.malkon.cursomc.domain.PagamentoComBoleto;
import me.malkon.cursomc.domain.PagamentoComCartao;
import me.malkon.cursomc.domain.Pedido;
import me.malkon.cursomc.domain.Produto;
import me.malkon.cursomc.domain.enums.EstadoPagamento;
import me.malkon.cursomc.domain.enums.TipoCliente;
import me.malkon.cursomc.repositories.CategoriaRepository;
import me.malkon.cursomc.repositories.CidadeRepository;
import me.malkon.cursomc.repositories.ClienteRepository;
import me.malkon.cursomc.repositories.EnderecoRepository;
import me.malkon.cursomc.repositories.EstadoRepository;
import me.malkon.cursomc.repositories.ItemPedidoRepository;
import me.malkon.cursomc.repositories.PagamentoRepository;
import me.malkon.cursomc.repositories.PedidoRepository;
import me.malkon.cursomc.repositories.ProdutoRepository;

@SpringBootApplication
public class CursomcApplication implements CommandLineRunner {

	@Autowired
	private CategoriaRepository categoriaRepository;

	@Autowired
	private ProdutoRepository produtoRepository;

	@Autowired
	private EstadoRepository estadoRepository;

	@Autowired
	private CidadeRepository cidadeRepository;

	@Autowired
	private ClienteRepository clienteRepository;

	@Autowired
	private EnderecoRepository enderecoRepository;

	@Autowired
	private PedidoRepository pedidoRepository;

	@Autowired
	private PagamentoRepository pagamentoRepository;

	@Autowired
	private ItemPedidoRepository itemPedidoRepository;

	public static void main(String[] args) {
		SpringApplication.run(CursomcApplication.class, args);
	}

	/*
	 * método run executa antes de
	 * SpringApplication.run(CursomcApplication.class,args). Para usá-lo devemos
	 * implementar CommandLineRunner na classe
	 */
	@Override
	public void run(String... args) throws Exception {

		Categoria cat1 = new Categoria(null, "Informática"); // id é gerado automaticamente, por isso coloca-se null
		Categoria cat2 = new Categoria(null, "Escritório");
		Categoria cat3 = new Categoria(null, "Cama mesa e banho");
		Categoria cat4 = new Categoria(null, "Eletrônicos");
		Categoria cat5 = new Categoria(null, "Jardinagem");
		Categoria cat6 = new Categoria(null, "Decoração");
		Categoria cat7 = new Categoria(null, "Perfumaria");

		Produto p1 = new Produto(null, "Computador", 2000.00);
		Produto p2 = new Produto(null, "Impressora", 800.00);
		Produto p3 = new Produto(null, "Mouse", 80.00);
		/*
		 * aqui usa-se get p puxar a lista de produtos para adicionar os produtos no
		 * final dela. Se usarmos setProdutos, os produtos que já constam na lista vão
		 * ser apagados p inserir os novos.
		 */
		cat1.getProdutos().addAll(Arrays.asList(p1, p2, p3));
		cat2.getProdutos().addAll(Arrays.asList(p2));

		p1.getCategorias().addAll(Arrays.asList(cat1));
		p2.getCategorias().addAll(Arrays.asList(cat1, cat2));
		p3.getCategorias().addAll(Arrays.asList(cat1));

		/*
		 * cria os objetos através da instanciação, transforma os em lista e insere
		 * todos objetos no banco de dados. Poderia ser inserido cada objeto por vez,
		 * mas fica mais prático transformar todos numa lista e inserir de uma vez só.
		 */
		categoriaRepository.saveAll(Arrays.asList(cat1, cat2, cat3, cat4, cat5, cat6, cat7));
		produtoRepository.saveAll(Arrays.asList(p1, p2, p3));

		Estado est1 = new Estado(null, "Minas Gerais");
		Estado est2 = new Estado(null, "São Paulo");

		Cidade c1 = new Cidade(null, "Uberlândia", est1);
		Cidade c2 = new Cidade(null, "São Paulo", est2);
		Cidade c3 = new Cidade(null, "Campinas", est2);

		est1.getCidades().addAll(Arrays.asList(c1));
		est2.getCidades().addAll(Arrays.asList(c2, c3));
		/*
		 * primeiro tem q salvar o estado depois as cidades pq um estado pode n ter
		 * nenhuma cidade, mas uma cidade tem que ter uma estado
		 */
		estadoRepository.saveAll(Arrays.asList(est1, est2));
		cidadeRepository.saveAll(Arrays.asList(c1, c2, c3));

		Cliente cli1 = new Cliente(null, "Maria Silva", "maria@gmail.com", "36378912377", TipoCliente.PESSOAFISICA);
		// adiciona os telefone na lista que está em Cliente
		cli1.getTelefones().addAll(Arrays.asList("27363323", "93838393"));

		Endereco e1 = new Endereco(null, "Rua Flores", "300", "Apto 303", "Jardim", "38220834", cli1, c1);
		Endereco e2 = new Endereco(null, "Avenida Matos", "105", "Sala 800", "Centro", "38777012", cli1, c2);

		cli1.getEnderecos().addAll(Arrays.asList(e1, e2));

		clienteRepository.saveAll(Arrays.asList(cli1));
		enderecoRepository.saveAll(Arrays.asList(e1, e2));

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

		Pedido ped1 = new Pedido(null, sdf.parse("30/09/2017 10:32"), cli1, e1);
		Pedido ped2 = new Pedido(null, sdf.parse("10/10/2017 19:35"), cli1, e2);
		// id pag é nulo pq a id vai ser gerada na classe Pedido. Em Pagamento tem
		// anotação @MapsId
		Pagamento pagto1 = new PagamentoComCartao(null, EstadoPagamento.QUITADO, ped1, 6);
		ped1.setPagamento(pagto1);

		Pagamento pagto2 = new PagamentoComBoleto(null, EstadoPagamento.PENDENTE, ped2, sdf.parse("20/10/2017 00:00"),
				null);
		ped2.setPagamento(pagto2);

		cli1.getPedidos().addAll(Arrays.asList(ped1, ped2));

		pedidoRepository.saveAll(Arrays.asList(ped1, ped2));
		pagamentoRepository.saveAll(Arrays.asList(pagto1, pagto2));

		ItemPedido ip1 = new ItemPedido(ped1, p1, 0.00, 1, 2000.00);
		ItemPedido ip2 = new ItemPedido(ped1, p3, 0.00, 2, 80.00);
		ItemPedido ip3 = new ItemPedido(ped2, p2, 100.00, 1, 800.00);

		ped1.getItens().addAll(Arrays.asList(ip1, ip2));
		ped2.getItens().addAll(Arrays.asList(ip3));

		p1.getItens().addAll(Arrays.asList(ip1));
		p2.getItens().addAll(Arrays.asList(ip3));
		p3.getItens().addAll(Arrays.asList(ip2));

		itemPedidoRepository.saveAll(Arrays.asList(ip1, ip2, ip3));

	}
}// continuar vendo perguntas parei aula 13
