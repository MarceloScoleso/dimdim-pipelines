package br.com.fiap.dimdim.controller;

import br.com.fiap.dimdim.repository.ClienteRepository;
import br.com.fiap.dimdim.repository.ContaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("API de Contas (relacionamento com Cliente)")
class ContaControllerTest {

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper mapper;
    @Autowired ClienteRepository clienteRepository;
    @Autowired ContaRepository contaRepository;

    private long clienteId;

    @BeforeEach
    void preparar() throws Exception {
        contaRepository.deleteAll();
        clienteRepository.deleteAll();
        String resp = mvc.perform(post("/api/clientes").contentType(MediaType.APPLICATION_JSON).content("""
                        {"nome":"Bruno Lima","cpf":"987.654.321-00","email":"bruno@dimdim.com"}
                        """))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        clienteId = mapper.readTree(resp).get("id").asLong();
    }

    private long criarConta() throws Exception {
        String body = """
                {"numero":"12345-6","agencia":"0001","tipo":"CORRENTE","saldo":100.00,"clienteId":%d}
                """.formatted(clienteId);
        String resp = mvc.perform(post("/api/contas").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.cliente.id").value(clienteId))
                .andReturn().getResponse().getContentAsString();
        return mapper.readTree(resp).get("id").asLong();
    }

    @Test
    @DisplayName("POST cria conta vinculada a um cliente existente")
    void criaContaVinculada() throws Exception {
        long id = criarConta();
        mvc.perform(get("/api/clientes/" + clienteId + "/contas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(id));
    }

    @Test
    @DisplayName("Depósito e saque pela API atualizam o saldo")
    void depositoESaque() throws Exception {
        long id = criarConta();
        mvc.perform(post("/api/contas/" + id + "/deposito").contentType(MediaType.APPLICATION_JSON).content("{\"valor\":50.00}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.saldo").value(150.00));
        mvc.perform(post("/api/contas/" + id + "/saque").contentType(MediaType.APPLICATION_JSON).content("{\"valor\":500.00}"))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @DisplayName("Não é possível excluir cliente que possui contas (409)")
    void naoExcluiClienteComContas() throws Exception {
        criarConta();
        mvc.perform(delete("/api/clientes/" + clienteId)).andExpect(status().isConflict());
    }
}
