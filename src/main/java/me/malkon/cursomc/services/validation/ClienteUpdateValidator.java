package me.malkon.cursomc.services.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerMapping;

import me.malkon.cursomc.domain.Cliente;
import me.malkon.cursomc.dto.ClienteDTO;
import me.malkon.cursomc.repositories.ClienteRepository;
import me.malkon.cursomc.resources.exception.FieldMessage;

/*É usado o ClienteDTO ao invés do ClienteNewDTO pq este dto que é utilizado p atualizar*/
public class ClienteUpdateValidator implements ConstraintValidator<ClienteUpdate, ClienteDTO> {

	/*
	 * Qnd se faz uma atualização n é passado o id no corpo do dto e sim na URI Ex:
	 * localhost:8080/clientes/2 URI = 2. Como vou fazer pra pegar o id da uri
	 * dentro da classe validator? Vou injetar no validator um objeto httprequest ,
	 * esse objeto tem um metodo q permite obter o parametro da uri.
	 **/
	@Autowired
	private HttpServletRequest request;

	@Autowired
	private ClienteRepository repo;

	@Override
	public void initialize(ClienteUpdate ann) {
	}

	/*
	 * Como é uma atualização, o e-mail tem que ser o mesmo email que já está
	 * cadastrado, se for outro email de outra pessoa ele não pode estar cadastrado
	 * no banco. Para isso vai usar o id
	 */
	@Override
	public boolean isValid(ClienteDTO objDto, ConstraintValidatorContext context) {
		/*
		 * Qd se faz a requisição ela tem vários atributos e esses atributos são
		 * armazenados dentro de um Map get pega o id que veio nos atributos que estão
		 * no map
		 */
		@SuppressWarnings("unchecked") // para tirar exclamacao amarela no n da linha do STS
		Map<String, String> map = (Map<String, String>) request
				.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
		Integer uriId = Integer.parseInt(map.get("id"));

		List<FieldMessage> list = new ArrayList<>();
		/* Se existir um email e a id desse email n for a minha, apresentar erro */
		Cliente aux = repo.findByEmail(objDto.getEmail());
		if (aux != null && !aux.getId().equals(uriId))
			list.add(new FieldMessage("email", "Email já existente"));

		for (FieldMessage e : list) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName())
					.addConstraintViolation();
		}
		return list.isEmpty();
	}
}