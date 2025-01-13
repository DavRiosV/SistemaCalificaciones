package com.drvservicios.api.controllers;

import com.drvservicios.api.models.Alumno;
import com.drvservicios.api.services.AlumnoService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/alumnos")
public class AlumnoController {

    @Autowired
    private AlumnoService alumnoService;

    @GetMapping
    public List<Alumno> getAllAlumnos() {
        return alumnoService.findAll();
    }

    @PostMapping
    public Alumno saveAlumno(@RequestBody Alumno alumno) {
        return alumnoService.save(alumno);
    }

    @GetMapping("/{id}")
    public Alumno getAlumnoById(@PathVariable Long id) {
        return alumnoService.findById(id);
    }
    
    @DeleteMapping("/{id}")
    public void deleteAlumno(@PathVariable Long id) {
        alumnoService.delete(id);
    }
    
    @GetMapping("/test")
    public String test() {
        return "La API está funcionando correctamente";
    }


}
