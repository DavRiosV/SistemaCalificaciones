package com.drvservicios.api.reposotories;

import com.drvservicios.api.models.Alumno;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlumnoRepository extends JpaRepository<Alumno, Long>{

}
