package com.desenrola.controladores;

import com.desenrola.servicos.TesteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class TesteController {
    
    @Autowired
    private TesteService testeService;
    
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "OK");
        response.put("message", "Desenrola Backend está funcionando!");
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/teste-mensageria")
    public ResponseEntity<Map<String, String>> testeMensageria(@RequestBody Map<String, String> body) {
        String mensagem = body.getOrDefault("mensagem", "Teste de mensageria");
        
        // Envia mensagem para a fila RabbitMQ
        testeService.enviarMensagem(mensagem);
        
        Map<String, String> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Mensagem enviada para a fila RabbitMQ");
        response.put("mensagemEnviada", mensagem);
        
        return ResponseEntity.ok(response);
    }
}
