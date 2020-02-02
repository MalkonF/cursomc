package me.malkon.cursomc.services.validation;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import me.malkon.cursomc.domain.Cliente;
import me.malkon.cursomc.domain.enums.TipoCliente;
import me.malkon.cursomc.dto.ClienteNewDTO;
import me.malkon.cursomc.repositories.ClienteRepository;
import me.malkon.cursomc.resources.exception.FieldMessage;
import me.malkon.cursomc.services.validation.utils.BR;

/*No implements nome da anotação(ClienteInsert) e o tipo da classe que vai ser usada a anotação(CLienteNewDTO). Implementamos 
 * ConstraintValidator para sobrescrever o método isValid e personalizarmos e implementarmos nossa validação.
 * 
 * Toda notacao customizada precisa da classe da anotacao(ClienteInsert) e outra(ClienteInsertValidator) p ser o 
 * validator da anotacao*/
public class ClienteInsertValidator implements ConstraintValidator<ClienteInsert, ClienteNewDTO> {

	@Autowired
	private ClienteRepository repo;

	@Override // aqui coloca-se algo p iniciar antes de rodar o metodo isValid
	public void initialize(ClienteInsert ann) {
	}

	/*
	 * Esse método tem que retornar true se o objeto for válido e false se n for
	 * válido. Se retornar false lá no resources, no metodo post, tem a
	 * anotação @Valid, ela q vai validar se ta ok ou não
	 */
	@Override
	public boolean isValid(ClienteNewDTO objDto, ConstraintValidatorContext context) {
		List<FieldMessage> list = new ArrayList<>();

		if (objDto.getTipo().equals(TipoCliente.PESSOAFISICA.getCod()) && !BR.isValidCpf(objDto.getCpfOuCnpj())) {
			list.add(new FieldMessage("CpfOuCnpj", "CPF inválido"));
		}
		if (objDto.getTipo().equals(TipoCliente.PESSOAJURIDICA.getCod()) && !BR.isValidCnpj(objDto.getCpfOuCnpj())) {
			list.add(new FieldMessage("CpfOuCnpj", "CNPJ inválido"));
		}

		Cliente aux = repo.findByEmail(objDto.getEmail());
		if (aux != null)
			list.add(new FieldMessage("email", "Email já existente"));

		/*
		 * percorre os erros na lista FieldMessage e add os erros dela(erros
		 * personalizados) na lista de erros do
		 * framework(disableDefaultConstraintViolation()). Essa lista de erros do
		 * framework é usada na classe ResourceExceptionHandler q detectada a excecao
		 * qnd ela ocorre
		 */

		for (FieldMessage e : list) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName())
					.addConstraintViolation();
		}
		return list.isEmpty();// se a lista de erros está vazia n teve nenhum erro(retorna true) se tiver
								// cheia vai retornar false
	}
}