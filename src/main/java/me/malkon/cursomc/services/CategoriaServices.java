package me.malkon.cursomc.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import me.malkon.cursomc.domain.Categoria;
import me.malkon.cursomc.repositories.CategoriaRepository;
import me.malkon.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class CategoriaServices {

	@Autowired
	private CategoriaRepository repo;

	public Categoria buscar(Integer id) {

		Optional<Categoria> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Categoria.class.getName()));

	}

	public Categoria insert(Categoria obj) {
		obj.setId(null);// se for novo obj tem que ter o id nulo, se já tiver id, vai ser considerado
						// como atualização
		return repo.save(obj);
	}
}
