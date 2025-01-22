package com.drvservicios.api.controllers;

import com.drvservicios.api.models.Alumno;
import com.drvservicios.api.services.AlumnoService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alumnos")
public class AlumnoController {

    private static final Logger logger = LoggerFactory.getLogger(AlumnoController.class);

    @Autowired
    private AlumnoService alumnoService;

    @GetMapping
    public List<Alumno> getAllAlumnos() {
        logger.info("Recibiendo solicitud para obtener todos los alumnos");
        return alumnoService.findAll();
    }

    @PostMapping
    public Alumno saveAlumno(@RequestBody Alumno alumno) {
        logger.info("Recibiendo solicitud para guardar un alumno: {}", alumno.getNombre());
        return alumnoService.save(alumno);
    }

    @GetMapping("/{id}")
    public Alumno getAlumnoById(@PathVariable Long id) {
        logger.info("Recibiendo solicitud para obtener un alumno con ID: {}", id);
        return alumnoService.findById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteAlumno(@PathVariable Long id) {
        logger.info("Recibiendo solicitud para eliminar un alumno con ID: {}", id);
        alumnoService.delete(id);
    }

    @GetMapping("/test")
    public String test() {
        logger.info("Recibiendo solicitud para el endpoint de prueba '/test'");
        return "La API está funcionando correctamente";
    }
}
