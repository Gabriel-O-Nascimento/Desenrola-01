package com.desenrola.controladores;

import com.desenrola.servicos.TesteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class TesteController {

    private final TesteService testeService;

    public TesteController(TesteService testeService) {
        this.testeService = testeService;
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "OK");
        response.put("message", "Desenrola Backend esta funcionando!");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/teste-mensageria")
    public ResponseEntity<Map<String, String>> testeMensageria(@RequestBody Map<String, String> body) {
        String mensagem = body.getOrDefault("mensagem", "Teste de mensageria");
        testeService.enviarMensagem(mensagem);

        Map<String, String> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Mensagem processada para envio na fila RabbitMQ");
        response.put("mensagemEnviada", mensagem);
        return ResponseEntity.ok(response);
    }
}
