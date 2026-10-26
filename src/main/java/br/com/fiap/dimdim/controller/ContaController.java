package br.com.fiap.dimdim.controller;

import br.com.fiap.dimdim.dto.ContaRequest;
import br.com.fiap.dimdim.dto.ValorRequest;
import br.com.fiap.dimdim.exception.RecursoNaoEncontradoException;
import br.com.fiap.dimdim.model.Cliente;
import br.com.fiap.dimdim.model.Conta;
import br.com.fiap.dimdim.repository.ClienteRepository;
import br.com.fiap.dimdim.repository.ContaRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/contas")
public class ContaController {

    private final ContaRepository contaRepository;
    private final ClienteRepository clienteRepository;

    public ContaController(ContaRepository contaRepository, ClienteRepository clienteRepository) {
        this.contaRepository = contaRepository;
        this.clienteRepository = clienteRepository;
    }

    @GetMapping
    public List<Conta> listar() {
        return contaRepository.findAll();
    }

    @GetMapping("/{id}")
    public Conta buscar(@PathVariable Long id) {
        return buscarOuFalhar(id);
    }

    @PostMapping
    public ResponseEntity<Conta> criar(@Valid @RequestBody ContaRequest req) {
        Conta conta = new Conta();
        preencher(conta, req);
        conta.setSaldo(req.saldo() == null ? BigDecimal.ZERO : req.saldo());
        return ResponseEntity.status(HttpStatus.CREATED).body(contaRepository.save(conta));
    }

    @PutMapping("/{id}")
    public Conta atualizar(@PathVariable Long id, @Valid @RequestBody ContaRequest req) {
        Conta conta = buscarOuFalhar(id);
        preencher(conta, req);
        if (req.saldo() != null) {
            conta.setSaldo(req.saldo());
        }
        return contaRepository.save(conta);
    }

    @PostMapping("/{id}/deposito")
    public Conta depositar(@PathVariable Long id, @Valid @RequestBody ValorRequest req) {
        Conta conta = buscarOuFalhar(id);
        conta.depositar(req.valor());
        return contaRepository.save(conta);
    }

    @PostMapping("/{id}/saque")
    public Conta sacar(@PathVariable Long id, @Valid @RequestBody ValorRequest req) {
        Conta conta = buscarOuFalhar(id);
        conta.sacar(req.valor());
        return contaRepository.save(conta);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        buscarOuFalhar(id);
        contaRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private void preencher(Conta conta, ContaRequest req) {
        Cliente cliente = clienteRepository.findById(req.clienteId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Cliente " + req.clienteId() + " não encontrado"));
        conta.setNumero(req.numero());
        conta.setAgencia(req.agencia());
        conta.setTipo(req.tipo());
        conta.setCliente(cliente);
    }

    private Conta buscarOuFalhar(Long id) {
        return contaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Conta " + id + " não encontrada"));
    }
}
