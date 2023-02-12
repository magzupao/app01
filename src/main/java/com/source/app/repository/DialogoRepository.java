package com.source.app.repository;

import com.source.app.domain.Dialogo;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Dialogo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DialogoRepository extends JpaRepository<Dialogo, Long> {}
