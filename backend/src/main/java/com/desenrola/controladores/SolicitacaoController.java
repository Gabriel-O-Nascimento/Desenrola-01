package com.desenrola.controladores;

import com.desenrola.controladores.dto.AtualizarStatusDTO;
import com.desenrola.controladores.dto.CriarSolicitacaoDTO;
import com.desenrola.repositorios.entidades.Solicitacao;
import com.desenrola.servicos.aplicacao.SolicitacaoAppService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/solicitacoes")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class SolicitacaoController {

    private final SolicitacaoAppService solicitacaoAppService;

    @PostMapping
    public ResponseEntity<Solicitacao> criar(@RequestBody CriarSolicitacaoDTO dto) {
        Solicitacao solicitacao = solicitacaoAppService.criar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(solicitacao);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Solicitacao> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(solicitacaoAppService.buscarPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<Solicitacao>> listar(
            @RequestParam(required = false) Long idCliente,
            @RequestParam(required = false) Long idProfissional) {
        if (idCliente != null) {
            return ResponseEntity.ok(solicitacaoAppService.listarPorCliente(idCliente));
        }
        if (idProfissional != null) {
            return ResponseEntity.ok(solicitacaoAppService.listarPorProfissional(idProfissional));
        }
        return ResponseEntity.badRequest().build();
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Solicitacao> atualizarStatus(@PathVariable Long id,
                                                        @RequestBody AtualizarStatusDTO dto) {
        return ResponseEntity.ok(solicitacaoAppService.atualizarStatus(id, dto));
    }
}
