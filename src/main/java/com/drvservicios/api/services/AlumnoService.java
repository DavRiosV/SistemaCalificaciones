package com.drvservicios.api.services;

import com.drvservicios.api.models.Alumno;
import com.drvservicios.api.reposotories.AlumnoRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlumnoService {

    private static final Logger logger = LoggerFactory.getLogger(AlumnoService.class);

    @Autowired
    private AlumnoRepository alumnoRepository;

    public List<Alumno> findAll() {
        try {
            logger.info("Obteniendo todos los alumnos");
            return alumnoRepository.findAll();
        } catch (Exception e) {
            logger.error("Error al obtener todos los alumnos", e);
            throw e;
        }
    }

    public Alumno save(Alumno alumno) {
        try {
            logger.info("Guardando el alumno: {}", alumno.getNombre());
            return alumnoRepository.save(alumno);
        } catch (Exception e) {
            logger.error("Error al guardar el alumno: {}", alumno.getNombre(), e);
            throw e;
        }
    }

    public Alumno findById(Long id) {
        try {
            logger.info("Buscando alumno por ID: {}", id);
            return alumnoRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Alumno no encontrado con ID: " + id));
        } catch (Exception e) {
            logger.error("Error al buscar el alumno por ID: {}", id, e);
            throw e;
        }
    }

    public void delete(Long id) {
        try {
            logger.info("Eliminando alumno por ID: {}", id);
            alumnoRepository.deleteById(id);
        } catch (Exception e) {
            logger.error("Error al eliminar el alumno por ID: {}", id, e);
            throw e;
        }
    }
}
