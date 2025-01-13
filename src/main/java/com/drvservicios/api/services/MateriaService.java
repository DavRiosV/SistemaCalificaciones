package com.drvservicios.api.services;

import com.drvservicios.api.models.Materia;
import com.drvservicios.api.reposotories.MateriaRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MateriaService {

    private static final Logger logger = LoggerFactory.getLogger(MateriaService.class);

    @Autowired
    private MateriaRepository materiaRepository;

    public Materia save(Materia materia) {
        logger.info("Guardando materia: {}", materia.getNombre());
        return materiaRepository.save(materia);
    }

    public List<Materia> findAll() {
        logger.info("Obteniendo todas las materias");
        return materiaRepository.findAll();
    }

    public Materia findById(Long id) {
        logger.info("Buscando materia por ID: {}", id);
        Optional<Materia> materia = materiaRepository.findById(id);
        return materia.orElse(null);
    }

    public Materia update(Long id, Materia materia) {
        logger.info("Actualizando materia con ID: {}", id);
        Optional<Materia> existingMateria = materiaRepository.findById(id);
        if (existingMateria.isPresent()) {
            Materia updatedMateria = existingMateria.get();
            updatedMateria.setNombre(materia.getNombre());
            updatedMateria.setAlumno(materia.getAlumno());
            return materiaRepository.save(updatedMateria);
        }
        return null;
    }

    public void delete(Long id) {
        logger.info("Eliminando materia con ID: {}", id);
        materiaRepository.deleteById(id);
    }

    public List<Materia> findByAlumnoId(Long alumnoId) {
        logger.info("Obteniendo materias del alumno con ID: {}", alumnoId);
        return materiaRepository.findByAlumno_Id(alumnoId);
    }

}
