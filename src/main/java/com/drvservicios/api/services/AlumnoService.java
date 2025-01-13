package com.drvservicios.api.services;

import com.drvservicios.api.models.Alumno;
import com.drvservicios.api.reposotories.AlumnoRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AlumnoService {

    private static final Logger logger = LoggerFactory.getLogger(AlumnoService.class);

    @Autowired
    private AlumnoRepository alumnoRepository;

    public List<Alumno> findAll() {
        logger.info("Obteniendo todos los alumnos");
        return alumnoRepository.findAll();
    }

    public Alumno save(Alumno alumno) {
        logger.info("Guardando el alumno: {}", alumno.getNombre());
        return alumnoRepository.save(alumno);
    }

    public Alumno findById(Long id) {
        logger.info("Buscando alumno por ID: {}", id);
        Optional<Alumno> alumno = alumnoRepository.findById(id);
        return alumno.orElse(null);
    }

    public void delete(Long id) {
        logger.info("Eliminando alumno por ID: {}", id);
        alumnoRepository.deleteById(id);
    }

}