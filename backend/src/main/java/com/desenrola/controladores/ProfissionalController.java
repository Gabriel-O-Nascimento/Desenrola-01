package com.desenrola.controladores;

import com.desenrola.controladores.dto.CadastroProfissionalDTO;
import com.desenrola.repositorios.ProfissionalRepository;
import com.desenrola.repositorios.entidades.Profissional;
import com.desenrola.servicos.aplicacao.ProfissionalAppService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/profissionais")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ProfissionalController {

    private final ProfissionalAppService profissionalAppService;
    private final ProfissionalRepository profissionalRepository;

    @PostMapping
    public ResponseEntity<Profissional> cadastrar(@RequestBody CadastroProfissionalDTO dto) {
        Profissional profissional = profissionalAppService.cadastrar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(profissional);
    }

    @GetMapping
    public ResponseEntity<List<Profissional>> listar(
            @RequestParam(required = false) Integer idCategoria) {
        if (idCategoria != null) {
            return ResponseEntity.ok(profissionalRepository.findByDisponivelTrueAndCategoriaId(idCategoria));
        }
        return ResponseEntity.ok(profissionalRepository.findByDisponivelTrue());
    }
}
