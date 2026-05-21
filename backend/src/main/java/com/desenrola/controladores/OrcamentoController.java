package com.desenrola.controladores;

import com.desenrola.controladores.dto.CriarOrcamentoDTO;
import com.desenrola.repositorios.entidades.Orcamento;
import com.desenrola.servicos.aplicacao.OrcamentoAppService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orcamentos")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class OrcamentoController {

    private final OrcamentoAppService orcamentoAppService;

    @PostMapping
    public ResponseEntity<Orcamento> criar(@RequestBody CriarOrcamentoDTO dto) {
        Orcamento orcamento = orcamentoAppService.criar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(orcamento);
    }
}
