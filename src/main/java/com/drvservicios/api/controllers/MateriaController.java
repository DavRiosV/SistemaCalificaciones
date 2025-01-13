package com.drvservicios.api.controllers;

import com.drvservicios.api.models.Materia;
import com.drvservicios.api.services.MateriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/materias")
public class MateriaController {

    @Autowired
    private MateriaService materiaService;

    @PostMapping
    public Materia saveMateria(@RequestBody Materia materia) {
        return materiaService.save(materia);
    }

    @GetMapping
    public List<Materia> getAllMaterias() {
        return materiaService.findAll();
    }

    @GetMapping("/{id}")
    public Materia getMateriaById(@PathVariable Long id) {
        return materiaService.findById(id);
    }

    @PutMapping("/{id}")
    public Materia updateMateria(@PathVariable Long id, @RequestBody Materia materia) {
        return materiaService.update(id, materia);
    }

    @DeleteMapping("/{id}")
    public void deleteMateria(@PathVariable Long id) {
        materiaService.delete(id);
    }

    @GetMapping("/alumno/{alumnoId}")
    public List<Materia> getMateriasByAlumnoId(@PathVariable Long alumnoId) {
        return materiaService.findByAlumnoId(alumnoId);
    }
}