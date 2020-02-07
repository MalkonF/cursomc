package me.malkon.cursomc.resources;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import me.malkon.cursomc.domain.Categoria;
import me.malkon.cursomc.dto.CategoriaDTO;
import me.malkon.cursomc.services.CategoriaService;

/*Indica que a classe toda vai ser um controlador rest e vai responder pelo endpoint abaixo. Sempre use nomes de endpoints no plural*/
@RestController
@RequestMapping(value = "/categorias")
public class CategoriaResource {

	@Autowired
	private CategoriaService service;

	/*
	 * para o método ter alguma função REST tem que associar a alguns dos verbos do
	 * http. O id diz que o endpoint n vai ser formado so por categorias e sim
	 * categoria mais o id. PathVariable diz que o id que vc receber na url vai ser
	 * passado p esse método find. ResponseEntity encapsula/armazena varias
	 * informações de uma resposta http p um serviço REST.
	 * 
	 * 
	 * Aqui se der exceção no service poderia ter um try/catch p capturar a exceção
	 * e tratar ela mas em resources não é aconselhável ter métodos grandes e sim
	 * menores. Por isso vamos criar um handler q é um obj especial q vai
	 * interceptar aqui caso ocorra uma exceção. O handler q vai lançar a resposta
	 * http adequada, no caso 404. P isso foi criado a classe
	 * ResourceExceptionHandler
	 */
	// ReponseEntity retorna uma categoria, se ela fosse retornar vários tipos de
	// objetos diferentes poderia colocar o <?>
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<Categoria> find(@PathVariable Integer id) {

		Categoria obj = service.find(id);
		/*
		 * operação ocorreu com sucesso(ok) e a resposta vai ter como corpo o objeto que
		 * busquei:obj
		 */
		return ResponseEntity.ok().body(obj);

	}

	/*
	 * ResponseEntity recebe um retorno de resposta http e void diz que o corpo da
	 * resposta é vazio
	 * 
	 * @Valid valida a informação(atributos da classe) que tem as anotacoes,
	 * ex: @notEmpty, se o nome tiver vazio, por exemplo, vai retornar um bad
	 * request. O @Valid ele valida as informações a partir do cliente, no sentido
	 * Cliente - Controller - Service...
	 * 
	 * @RequestBody obj vai ser construido a partir dos dados JSON q vc enviar. O
	 * JSON vai ser convertido p objeto java automaticamente
	 */
	@RequestMapping(method = RequestMethod.POST) // método vai ser mapeado no endpoint categorias tipo POST
	@PreAuthorize("hasAnyRole('ADMIN')")// so quem é admin pode acessar este endpoint, ou seja, cadastrar uma nova
	public ResponseEntity<Void> insert(@Valid @RequestBody CategoriaDTO objDto) {

		Categoria obj = service.fromDTO(objDto);
		obj = service.insert(obj);// retorna o objeto após insert ser executado
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
		return ResponseEntity.created(uri)
				.build(); /*
							 * buildAndExpand pega o id que o banco gerou c obj.getId e fornece ele p path e
							 * fromCurrentRequest vai emendar com o endereco q foi requerido mais id e gerar
							 * a URI do novo objeto. fromCurrentRequest() pega a url que usamos p inserir
							 * /categorias/ e created() retorna http status created - 201 junto com a uri do
							 * novo recurso criado
							 */
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	@PreAuthorize("hasAnyRole('ADMIN')") // @PathVariable pega o parametro da URL {id} e atribui a var id
	public ResponseEntity<Void> update(@Valid @RequestBody CategoriaDTO objDto, @PathVariable Integer id) {
		Categoria obj = service.fromDTO(objDto);
		obj.setId(id);// garante que a categoria atualizada vai ser a que vc passou na url
		obj = service.update(obj);
		return ResponseEntity.noContent().build();// nocontent retorna conteúdo vazio
	}

	// ResponseEntity<Void> é void pq ele retorna uma reposta com corpo vazio
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}

	/*
	 * Aqui será listada todas as categorias de uma vez, mas qnd vc solicita todas
	 * as categorias elas vem associada com os produtos pendurados. Para vir somente
	 * as categorias tem que usar DTO(Data Transfer Object) objeto de tansf de
	 * dados. Ele é um obj que vai ter somente os dados que vc quer q alguma
	 * operação. Por isso foi criado o pct DTO
	 * 
	 * Esse endpoint sera acessado somente por /categorias n foi colocado um value p
	 * ele
	 */
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<CategoriaDTO>> findAll() {
		List<Categoria> list = service.findAll();// busca lista categorias no banco
		List<CategoriaDTO> listDto = list.stream().map(obj -> new CategoriaDTO(obj)).collect(Collectors.toList());
		/*
		 * converte lista para listaDto. P cada elemento da lista vai ser instanciado um
		 * objeto DTO correspondente. Aqui qnd ele passa obj para CategoriaDTO, o obj
		 * passa so o nome e a id para o dto p simplificar informações. stream vai
		 * percorrer a lista, map vai executar uma operação p cada elemento da lista.
		 * cada elemento da lista é representado por obj e p cada elemento obj na lista
		 * vou usar o operador -> p criar função anonima q recebe objeto e instancia
		 * CategoriaDTO com obj como argumento. Feito isso tem que voltar o stream de
		 * objeto p o tipo lista. Collectors.tolist()
		 */
		return ResponseEntity.ok().body(listDto);

	}

	/*
	 * faz o endpoint p chamar o método p listar as categorias paginadas.Isso é
	 * feito p questões de performance se vc tiver muita informações imagina vir
	 * tudo de uma vez?
	 * 
	 * @RequestParam é como se fosse o @PathVariable só que não é com caminhos / e
	 * sim com parametros ?page=0&linesPerPage=3 o defaultValue vai valer se n for
	 * informado esses dados
	 */
	@RequestMapping(value = "/page", method = RequestMethod.GET) // concatena /pages com /categorias
	public ResponseEntity<Page<CategoriaDTO>> findPage(@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "linesPerPage", defaultValue = "24") Integer linesPerPage,
			@RequestParam(value = "orderBy", defaultValue = "nome") String orderBy,
			@RequestParam(value = "direction", defaultValue = "ASC") String direction) {
		Page<Categoria> list = service.findPage(page, linesPerPage, orderBy, direction);
		Page<CategoriaDTO> listDto = list.map(obj -> new CategoriaDTO(obj));// converte Categoria p CategoriaDTO
		return ResponseEntity.ok().body(listDto);
	}

}
