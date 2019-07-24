package me.malkon.cursomc.resources.exception;

import java.util.ArrayList;
import java.util.List;

/*Herda do Standard error e acrescenta a lista do FieldMessage*/
public class ValidationError extends StandardError {

	private static final long serialVersionUID = 1L;

	private List<FieldMessage> errors = new ArrayList<>();

	public ValidationError(Integer status, String msg, Long timeStamp) {
		super(status, msg, timeStamp);
	}

	public List<FieldMessage> getErrors() {// Esse get é q vai ser dados no JSON
		return errors;
	}

	// Add na lista apenas os campos nome e a mensagem de erro
	public void addError(String fieldName, String messagem) {
		errors.add(new FieldMessage(fieldName, messagem));
	}

}
