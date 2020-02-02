package me.malkon.cursomc.services;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import me.malkon.cursomc.domain.Cliente;
import me.malkon.cursomc.domain.ItemPedido;
import me.malkon.cursomc.domain.PagamentoComBoleto;
import me.malkon.cursomc.domain.Pedido;
import me.malkon.cursomc.domain.enums.EstadoPagamento;
import me.malkon.cursomc.domain.enums.Perfil;
import me.malkon.cursomc.repositories.ItemPedidoRepository;
import me.malkon.cursomc.repositories.PagamentoRepository;
import me.malkon.cursomc.repositories.PedidoRepository;
import me.malkon.cursomc.resources.exception.AuthorizationException;
import me.malkon.cursomc.security.UserSS;
import me.malkon.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class PedidoServices {

	@Autowired
	private PedidoRepository repo;

	@Autowired
	private BoletoServices boletoService;

	@Autowired
	private PagamentoRepository pagamentoRepository;

	@Autowired
	private ItemPedidoRepository itemPedidoRepository;

	@Autowired
	private ProdutoServices produtoService;

	@Autowired
	private ClienteServices clienteService;

	@Autowired
	private EmailServices emailService;// está interface será instanciada como MockMailServices em TestConfig

	public Pedido find(Integer id) {

		UserSS user = UserService.authenticated();

		Optional<Pedido> obj = repo.findById(id);
		if (user == null || !user.hasRole(Perfil.ADMIN) && !obj.get().getCliente().getId().equals(user.getId())) {

			throw new AuthorizationException("Acesso Negado!");
		}

		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Pedido.class.getName()));

	}

	/*
	 * Se ficar na duvida o que salvar na operacao insert so olhar no diagrama de
	 * classes, devera ser salva toda classe independente envolvida na transacao
	 * ex(inserir um pedido envolve as classes Pedido, itemPedido, Pagamento e
	 * produto, as 3 terao que ser salvas, produto n, pq já foi instanciada. Isso e
	 * necessario pq a cada registro no bd todos os campos vao ter q ter
	 * correspondencias, ex: a cada pedido tem q ter um cliente, um pag
	 */
	@Transactional
	public Pedido insert(Pedido obj) {
		obj.setId(null);// garante q esta inserindo um novo pedido
		obj.setInstante(new Date());// registra o pedido com a hora,data atual
		obj.setCliente(clienteService.find(obj.getCliente().getId()));
		obj.getPagamento().setEstado(EstadoPagamento.PENDENTE);
		obj.getPagamento().setPedido(obj);
		if (obj.getPagamento() instanceof PagamentoComBoleto) {// simula calculo dt vencimento boleto
			PagamentoComBoleto pagto = (PagamentoComBoleto) obj.getPagamento();
			boletoService.preencherPagamentoComBoleto(pagto, obj.getInstante());
		}
		obj = repo.save(obj);
		pagamentoRepository.save(obj.getPagamento());/*
														 * e salvo o pagamento pq optamos por essa forma, outra forma
														 * seria usar o setPagamento da propria classe Pedido
														 */
		for (ItemPedido ip : obj.getItens()) {
			ip.setDesconto(0.0);
			ip.setProduto(produtoService.find(ip.getProduto().getId()));
			ip.setPreco(ip.getProduto().getPreco());// busca o preço do produto
			ip.setPedido(obj);
		}
		itemPedidoRepository.saveAll(obj.getItens());// repository é capaz de salvar listas
		emailService.sendOrderConfirmationHtmlEmail(obj);
		return obj;
	}

	// Verifica quem é o user logado e busca os pedidos somente dele
	public Page<Pedido> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {
		UserSS user = UserService.authenticated();
		if (user == null) { // n está autenticado?
			throw new AuthorizationException("Acesso negado");
		}
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		Cliente cliente = clienteService.find(user.getId());
		return repo.findByCliente(cliente, pageRequest);
	}
}
