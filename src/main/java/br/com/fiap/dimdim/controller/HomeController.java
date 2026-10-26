package br.com.fiap.dimdim.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
public class HomeController {

    @Value("${app.versao}")
    private String versao;

    @GetMapping("/")
    public Map<String, Object> home() {
        Map<String, Object> info = new LinkedHashMap<>();
        info.put("aplicacao", "DimDim API");
        info.put("versao", versao);
        info.put("status", "UP");
        info.put("endpoints", new String[]{"/api/clientes", "/api/contas"});
        return info;
    }
}
