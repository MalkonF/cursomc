package me.malkon.cursomc.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import me.malkon.cursomc.domain.Categoria;
import me.malkon.cursomc.dto.CategoriaDTO;
import me.malkon.cursomc.repositories.CategoriaRepository;
import me.malkon.cursomc.services.exceptions.DataIntegrityException;
import me.malkon.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class CategoriaServices {

	@Autowired // automaticamente instanciada pelo Spring pelo mecanismo de i.d e i.c
	private CategoriaRepository repo;

	/*
	 * findById retorna um objeto Optional do tipo que está especificado. Se o obj
	 * não existir vai lançar uma exceção. O método orElseThrow recebe uma
	 * função(sem argumentos) que instancia uma exceção. getName retorna o nome da
	 * classe. No REST vai ter q capturar essa exceção qnd ocorrer e mandar uma
	 * resposta http adequada
	 */
	public Categoria find(Integer id) {
		Optional<Categoria> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Categoria.class.getName()));

	}

	/*
	 * se for novo obj tem que ter o id nulo, se já tiver id, o método save vai ser
	 * considerado como atualização
	 */
	public Categoria insert(Categoria obj) {
		obj.setId(null);
		return repo.save(obj);
	}

	/*
	 * vai buscar obj no banco, se n existir lança a exceção q é implemenetada no
	 * proprio metodo find
	 * 
	 * save serve tanto p inserir qt p atualizar. O q importa é o id ta valendonulo,
	 * ele insere, qnd n é nulo ele atualiza
	 */
	public Categoria update(Categoria obj) {
		Categoria newObj = find(obj.getId());
		updateData(newObj, obj);
		return repo.save(newObj);
	}

	public void delete(Integer id) {
		find(id);// verifica se o id existe, se n existir dispara exceção
		try {
			repo.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não é possível excluir uma categoria que possui produtos");
		} /*
			 * se excluir uma categoria que tem produtos vai lançar essa exceção
			 * personalizada. Aí lá na classe do controller vai pegar a exceção pelo mesmo
			 * mecanismo da exceção da categoria
			 */

	}

	public List<Categoria> findAll() {
		return repo.findAll();
	}

	/*
	 * recurso de paginação. direction tem que ser convertido de string p Direction
	 * Classe Page encapsula informações da paginação
	 */
	public Page<Categoria> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		return repo.findAll(pageRequest);// retorna a página
	}

	// converte CategoriaDTO para Categoria
	public Categoria fromDTO(CategoriaDTO objDto) {
		return new Categoria(objDto.getId(), objDto.getNome());
	}

	private void updateData(Categoria newObj, Categoria obj) {
		newObj.setNome(obj.getNome());
	}
}
