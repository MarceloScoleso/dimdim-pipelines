package br.com.fiap.dimdim.controller;

import br.com.fiap.dimdim.exception.RecursoNaoEncontradoException;
import br.com.fiap.dimdim.model.Cliente;
import br.com.fiap.dimdim.model.Conta;
import br.com.fiap.dimdim.repository.ClienteRepository;
import br.com.fiap.dimdim.repository.ContaRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final ClienteRepository clienteRepository;
    private final ContaRepository contaRepository;

    public ClienteController(ClienteRepository clienteRepository, ContaRepository contaRepository) {
        this.clienteRepository = clienteRepository;
        this.contaRepository = contaRepository;
    }

    @GetMapping
    public List<Cliente> listar() {
        return clienteRepository.findAll();
    }

    @GetMapping("/{id}")
    public Cliente buscar(@PathVariable Long id) {
        return buscarOuFalhar(id);
    }

    @GetMapping("/{id}/contas")
    public List<Conta> contasDoCliente(@PathVariable Long id) {
        buscarOuFalhar(id);
        return contaRepository.findByClienteId(id);
    }

    @PostMapping
    public ResponseEntity<Cliente> criar(@Valid @RequestBody Cliente cliente) {
        cliente.setId(null);
        return ResponseEntity.status(HttpStatus.CREATED).body(clienteRepository.save(cliente));
    }

    @PutMapping("/{id}")
    public Cliente atualizar(@PathVariable Long id, @Valid @RequestBody Cliente dados) {
        Cliente cliente = buscarOuFalhar(id);
        cliente.setNome(dados.getNome());
        cliente.setCpf(dados.getCpf());
        cliente.setEmail(dados.getEmail());
        cliente.setTelefone(dados.getTelefone());
        return clienteRepository.save(cliente);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        buscarOuFalhar(id);
        if (!contaRepository.findByClienteId(id).isEmpty()) {
            throw new org.springframework.dao.DataIntegrityViolationException("Cliente possui contas");
        }
        clienteRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private Cliente buscarOuFalhar(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Cliente " + id + " não encontrado"));
    }
}
