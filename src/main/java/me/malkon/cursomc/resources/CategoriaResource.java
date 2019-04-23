package me.malkon.cursomc.resources;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import me.malkon.cursomc.domain.Categoria;
import me.malkon.cursomc.services.CategoriaServices;

@RestController
@RequestMapping(value = "/categorias")
public class CategoriaResource {

	@Autowired
	private CategoriaServices service;

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> find(@PathVariable Integer id) {

		Categoria obj = service.buscar(id);

		return ResponseEntity.ok().body(obj);

	}

	// ResponseEntity recebe um retorno de resposta http e void diz que o corpo da
	// resposta é vazio
	@RequestMapping(method = RequestMethod.POST) // método vai ser mapeado no endpoint categorias tipo POST
	public ResponseEntity<Void> insert(@RequestBody Categoria obj) {// obj vai ser construido a partir dos dados JSON.
																	// JSON vai ser convertido p obj java
		obj = service.insert(obj);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
		return ResponseEntity.created(uri).build(); // retorna http status created
	}

}
