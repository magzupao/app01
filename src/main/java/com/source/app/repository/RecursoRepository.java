package com.source.app.repository;

import com.source.app.domain.Recurso;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Recurso entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RecursoRepository extends JpaRepository<Recurso, Long> {}
