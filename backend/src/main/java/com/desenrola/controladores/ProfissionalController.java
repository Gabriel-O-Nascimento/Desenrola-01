package com.desenrola.controladores;

import com.desenrola.dto.CriarProfissionalRequest;
import com.desenrola.dto.ProfissionalResponse;
import com.desenrola.servicos.ProfissionalService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/profissionais")
@CrossOrigin(origins = "*")
public class ProfissionalController {

    private final ProfissionalService profissionalService;

    public ProfissionalController(ProfissionalService profissionalService) {
        this.profissionalService = profissionalService;
    }

    @GetMapping
    public List<ProfissionalResponse> listar(
            @RequestParam(required = false) String especialidade,
            @RequestParam(required = false) Boolean disponivel
    ) {
        return profissionalService.listar(especialidade, disponivel);
    }

    @GetMapping("/{id}")
    public ProfissionalResponse buscarPorId(@PathVariable Long id) {
        return profissionalService.buscarPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProfissionalResponse criar(@Valid @RequestBody CriarProfissionalRequest request) {
        return profissionalService.criar(request);
    }
}
