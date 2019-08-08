package me.malkon.cursomc.services;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import me.malkon.cursomc.domain.ItemPedido;
import me.malkon.cursomc.domain.PagamentoComBoleto;
import me.malkon.cursomc.domain.Pedido;
import me.malkon.cursomc.domain.enums.EstadoPagamento;
import me.malkon.cursomc.repositories.ItemPedidoRepository;
import me.malkon.cursomc.repositories.PagamentoRepository;
import me.malkon.cursomc.repositories.PedidoRepository;
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

	public Pedido find(Integer id) {

		Optional<Pedido> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Pedido.class.getName()));

	}

	public Pedido insert(Pedido obj) {
		obj.setId(null);
		obj.setInstante(new Date());// registra o pedido com a hora,data atual
		obj.setCliente(clienteService.find(obj.getCliente().getId()));
		obj.getPagamento().setEstado(EstadoPagamento.PENDENTE);
		obj.getPagamento().setPedido(obj);
		if (obj.getPagamento() instanceof PagamentoComBoleto) {
			PagamentoComBoleto pagto = (PagamentoComBoleto) obj.getPagamento();
			boletoService.preencherPagamentoComBoleto(pagto, obj.getInstante());
		}
		obj = repo.save(obj);
		pagamentoRepository.save(obj.getPagamento());
		for (ItemPedido ip : obj.getItens()) {
			ip.setDesconto(0.0);
			ip.setProduto(produtoService.find(ip.getProduto().getId()));
			ip.setPreco(ip.getProduto().getPreco());// busca o preço do produto
			ip.setPedido(obj);
		}
		itemPedidoRepository.saveAll(obj.getItens());// repository é capaz de salvar listas
		System.out.println(obj);
		return obj;
	}
}
