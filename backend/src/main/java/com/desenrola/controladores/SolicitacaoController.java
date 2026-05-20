package com.desenrola.controladores;

import com.desenrola.dto.CriarSolicitacaoRequest;
import com.desenrola.dto.SolicitacaoResponse;
import com.desenrola.servicos.SolicitacaoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/solicitacoes")
@CrossOrigin(origins = "*")
public class SolicitacaoController {

    private final SolicitacaoService solicitacaoService;

    public SolicitacaoController(SolicitacaoService solicitacaoService) {
        this.solicitacaoService = solicitacaoService;
    }

    @GetMapping
    public List<SolicitacaoResponse> listar() {
        return solicitacaoService.listar();
    }

    @GetMapping("/{id}")
    public SolicitacaoResponse buscarPorId(@PathVariable Long id) {
        return solicitacaoService.buscarPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SolicitacaoResponse criar(@Valid @RequestBody CriarSolicitacaoRequest request) {
        return solicitacaoService.criar(request);
    }

    @PatchMapping("/{id}/aceitar")
    public SolicitacaoResponse aceitar(@PathVariable Long id, @RequestParam Long profissionalId) {
        return solicitacaoService.aceitar(id, profissionalId);
    }

    @PatchMapping("/{id}/concluir")
    public SolicitacaoResponse concluir(@PathVariable Long id) {
        return solicitacaoService.concluir(id);
    }
}
