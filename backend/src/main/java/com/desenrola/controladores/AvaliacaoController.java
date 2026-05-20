package com.desenrola.controladores;

import com.desenrola.dto.AvaliacaoResponse;
import com.desenrola.dto.CriarAvaliacaoRequest;
import com.desenrola.servicos.AvaliacaoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/avaliacoes")
@CrossOrigin(origins = "*")
public class AvaliacaoController {

    private final AvaliacaoService avaliacaoService;

    public AvaliacaoController(AvaliacaoService avaliacaoService) {
        this.avaliacaoService = avaliacaoService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AvaliacaoResponse criar(@Valid @RequestBody CriarAvaliacaoRequest request) {
        return avaliacaoService.criar(request);
    }

    @GetMapping("/profissional/{profissionalId}")
    public List<AvaliacaoResponse> listarPorProfissional(@PathVariable Long profissionalId) {
        return avaliacaoService.listarPorProfissional(profissionalId);
    }
}
