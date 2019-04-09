package me.malkon.cursomc.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import me.malkon.cursomc.domain.Pedido;
import me.malkon.cursomc.repositories.PedidoRepository;
import me.malkon.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class PedidoServices {
	
	@Autowired
	private PedidoRepository repo;
	
	public Pedido buscar(Integer id) {
		
		Optional<Pedido> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Pedido.class.getName()));
		
	
		
		}
}
