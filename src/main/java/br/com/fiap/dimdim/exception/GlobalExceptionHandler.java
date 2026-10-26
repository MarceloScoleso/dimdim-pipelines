package br.com.fiap.dimdim.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<Map<String, Object>> naoEncontrado(RecursoNaoEncontradoException ex) {
        return erro(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> regraNegocio(IllegalArgumentException ex) {
        return erro(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> integridade(DataIntegrityViolationException ex) {
        return erro(HttpStatus.CONFLICT,
                "Operação viola uma restrição do banco (registro duplicado ou cliente com contas vinculadas)");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> validacao(MethodArgumentNotValidException ex) {
        String detalhes = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .collect(Collectors.joining("; "));
        return erro(HttpStatus.BAD_REQUEST, detalhes);
    }

    private ResponseEntity<Map<String, Object>> erro(HttpStatus status, String mensagem) {
        Map<String, Object> corpo = new LinkedHashMap<>();
        corpo.put("timestamp", LocalDateTime.now().toString());
        corpo.put("status", status.value());
        corpo.put("erro", mensagem);
        return ResponseEntity.status(status).body(corpo);
    }
}
