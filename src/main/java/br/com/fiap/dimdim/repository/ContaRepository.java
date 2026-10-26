package br.com.fiap.dimdim.repository;

import br.com.fiap.dimdim.model.Conta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContaRepository extends JpaRepository<Conta, Long> {
    List<Conta> findByClienteId(Long clienteId);
}
