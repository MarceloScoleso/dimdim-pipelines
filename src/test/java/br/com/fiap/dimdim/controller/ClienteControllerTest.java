package br.com.fiap.dimdim.controller;

import br.com.fiap.dimdim.repository.ClienteRepository;
import br.com.fiap.dimdim.repository.ContaRepository;
import com.fasterxml.jackson.databind.JsonNode;
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
@DisplayName("API de Clientes")
class ClienteControllerTest {

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper mapper;
    @Autowired ClienteRepository clienteRepository;
    @Autowired ContaRepository contaRepository;

    private static final String CLIENTE_JSON = """
            {"nome":"Ana Souza","cpf":"123.456.789-00","email":"ana@dimdim.com","telefone":"11999990000"}
            """;

    @BeforeEach
    void limpar() {
        contaRepository.deleteAll();
        clienteRepository.deleteAll();
    }

    private long criarCliente() throws Exception {
        String resp = mvc.perform(post("/api/clientes").contentType(MediaType.APPLICATION_JSON).content(CLIENTE_JSON))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        JsonNode node = mapper.readTree(resp);
        return node.get("id").asLong();
    }

    @Test
    @DisplayName("POST cria cliente e GET retorna o mesmo cliente")
    void criaEBuscaCliente() throws Exception {
        long id = criarCliente();
        mvc.perform(get("/api/clientes/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Ana Souza"))
                .andExpect(jsonPath("$.cpf").value("123.456.789-00"));
    }

    @Test
    @DisplayName("POST com CPF e e-mail inválidos retorna 400")
    void validacaoRetorna400() throws Exception {
        String invalido = """
                {"nome":"","cpf":"123","email":"nao-e-email"}
                """;
        mvc.perform(post("/api/clientes").contentType(MediaType.APPLICATION_JSON).content(invalido))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("PUT atualiza os dados do cliente")
    void atualizaCliente() throws Exception {
        long id = criarCliente();
        String novo = """
                {"nome":"Ana Souza Lima","cpf":"123.456.789-00","email":"ana.lima@dimdim.com","telefone":"11988887777"}
                """;
        mvc.perform(put("/api/clientes/" + id).contentType(MediaType.APPLICATION_JSON).content(novo))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Ana Souza Lima"))
                .andExpect(jsonPath("$.email").value("ana.lima@dimdim.com"));
    }

    @Test
    @DisplayName("DELETE remove o cliente e GET passa a retornar 404")
    void excluiCliente() throws Exception {
        long id = criarCliente();
        mvc.perform(delete("/api/clientes/" + id)).andExpect(status().isNoContent());
        mvc.perform(get("/api/clientes/" + id)).andExpect(status().isNotFound());
    }
}
