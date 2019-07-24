package me.malkon.cursomc.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import me.malkon.cursomc.domain.Categoria;

/*
 * JpaRepository é uma interface que vai fazer operações de acesso a dados com
 * objetos de acordo com o tipo que vc especificar. Nesse caso objetos do tipo
 * categoria. Após a categoria vc deve especificar o tipo do atributo
 * identificador da classe Categoria que é Integer de id.
 */
@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {

}
