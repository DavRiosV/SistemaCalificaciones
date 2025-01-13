package com.drvservicios.api.reposotories;

import com.drvservicios.api.models.Materia;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;


public interface MateriaRepository extends JpaRepository<Materia, Long> {
	List<Materia> findByAlumno_Id(Long alumnoId);
	List<Materia> findByAlumnoIdAndNombre(Long alumnoId, String nombre);
}
