package br.com.fiap.dimdim.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@Entity
@Table(name = "conta")
public class Conta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    private String numero;

    @Column(nullable = false, length = 10)
    private String agencia;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoConta tipo;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal saldo = BigDecimal.ZERO;

    @Column(name = "data_abertura", nullable = false, updatable = false)
    private LocalDateTime dataAbertura;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @PrePersist
    void prePersist() {
        if (dataAbertura == null) {
            dataAbertura = LocalDateTime.now();
        }
        if (saldo == null) {
            saldo = BigDecimal.ZERO;
        }
    }

    // ---------- Regras de negócio ----------

    public void depositar(BigDecimal valor) {
        validarValorPositivo(valor);
        this.saldo = this.saldo.add(valor).setScale(2, RoundingMode.HALF_EVEN);
    }

    public void sacar(BigDecimal valor) {
        validarValorPositivo(valor);
        if (valor.compareTo(this.saldo) > 0) {
            throw new IllegalArgumentException("Saldo insuficiente para o saque");
        }
        this.saldo = this.saldo.subtract(valor).setScale(2, RoundingMode.HALF_EVEN);
    }

    /**
     * NOVO (Task do Azure Boards): rendimento mensal da poupanca.
     * Aplica um percentual sobre o saldo. So vale para conta POUPANCA.
     */
    public void aplicarRendimento(BigDecimal percentual) {
        if (this.tipo != TipoConta.POUPANCA) {
            throw new IllegalArgumentException("Rendimento só se aplica a contas poupança");
        }
        validarValorPositivo(percentual);
        BigDecimal rendimento = this.saldo.multiply(percentual)
                .divide(new BigDecimal("100"), 2, RoundingMode.HALF_EVEN);
        this.saldo = this.saldo.add(rendimento).setScale(2, RoundingMode.HALF_EVEN);
    }

    private void validarValorPositivo(BigDecimal valor) {
        if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O valor deve ser maior que zero");
        }
    }

    // ---------- Getters e setters ----------

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }
    public String getAgencia() { return agencia; }
    public void setAgencia(String agencia) { this.agencia = agencia; }
    public TipoConta getTipo() { return tipo; }
    public void setTipo(TipoConta tipo) { this.tipo = tipo; }
    public BigDecimal getSaldo() { return saldo; }
    public void setSaldo(BigDecimal saldo) { this.saldo = saldo; }
    public LocalDateTime getDataAbertura() { return dataAbertura; }
    public void setDataAbertura(LocalDateTime dataAbertura) { this.dataAbertura = dataAbertura; }
    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }
}
