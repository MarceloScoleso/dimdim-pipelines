package br.com.fiap.dimdim.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Regras de negócio da Conta")
class ContaTest {

    private Conta conta;

    @BeforeEach
    void setUp() {
        conta = new Conta();
        conta.setSaldo(new BigDecimal("100.00"));
    }

    @Test
    @DisplayName("Depósito soma o valor ao saldo")
    void depositoSomaAoSaldo() {
        conta.depositar(new BigDecimal("50.00"));
        assertEquals(new BigDecimal("150.00"), conta.getSaldo());
    }

    @Test
    @DisplayName("Depósito com valor zero ou negativo é rejeitado")
    void depositoInvalidoLancaErro() {
        assertThrows(IllegalArgumentException.class, () -> conta.depositar(BigDecimal.ZERO));
        assertThrows(IllegalArgumentException.class, () -> conta.depositar(new BigDecimal("-10")));
    }

    @Test
    @DisplayName("Saque subtrai o valor do saldo")
    void saqueSubtraiDoSaldo() {
        conta.sacar(new BigDecimal("30.00"));
        assertEquals(new BigDecimal("70.00"), conta.getSaldo());
    }

    @Test
    @DisplayName("Saque maior que o saldo é rejeitado")
    void saqueSemSaldoLancaErro() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> conta.sacar(new BigDecimal("500.00")));
        assertEquals("Saldo insuficiente para o saque", ex.getMessage());
        assertEquals(new BigDecimal("100.00"), conta.getSaldo());
    }

    @Test
    @DisplayName("NOVO: rendimento de 0,5% na poupança atualiza o saldo")
    void rendimentoNaPoupanca() {
        conta.setTipo(TipoConta.POUPANCA);
        conta.aplicarRendimento(new BigDecimal("0.5"));
        assertEquals(new BigDecimal("100.50"), conta.getSaldo());
    }

    @Test
    @DisplayName("NOVO: rendimento em conta corrente é rejeitado")
    void rendimentoEmContaCorrenteLancaErro() {
        conta.setTipo(TipoConta.CORRENTE);
        assertThrows(IllegalArgumentException.class, () -> conta.aplicarRendimento(new BigDecimal("0.5")));
        assertEquals(new BigDecimal("100.00"), conta.getSaldo());
    }
}
