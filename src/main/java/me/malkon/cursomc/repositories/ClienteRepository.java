package me.malkon.cursomc.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import me.malkon.cursomc.domain.Cliente;

/*Padrão de nomes - se fizer um método que comece com findBy(campo), o spring vai entender que o método
 * é para pesquisar de acordo com o campo especificado(no caso email) na entidade clientes.*/
@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Integer> {

	/*
	 * A transação n é envolvida com uma transação de bd. fica| mais rápida e
	 * diminui o locking no gerenciamento de transações do bd
	 */
	@Transactional(readOnly = true)
	Cliente findByEmail(String email);

}
