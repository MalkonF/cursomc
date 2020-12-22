package me.malkon.cursomc.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import me.malkon.cursomc.domain.Pagamento;
// é necessário somente a criação do repository da superclasse
@Repository
public interface PagamentoRepository extends JpaRepository<Pagamento, Integer> {

}
